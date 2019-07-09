package fr.aiidor.task;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;
import fr.aiidor.roles.use.LG;
import fr.aiidor.roles.use.LGPetiteFille;
import fr.aiidor.roles.use.LGroleSolo;

public class LGTime extends BukkitRunnable{
	
	private long time;
	
	public LGTime(LGUHC lguhc) {
	}

	@Override
	public void run() {
		
		time = Bukkit.getWorld("world").getTime();
		Bukkit.getWorld("world").setTime(time + 1);
		
		///////////////////////////////////
		
		//NUIT ///
		if (time == 13000 || time == 13001) {
			for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
				Player pl = Bukkit.getPlayer(uuid);
				
				for(PotionEffect effect: pl.getActivePotionEffects()){
						pl.removePotionEffect(effect.getType());
				}
				
				LGPetiteFille.Scan(pl);
			}
		}
		
		if (time > 13000 && time < 23500 ) {
			
			for (UUID uuid : LGUHC.getInstance().PlayerInGame) {	
				Player pl = Bukkit.getPlayer(uuid);
				
				LG.NightEffect(pl);
				LGPetiteFille.NightEffect(pl);
			}
		}
		
		///////////////////////////////////
		
		//JOUR ///
		if (time == 23500 || time == 23501) {
			for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
				Player pl = Bukkit.getPlayer(uuid);
				
				for(PotionEffect effect: pl.getActivePotionEffects()){
						pl.removePotionEffect(effect.getType());
				}
			}
		}
		
		
		if (time > 23500 || time < 13000) {
			
			for (UUID uuid : LGUHC.getInstance().PlayerInGame) {	
				Player pl = Bukkit.getPlayer(uuid);
				
				LGroleSolo.AssassinBuff(pl);
			}
		}
		
	}
	
}
