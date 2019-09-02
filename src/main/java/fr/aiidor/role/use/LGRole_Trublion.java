package fr.aiidor.role.use;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;

public class LGRole_Trublion {
	
	private LGUHC main;
	public LGRole_Trublion(LGUHC main) {
		this.main = main;
	}
	
	public void canChoose() {
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				cannotChoose();
			}
		}, 2400);
		
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.Trublion) {
				if (!j.isDead()) {
					j.setPower(1);
					
					if (j.isConnected()) {
						Bukkit.getScheduler().runTaskLater(main, new Runnable() {
							@Override
							public void run() {
								j.getPlayer().sendMessage(main.gameTag + "§3 C'est le moment ! Vous avez deux minutes pour choisir les joueurs dont vous souhaitez "
										+ "échanger les rôles. Faites §l/lg switch <pseudo1> <pseudo2> §3pour échanger les rôles !");
							}
						}, 20);

					}
				}
			}
		}
	}
	
	
	private void cannotChoose() {
		
		if (!main.canSeeLgList()) {
			for (Joueur lg : main.getLg()) {
				lg.getPlayer().sendMessage("");
				lg.getPlayer().sendMessage(main.gameTag + "§6Vous pouvez désormais consulter la liste des Loups-Garous en faisant §l/lg role !");
			}
		}
		
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.Trublion) {
				if (!j.isDead()) {
					if (j.getPower() > 0) {
						
						j.setPower(0);
						
						if (j.isConnected()) {
							j.getPlayer().sendMessage(main.gameTag + "§cVous avez attendu plus de 2 min, vous pourrez donc plus échanger de joueurs");
						}
					}
				}
			}
		}
	}
	
	public void canChoose(Joueur j, String targetname, String targetname2) {
		
		Player p = j.getPlayer();
		
		if (j.getRole() != LGRoles.Trublion) {
			p.sendMessage(main.gameTag + "§cErreur, vous devez être §oTrublion §cpour effectuer cette commande !");
			return;
		}
		
		if (j.getPower() < 1 ) {
			p.sendMessage(main.gameTag + "§cVous ne pouvez pas échanger les rôles des joueurs !");
			return;
		}
		
		if (j.getName().equalsIgnoreCase(targetname) || j.getName().equalsIgnoreCase(targetname2)) {
			p.sendMessage(main.gameTag + "§cErreur, vous ne pouvez pas échanger votre propre rôle !");
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
		
		choose(j, TargetJ, TargetJ2);
	}
	
	public void choose(Joueur j, Joueur t, Joueur t2) {
		
		j.getPlayer().sendMessage(main.gameTag + "§aVous avez bien échangé les rôles de " + t.getName() + " et " + t2.getName());
		j.getPlayer().sendMessage(" ");
		
		LGRoles role1 = t.getRole();
		LGRoles role2 = t2.getRole();
		
		LGCamps camp1 = t.getCamp();
		LGCamps camp2 = t2.getCamp();
		
		int power1 = t.getPower();
		int power2 = t2.getPower();
		
		Joueur Soeur = t.Soeur;
		Joueur Soeur2 = t2.Soeur;
		
		if (t.getRole() == LGRoles.LGA || t2.getRole() == LGRoles.LGA) {
			List<Joueur> list1 = t.lglist;
			List<Joueur> list2 = t2.lglist;
			
			t.lglist = list2;
			t2.lglist = list1;
			
			t.lglist.add(t);
			t2.lglist.add(t2);
		}
		
		if (t.getRole() == LGRoles.EnfantS || t2.getRole() == LGRoles.EnfantS) {
			Joueur model1 = t.Model;
			Joueur model2 = t2.Model;
			
			t.Model = model2;
			t2.Model = model1;
		}
		
		//VOLEUR
		if (t.Rob) {
			power1 = 0;
			role1 = LGRoles.Voleur;
		}
		
		if (t2.Rob) {
			power2 = 0;
			role2 = LGRoles.Voleur;
		}
		
		t.getPlayer().setMaxHealth(20);
		t2.getPlayer().setMaxHealth(20);
		
		if (role1 == LGRoles.LGB) t2.getPlayer().setMaxHealth(30);
		if (role2 == LGRoles.LGB) t.getPlayer().setMaxHealth(30);
		
		t.setRole(role2);
		t2.setRole(role1);
		
		t.setCamp(camp2);
		t2.setCamp(camp1);
		
		t.setPower(power2);
		t2.setPower(power1);
		
		t.setPower(power2);
		t2.setPower(power1);
		
		t.trublion = true;
		t2.trublion = true;
		
		t.Soeur = Soeur2;
		t2.Soeur = Soeur;
		
		
		//MSG
		t.getPlayer().sendMessage("");
		t.getPlayer().sendMessage(main.gameTag + "§9Votre rôle viens d'être switché par le Trublion ! Prenez connaissance de votre nouveau rôle en faisant /lg role");
		
		t2.getPlayer().sendMessage("");
		t2.getPlayer().sendMessage(main.gameTag + "§9Votre rôle viens d'être switché par le Trublion ! Prenez connaissance de votre nouveau rôle en faisant /lg role");
		
		
		if (main.canSeeLgList()) {
			for (Joueur lg : main.getLg()) {
				lg.getPlayer().sendMessage("");
				lg.getPlayer().sendMessage(main.gameTag + "§6Vous pouvez désormais consulter la liste des Loups-Garous en faisant §l/lg role !");
			}
		}
	}
}
