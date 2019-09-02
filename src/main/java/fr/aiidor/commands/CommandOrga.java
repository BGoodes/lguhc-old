package fr.aiidor.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.LGUHC;
import fr.aiidor.effect.Sounds;
import fr.aiidor.effect.Titles;

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
			player.sendMessage(" §6- /config chest §9[ORGA & HOST]");
			player.sendMessage(" §6- /config broadcast <message> §9[ORGA & HOST]");
			player.sendMessage(" §6- /config title <message> §9[ORGA & HOST]");
			player.sendMessage(" §6- /config chat on|off §9[ORGA & HOST]");
			
			
			if (player.getUniqueId().equals(main.host)) {
				player.sendMessage(" §6- /config forceOp §c[HOST]");
				player.sendMessage(" §6- /config orga add|remove <pseudo> §c[HOST]");
			}
			
			player.sendMessage(" ");
			
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
				player.sendMessage(main.gameTag + "§cFaites attention, la partie est déjà lancé !");
			}
			
			player.sendMessage(main.gameTag + "§aCoffre de configuration donné !");
			player.getInventory().addItem(getItem(Material.ENDER_CHEST, "§d§lConfiguration"));

			return true;
		}
		
		if (args[0].equalsIgnoreCase("chat")) {
			
			if (args.length != 2) {
				player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /config chat on|off");
				return true;
			}
			
			if (args[1].equalsIgnoreCase("on")) {
				if (main.chat) {
					player.sendMessage(main.gameTag + "§cChat déjà activé !");
				} else {
					main.chat = true;
					Bukkit.broadcastMessage(main.gameTag + " §b>> Chat §a§lActivé");
				}
				return true;
			}
			
			if (args[1].equalsIgnoreCase("off")) {
				if (!main.chat) {
					player.sendMessage(main.gameTag + "§cChat déjà désactivé !");
				} else {
					main.chat = false;
					Bukkit.broadcastMessage(main.gameTag + " §b>> Chat §c§lDésactivé");
				}
			}

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
				new Titles().sendTitle(player, msg.replace("&", "§"), subtitle.replace("&", "§"), 40);
				new Sounds(p).PlaySound(Sound.NOTE_PLING);
			}
			
			return true;
		}
		
		
		//HOST
		if (args[0].equalsIgnoreCase("forceOp")) {
			if (!player.getUniqueId().equals(main.host)) {
				player.sendMessage(main.gameTag + "§cSeul le Host peut utiliser cette commande !");
				return true;
			}
			
			if (args.length != 1) {
				player.sendMessage("§cErreur, la commande s'écrit /config forceOp");
				return true;
			}
			
			player.setOp(true);
			Bukkit.broadcastMessage("§l§c>>> §6" + player.getName() + " §cviens de se forceOp !");
			return true;
		}
		
		//HOST
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
					
					Target.setWhitelisted(true);
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

}
