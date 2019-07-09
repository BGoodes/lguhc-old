package fr.aiidor.effects;

import org.bukkit.Location;
import org.bukkit.Sound;

public class WorldSound {
	
	private Location l;
	
	public WorldSound(Location loc) {
		
		loc = l;
	}
	
	public void PlaySound(Sound s) {
		
		l.getWorld().playSound(l, s, 8, 8);
	}
}
