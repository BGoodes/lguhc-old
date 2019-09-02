package fr.aiidor.role.use;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGRoles;

public class LGRole_Voyante {
	
	private LGUHC main;
	public LGRole_Voyante(LGUHC main) {
		this.main = main;
	}
	
	public void canSee() {
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				cannotSee();
			}
		}, 6000);
		
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.Voyante || j.getRole() == LGRoles.VoyanteB ) {
				if (!j.isDead()) {
					j.setPower(1);
					
					if (j.isConnected()) {
						j.getPlayer().sendMessage(main.gameTag + "�bVous avez 5 minutes pour conna�tre le r�le d'un joueur gr�ce � la commande /lg see <Pseudo> !");
					}
				}
			}
		}
	}
	
	
	private void cannotSee() {
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.Voyante || j.getRole() == LGRoles.VoyanteB ) {
				if (!j.isDead()) {
					if (j.getPower() > 0) {
						
						j.setPower(0);
						
						if (j.isConnected()) {
							j.getPlayer().sendMessage(main.gameTag + "�cVous avez attendu plus de 5 min, vous pourrez donc utiliser votre pouvoir qu'� partir du prochaine �pisode !");
						}
					}
				}
			}
		}
	}
	
	public void canSee(Joueur j, String targetname) {
		
		Player p = j.getPlayer();
		
		if (j.getRole() != LGRoles.Voyante && j.getRole() != LGRoles.VoyanteB) {
			p.sendMessage(main.gameTag + "�cErreur, vous devez �tre �oVoyante �cpour effectuer cette commande !");
			return;
		}
		
		if (j.getPower() < 1 ) {
			p.sendMessage(main.gameTag + "�cVous avez d�j� utilis� votre pouvoir ou avez attendu trop longtemps (5min) ! Attendez le prochaine �pisode avant de pouvoir le r�utiliser !");
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
		Boolean bavarde = false;
		if (j.getRole() == LGRoles.VoyanteB) bavarde = true;
		
		see(j, TargetJ, bavarde);
	}
	
	private void see(Joueur j, Joueur t, Boolean bavarde) {
		
		Player p = j.getPlayer();
		
		j.setPower(0);
		p.sendMessage(" ");
		p.sendMessage(main.gameTag + "�aLe joueur que vous avez espionn� est " + t.getRole().name);
		
		if (bavarde) {
			Bukkit.broadcastMessage(main.gameTag + "�4La voyante Bavarde � espionn� un joueur qui est... �l" + t.getRole().name);
			Bukkit.broadcastMessage(" ");
		}
	}
}
