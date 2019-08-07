package fr.aiidor.utils;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.LGUHC;

public class UHCBlockDrops implements Listener {
	
	@EventHandler
	public void breakBlock(BlockBreakEvent e) {
		
		if (LGUHC.getInstance().Run == false) return;
		
		Location loc = e.getBlock().getLocation();
		
		if (e.getBlock().getType() == Material.IRON_ORE) {
			
			Random rand = new Random();
			int Drop = rand.nextInt(3) + 1;
			
			ExperienceOrb orb = (ExperienceOrb) loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
			orb.setExperience(Drop);
			
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			
			loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.IRON_INGOT, 1));
			return;
		}
		
		if (e.getBlock().getType() == Material.GOLD_ORE) {
			
			Random rand = new Random();
			int Drop = rand.nextInt(3) + 2;
			
			ExperienceOrb orb = (ExperienceOrb) loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
			orb.setExperience(Drop);
			
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			
			loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_INGOT, 1));
			return;
		}
		
		if (e.getBlock().getType() == Material.GRAVEL) {
			
			Random rand = new Random();
			int Drop = rand.nextInt(2) + 1;
			
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			
			loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.ARROW, Drop));
			return;
		}
		
		if (e.getBlock().getType() == Material.SAND) {
			
			
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			
			loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GLASS));
			return;
		}
	}
}
