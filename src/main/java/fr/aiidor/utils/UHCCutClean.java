package fr.aiidor.utils;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.LGUHC;

public class UHCCutClean implements Listener {
	
	private LGUHC main;
	
	public UHCCutClean(LGUHC main) {
		this.main = main;
	}
	
	@EventHandler
	public void breakBlock(BlockBreakEvent e) {
		
		if (main.run == false) return;
		if (main.cutclean == false) return;
		
		Location loc = e.getBlock().getLocation();
		
		if (e.getBlock().getType() == Material.IRON_ORE) {
			
			Integer choose = new Random().nextInt(10);
			
			if (choose <= 7) {
				ExperienceOrb orb = (ExperienceOrb) loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
				orb.setExperience(1);
			}

			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			
			loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.IRON_INGOT, 1));
			return;
		}
		
		if (e.getBlock().getType() == Material.GOLD_ORE) {
			
			ExperienceOrb orb = (ExperienceOrb) loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
			orb.setExperience(1);
			
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			
			loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_INGOT, 1));
			return;
		}
	}
	
	@EventHandler
	public void onMobDeath(EntityDeathEvent e) {
		
		if (main.run == false) return;
		if (main.cutclean == false) return;
		
		if (e.getEntity().getType() == EntityType.COW) {
			
			Random rand = new Random();
			int Drop = rand.nextInt(2);
			
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Material.LEATHER, Drop));
			e.getDrops().add(new ItemStack(Material.COOKED_BEEF, Drop + 1));
			
		}
		
		if (e.getEntity().getType() == EntityType.SHEEP) {
			
			Random rand = new Random();
			int Drop = rand.nextInt(1);
			
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Material.COOKED_MUTTON, Drop + 1));
			
		}
		
		if (e.getEntity().getType() == EntityType.PIG) {
			
			Random rand = new Random();
			int Drop = rand.nextInt(2) + 1;
			
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Material.GRILLED_PORK, Drop + 1));
			
		}
		
		
		
		if (e.getEntity().getType() == EntityType.CHICKEN) {
			
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Material.COOKED_CHICKEN, 1));
			
		}
		
		if (e.getEntity().getType() == EntityType.RABBIT) {
			
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Material.COOKED_RABBIT, 1));
			
		}
	}	
}
