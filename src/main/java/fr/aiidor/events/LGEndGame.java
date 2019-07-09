package fr.aiidor.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.game.UHCState;
import fr.aiidor.roles.LGCamps;
import fr.aiidor.roles.LGRoleManager;
import fr.aiidor.roles.LGRoles;

public class LGEndGame {
	
	public static void CheckWin() {
		
		if (UHCState.isState(UHCState.FINISH)) {
			return;
		}
		
		int vil = 0;
		int lg = 0;
		int lgb = 0;
		
		int assassin = 0;
		
		int couple = 0;
		int cupidon = 0;
		
		for(Player p : Bukkit.getOnlinePlayers()){
			
			if (LGUHC.getInstance().PlayerInGame.contains(p.getUniqueId())) {
				
				UUID uuid = p.getUniqueId();
				
				if (LGCamps.getCamp(uuid) == LGCamps.VILLAGEOIS) {
					vil++;
					if (LGRoles.getRole(uuid) == LGRoles.Cupidon) {
						cupidon++;
					}
				}
				if (LGCamps.getCamp(uuid) == LGCamps.LOUPGAROUS && LGRoles.getRole(uuid) != LGRoles.LGB) {
					lg++;
				}
				if (LGRoles.getRole(uuid) == LGRoles.LGB) {
					lgb++;
				}
				if (LGCamps.getCamp(uuid) == LGCamps.ASSASSIN) {
					assassin++;
				}
				if (LGCamps.getCamp(uuid) == LGCamps.COUPLE) {
					couple++;
				}
				
			}
		}		

		
		//WIN DES VILLAGEOIS
		if (vil > 0 &&
			lg + assassin + lgb + couple  == 0) {
			Bukkit.broadcastMessage("§b§l[§6§lLOUP-GAROUS§b§l]§2 Victoire du village ! Bravo à eux !");
			stopGame(30);
		}
		//WIN DES LG
		if (lg > 0 &&
				vil + assassin + lgb + couple  == 0) {
			Bukkit.broadcastMessage("§b§l[§6§lLOUP-GAROUS§b§l]§4 Victoire des Loups-Garous ! Bravo à eux !");
			stopGame(30);
			}
		//WIN DE L'ASSASSIN
		if (assassin > 0 &&
				vil + lg + lgb + couple == 0) {
			Bukkit.broadcastMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c Victoire du Loups-Blanc ! Bravo à lui !");
			stopGame(30);
			}	
		//WIN DU LGB
		if (lgb > 0 &&
				lg + assassin + vil + couple == 0) {
			Bukkit.broadcastMessage("§b§l[§6§lLOUP-GAROUS§b§l]§e Victoire de l'Assassin ! Bravo à lui !");
			stopGame(30);
			}	
		
		
		//WIN DU COUPLE
		if (couple> 0 && 
				lg + assassin + vil + lgb - cupidon == 0) {
			
			Bukkit.broadcastMessage("§b§l[§6§lLOUP-GAROUS§b§l]§5 Victoire du couple ! Bravo à eux !");
			stopGame(30);
		}
	}
	
	public static void stopGame(int TimeUntilStop) {
		
		if (UHCState.isState(UHCState.FINISH)) {
			return;
		}
		UHCState.setState(UHCState.FINISH);
		
		for (UUID uuid : LGRoleManager.PlayerRole.keySet()) {
			
			if (!LGUHC.getInstance().PlayerInGame.contains(uuid)) {
				String name = LGUHC.getInstance().getNameString(uuid);
				
				if (LGRoleManager.Infect.contains(uuid)) {
					Bukkit.broadcastMessage("§d§l§m" + name + " : §d§m" + LGRoles.getRoleName(uuid) + "§4 (infecté)");
				}
				else if (LGRoleManager.couple.contains(uuid)){
					Bukkit.broadcastMessage("§d§l§m" + name + " : §d§m" + LGRoles.getRoleName(uuid) + "§c ♥");
				}
				else if (LGRoleManager.Voleur.contains(uuid)){
					Bukkit.broadcastMessage("§d§l§m" + name + " : §d§m" + LGRoles.getRoleName(uuid) + "§3 (Voleur)");
				}
				else {
					Bukkit.broadcastMessage("§d§l§m" + name + " : §d§m" + LGRoles.getRoleName(uuid));
				}
				
			}
		}
		
		for (UUID uuid : LGRoleManager.PlayerRole.keySet()) {
			
			if (LGUHC.getInstance().PlayerInGame.contains(uuid)) {
				String name = Bukkit.getPlayer(uuid).getName();
				if (LGRoleManager.Infect.contains(uuid)) {
					Bukkit.broadcastMessage("§d§l" + name + " : §d" + LGRoles.getRoleName(uuid) + " (infecté)");
				}
				else if (LGRoleManager.couple.contains(uuid)){
					Bukkit.broadcastMessage("§d§l" + name + " : §d" + LGRoles.getRoleName(uuid) + "§c ♥");
				}
				else if (LGRoleManager.Voleur.contains(uuid)){
					Bukkit.broadcastMessage("§d§l§m" + name + " : §d§m" + LGRoles.getRoleName(uuid) + "§3 (Voleur)");
				}
				else {
					Bukkit.broadcastMessage("§d§l" + name + " : §d" + LGRoles.getRoleName(uuid));
				}
				
			}
		}
		
		
		//TIMER
		Bukkit.getScheduler().runTaskLater(LGUHC.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				
				for (Player pl : Bukkit.getOnlinePlayers()) {
					
					pl.kickPlayer("§c===============\n§6Fin de la partie !\n§c===============\n§2Merci d'avoir joué !!");
				}
			}
		}, TimeUntilStop * 20);
	}
}
