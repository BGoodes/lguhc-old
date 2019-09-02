package fr.aiidor.role.use;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGRoles;

public class LGRole_Chasseur {
	
	private LGUHC main;
	public LGRole_Chasseur(LGUHC main) {
		this.main = main;
	}
	
	public void canPan(Joueur j) {
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				cannotPan();
			}
		}, 1200);
		
		j.setPower(1);
		if (j.isConnected()) {
			j.getPlayer().sendMessage(main.gameTag + "�bVous �tes mort mais vous pouvez faire �l/lg pan <pseudo> �bpour r�duire la vie d'un joueur de moiti� ! (Vous avez 1 minute)");
		}
		
	}
	
	
	private void cannotPan() {
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.Chasseur) {
				if (j.isDead()) {
					if (j.getPower() > 0) {
						
						j.setPower(0);
						
						if (j.isConnected()) {
							j.getPlayer().sendMessage(main.gameTag + "�cVous avez attendu plus de 1 min, vous ne pourrez donc pas utiliser votre pouvoir !");
						}
					}
				}
			}
		}
	}
	
	public void canPan(Joueur j, String targetname) {
		
		Player p = j.getPlayer();
		
		if (j.getRole() != LGRoles.Chasseur) {
			p.sendMessage(main.gameTag + "�cErreur, vous devez �tre �oChasseur �cpour effectuer cette commande !");
			return;
		}
		
		if (!j.isDead()) {
			p.sendMessage(main.gameTag + "�cVous ne pourrez effectuer cette commande que � votre mort !");
			return;
		}
		
		if (j.getPower() < 1 ) {
			p.sendMessage(main.gameTag + "�cVous ne pouvez plus utiliser votre pouvoir !");
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
		
		if (!TargetJ.isConnected()) {
			p.sendMessage(main.gameTag + "�cErreur, le joueur vis� doit �tre connect� !");
			return;
		}
		
		pan(j, TargetJ);
	}
	
	private void pan(Joueur j, Joueur t) {
		
		Player p = j.getPlayer();
		
		j.setPower(0);
		p.sendMessage(" ");
		
		t.getPlayer().setMaxHealth(t.getPlayer().getMaxHealth() / 2);
		t.getPlayer().damage(0);
        
		Bukkit.broadcastMessage(main.gameTag + "�4Le chasseur � d�cid� de tirer sur �l" + t.getName() + "�4 Celui ci perd la moiti� de sa vie !");
	}
}
