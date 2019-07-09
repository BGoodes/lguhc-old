package fr.aiidor.roles.use;

import java.util.UUID;

import org.bukkit.Bukkit;

import fr.aiidor.LGUHC;
import fr.aiidor.roles.LGCamps;
import fr.aiidor.roles.LGRoles;

public class LGMontreur {
	
	public static void Grrr(UUID uuid) {
		
		if (LGRoles.getRole(uuid) == LGRoles.Montreur) {
			
			for (UUID p : LGUHC.getInstance().PlayerInGame) {
				
				if (Bukkit.getPlayer(uuid).getLocation().distance(Bukkit.getPlayer(p).getLocation()) <= 50) {
					
					if (LGCamps.getCamp(p) == LGCamps.LOUPGAROUS) {
						
						Bukkit.broadcastMessage("§b§l[§6§lLOUP-GAROUS§b§l]§6 Grrrrrrr !");
					}
				}
			}
		}
	}
}
