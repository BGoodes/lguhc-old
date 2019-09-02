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
						j.getPlayer().sendMessage(main.gameTag + "�bVous avez 2 minutes pour prot�ger un joueur gr�ce � la commande �l/lg protect <Pseudo> �b!");
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
							j.getPlayer().sendMessage(main.gameTag + "�cVous avez attendu plus de 2 min, vous pourrez donc utiliser votre pouvoir qu'� partir du prochaine �pisode !");
						}
					}
				}
			}
		}
	}
	
	public void canProtect(Joueur j, String targetname) {
		
		Player p = j.getPlayer();
		
		if (j.getRole() != LGRoles.Salvateur) {
			p.sendMessage(main.gameTag + "�cErreur, vous devez �tre �oSalvateur �cpour effectuer cette commande !");
			return;
		}
		
		if (j.getPower() < 1 ) {
			p.sendMessage(main.gameTag + "�cVous avez d�j� utilis� votre pouvoir ou avez attendu trop longtemps (2min) ! Attendez le prochaine �pisode avant de pouvoir le r�utiliser !");
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
		
		if (j.whoProtect != null) {
			if (TargetJ.equals(j.whoProtect)) {
				p.sendMessage(main.gameTag + "�cVous ne pouvez pas la m�me personne 2 �pisodes de suite !");
				return;
			}
		}

		
		protect(j, TargetJ);
	}
	
	private void protect(Joueur j, Joueur t) {
		
		Player p = j.getPlayer();
		
		p.sendMessage(main.gameTag + "�aVous avez bien donn� la salvation � " + t.getName());
		p.sendMessage(" ");
		j.setPower(0);
		j.whoProtect = t;
		
		t.salvation = true;
		t.getPlayer().sendMessage(main.gameTag + "�aLe salvateur � d�cid� de vous prot�ger ! "
				+ "Vous ne prendrez plus de d�g�ts de chute et obtenez l'effet de r�sistance I pendant un �pisode !"); 
		p.sendMessage(" ");
		new Sounds(t.getPlayer()).PlaySound(Sound.DRINK);
	}
}
