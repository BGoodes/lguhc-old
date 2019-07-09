package fr.aiidor.task;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;
import fr.aiidor.roles.LGRoleManager;

public class LGA extends BukkitRunnable{
	
	private int Timer = 0;
	private int Next = 120; //600
	
	public Player lgaPl;
	public UUID lga;
	public static ArrayList<String> lgM = new ArrayList<>();
	
	public LGA(LGUHC instance, UUID uuid) {
		
		this.lga = uuid;
	}

	
	@Override
	public void run() {
		
		if (Timer == 0) {
			
			lgaPl = Bukkit.getPlayer(lga);
			
			lgM.add(lgaPl.getName());
			lgaPl.sendMessage("§cFaites attention car vos congénères ne connaitrons votre identité que dans 5 minutes ! ");
		}
		//LOAD
		
		Timer ++;
	

		//DEATH
		if (!LGUHC.getInstance().PlayerInGame.contains(lga)) {
			
			cancel();
		}
		
		if (Timer == 60) {
			
			for (String name : LGRoleManager.lg) {
				
				Player lg = Bukkit.getPlayer(name);
				lg.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c Un nouveau Loup à rejoint votre camp ! Faite /lg role pour plus de détail !");  //NOUVEAU JOUEUR
			}
			
			LGRoleManager.lg.add(lgaPl.getName());
			
			for (String name : LGRoleManager.lg) {
				if (!lgM.contains(name)) {
					lgM.add(name);
					break;
				}
			}
			
			lgaPl.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c Les Loups-Garous vous reconnaissent à nouveau !");
			lgaPl.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c Votre mémoire vous revient ! Vous connaissez un nouveau nom de Loup ! Faite /lg role pour en savoir plus !");
		}
		
		//NOUVELLE REVELATION
		if (Timer == Next) {
			
			for (String name : LGRoleManager.lg) {
				if (!lgM.contains(name)) {
					lgM.add(name);
					break;
				}
			}
			
			lgaPl.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c Votre mémoire vous revient ! Vous connaissez un nouveau membre du camps des loups-garous ! Faite /lg role pour avoir plus de détail !");
			Next = Next + 60; //300
		}
		
		
		if (Timer == LGRoleManager.lg.size() * 60 - 60) { //* 5

			lgaPl.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c Vous vous souvenez à nouveau de tous vos congénères ! Bienvenue chez les loups !");
			cancel();
		}
	}
	
	
	
}
