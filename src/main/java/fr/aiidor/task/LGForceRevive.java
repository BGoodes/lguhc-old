package fr.aiidor.task;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;

public class LGForceRevive extends BukkitRunnable{
	
	private LGUHC main;
	private Player Target;
	private int timer = 15;
	
	private LGForceRevive(LGUHC main, Player p) {
		this.main = main;
		this.Target = p;
		
		p.sendMessage(main.gameTag + "§bVous allez être mis en Survie dans §f15 §bsecondes !");
	}
	
	@Override
	public void run() {
		timer --;
		
		if (timer == 10) {
			Target.sendMessage(main.gameTag + "§bIl vous reste §f" + timer + " §bsecondes");
		}
		
		if (timer <= 5) {
			Target.sendMessage(main.gameTag + "§f" + timer + " §bSecondes");
		}
		
		if (timer == 0) {
			Target.setGameMode(GameMode.SURVIVAL);
			cancel();
		}
	}
}
