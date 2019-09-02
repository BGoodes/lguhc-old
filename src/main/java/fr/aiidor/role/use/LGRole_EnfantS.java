package fr.aiidor.role.use;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGRoles;

public class LGRole_EnfantS {
	
	private LGUHC main;
	public LGRole_EnfantS(LGUHC main) {
		this.main = main;
	}
	
	public void canChoose() {
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				cannotChoose();
			}
		}, 6000);
		
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.EnfantS) {
				if (!j.isDead()) {
					j.setPower(1);
					
					if (j.isConnected()) {
						j.getPlayer().sendMessage(main.gameTag + "�bVous avez 5 minutes pour choisir votre mod�le avec la commande �l/lg choose <pseudo> �b!");
					}
				}
			}
		}
	}
	
	
	private void cannotChoose() {
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.EnfantS) {
				if (!j.isDead()) {
					if (j.getPower() > 0) {
						
						j.setPower(0);
						
						if (j.isConnected()) {
							j.getPlayer().sendMessage(main.gameTag + "�cVous avez attendu plus de 5 min, vous pourrez donc plus choisir votre mod�le et resterez donc villageois !");
						}
					}
				}
			}
		}
	}
	
	public void canChoose(Joueur j, String targetname) {
		
		Player p = j.getPlayer();
		
		if (j.getRole() != LGRoles.EnfantS) {
			p.sendMessage(main.gameTag + "�cErreur, vous devez �tre �oEnfant Sauvage �cpour effectuer cette commande !");
			return;
		}
		
		if (j.getPower() < 1 ) {
			p.sendMessage(main.gameTag + "�cVous avez d�j� choisit votre mod�le ou avez attendu trop longtemps (5min) !");
			return;
		}
		
		if (targetname.equalsIgnoreCase(j.getName())) {
			p.sendMessage(main.gameTag + "�cVous ne pouvez pas vous choisir comme mod�le !");
			return;
		}
		
		if (Bukkit.getPlayer(targetname) == null) {
			p.sendMessage(main.gameTag + "�cErreur, le joueur "+ targetname + " n'est pas connect� ou n'existe pas !");
			return;
		}
		
		Player Target = Bukkit.getPlayer(targetname);
		
		if (main.getPlayer(Target.getUniqueId()) == null) {
			p.sendMessage(main.gameTag + "�cErreur, le joueur vis� doit �tre dans la partie !");
			return;
		}
		
		Joueur TargetJ = main.getPlayer(Target.getUniqueId());
		
		if (TargetJ.isDead()) {
			p.sendMessage(main.gameTag + "�cErreur, le joueur vis� doit �tre en vie !");
			return;
		}
		
		choose(j, TargetJ);
	}
	
	private void choose(Joueur j, Joueur t) {
		
		Player p = j.getPlayer();
		
		j.setPower(0);
		p.sendMessage("�aVous avez bien choisit �6" + t.getName() + " �a comme mod�le ! Si il vient � mourrir, vous passerez dans le camp des Loups-Garous !");
		j.Model = t;
	}
}
