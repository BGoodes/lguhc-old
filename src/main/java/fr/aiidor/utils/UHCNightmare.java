package fr.aiidor.utils;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftWitch;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.LGUHC;

public class UHCNightmare implements Listener {
	
	private LGUHC main;
	
	public UHCNightmare(LGUHC pl) {
		this.main = pl;
	}	
	
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent e) {
		
		if (!main.NightMare) return;
		
		if (e.getEntity() instanceof Witch) {
			
			Witch witch = (Witch) e.getEntity();
			
			witch.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4));
			((CraftWitch) witch).getHandle().b(false);
			
			return;
		}
		
		if (e.getEntity() instanceof Spider) {
			
			Spider spider = (Spider) e.getEntity();
			spider.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
			
			return;
		}
		
		if (e.getEntity() instanceof Creeper) {
			
			Creeper creeper = (Creeper) e.getEntity();
			creeper.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
			
			return;
		}
		
		if (e.getEntity() instanceof Zombie) {
			
			Zombie zombie = (Zombie) e.getEntity();
			zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
			
			return;
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (!main.NightMare) return;
		
		if (e.getEntity() instanceof Zombie) {
			
			if (e.getCause() == DamageCause.FIRE_TICK || e.getCause() == DamageCause.FIRE) {
				if (new Random().nextBoolean()) {
					e.setCancelled(true);
				}
				return;
			}
			
			return;
		}
		
		if (e.getEntity() instanceof Silverfish) {
			if (e.getCause() == DamageCause.BLOCK_EXPLOSION ) {
				
				if (new Random().nextBoolean()) {
					e.setDamage(0);
				}
			}
			
			return;
		}
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if (!main.NightMare) return;
		
		if (e.getEntity() instanceof Spider) {
			
			for (int x = e.getEntity().getLocation().getBlockX()-4; x <= e.getEntity().getLocation().getBlockX() + 4; x++) {
				for (int z = e.getEntity().getLocation().getBlockZ()-4; z <= e.getEntity().getLocation().getBlockZ() + 4; z++) {
					
					if (new Random().nextInt(3) == 0) {
						
						int choose = new Random().nextInt(2);
						if (new Random().nextBoolean()) {
							if (main.world.getBlockAt(x, e.getEntity().getLocation().getBlockY() + choose , z).getType() == Material.AIR) {
								main.world.getBlockAt(x, e.getEntity().getLocation().getBlockY() + choose , z).setType(Material.WEB);
							}
							
						}
						else {
							if (main.world.getBlockAt(x, e.getEntity().getLocation().getBlockY() - choose , z).getType() == Material.AIR) {
								main.world.getBlockAt(x, e.getEntity().getLocation().getBlockY() - choose , z).setType(Material.WEB);
							}
						}
						
					}
				}
			}
			return;
		}
	}
	
	@EventHandler
	public void onBoom(EntityExplodeEvent e) {
		if (!main.NightMare) return;

		
		if (e.getEntity() instanceof Creeper) {
			e.getLocation().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.SILVERFISH);
			e.getLocation().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.SILVERFISH);
			e.getLocation().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.SILVERFISH);
			e.getLocation().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.SILVERFISH);
			e.getLocation().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.SILVERFISH);
			
			e.setCancelled(true);
			e.getLocation().getWorld().createExplosion(e.getLocation(), 4);
			for (Entity ent : e.getLocation().getWorld().getNearbyEntities(e.getLocation(), 2, 2, 2)) {
				if (ent.getType() == EntityType.DROPPED_ITEM) {
					ent.remove();
				}
			}
			return;
		}
	}
	
	
}
