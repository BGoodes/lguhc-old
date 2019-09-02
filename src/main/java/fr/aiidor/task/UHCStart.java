package fr.aiidor.task;

import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;
import fr.aiidor.effect.Sounds;
import fr.aiidor.effect.Titles;
import fr.aiidor.game.UHCGame;
import fr.aiidor.game.UHCState;
import fr.aiidor.scoreboard.ScoreboardSign;

public class UHCStart extends BukkitRunnable {
	
	private LGUHC main;
	private Integer Timer = 10;
	
	public UHCStart(LGUHC main) {
		this.main = main;
	}
	
	
	@Override
	public void run() {
		
		if (main.cancelStart) {
			main.cancelStart = false;
			cancel();
			Bukkit.broadcastMessage(main.gameTag + "§cAnnulation de la partie");
			
			main.setState(UHCState.WAITING);
			
		    for (Entry<Player, ScoreboardSign> sign : main.boards.entrySet()) {
		    	sign.getValue().setLine(1, "§ePartie en Attente !");
		     }
		    
			return;
		}
		
		if (Timer == 60) {
			Bukkit.broadcastMessage(main.gameTag + "§9Début de la partie dans §61 minute §9!");
			for (Player pl : Bukkit.getOnlinePlayers()) {
				new Sounds(pl).PlaySound(Sound.SHOOT_ARROW);
			}
			
		}
		
		if (Timer == 45 || Timer == 30 || Timer == 20 || Timer == 15) {
			
			Bukkit.broadcastMessage(main.gameTag + "§9Début de la partie  dans §6" + Timer + " §9s");
			for (Player pl : Bukkit.getOnlinePlayers()) {
				new Sounds(pl).PlaySound(Sound.SHOOT_ARROW);
			}
			
		}
		
		if (Timer <= 10) {
			Bukkit.broadcastMessage(main.gameTag + "§9Début de la partie  dans §6" + Timer + " §9s");
			for (Player pl : Bukkit.getOnlinePlayers()) {
				new Sounds(pl).PlaySound(Sound.NOTE_PLING);
			}
			
		}
		
		if (Timer == 0) {
			cancel();
			Start();
			return;
		}
		
	    for (Entry<Player, ScoreboardSign> sign : main.boards.entrySet()) {
	    	sign.getValue().setLine(1, "§6Lancement : §e" + Timer + "s");
	     }
	    
		Timer --;
		
		main.world.setTime(23500);
	}
	
	private void Start() {
		
		Bukkit.broadcastMessage(main.gameTag + "§6Début de la partie !");
		Bukkit.broadcastMessage(" ");
		main.setState(UHCState.PREGAME);
		
		main.wb.setSize(main.Map * 2);
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
			main.reset(pl);
			
			if (main.Spectator.contains(pl.getUniqueId())) {
				pl.setGameMode(GameMode.SPECTATOR);
				
				pl.sendMessage("§6===============§k§60§6===============");
				pl.sendMessage("§bVous êtes spectateur : §9faites /spec §bpour accéder aux commandes des spectateurs !");
				pl.sendMessage("§6===============§k§60§6===============");
				pl.sendMessage(" ");
				
			} else {
				pl.setGameMode(GameMode.SURVIVAL);
				main.PlayerInGame.add(pl.getUniqueId());
				
				main.randomTp(pl, main.Map);
				
				if (main.startItem != null) {
					int slot = 0;
					for (ItemStack it : main.startItem) {
						if (it != null) {
							if (slot == 36) pl.getInventory().setHelmet(it);
							else if (slot == 37) pl.getInventory().setChestplate(it);
							else if (slot == 38) pl.getInventory().setLeggings(it);
							else if (slot == 39) pl.getInventory().setBoots(it);
							else pl.getInventory().setItem(slot, it);
							
							pl.updateInventory();
						}
						
						slot ++;
						
						if (slot == 40) break;
					}
				}
				
			}
		}
		
		for (UUID uuid : main.PlayerInGame) {
			Player p = Bukkit.getPlayer(uuid);
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, true, true));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 254, true, true));
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 254, false, false));
			
			if (main.fun && main.goneFishin) {
				goneFishin(p);
			}
			
			new Titles().sendTitle(p, "§2Lancement .", "§6", 60);
		}
		
		
	    for (Entry<Player, ScoreboardSign> sign : main.boards.entrySet()) {
	    	sign.getValue().setLine(1, "§aInitialisation de la partie");
	     }
	    
	    
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				
				UHCGame task = new UHCGame(main);
				task.runTaskTimer(main, 0, 20);
				for (UUID uuid : main.PlayerInGame) {
					Player p = Bukkit.getPlayer(uuid);
					for(PotionEffect effect:p.getActivePotionEffects()){p.removePotionEffect(effect.getType());}
					
					new Titles().sendTitle(p, "§cLG-UHC", "§6", 60);
				}
			}
		}, 200);
	}
	
	private void goneFishin(Player p) {
		ItemStack rod = new ItemStack(Material.FISHING_ROD);
		ItemMeta rodM = rod.getItemMeta();
		
		rodM.addEnchant(Enchantment.LUCK, 250, true);
		rodM.addEnchant(Enchantment.DURABILITY, 250, true);
		rod.setItemMeta(rodM);
		
		p.getInventory().addItem(rod);
		p.getInventory().addItem(new ItemStack(Material.ANVIL, 20));
	}
	
}
