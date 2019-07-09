package fr.aiidor.roles.use;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import fr.aiidor.roles.LGCamps;
import fr.aiidor.roles.LGRoleManager;

public class LGRevive{
	
	
	public static void ancienRevive(Player ancien, Player killer) {
		
		tp(ancien.getUniqueId());
		ancien.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§b Votre pouvoir vous à sauvé !");
		
		
		if (killer == null || LGCamps.getCamp(killer.getUniqueId()) != LGCamps.LOUPGAROUS) {
			
			ancien.setHealth(10);
			ancien.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10);
			
			LGRoleManager.Power.remove(ancien.getUniqueId());
			
			return;
			
		}
	}
	
	
	
	
	//TP
	public static void tp(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		
		Random rand = new Random();
		
	    int rangeMax = 300;
	    int rangeMin = -300;

	    World world = player.getWorld();
	    
	    int x = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
	    int z = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
	    int y = world.getHighestBlockYAt(x, z);
	    		
		Location randomLoc = new Location(world, x, y, z);
		
		//ANTI WATER AND LAVA
		while (randomLoc.getBlock().getType() == Material.WATER || randomLoc.getBlock().getType() == Material.LAVA ||
				randomLoc.getBlock().getType() == Material.STATIONARY_WATER || randomLoc.getBlock().getType() == Material.STATIONARY_LAVA) {
			
		   x = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
		   z = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
		   y = world.getHighestBlockYAt(x, z);
		}
		
		for(PotionEffect effect:player.getActivePotionEffects()){player.removePotionEffect(effect.getType());}
		player.teleport(randomLoc.add(0, 1, 0));
		player.setGameMode(GameMode.SURVIVAL);
	}
	
}
