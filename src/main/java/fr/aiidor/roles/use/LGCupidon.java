package fr.aiidor.roles.use;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.aiidor.LGUHC;
import fr.aiidor.roles.LGCamps;
import fr.aiidor.roles.LGRoleManager;
import fr.aiidor.roles.LGRoles;

public class LGCupidon implements Listener{
	
	public static void canChoose() {
		
		for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
			if (LGRoles.getRole(uuid) == LGRoles.Cupidon) {
					Bukkit.getPlayer(uuid).sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§b Vous avez 5 minutes pour choisir le couple grâce à la commande /lg love <Pseudo1> <Pseudo2>");
				}
			}
	}
	
	public static void choose(Player a, Player b) {
		
		LGRoleManager.couple.add(a.getUniqueId());
		LGRoleManager.couple.add(b.getUniqueId());
		
		a.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c ♥§3 Vous êtes amoureux de §6" + b.getName() + "§3 si il vient à mourrir, vous mourrerez aussitôt !");
		b.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c ♥§3 Vous êtes amoureux de §6" + a.getName() + "§3 si il vient à mourrir, vous mourrerez aussitôt !");
		
		if (LGCamps.getCamp(a.getUniqueId()) != LGCamps.getCamp(b.getUniqueId())) {
			
			LGCamps.SetCamp(a.getUniqueId(), LGCamps.COUPLE);
			LGCamps.SetCamp(b.getUniqueId(), LGCamps.COUPLE);
		}
		
		Bukkit.getScheduler().runTaskLater(LGUHC.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				
				Cannotchoose();
			}
		}, 20*60*5);
	}
	
	public static void Cannotchoose() {
		
		for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
			if (LGRoles.getRole(uuid) == LGRoles.Cupidon) {
				
				if (LGRoleManager.Power.containsKey(uuid)) {
					
					Player pl =Bukkit.getPlayer(uuid);
					LGRoleManager.Power.remove(uuid);
					pl.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c Vous avez attendu plus de 5 min, vous ne pourrez donc plus utiliser votre pouvoir !");
				}
				
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void OnCompassUse(PlayerInteractEvent e) {
		
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = e.getPlayer();
			
			if (player.getItemInHand().getType() == Material.COMPASS) {
			}
			
			if (!(LGRoleManager.couple.contains(player.getUniqueId()))) {
				return;
			}
			
			
			if (LGRoleManager.couple.contains(player.getUniqueId())) {
				for (UUID uuid : LGRoleManager.couple) {
					if (uuid != player.getUniqueId()) {
						
						Player couple = Bukkit.getPlayer(uuid);
						player.setCompassTarget(couple.getLocation());
					}
				}
			}
		}
	}
}
