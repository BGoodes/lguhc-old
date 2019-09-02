package fr.aiidor.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;

public class LGEndGame {
	
	private LGUHC main;
	
	public LGEndGame(LGUHC main) {
		this.main = main;
	}
	
	public void isEnd() {
		
		if (main.isState(UHCState.FINISH)) return;
		
		int vil = 0;
		int lg = 0;
		int lgb = 0;
		
		int assassin = 0;
		int voleur = 0;
		
		int couple = 0;
		int cupidon = 0;
		
		if (main.getJoueurs().size() == 0) {
			Bukkit.broadcastMessage(main.gameTag + "§eVictoire de personne car tout le monde est mort ^^");
			stopGame();
			return;
		}
		
		for (Joueur j : main.getJoueurs()) {
				
				LGCamps camp = j.getCamp();
				LGRoles role = j.getRole();
				
				if (j.hasCouple() && (j.getCamp() != j.getCouple().getCamp())) {
					couple++;
					
				} else {
					if (camp == LGCamps.Village) {
						vil++;
						if (role == LGRoles.Cupidon) {
							cupidon++;
						}
					}
					if (camp == LGCamps.LoupGarou && role != LGRoles.LGB) {
						lg++;
					}
					if (camp== LGCamps.LGB) {
						lgb++;
					}
					if (camp == LGCamps.Assassin) {
						assassin++;
					}
					if (camp == LGCamps.Voleur) {
						voleur++;
					}
				}
		}
		
		//WIN DU VILLAGE
		if (vil > 0 &&
				lg + assassin + lgb + couple + voleur  == 0) {
				Bukkit.broadcastMessage(main.gameTag + "§2Victoire du village ! Bravo à eux !");
				stopGame();
			}
			//WIN DES LG
			if (lg > 0 &&
					vil + assassin + lgb + couple + voleur  == 0) {
				Bukkit.broadcastMessage(main.gameTag + "§cVictoire des Loups-Garous ! Bravo à eux !");
				stopGame();
				}
			
			//WIN DE L'ASSASSIN
			if (assassin > 0 &&
					vil + lg + lgb + couple + voleur == 0) {
				Bukkit.broadcastMessage(main.gameTag + "§eVictoire de l'Assassin ! Bravo à lui !");
				stopGame();
				}	
			
			//WIN DU LGB
			if (lgb > 0 &&
					lg + assassin + vil + couple + voleur == 0) {
				Bukkit.broadcastMessage(main.gameTag + "§4Victoire du Loups-Blanc ! Bravo à lui !");
				stopGame();
				}	
			
			if (voleur > 0 &&
					lg + assassin + vil + couple + lgb == 0) {
				Bukkit.broadcastMessage(main.gameTag + "§eVictoire du Voleur ! Bravo à lui !");
				stopGame();
				}	
			
			//WIN DU COUPLE
			if (couple> 0 && 
					lg + assassin + vil + lgb + voleur - cupidon == 0) {
				
				Bukkit.broadcastMessage(main.gameTag + "§5Victoire du couple ! Bravo à eux !");
				stopGame();
			}
	}
	
	private void stopGame() {
		
		if (main.isState(UHCState.FINISH)) {
			return;
		}
		
		main.setState(UHCState.FINISH);
		
		StringBuilder msg = new StringBuilder();
		
		for (Joueur j : main.death) {
			if (j.Rob) msg.append("§d§l§m" + j.getName() + "§d§m : " + LGRoles.Voleur.name);
			else msg.append("§d§l§m" + j.getName() + "§d§m : " + j.getRole().name);
			
			if (j.isInfect()) msg.append(" §d§m(Infecté)");
			else if (j.getRole() == LGRoles.EnfantS && j.getCamp() == LGCamps.LoupGarou) msg.append(" §d§m(Transformé)");
			if (j.trublion) msg.append(" §b⇋");
			if (j.hasCouple()) msg.append(" §c♥");
			
			Bukkit.broadcastMessage(msg.toString());
			msg = new StringBuilder();
		}
		
		msg = new StringBuilder();
		
		for (Player p : main.getPlayers()) {
			if (main.getPlayer(p.getUniqueId()) != null) {
				Joueur j = main.getPlayer(p.getUniqueId());
				
				if (!j.isDead()) {
					if (j.Rob) msg.append("§d§l" + j.getName() + "§d : " + LGRoles.Voleur.name);
					else msg.append("§d§l" + j.getName() + "§d : " + j.getRole().name);
					
					if (j.isInfect()) msg.append(" §d(Infecté)");
					else if (j.getRole() == LGRoles.EnfantS && j.getCamp() == LGCamps.LoupGarou) msg.append(" §d(Transformé)");
					if (j.trublion) msg.append(" §b⇋");
					if (j.hasCouple()) msg.append(" §c♥");
					
					Bukkit.broadcastMessage(msg.toString());
					msg = new StringBuilder();
				}
			}
		}
		
		
	}
}
