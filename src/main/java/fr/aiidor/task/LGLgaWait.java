package fr.aiidor.task;

import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;
import fr.aiidor.effect.Sounds;
import fr.aiidor.game.Joueur;

public class LGLgaWait extends BukkitRunnable {
	
	private Joueur lga;
	private LGUHC main;
	private int Timer = 0;
	
	public LGLgaWait(LGUHC main, Joueur lga) {
		this.main = main;
		this.lga = lga;
	}

	@Override
	public void run() {
		if (Timer == 900) {
			
			if (lga.isConnected()) {
				
				new Sounds(lga.getPlayer()).PlaySound(Sound.WOLF_GROWL);
				
				lga.getPlayer().sendMessage(main.gameTag + "§bLoups-garous vous reconnaissent désormais ! Maintenant à vous de les reconnaître, vous découvrirez "
						+ "l'identité de l'un de vos congénères à chaque épisode !");
				
				lga.setPower(2);
				main.revealLg(lga);
			}
			
			cancel();
			return;
		}
		
		if (lga.isDead()) {
			cancel();
			return;
		}
	}
	
}
