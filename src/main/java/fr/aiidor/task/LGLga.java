package fr.aiidor.task;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;

public class LGLga extends BukkitRunnable{
	
	private final LGUHC main;
	private final Joueur j;
	private int timer = 0;
	
	private int nextReveal = 300;
	
	public LGLga(LGUHC main, Joueur j) {
		this.main = main;
		this.j = j;
	}
	
	@Override
	public void run() {
		timer ++;
		
		if (j.isDead()) {
			cancel();
		}
		
		if (timer == 5) {
			j.sendDesc();
		}
		
		if (timer == 300) {
			
			j.setPower(2);
			
			main.addLg(j);
			
			j.lglist.add(j);
			
			if (j.isConnected()) {
				j.getPlayer().sendMessage(" ");
				j.getPlayer().sendMessage(main.gameTag + "§cLes Loups-Garous vous reconnaissent à nouveau !");
			}

			
			if (main.getLgOff().size() == 0 || main.getLgOff().size() == 1 || main.getLgOff().size() == 2) {
				if (j.isConnected()) {
					j.getPlayer().sendMessage(" ");
					j.getPlayer().sendMessage(main.gameTag + "§4Vous vous souvenez à nouveau de tous vos congénères ! Bienvenue chez les loups !");
				}
				
				j.setPower(3);
				cancel();
				return;
			}
		}
		
		//REVEAL -------------
		if (timer == nextReveal) {
			ArrayList<Joueur> all = new ArrayList<>();
			
			for (Joueur lg : main.getLgOff()) {
				if (!j.lglist.contains(lg)) {
					all.add(lg);
				}
			}
			
			if (all.size() != 0) {
				j.lglist.add(all.get(new Random().nextInt(all.size())));
				
				if (j.isConnected()) {
					j.getPlayer().sendMessage(" ");
					j.getPlayer().sendMessage(main.gameTag + "§cVotre mémoire vous revient ! Vous connaissez un nouveau nom de Loup ! Faite /lg role pour en savoir plus !");
				}
			}
			
			nextReveal = nextReveal + 300;
		}
		// ------------------
		
		boolean Test = true;
		for (Joueur lg : main.getLgOff()) {
			if (!j.lglist.contains(lg)) {
				Test = false;
				break;
			}
		}
		
		if (Test) {
			if (j.isConnected()) {
				j.getPlayer().sendMessage(" ");
				j.getPlayer().sendMessage(main.gameTag + "§4Vous vous souvenez à nouveau de tous vos congénères ! Bienvenue chez les loups !");
			}
			
			j.setPower(3);
			
			cancel();
			return;
		}
		
	}
}
