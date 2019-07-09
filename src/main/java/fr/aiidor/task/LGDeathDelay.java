package fr.aiidor.task;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;
import fr.aiidor.game.UHCGame;
import fr.aiidor.roles.LGCamps;
import fr.aiidor.roles.LGRoleManager;
import fr.aiidor.roles.LGRoles;
import fr.aiidor.roles.use.LGEnfantS;
import fr.aiidor.roles.use.LGRevive;
import fr.aiidor.roles.use.LGSorcière;

public class LGDeathDelay extends BukkitRunnable{
	
	private int timer = 12;
	private Player player;
	private Location grave;
	
	private Player killer;
	
	public LGDeathDelay(LGUHC instance, Player player, Location grave, Player killer) {
		
		this.player = player;
		this.grave = grave;
		this.killer = killer;
	}

	@Override
	public void run() {
		
		timer --;
		
		if (timer == 6) {
			if (LGRoleManager.Infect.contains(player.getUniqueId()) && LGRoleManager.Rea.contains(player.getUniqueId())) {
				LGRoleManager.Rea.remove(player.getUniqueId());
				
				LGRoleManager.mortD.remove(player.getUniqueId());
				LGRevive.tp(player.getUniqueId());
				cancel();
				return;
			}	
			
			//SORCIERE
			LGRoleManager.mortD.remove(player.getUniqueId());
			LGRoleManager.mortD.put(player.getUniqueId(), LGCamps.VILLAGEOIS);
			
			for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
				
				if (LGRoles.getRole(uuid) == LGRoles.Sorcière && LGRoleManager.Power.containsKey(uuid)) {
					LGSorcière.ReviveMsg(uuid, player);
				}
			}
		}
		
		//TIMER == 0
		if (timer == 0) {
			
			if (LGRoleManager.Rea.contains(player.getUniqueId())) {
				LGRoleManager.Rea.remove(player.getUniqueId());
				
				LGRoleManager.mortD.remove(player.getUniqueId());
				LGRevive.tp(player.getUniqueId());
				cancel();
				return;
			}
			
			vole(player, killer);
			death(player, grave, killer);
			
			cancel();
        	}
		
	}
	
	public static void death(Player player , Location grave, Player damager) {
		
		//REMOVE
		LGRoleManager.mortD.remove(player.getUniqueId());
		
		LGRoles role = LGRoles.getRole(player.getUniqueId());
		if (role == LGRoles.LG || role == LGRoles.LGB || role == LGRoles.LgInfect || role == LGRoles.LGA || role == LGRoles.VilainPL) {
			
			UHCGame.GroupLimit --;
		}
		
		//DROPS
		ItemStack[] inv = player.getInventory().getContents();
		
		for (ItemStack item : inv) {
			
			if (item != null) {
				grave.getWorld().dropItemNaturally(grave, item);
			}
		}
			
		//EFFETS
		player.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c Malheureusement l'aventure s'arrête maintenant pour vous ! Veuillez ne plus parler jusqu'à la fin de la partie !"); 
		
		if (LGRoles.getRole(player.getUniqueId()) == null) {
			Bukkit.broadcastMessage("§c§l==========§4§k0§c§l==========");
			Bukkit.broadcastMessage("§2Le village à perdu un de ses membres : §l" + player.getName() + "§2 qui n'avais §o pas de rôle !");
			Bukkit.broadcastMessage("§c§l=====================");
		}
		else {
			
			if (LGRoleManager.Voleur.contains(player.getUniqueId())){
				
				Bukkit.broadcastMessage("§c§l==========§4§k0§c§l==========");
				Bukkit.broadcastMessage("§2Le village à perdu un de ses membres : §l" + player.getName() + "§2 qui étais §oVoleur");
				Bukkit.broadcastMessage("§c§l=====================");	
			}
			else {
				
				Bukkit.broadcastMessage("§c§l==========§4§k0§c§l==========");
				Bukkit.broadcastMessage("§2Le village à perdu un de ses membres : §l" + player.getName() + "§2 qui étais §o" + LGRoles.getRoleName(player.getUniqueId()));
				Bukkit.broadcastMessage("§c§l=====================");	
			}

		}

		
		for (Player pl : Bukkit.getOnlinePlayers()) {
	    	pl.playSound(pl.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 8.0F, 0.8F);
	    }
		
		//HEAL + FEED
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(10);

		
		//CLEAR
		player.getInventory().clear();
		
		player.getActivePotionEffects().clear();
		
		
		//REMOVE
		if (LGRoleManager.lg.contains(player.getName())) {
			LGRoleManager.lg.remove(player.getName());
		}
		
		if (LGA.lgM.contains(player.getName())) {
			LGA.lgM.remove(player.getName());
		}
		
		if (LGUHC.getInstance().PlayerInGame.contains(player.getUniqueId())) {
			LGUHC.getInstance().PlayerInGame.remove(player.getUniqueId());
		}
		
		//ENFANT SAUVAGE
		if (LGRoleManager.model.containsKey(player.getUniqueId())) {
			
			Player enfant = Bukkit.getPlayer(LGRoleManager.model.get(player.getUniqueId()));
			LGCamps.SetCamp(enfant.getUniqueId(), LGCamps.LOUPGAROUS);
			
			LGEnfantS.Transform(player);
		}
		
		//TRUBLION
		if (LGRoles.getRole(player.getUniqueId()) == LGRoles.Trublion) {
			
			for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
				Player pl = Bukkit.getPlayer(uuid);
				
				pl.sendMessage(" ");
				pl.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§9 Le trublion est mort ! Vous êtes donc Tp aléatoirement sur la map !");
				teleport(pl);
			}
		}
		
		
		//COUPLE
		if (LGRoleManager.couple.contains(player.getUniqueId())) {
			for (UUID uuid : LGRoleManager.couple) {
				if (uuid != player.getUniqueId()) {
					
					Player couple = Bukkit.getPlayer(uuid);
					death(couple, couple.getLocation(), null);
					
					Bukkit.broadcastMessage("§b§l[§6§lLOUP-GAROUS§b§l]§9 Dans un élan de chagrin §6" + couple.getName() + "§3 à décidé de le rejoindre dans sa tombe ! §c♥");
					
				}
			}
		}
	}
	
	public static void vole(Player player, Player damager) {
		
		if (damager != null) {
			if (LGRoles.getRole(damager.getUniqueId()) != LGRoles.Voleur) {
				return;
			}
			LGRoleManager.Power.remove(damager.getUniqueId());
			
			LGRoles role = LGRoles.getRole(player.getUniqueId());
			
			LGRoles.SetRole(damager.getUniqueId(), role);
			LGCamps.SetCamp(damager.getUniqueId(), LGRoles.getCamp(role));
			
			
			if (LGRoleManager.Power.containsKey(player.getUniqueId())) {
				
				int Power = LGRoleManager.Power.get(player.getUniqueId());
				LGRoleManager.Power.put(damager.getUniqueId(), Power);
			}
			
			if (LGRoles.getCamp(role) == LGCamps.LOUPGAROUS) {
				
				LGRoleManager.lg.add(damager.getName());
				
				for (String name : LGRoleManager.lg) {
					Player lg = Bukkit.getPlayer(name);
					lg.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c Un nouveau Loup à rejoint votre camp ! Faite /lg role pour plus de détail !");  //NOUVEAU JOUEUR
				}
				
				for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
					
					if (LGRoles.getRole(uuid) == LGRoles.LGA && LGRoleManager.Power.containsKey(uuid)) {
						if (LGRoleManager.Power.containsKey(uuid)) {
							LGA.lgM.add(damager.getName());
						}
					}
				}
			}
			
			if (LGRoleManager.couple.contains(player.getUniqueId())) {
				
				LGRoleManager.couple.add(damager.getUniqueId());
				LGRoleManager.couple.remove(player.getUniqueId());
				
				for (UUID uuid : LGRoleManager.couple) {
					
					if (uuid != damager.getUniqueId()) {
						
						damager.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c ♥§3 Vous êtes amoureux de §6" + LGUHC.getInstance().getNameString(uuid) 
								+ "§3 si il vient à mourrir, vous mourrerez aussitôt !");
						
						Bukkit.getPlayer(uuid).sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c ♥§3 Vous êtes amoureux de §6" + damager.getName() 
								+ "§3 si il vient à mourrir, vous mourrerez aussitôt !");
					}
				}
			}
			
			for(PotionEffect effect:damager.getActivePotionEffects()){damager.removePotionEffect(effect.getType());}
			LGRoleManager.Voleur.add(damager.getUniqueId());
		}
	}
	
	private static void teleport(Player player) {
		
		Random rand = new Random();
		
	    int rangeMax = 300;
	    int rangeMin = -300;

	    World world = player.getWorld();
	    
	    int x = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
	    int z = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
	    int y = world.getHighestBlockYAt(x, z);
	    		
		Location randomLoc = new Location(world, x, y, z);
		
		//ANTI WATER AND LAVA
		while (randomLoc.getBlock().getType() == Material.WATER || randomLoc.getBlock().getType() == Material.LAVA ||
				randomLoc.getBlock().getType() == Material.STATIONARY_WATER || randomLoc.getBlock().getType() == Material.STATIONARY_LAVA) {
			
		   x = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
		   z = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
		   y = world.getHighestBlockYAt(x, z);
		}
		
		player.teleport(randomLoc.add(0, 1, 0));
	}
	

}
