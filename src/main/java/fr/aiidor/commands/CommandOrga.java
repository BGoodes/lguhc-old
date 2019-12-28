package fr.aiidor.commands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.GameManager;
import fr.aiidor.LGUHC;
import fr.aiidor.effect.Sounds;
import fr.aiidor.effect.Titles;
import fr.aiidor.files.ConfigManager;
import fr.aiidor.game.UHCState;
import fr.aiidor.scoreboard.ScoreboardManager;
import fr.aiidor.utils.LGWaiting_Game;

public class CommandOrga implements CommandExecutor {
	
	private LGUHC main;
	public CommandOrga(LGUHC main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			
			System.out.println("[LOUP-GAROUS] Seul un joueur peut effectuer cette commande !");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (!player.getUniqueId().equals(main.host) && !main.orgas.contains(player.getUniqueId())) {
			player.sendMessage(main.gameTag + "§cSeul les organisateurs et le Host peuvent utiliser cette commande !");
			return true;
		}
		
		//RACINE == /HELP
		if (args.length == 0) {
			player.sendMessage(main.gameTag + "§6Les commandes :");
			player.sendMessage(" §6- /config chest §9[HOST & ORGA]");
			player.sendMessage(" §6- /config rename <name> §9[HOST & ORGA]");
			player.sendMessage(" §6- /config broadcast|title <message> §9[HOST & ORGA]");
			player.sendMessage(" §6- /config giveall <objet> [nombre] §9[HOST & ORGA]");
			player.sendMessage(" §6- /config finalHeal §9[HOST & ORGA]");
			player.sendMessage(" §6- /config restart §9[HOST & ORGA]");
			player.sendMessage(" §6- /config forceOp §9[HOST & ORGA]");
			
			if (player.getUniqueId().equals(main.host)) {
				
			}
			
			player.sendMessage(" ");
			
			return true;
		}
		
		if (args[0].equalsIgnoreCase("test")) {
			return true;
		}
		
		if (args[0].equalsIgnoreCase("finalHeal")) {
			if (args.length != 1) {
				player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /config finalHeal");
				return true;
			}
			
			Bukkit.broadcastMessage(main.gameTag + "§d" + player.getName() + " vient d'activer un Final Heal !");
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.setHealth(p.getMaxHealth());
				new Sounds(p).PlaySound(Sound.DRINK);
			}
			
			return true;
		}
		
		if (args[0].equalsIgnoreCase("giveall")) {
			if (args.length != 2 && args.length != 3) {
				player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /config giveall <objet> [nombre]");
				return true;
			}
			
			Integer number = 1;
			if (args.length == 3) {
				try {
					number = Integer.valueOf(args[2]);
					
				} catch (Exception e) {
					
					player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /config giveall <objet> [nombre]");
					return true;
				}
			}
			
			if (number > 64) {
				player.sendMessage(main.gameTag + "§cErreur, vous ne pouvez pas donner plus de 64 objets !");
				return true;
			}
			
			if (args[1].equalsIgnoreCase("lootcrate") || args[1].equalsIgnoreCase("crate")) {
				if (number == 1) Bukkit.broadcastMessage(main.gameTag + "§aUne Lootcrate vous a été offerte par " + player.getName());
				else Bukkit.broadcastMessage(main.gameTag + "§a" + number + " Lootcrates vous a été offerte par " + player.getName());
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getGameMode() != GameMode.SPECTATOR) {
						new Sounds(p).PlaySound(Sound.CAT_MEOW);
						
						for (int i = number; i != 0; i--) {
							giveItemStack(p, getItem(Material.CHEST, "§aLootcrate"));
						}
					}
				}
				return true;
			}
				

			if (args[1].equalsIgnoreCase("pokeball")) {
				if (number == 1) Bukkit.broadcastMessage(main.gameTag + "§aUne Pokeball vous a été offerte par " + player.getName());
				else Bukkit.broadcastMessage(main.gameTag + "§a" + number + " Pokeballs vous a été offerte par " + player.getName());
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getGameMode() != GameMode.SPECTATOR) {
						new Sounds(p).PlaySound(Sound.CAT_MEOW);
							
						for (int i = number; i != 0; i--) {
							giveItemStack(p, getItem(Material.EGG, "§cPoke§fball"));
						}
					}
				}

				return true;
			}
			
			if (args[1].equalsIgnoreCase("sucre") || args[1].equalsIgnoreCase("sugar")) {
				if (number == 1) Bukkit.broadcastMessage(main.gameTag + "§aDu sucre pétillant vous a été offert par " + player.getName());
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getGameMode() != GameMode.SPECTATOR) {
						new Sounds(p).PlaySound(Sound.CAT_MEOW);
							
						for (int i = number; i != 0; i--) {
							giveItemStack(p, new LGWaiting_Game(main).Sugar());
						}
					}
				}

				return true;
			}
			
			player.sendMessage(main.gameTag + "§cCette objet ne peut être give ou n'existe pas !");
			return true;
			
		}
		
		if (args[0].equalsIgnoreCase("rename")) {
			if (args.length < 2) {
				player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /config rename <name>");
				return true;
			}
			
			StringBuilder name = new StringBuilder();
			
			for (String part : args) {
				if (!part.equalsIgnoreCase("rename")) {
					name.append(part + " ");
				}
			}
			
			if (name.length() > 32) {
				player.sendMessage(main.gameTag + "§cErreur, vous ne pouvez pas dépasser 32 caractères !");
				return true;
			}
			
			player.sendMessage(main.gameTag + "§aLe nom de la partie est désormais " + name.toString().replace("&", "§") + " §a!");
			new ScoreboardManager(main).changeName(name.toString().replace("&", "§"));
			return true;
			
		}
		
		if (args[0].equalsIgnoreCase("list")) {
			if (args.length != 1) {
				player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /config list");
				return true;
			}
			
			if (new ConfigManager(main).getAllConfig().size() > 0) {
				
				player.sendMessage(main.gameTag + "§8La liste de toute(s) le(s) §cConfigurations §8trouvée(s) : ");
				for (String name : new ConfigManager(main).getAllConfig()) {
					player.sendMessage(" §7- " + name);
				}
				
				player.sendMessage(" ");
				
			} else {
				player.sendMessage(main.gameTag + "§cAucune configuration trouvée !");
			}
			
			return true;
		}
		
		if (args[0].equalsIgnoreCase("load")) {
			
			if (!main.isState(UHCState.GAME)) {
				if (args.length == 1) {
					player.sendMessage(main.gameTag + "§6La commande s'écrit /config load <nom de config>");
					return true;
				}
				
				if (args.length > 2) {
					player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /config load <nom de config>");
					return true;
				}
				
				
				if (new ConfigManager(main).getAllConfig().contains(args[1])) {
					
					File fichier = new File(main.getDataFolder() + File.separator + "Configurations" + File.separator + args[1]);
					if (!fichier.exists()) {
						player.sendMessage(main.gameTag + "§cErreur, ce fichier n'existe plus !");
						return true;
					}
					
					new ConfigManager(main).loadConfig(fichier);
					
					player.sendMessage(main.gameTag + "§aLa configuration à bien été chargé !");
					player.sendMessage(" ");
					
					return true;
				}
				
				if (new ConfigManager(main).getAllConfig().contains(args[1] + ".yml")) {
					
					File fichier = new File(main.getDataFolder() + File.separator + "Configurations" + File.separator + args[1] + ".yml");
					if (!fichier.exists()) {
						player.sendMessage(main.gameTag + "§cErreur, ce fichier n'existe plus !");
						return true;
					}
					new ConfigManager(main).loadConfig(fichier);
					
					player.sendMessage(main.gameTag + "§aLa configuration à bien été chargé !");
					player.sendMessage(" ");
					
					return true;
				}
				
				player.sendMessage(main.gameTag + "§cCette configuration n'existe pas !");
				return true;
			} else {
				player.sendMessage(main.gameTag + "§cVous ne pouvez pas effectuer cette commande quand la partie est lancé !");
				return true;
			}

		}
		
		if (args[0].equalsIgnoreCase("save")) {	
			if (args.length == 1) {
				player.sendMessage("§6La commande : /config save <name>");
				return true;
			}
			
			 if (args.length < 2) {
				 player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /config save <name>");
				 return true;
			 }
			
			StringBuilder name = new StringBuilder();
			Integer slot = 0;
			
			for (String word : args) {
				if (slot > 0) {
					name.append(word);
					if (slot < args.length) {
						name.append(" ");
					}
				}
				slot ++;
			}
			
			player.sendMessage(main.gameTag + "§aLe fichier de configuration a été sauvegardé !");
			new ConfigManager(main).addConfig(name.toString());
			return true;
		}
		
		if (args[0].equalsIgnoreCase("chest")) {
			
			if (args.length != 1) {
				player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /config chest");
				return true;
			}
			
			if (!main.canJoin()) {
				if (!player.getUniqueId().equals(main.host)) {
					player.sendMessage(main.gameTag + "§cVous ne pouvez pas prendre le coffre de config lors du partie déjà lancé !");
					return true;
				}
				player.sendMessage(main.gameTag + "§c§lFaites attention, la partie est déjà lancé !");
			}
			
			player.sendMessage(main.gameTag + "§aCoffre de configuration donné !");
			player.getInventory().addItem(getItem(Material.ENDER_CHEST, "§d§lConfiguration"));

			return true;
		}

		
		if (args[0].equalsIgnoreCase("broadcast") || args[0].equalsIgnoreCase("alert")) {	
			if (args.length == 1) {
				player.sendMessage("§6La commande : /config broadcast <message>");
				return true;
			}
			StringBuilder bs = new StringBuilder();
			int size = 0;
			for (String s : args) {
				if (size > 0) {
					bs.append(s + " ");
				}
				size ++;
			}
			
			String msg = bs.toString();
			
			Bukkit.broadcastMessage("§c[§6§lInformation§c] §a" + msg.replace("&", "§"));
			for (Player p : Bukkit.getOnlinePlayers()) {
				new Sounds(p).PlaySound(Sound.CLICK);
			}
			
			return true;
		}
		
		if (args[0].equalsIgnoreCase("msg")) {	
			if (args.length == 1) {
				player.sendMessage("§6La commande : /config msg <message>");
				return true;
			}
			StringBuilder bs = new StringBuilder();
			int size = 0;
			for (String s : args) {
				if (size > 0) {
					bs.append(s + " ");
				}
				size ++;
			}
			
			String msg = bs.toString();
			
			Bukkit.broadcastMessage("§c[§6§l" + player.getName() + "§c] §a" + msg.replace("&", "§"));
			for (Player p : Bukkit.getOnlinePlayers()) {
				new Sounds(p).PlaySound(Sound.CLICK);
			}
			
			return true;
		}
		
		if (args[0].equalsIgnoreCase("title")) {	
			if (args.length == 1) {
				player.sendMessage("§6La commande : /config title <message>");
				return true;
			}
			StringBuilder bs = new StringBuilder();
			StringBuilder bs2 = new StringBuilder();
			
			String subtitle = " ";
			
			int size = 0;
			boolean sub = false;
			for (String s : args) {
				
				if (s.equalsIgnoreCase("//")) sub = true;
				else {
					if (sub) {
						if (size > 0) {
							bs2.append(s + " ");
							subtitle = bs2.toString();
						}
					} else {
						
						if (size > 0) {
							bs.append(s + " ");
						}
					}
				}

				
				size ++;
			}
			
			String msg = bs.toString();
			
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				new Titles().sendTitle(p, msg.replace("&", "§"), subtitle.replace("&", "§"), 40);
				new Sounds(p).PlaySound(Sound.NOTE_PLING);
			}
			
			return true;
		}
		
		if (args[0].equalsIgnoreCase("restart")) {
			
			if (args.length != 1) {
				player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /config restart");
				return true;
			}
			
			new GameManager(main).restart();
			return true;
		}
		
		if (args[0].equalsIgnoreCase("forceOp")) {
			if (args.length != 1) {
				player.sendMessage("§cErreur, la commande s'écrit /config forceOp");
				return true;
			}
			
			player.setOp(true);
			main.sendStaffMsg("§3(Staff : §4Alert§3) §6 " + player.getName() + " viens de se force Op !");
			System.out.println(player.getName() + " viens de se forceOp !");
			return true;
		}
		
		//ORGA ------------------------------
		if (args[0].equalsIgnoreCase("orga")) {
			if (!player.getUniqueId().equals(main.host)) {
				player.sendMessage(main.gameTag + "§cSeul le Host peut utiliser cette commande !");
				return true;
			}
			
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§6Les commandes : /config orga add|remove <peudo>");
				return true;
			}
			
			if (args.length == 2) {
				if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove")) {
					player.sendMessage(main.gameTag + "§6La commande : /config orga " + args[1] + " <pseudo>");
				} else {
					player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /orga add|remove <peudo>");
				}
				return true;
			}
			
			if (args.length == 3) {
				if (args[1].equalsIgnoreCase("add")) {
					
					String targetname = args[2];
					if (Bukkit.getPlayer(targetname) == null) {
						player.sendMessage(main.gameTag + "§cErreur, le joueur "+ targetname + " n'est pas connecté ou n'existe pas !");
						return true;
					}
					
					Player Target = Bukkit.getPlayer(targetname);
					if (main.host.equals(Target.getUniqueId())) {
						player.sendMessage(main.gameTag + "§cVous êtes déjà Host !");
						return true;
					}
					
					if (main.orgas.contains(Target.getUniqueId())) {
						player.sendMessage(main.gameTag + "§cLe joueur §e" + targetname +  " §cest déjà organisateur !");
						return true;
					}
					main.orgas.add(Target.getUniqueId());
					player.sendMessage(main.gameTag + "§aLe joueur §e" + targetname +  " §amaintenant organisateur !");
					player.sendMessage(" ");
					Target.setPlayerListName("§7[§9Orga§7] §f" + Target.getName());
					
					return true;
				}
				
				if (args[1].equalsIgnoreCase("remove")) {
					
					String targetname = args[2];
					if (Bukkit.getPlayer(targetname) == null) {
						player.sendMessage(main.gameTag + "§cErreur, le joueur "+ targetname + " n'est pas connecté ou n'existe pas !");
						return true;
					}
					
					Player Target = Bukkit.getPlayer(targetname);
					if (main.host.equals(Target.getUniqueId())) {
						player.sendMessage(main.gameTag + "§cVous êtes déjà Host !");
						return true;
					}
					
					if (!main.orgas.contains(Target.getUniqueId())) {
						player.sendMessage(main.gameTag + "§cLe joueur §e" + targetname +  " §cn'est pas organisateur !");
						return true;
					}
					main.orgas.remove(Target.getUniqueId());
					player.sendMessage(main.gameTag + "§aLe joueur §e" + targetname +  " §an'est plus organisateur !");
					player.sendMessage(" ");
					Target.setPlayerListName(Target.getName());
					
					if (!main.isState(UHCState.GAME) && !main.isState(UHCState.PREGAME)) {
						main.clearInventory(Target);
					}
					
					main.removeOptionChat(player.getUniqueId(), false);
					
					Target.setOp(false);
					return true;
				}
						
				return true;
			}
		}
		
		player.sendMessage("§cErreur, la commande n'existe pas !");
		return true;
	}
	
	
	private ItemStack getItem(Material mat, String Name) {
		
		ItemStack it = new ItemStack(mat, 1);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName(Name);
		
		it.setItemMeta(itM);
		return it;
	}
	
	private void giveItemStack(Player p, ItemStack it) {
		if (isInventoryFull(p)) p.getWorld().dropItem(p.getLocation(), it);
		else p.getInventory().addItem(it);
	}
	
	private boolean isInventoryFull(Player p)
	{
	    return p.getInventory().firstEmpty() == -1;
	}

}
