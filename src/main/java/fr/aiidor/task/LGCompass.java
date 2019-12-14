package fr.aiidor.task;

import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;

public class LGCompass extends BukkitRunnable{
	
	private Joueur a ;
	private Joueur b ;
	private LGUHC main;
	
	public LGCompass(LGUHC main, Joueur a, Joueur b) {
		this.a = a;
		this.b = b;
		this.main = main;
	}
	
	
	@Override
	public void run() {
		
		if (a.isDead() || b.isDead()) {
			cancel();
			return;
		}
		if (a.isConnected() && b.isConnected()) {
			a.getPlayer().setCompassTarget(b.getPlayer().getLocation());
			b.getPlayer().setCompassTarget(a.getPlayer().getLocation());
		}
		
		if (a.getPlayer().getLocation().distance(b.getPlayer().getLocation()) < 10) {
			a.getPlayer().sendMessage(main.gameTag + "§dVous avez retrouvé votre amoureux(se), votre boussole indiquera désormais le 0 0");
			b.getPlayer().sendMessage(main.gameTag +"§dVous avez retrouvé votre amoureux(se), votre boussole indiquera désormais le 0 0");
			
			a.getPlayer().setCompassTarget(main.Spawn);
			b.getPlayer().setCompassTarget(a.getPlayer().getLocation());
			cancel();
			return;
		}
	}
	
	
}
