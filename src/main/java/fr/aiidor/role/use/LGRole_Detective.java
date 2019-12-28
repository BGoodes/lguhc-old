package fr.aiidor.role.use;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;

public class LGRole_Detective {
	
	private LGUHC main;
	public LGRole_Detective(LGUHC main) {
		this.main = main;
	}
	
	public void canInspect() {
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				cannotInspect();
			}
		}, 6000);
		
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.Detective) {
				if (!j.isDead() && !j.noPower) {
					j.setPower(1);
					
					if (j.isConnected()) {
						j.getPlayer().sendMessage(main.gameTag + "§bVous avez 5 minutes pour inspecter 2 joueurs grâce à la commande /lg inspect <Joueur1> <Joueur2>");
					}
				}
			}
		}
	}
	
	
	private void cannotInspect() {
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.Detective) {
				if (!j.isDead()) {
					if (j.getPower() > 0) {
						
						j.setPower(0);
						
						if (j.isConnected()) {
							j.getPlayer().sendMessage(main.gameTag + "§cVous avez attendu plus de 5 min, vous pourrez donc utiliser votre pouvoir qu'à partir du prochaine épisode !");
						}
					}
				}
			}
		}
	}
	
	public void Inspect(Joueur j, String targetname, String targetname2) {
		
		Player p = j.getPlayer();
		
		if (j.getRole() != LGRoles.Detective) {
			p.sendMessage(main.gameTag + "§cErreur, vous devez être §oDétective §cpour effectuer cette commande !");
			return;
		}
		
		if (j.getPower() < 1 ) {
			p.sendMessage(main.gameTag + "§cVous avez déjà utilisé votre pouvoir ou avez attendu trop longtemps (5min) ! Attendez le prochaine épisode avant de pouvoir le réutiliser !");
			return;
		}
		
		if (j.getName().equalsIgnoreCase(targetname) || j.getName().equalsIgnoreCase(targetname2)) {
			p.sendMessage(main.gameTag + "§cErreur, vous ne pouvez pas vous choisir vous même !");
			return;
		}
		
		if (targetname.equalsIgnoreCase(targetname2)) {
			p.sendMessage(main.gameTag + "§cErreur, vous ne pouvez pas choisir deux fois la même personne !");
			return;
		}
		
		if (Bukkit.getPlayer(targetname) == null || Bukkit.getPlayer(targetname2) == null) {
			p.sendMessage(main.gameTag + "§cErreur, le joueur l'un des joueurs n'est pas connecté ou n'existe pas !");
			return;
		}
		
		Player Target = Bukkit.getPlayer(targetname);
		Player Target2 = Bukkit.getPlayer(targetname2);
		
		if (main.getPlayer(Target.getUniqueId()) == null || main.getPlayer(Target2.getUniqueId()) == null) {
			p.sendMessage(main.gameTag + "§cErreur, les joueurs visés doit être dans la partie !");
			return;
		}
		
		Joueur TargetJ = main.getPlayer(Target.getUniqueId());
		Joueur TargetJ2 = main.getPlayer(Target2.getUniqueId());
		
		
		if (TargetJ.isDead() || TargetJ2.isDead()) {
			p.sendMessage(main.gameTag + "§cErreur, les joueurs visés doit être en vie !");
			return;
		}
		
		if (j.hasInspect.contains(TargetJ) || j.hasInspect.contains(TargetJ2)) {
			
			p.sendMessage(main.gameTag + "§cVous avez déjà inspecté l'un des deux joueurs.");
			return;
		}
		
		choose(j, TargetJ, TargetJ2);
		
	}
	
	public void choose(Joueur j, Joueur t1, Joueur t2) {
		
		LGCamps camp1 = t1.getCamp();
		LGCamps camp2 = t2.getCamp();
		
		if (t1.getRole() == LGRoles.LGFeutre) {
			camp1 = t1.getFakeRole().camp;
		}
		
		if (t2.getRole() == LGRoles.LGFeutre) {
			camp2 = t2.getFakeRole().camp;
		}
		
		j.hasInspect.add(t1);
		j.hasInspect.add(t2);
		
		j.setPower(0);
		
		//COUPLE
		if (t1.hasCouple() || t2.hasCouple()) {
			if (t1.hasCouple() && t1.getCouple().equals(t2)) {
				j.getPlayer().sendMessage(main.gameTag + "§eLes joueurs "+ t1.getName() + " et " + t2.getName() + " sont du même camp !");
				return;
			}
			
			if (t1.hasCouple() && t1.getCouple().getCamp() != t1.getCamp()) {
				j.getPlayer().sendMessage(main.gameTag + "§eLes joueurs "+ t1.getName() + " et " + t2.getName() + " ne sont pas du même camp !");
				return;
			}
			
			if (t2.hasCouple() && t2.getCouple().getCamp() != t2.getCamp()) {
				j.getPlayer().sendMessage(main.gameTag + "§eLes joueurs "+ t1.getName() + " et " + t2.getName() + " ne sont pas du même camp !");
				return;
			}
		}
		
		
		int inspectCamp1 = getCampState(camp1);
		int inspectCamp2 = getCampState(camp2);
		
		if (inspectCamp1 == inspectCamp2) {
			j.getPlayer().sendMessage(main.gameTag + "§eLes joueurs " + t1.getName() + " et " + t2.getName() + " sont du même camp !");
			
		} else {
			j.getPlayer().sendMessage(main.gameTag + "§eLes joueurs "+ t1.getName() + " et " + t2.getName() + " ne sont pas du même camp !");
		}
		
		
	}
	
	private int getCampState(LGCamps camp) {
		
		if (camp == LGCamps.Village) return 1;
		else if (camp == LGCamps.LoupGarou || camp == LGCamps.LGB) return 2;
		else return 3;
	}
	
	
}
