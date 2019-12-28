package fr.aiidor.task;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;

public class LGDécoKill extends BukkitRunnable {
	
	private int timer = 0;
	
	private Location grave;
	
	private LGUHC main;
	
	private UUID dead;
	private List<ItemStack> inv;
	private String name;
	
	public LGDécoKill(LGUHC main, UUID dead, String name, Location grave, List<ItemStack> inv) {
		
		this.main = main;
		this.dead = dead;
		this.grave = grave;
		this.inv = inv;
		this.name = name;
	}
	
	
	@Override
	public void run() {
		
		timer++;
		
		if (Bukkit.getPlayer(dead) != null) {
			cancel();
			return;
		}
		
		if (main.Spectator.contains(dead)) {
			cancel();
			return;
		}
		
		if (main.PlayerHasRole) {
			if (!main.hasRole(dead)) {
				cancel();
				return;
			} 
			else {
				if (main.getPlayer(dead).isDead()) {
					cancel();
					return;
				}
			}
		}
		
		if (timer == main.decoTime) {
			
			death();
			
			cancel();
			return;
		}
	}
	
	public void death() {
		if (!main.PlayerHasRole) main.kill(dead, name, grave, inv);
		else new LGDeath(main, dead, null, grave, inv).death();
	}
	
}
