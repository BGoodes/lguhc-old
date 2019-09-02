package fr.aiidor.role.use;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.effect.Sounds;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGRoles;

public class LGRole_Salvateur {
	
	private LGUHC main;
	public LGRole_Salvateur(LGUHC main) {
		this.main = main;
	}
	
	public void canProtect() {
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				cannotProtect();
			}
		}, 2400);
		
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.Salvateur) {
				if (!j.isDead()) {
					j.setPower(1);
					
					if (j.isConnected()) {
						j.getPlayer().sendMessage(main.gameTag + "§bVous avez 2 minutes pour protéger un joueur grâce à la commande §l/lg protect <Pseudo> §b!");
					}
				}
			}
		}
	}
	
	
	private void cannotProtect() {
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.Salvateur) {
				if (!j.isDead()) {
					if (j.getPower() > 0) {
						
						j.setPower(0);
						j.whoProtect = null;
						
						if (j.isConnected()) {
							j.getPlayer().sendMessage(main.gameTag + "§cVous avez attendu plus de 2 min, vous pourrez donc utiliser votre pouvoir qu'à partir du prochaine épisode !");
						}
					}
				}
			}
		}
	}
	
	public void canProtect(Joueur j, String targetname) {
		
		Player p = j.getPlayer();
		
		if (j.getRole() != LGRoles.Salvateur) {
			p.sendMessage(main.gameTag + "§cErreur, vous devez être §oSalvateur §cpour effectuer cette commande !");
			return;
		}
		
		if (j.getPower() < 1 ) {
			p.sendMessage(main.gameTag + "§cVous avez déjà utilisé votre pouvoir ou avez attendu trop longtemps (2min) ! Attendez le prochaine épisode avant de pouvoir le réutiliser !");
			return;
		}
		
		if (Bukkit.getPlayer(targetname) == null) {
			p.sendMessage(main.gameTag + "§cErreur, le joueur "+ targetname + " n'est pas connecté ou n'existe pas !");
			return;
		}
		
		Player Target = Bukkit.getPlayer(targetname);
		
		if (main.getPlayer(Target.getUniqueId()) == null) {
			p.sendMessage(main.gameTag + "§cErreur, le joueur visé doit être dans la partie !");
			return;
		}
		
		Joueur TargetJ = main.getPlayer(Target.getUniqueId());
			
		if (TargetJ.isDead()) {
			p.sendMessage(main.gameTag + "§cErreur, le joueur visé doit être en vie !");
			return;
		}
		
		if (j.whoProtect != null) {
			if (TargetJ.equals(j.whoProtect)) {
				p.sendMessage(main.gameTag + "§cVous ne pouvez pas la même personne 2 épisodes de suite !");
				return;
			}
		}

		
		protect(j, TargetJ);
	}
	
	private void protect(Joueur j, Joueur t) {
		
		Player p = j.getPlayer();
		
		p.sendMessage(main.gameTag + "§aVous avez bien donné la salvation à " + t.getName());
		p.sendMessage(" ");
		j.setPower(0);
		j.whoProtect = t;
		
		t.salvation = true;
		t.getPlayer().sendMessage(main.gameTag + "§aLe salvateur à décidé de vous protèger ! "
				+ "Vous ne prendrez plus de dégâts de chute et obtenez l'effet de résistance I pendant un épisode !"); 
		p.sendMessage(" ");
		new Sounds(t.getPlayer()).PlaySound(Sound.DRINK);
	}
}
