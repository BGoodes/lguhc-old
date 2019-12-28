package fr.aiidor.task;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.game.UHCState;
import fr.aiidor.role.LGRoles;
import net.minecraft.server.v1_8_R3.EntityLiving;

public class UHCTime extends BukkitRunnable {
	
	private LGUHC main;
	private long time;
	private World world;
	
	private Boolean day = true;
	
	
	public UHCTime(LGUHC main) {
		this.world = main.world;
		this.main = main;
	}
	
	@Override
	public void run() {
		
		if (main.isState(UHCState.FINISH)) {
			cancel();
			return;
		}
		
		time = world.getTime();
		
		if (main.PoisonLess) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.removePotionEffect(PotionEffectType.POISON);
			}
		}
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			EntityLiving cp = ((CraftPlayer)p.getPlayer()).getHandle();
			if (cp.getAbsorptionHearts() != 0) {
				if (!p.hasPotionEffect(PotionEffectType.ABSORPTION)) {
					cp.setAbsorptionHearts(0);
				}
			}
		}
		

		
		if (main.ep > 1) {
			if (main.isNight() && day) {
				
				PFeffect();
				LGPEffect();
				clearEffect();
				
			}
			
			if (main.isDay() && !day) {
				clearEffect();
			}
			
			if (main.DailyCycle) {
				
				if (time > 18500 && time < 23500);
				else world.setTime(time + 1);
				
				if (time > 5000 && time < 13000) {
					world.setTime(time + 1);
				}
			}
		}
		
		if (main.isDay()) day = true;
		if (main.isNight()) day = false;
		
		

	}
	
	private void clearEffect() {
		for (Joueur j : main.Players) {
			if (j.isConnected()) {
				Player p = j.getPlayer();
				
				for (PotionEffect potion : p.getActivePotionEffects()) {
					
					if (!potion.getType().equals(PotionEffectType.NIGHT_VISION)) {
						if (potion.getDuration() >= 10000000) {
							p.removePotionEffect(potion.getType());
						}
					}
				}
				
			}
		}
	}
	
	private void LGPEffect() {
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.LGP && !j.isDead()) {
				j.setPower(1);
			}
		}
	}
	private void PFeffect() {
		
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.PetiteFille && !j.isDead()) {
				
				j.setPower(1);
				
				if (j.isConnected()) {
					Player p = j.getPlayer();
					
					StringBuilder msg = new StringBuilder();
					
					int Joueur = 0;
					
					for (Joueur j2 : main.Players) {
						if (!j2.isDead()) {
							Player pl = j2.getPlayer();
							if (pl != p) {
								
								if (p.getLocation().distance(pl.getLocation()) <= 100) {
									msg.append("§6" +pl.getName()  + "  ");
									
									Joueur ++;
								}
							}
						}
					}
					
					p.sendMessage(" ");
					if (Joueur == 0) {
						p.sendMessage("§cAucun joueur ne se trouve dans un rayon de 100 blocs !");
						return;
					}
					
					if (Joueur == 1) {
						p.sendMessage("§3Le joueur suivant se trouve dans un rayon de 100 blocs : ");
						p.sendMessage(msg.toString());
						return;
					}
					
					p.sendMessage("§3Les joueurs suivants se trouvent dans un rayon de 100 blocs : ");
					p.sendMessage(msg.toString());
				}
			}
		}
	}
}
