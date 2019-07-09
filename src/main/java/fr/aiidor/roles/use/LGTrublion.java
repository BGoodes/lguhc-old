package fr.aiidor.roles.use;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.roles.LGCamps;
import fr.aiidor.roles.LGRoleManager;
import fr.aiidor.roles.LGRoles;
import fr.aiidor.task.LGA;

public class LGTrublion {
	

	public static void canSwitch() {
		for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
			if (LGRoles.getRole(uuid) == LGRoles.Trublion) {
				
				LGRoleManager.Power.put(uuid, 1);
				Bukkit.getPlayer(uuid).sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§3 C'est le moment ! Vous avez 5min pour échanger le rôle de 2 joueurs avec la commande "
						+ "§9/lg switch <Pseudo1> <Pseudo2> !");
			}
		}
		
		Bukkit.getScheduler().runTaskLater(LGUHC.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				
				cannotSwitch();
			}
		}, 20*60*5);
	}
	
	public static void cannotSwitch() {
		for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
			if (LGRoles.getRole(uuid) == LGRoles.Trublion) {
				
				if (LGRoleManager.Power.containsKey(uuid)) {
					
					Player pl =Bukkit.getPlayer(uuid);
					LGRoleManager.Power.remove(uuid);
					pl.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c Vous avez attendu plus de 5 min, vous pourrez donc plus utiliser votre pouvoir !");
				}
				
			}
		}	
	}
	
	public static void Switch(Player Target1, Player Target2) {
		
		int Power = 0;
		
		LGRoles Role1 = LGRoles.getRole(Target1.getUniqueId());
		LGRoles Role2 = LGRoles.getRole(Target2.getUniqueId());
		
		///
		LGRoles.SetRole(Target1.getUniqueId(), Role2);
		LGCamps.SetCamp(Target1.getUniqueId(), LGRoles.getCamp(Role2));
		
		if (LGRoleManager.Power.containsKey(Target2.getUniqueId())) {
			Power = LGRoleManager.Power.get(Target2.getUniqueId());
			LGRoleManager.Power.put(Target1.getUniqueId(), Power);
		}
		
		
		///
		LGRoles.SetRole(Target2.getUniqueId(), Role1);
		LGCamps.SetCamp(Target2.getUniqueId(), LGRoles.getCamp(Role1));
		
		if (LGRoleManager.Power.containsKey(Target1.getUniqueId())) {
			Power = LGRoleManager.Power.get(Target1.getUniqueId());
			LGRoleManager.Power.put(Target2.getUniqueId(), Power);
		}
		 
		if (LGCamps.getCamp(Target1.getUniqueId()) != LGCamps.getCamp(Target2.getUniqueId())) {
			
			if (LGCamps.LOUPGAROUS == LGRoles.getCamp(Role2)) {
				
				LGRoleManager.lg.add(Target1.getName());
				
				for (String name : LGRoleManager.lg) {
					Player lg = Bukkit.getPlayer(name);
					lg.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c Un nouveau Loup à rejoint votre camp ! Faite /lg role pour plus de détail !");  //NOUVEAU JOUEUR
				}
				
				for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
					
					if (LGRoles.getRole(uuid) == LGRoles.LGA && LGRoleManager.Power.containsKey(uuid)) {
						if (LGRoleManager.Power.containsKey(uuid)) {
							LGA.lgM.add(Target1.getName());
						}
					}
				}
			}
			
			if (LGCamps.LOUPGAROUS == LGRoles.getCamp(Role2)) {
				
				LGRoleManager.lg.add(Target2.getName());
				
				for (String name : LGRoleManager.lg) {
					Player lg = Bukkit.getPlayer(name);
					lg.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c Un nouveau Loup à rejoint votre camp ! Faite /lg role pour plus de détail !");  //NOUVEAU JOUEUR
				}
				
				for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
					
					if (LGRoles.getRole(uuid) == LGRoles.LGA && LGRoleManager.Power.containsKey(uuid)) {
						if (LGRoleManager.Power.containsKey(uuid)) {
							LGA.lgM.add(Target2.getName());
						}
					}
				}
			}
		}
		
		Target1.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§9 Votre vient d'être swiché par le Trublion ! Faite /lg role pour prendre connaissance de votre nouveau rôle !");
		Target2.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§9 Votre vient d'être swiché par le Trublion ! Faite /lg role pour prendre connaissance de votre nouveau rôle !");
		
	}
}
