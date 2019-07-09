package fr.aiidor.roles;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;

public class LGSetRole {
	
	private  static ArrayList<UUID> HasRole = new ArrayList<>();
	
	public static void setRole() {
		
		
		for (int i = 0; i < LGUHC.getInstance().PlayerInGame.size(); i++) {
			
			int random = new Random().nextInt(Bukkit.getServer().getOnlinePlayers().size());
			Player p = (Player) Bukkit.getServer().getOnlinePlayers().toArray()[random];
			UUID pl = p.getUniqueId();
			
			//TOUS LES JOUEURS IN GAME
			while (HasRole.contains(pl)) {
				
				if (!(LGUHC.getInstance().PlayerInGame.contains(pl))) { //!
					break;
				}
				random = new Random().nextInt(Bukkit.getServer().getOnlinePlayers().size());
				p = (Player) Bukkit.getServer().getOnlinePlayers().toArray()[random];
				pl = p.getUniqueId();
				
			}
			
			//TOUS LES ROLES
			for (LGRoles role : LGRoles.values()) {
				
				if (role.number >= 1) {
					
					HasRole.add(pl);
					giveRole(pl, role);
					role.number --;
					
					break;
				}
			}
		}
		
	}
	
	
	
	private static void giveRole(UUID uuid, LGRoles role) {
		
		LGRoles.SetRole(uuid, role);
		LGCamps.SetCamp(uuid, LGRoles.getCamp(role));
		
		Player player = Bukkit.getPlayer(uuid);
		
		//LOUP-GAROUS AMNESIQUE
		if (role == LGRoles.LGA) {
			player.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§9 [Privé] Vous êtes §o" + "Simple Villageois" + ".");
			return;
		}
		else {
			player.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§9 [Privé] Vous êtes §o" + LGRoles.getName(role) + ".");
		}
		
		
		LGItem.GiveItem(player, role);
		
		//MESSAGE POUR LES LOUPS
		if (LGRoles.getCamp(role) == LGCamps.LOUPGAROUS) {
			
			LGRoleManager.lg.add(player.getName());
			
		}
	}
}
