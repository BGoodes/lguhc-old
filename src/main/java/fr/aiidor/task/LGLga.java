package fr.aiidor.task;

import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;

public class LGLga extends BukkitRunnable{
	
	private LGUHC main;
	private Joueur j;
	private int timer = 0;
	
	private int nextReveal = 300;
	
	public LGLga(LGUHC main, Joueur j) {
		this.main = main;
		this.j = j;
	}
	
	@Override
	public void run() {
		timer ++;
		
		if (timer == 300) {
			
			for (Joueur lg : main.getLg()) {
				lg.getPlayer().sendMessage(main.gameTag + "§cUn nouveau joueur à rejoint votre camp ! Faites /lg role pour plus de détails !");
				
				if (lg.getRole() == LGRoles.LGA && lg.getPower() == 1) {
					j.lglist.add(j);
				}
			}
			
			j.setCamp(LGCamps.LoupGarou);
			j.lglist.add(j);
			
			j.getPlayer().sendMessage(" ");
			j.getPlayer().sendMessage(main.gameTag + "§cLes Loups-Garous vous reconnaissent à nouveau !");
			j.getPlayer().sendMessage(" ");
			j.getPlayer().sendMessage(main.gameTag + "§cVotre mémoire vous revient ! Vous connaissez un nouveau nom de Loup ! Faite /lg role pour en savoir plus !");
			
			
			for (Joueur lg : main.getLg()) {
				if (!j.lglist.contains(lg)) {
					j.lglist.add(lg);
				}
			}
			nextReveal = timer + 300;
		}
		
		if (timer == nextReveal) {
			j.getPlayer().sendMessage(" ");
			j.getPlayer().sendMessage(main.gameTag + "§cVotre mémoire vous revient ! Vous connaissez un nouveau nom de Loup ! Faite /lg role pour en savoir plus !");
			
			for (Joueur lg : main.getLg()) {
				if (!j.lglist.contains(lg)) {
					j.lglist.add(lg);
				}
			}
		}
		
		int Test = 0;
		for (Joueur lg : main.getLg()) {
			if (!j.lglist.contains(lg)) {
				Test++;
			}
		}
		
		if (Test == 0) {
			j.setPower(0);
			j.getPlayer().sendMessage(" ");
			j.getPlayer().sendMessage(main.gameTag + "§4Vous vous souvenez à nouveau de tous vos congénères ! Bienvenue chez les loups !");
			
			cancel();
			return;
		}
		
	}
}
