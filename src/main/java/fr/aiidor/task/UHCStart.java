package fr.aiidor.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
import fr.aiidor.files.StatAction;
import fr.aiidor.files.StatManager;
import fr.aiidor.game.UHCGame;
import fr.aiidor.game.UHCState;
import fr.aiidor.scoreboard.ScoreboardSign;
import fr.aiidor.utils.LGCivilisation;

public class UHCStart extends BukkitRunnable {
	
	private LGUHC main;
	private Integer Timer = 10;
	
	public UHCStart(LGUHC main) {
		this.main = main;
		
		if (main.start == null) {
			main.start = this;
		} else {
			cancel();
			return;
		}
		
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
			pl.setLevel(Timer);
			main.removeOptionChat(pl.getUniqueId());
		}
	}
	
	
	@Override
	public void run() {
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
			pl.setLevel(Timer);
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
	}
	
	public void stopStarting() {
		cancel();
		
		Bukkit.broadcastMessage(main.gameTag + "§cAnnulation de la partie");
		
		main.setState(UHCState.WAITING);
		
	    for (Entry<Player, ScoreboardSign> sign : main.boards.entrySet()) {
	    	sign.getValue().setLine(1, "§ePartie en Attente !");
	     }
	    
		for (Player pl : Bukkit.getOnlinePlayers()) {
			pl.setLevel(0);
		}
		
		main.start = null;
		return;
	}
	
	private void Start() {
		
		cancel();
		main.start = null;
		
		Bukkit.broadcastMessage(main.gameTag + "§6Début de la partie !");
		Bukkit.broadcastMessage(" ");
		main.setState(UHCState.PREGAME);
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule reducedDebugInfo " + main.reduced.toString());
		
		main.world.setGameRuleValue("naturalRegeneration", "false");
		main.world.setGameRuleValue("keepInventory", "true");
		main.world.setGameRuleValue("showDeathMessages", "false");
		
		main.world.setPVP(false);
		main.world.setTime(23500);
		
		main.wb.setSize(main.Map * 2);
		
		UHCTime task = new UHCTime(main);
		task.runTaskTimer(main, 0, 1);
		
		if (main.Map < main.wbMax) {
			main.wbMax = main.Map  - main.Map/2;
			Bukkit.broadcastMessage(main.gameTag + "§cLa taille finale de la bordure est supérieur à la taille initiale. La taille finale sera désormais de " + main.wbMax);
		}
		
		if (main.fun && main.Civilisation) {
			createCivilisation();
		}
		
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
			
			pl.closeInventory();
			main.reset(pl);
			
			main.removeOptionChat(pl.getUniqueId());
			
			if (main.Spectator.contains(pl.getUniqueId())) {
				pl.setGameMode(GameMode.SPECTATOR);
				
				pl.sendMessage("§6===============§k§60§6===============");
				pl.sendMessage("§bVous êtes spectateur : §9faites /spec §bpour accéder aux commandes des spectateurs !");
				pl.sendMessage("§6===============§k§60§6===============");
				pl.sendMessage(" ");
				
			} else {
				
				pl.setGameMode(GameMode.SURVIVAL);
				main.PlayerInGame.add(pl.getUniqueId());
				
				if (main.fun && main.Civilisation && main.PlayerHasVillage(pl.getUniqueId())) {
					
					pl.teleport(main.getVillage(pl.getUniqueId()).getSpawn());
					pl.sendMessage(main.gameTag + "§eVous faites partie du village §l" + main.getVillage(pl.getUniqueId()).getName() + "§e ! Esperons que vous y vivrez paisiblement !");
					pl.sendMessage(" ");
					
				} else {
					main.randomTp(pl);
				}
				
				if (main.vanillaN && main.noNametag) {
					main.nhide.addEntry(pl.getName());
				}
				
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
				
				new StatManager(main).changeState(pl.getUniqueId(), "nombre_partie", StatAction.Increase);
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
					if (p != null) {
						for(PotionEffect effect:p.getActivePotionEffects()){p.removePotionEffect(effect.getType());}
						
						new Titles().sendTitle(p, "§cLG-UHC", "§6", 60);
						
						if (main.meetup) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 1, true, true));
						}
					}
				}
				
				Bukkit.broadcastMessage(" ");
				Bukkit.broadcastMessage(main.gameTag + "§bDébut de la partie, bonne chance !");
				Bukkit.broadcastMessage(" ");
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
	
	private void createCivilisation() {
		
		List<UUID> pl = new ArrayList<>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!main.Spectator.contains(p.getUniqueId())) {
				pl.add(p.getUniqueId());
			}
		}
		
		if (pl.size() < 2)  {
			main.Civilisation = false;
			Bukkit.broadcastMessage(main.gameTag + "§cIl n'y a pas assez de joueurs pour activer le scénario civilisation !");
			Bukkit.broadcastMessage(" ");
			return;
		}
		
		if (pl.size() < main.CivSize * 2)  {
			Bukkit.broadcastMessage("§e==========CIVILISATION==========");
			
			Bukkit.broadcastMessage(main.gameTag + "§cIl n'y a pas assez de joueurs pour faire des villages de " + main.CivSize + " Joueurs !");
			
			while (pl.size() < main.CivSize * 2) {
				main.CivSize --;
			}
			
			if (main.CivSize == 1) Bukkit.broadcastMessage(main.gameTag + "§bLes villages seront donc constituer de " + main.CivSize + " joueur");
			else Bukkit.broadcastMessage(main.gameTag + "§bLes villages seront donc constituer de " + main.CivSize + " joueurs");
			
			Bukkit.broadcastMessage("§e================================");
			Bukkit.broadcastMessage(" ");
		}
		
		int number = pl.size() / (int) main.CivSize; 
		
		if (pl.size() / main.CivSize != number) {
			number--;
		}
		
		if (number * main.CivSize < pl.size()) {
			number++;
		}
		
		for (int i = number; i != 0; i--) {
			int choose = new Random().nextInt(main.civNames.size());
			int range = main.vilRange;
			
			if (main.Map <= range) {
				range = main.Map;
			}
			
			LGCivilisation civ = new LGCivilisation(main.civNames.get(choose), getVillageSpawn(range));
			
			main.civilisations.add(civ);
			
			main.civNames.remove(choose);
			
			while (pl.size() != 0) {
				if (civ.getSize() == main.CivSize) break;
				
				int pChoose = new Random().nextInt(pl.size());
				
				civ.addPlayer(pl.get(pChoose));
				pl.remove(pChoose);
			}
		}
	}
	
	private Location getVillageSpawn(int range) {
        Random random = new Random();
        
	    int rangeMax = range;
	    int rangeMin = -range;
	    
	    
	    int x = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
	    int z = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
        int y = 150;
        
        return new Location(main.world, x + main.Spawn.getX(), y, z + main.Spawn.getZ());
	}
}
