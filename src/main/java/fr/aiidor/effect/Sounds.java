package fr.aiidor.effect;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Sounds {
	
	private Player player;
	
	public Sounds(Player p) {
		
		player = p;
	}
	
	public void PlaySound(Sound s) {
		
		player.playSound(player.getLocation(), s, 1, 1);
	}
}
