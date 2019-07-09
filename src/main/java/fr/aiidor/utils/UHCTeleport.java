package fr.aiidor.utils;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class UHCTeleport {

	public static void tpRandom(Player p) {
		
		Random rand = new Random();
		
	    int rangeMax = 1000;
	    int rangeMin = -1000;

	    World world = p.getWorld();
	    
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
		
		p.teleport(randomLoc.add(0, 1, 0));
	}

}
