package fr.aiidor.task;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;
import fr.aiidor.effects.Sounds;
import fr.aiidor.game.UHCGame;
import fr.aiidor.game.UHCState;
import fr.aiidor.utils.UHCTeleport;

public class UHCStart extends BukkitRunnable{

	public static int timer = 10;
	
	private LGUHC main;
	public boolean pause = false;
	
	public UHCStart(LGUHC main) {
		this.main = main;
	}


	@Override
	public void run() {
		
		if (timer == 120) {
			Bukkit.broadcastMessage("§b§l[§6§lUHC§b§l] §3Début de la partie dans §62 minutes §3!");
			
			for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
				Player pl = Bukkit.getPlayer(uuid);
				
				new Sounds(pl).PlaySound(Sound.ENTITY_ARROW_HIT_PLAYER);
			}
		}
		
		
		//TIMER --
		if (!pause) {
			timer --;
		}
		
		//PAS ASSEZ DE JOUEURS
		if (LGUHC.getInstance().PlayerInGame.size() < LGUHC.getInstance().PlayerMin) {
			
			cancel();
			
			timer = 120;
			
			xpReset();
			UHCState.setState(UHCState.WAIT);
			Bukkit.broadcastMessage("§b§l[§6§lUHC§b§l] §cAnnulation de la partie : §6Pas assez de joueurs !");	
		}		
			for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
				Player pl = Bukkit.getPlayer(uuid);
				
				pl.setLevel(timer);
			}
			
			
		
		if (timer == 60) {
			Bukkit.broadcastMessage("§b§l[§6§lUHC§b§l] §3Début de la partie dans §61 minute §3!");
			
			for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
				Player pl = Bukkit.getPlayer(uuid);
				
				new Sounds(pl).PlaySound(Sound.ENTITY_ARROW_HIT_PLAYER);
			}
		}
		
		if (timer == 45 || timer == 30 || timer == 20 || timer == 15) {
			
			Bukkit.broadcastMessage("§b§l[§6§lUHC§b§l] §3Début de la partie  dans §6" + timer + " §3s");
			
			for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
				Player pl = Bukkit.getPlayer(uuid);
				
				new Sounds(pl).PlaySound(Sound.ENTITY_ARROW_HIT_PLAYER);
			}
		}
		
		if (timer <= 10) {
			Bukkit.broadcastMessage("§b§l[§6§lUHC§b§l] §3Début de la partie  dans §6" + timer + " §3s");
			
			
			for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
				Player pl = Bukkit.getPlayer(uuid);
				
				new Sounds(pl).PlaySound(Sound.BLOCK_NOTE_PLING);
			}
		}
		
		
		//START
		if (timer == 0) {
			
			cancel();
			Start();
			
			if (LGUHC.getInstance().Run == false) {
				UHCGame start = new UHCGame(main);
				start.runTaskTimer(main, 0, 20);
			}
			else {
				UHCGame start = new UHCGame(main);
				start.runTaskTimer(main, 0, 20);  //A MODIFIER ==> 20 > 10
			}

		}
	}
	
	private void Start() {
		
		Bukkit.broadcastMessage("§b§l[§6§lUHC§b§l] §3Début de la partie !");
		xpReset();
		
		for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
			Player pl = Bukkit.getPlayer(uuid);
			
			pl.setGameMode(GameMode.SURVIVAL);
			
			UHCTeleport.tpRandom(pl);;
		}
		
		UHCState.setState(UHCState.PREGAME);
		Bukkit.getWorld("world").setTime(23500);
		
		//VERSION RUN
		if (LGUHC.getInstance().Run) {
			for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
				Player pl = Bukkit.getPlayer(uuid);
				
				pl.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 1, false, false), true);
				pl.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 6000, 0, false, false), true);
			}
		}
	}
	
	private void xpReset() {
		
		if (LGUHC.getInstance().PlayerInGame.size() == 0) return;
		
		
		for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
			Player pl = Bukkit.getPlayer(uuid);
			
			pl.setLevel(0);
			pl.setExp(0);
		}
	}

}
