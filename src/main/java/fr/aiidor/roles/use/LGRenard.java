package fr.aiidor.roles.use;

import org.bukkit.entity.Player;

import fr.aiidor.roles.LGCamps;
import fr.aiidor.roles.LGRoleManager;

public class LGRenard {
	
	public static void Renard(Player p, Player target) {
	
		double distance = p.getLocation().distance(target.getLocation());
		
		if (!LGRoleManager.Power.containsKey(p.getUniqueId())) {
			p.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c Vous avez déjà utilisé vos 3 flaires");
			return;
		}
		
		
		if (distance <= 10) {
			
			PowerRemove(p);
			LGCamps camp = LGCamps.getCamp(target.getUniqueId());
			
			if (camp == LGCamps.LOUPGAROUS) {
				p.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§e Le joueur §f" + target.getName() + "§e appartient au camp des Loups-Garous !");
				return;
			}
			p.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§e Le joueur §f" + target.getName() + "§e appartient au camp des Innocents !");
			return;
		}
		
		p.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c le Joueur ne se trouve pas dans un rayon de 10 blocs.");
		return;
	}
	
	private static void PowerRemove(Player p) {
		
		int Power = LGRoleManager.Power.get(p.getUniqueId());
		Power--;
		LGRoleManager.Power.remove(p.getUniqueId());
		
		if (Power == 0) { return; }
		LGRoleManager.Power.put(p.getUniqueId(), Power);
		
	}
}
