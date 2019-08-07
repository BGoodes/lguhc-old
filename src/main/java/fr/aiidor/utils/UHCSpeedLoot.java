package fr.aiidor.utils;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.LGUHC;

public class UHCSpeedLoot implements Listener{
	
	@EventHandler
	public void onMobDeath(EntityDeathEvent e) {
		
		if (LGUHC.getInstance().Run == false) return;
		
		if (e.getEntity().getType() == EntityType.COW) {
			
			Random rand = new Random();
			int Drop = rand.nextInt(3);
			
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Material.LEATHER, Drop));
			e.getDrops().add(new ItemStack(Material.COOKED_BEEF, Drop + 1));
			
		}
		
		if (e.getEntity().getType() == EntityType.SHEEP) {
			
			Random rand = new Random();
			int Drop = rand.nextInt(2);
			
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Material.LEATHER, Drop));
			e.getDrops().add(new ItemStack(Material.COOKED_BEEF, Drop + 1));
			
		}
		
		if (e.getEntity().getType() == EntityType.PIG) {
			
			Random rand = new Random();
			int Drop = rand.nextInt(2);
			
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Material.LEATHER, Drop));
			e.getDrops().add(new ItemStack(Material.COOKED_BEEF, Drop + 1));
			
		}
		
		if (e.getEntity().getType() == EntityType.CHICKEN) {
			
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Material.COOKED_CHICKEN, 1));
			
		}
	}
}
