package fr.aiidor.utils;

import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.game.Joueur;

public class UHCNoFall extends BukkitRunnable{
	
	private Joueur j;
	private int timer = 0;
	
	public UHCNoFall(Joueur j) {
		this.j = j;
	}
	@Override
	public void run() {
		timer++;
		j.setNoFall(true);
		
		if (timer == 10) {
			if (!j.salvation) j.setNoFall(false);
			cancel();
			return;
		}
	}
	
	
}
