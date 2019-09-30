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

import fr.aiidor.ConfigManager;
import fr.aiidor.GameManager;
import fr.aiidor.LGUHC;
import fr.aiidor.effect.Sounds;
import fr.aiidor.effect.Titles;
import fr.aiidor.scoreboard.ScoreboardManager;

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
			player.sendMessage(" §6- /config rename <name> §9[ORGA & HOST]");
			player.sendMessage(" §6- /config broadcast|title <message> §9[ORGA & HOST]");
			player.sendMessage(" §6- /config chat on|off §9[ORGA & HOST]");
			player.sendMessage(" §6- /config revive <player> §9[ORGA & HOST]");
			player.sendMessage(" §6- /config load|list|save §9[ORGA & HOST]");
			player.sendMessage(" §6- /config giveall lootcrate §9[ORGA & HOST]");

			if (player.getUniqueId().equals(main.host)) {
				player.sendMessage(" §6- /config forceOp §c[HOST]");
				player.sendMessage(" §6- /config orga add|remove <pseudo> §c[HOST]");
				player.sendMessage(" §6- /config restart §c[HOST]");

			}

			player.sendMessage(" ");

			return true;
		}

		if (args[0].equalsIgnoreCase("giveall")) {
			if (args.length != 2) {
				player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /config giveall <objet>");
				return true;
			}

			if (args[1].equalsIgnoreCase("lootcrate")) {
				Bukkit.broadcastMessage(main.gameTag + "§aUne Lootcrate vous a été offerte par " + player.getName());
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getGameMode() != GameMode.SPECTATOR) {
						new Sounds(p).PlaySound(Sound.CAT_MEOW);
						giveItemStack(p, getItem(Material.CHEST, "§aLootcrate"));
					}
				}
				return true;
			}

			player.sendMessage(main.gameTag + "§cCette objet ne peut être give ou n'existe pas !");
			return true;

		}

		if (args[0].equalsIgnoreCase("rename")) {
			if (args.length != 2) {
				player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /config rename <name>");
				return true;
			}

			player.sendMessage(main.gameTag + "§aLe nom de la partie est désormais " + args[1].replace("&", "§") + " §a!");
			new ScoreboardManager(main).changeName(args[1].replace("&", "§"));
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
				new Titles().sendTitle(p, msg.replace("&", "§"), subtitle.replace("&", "§"), 40);
				new Sounds(p).PlaySound(Sound.NOTE_PLING);
			}

			return true;
		}

		if (args[0].equalsIgnoreCase("revive")) {
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§6La commande s'écrit /config revive <player>");
				return true;
			}

			if (args.length != 2) {
				player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /config revive <player>");
				return true;
			}

			String targetname = args[1];
			if (Bukkit.getPlayer(targetname) == null) {
				player.sendMessage(main.gameTag + "§cErreur, le joueur "+ targetname + " n'est pas connecté ou n'existe pas !");
				return true;
			}

			Player Target = Bukkit.getPlayer(targetname);


			if (!main.PlayerHasRole) {
				if (!main.PlayerInGame.contains(Target.getUniqueId())) {
					player.sendMessage(main.gameTag + "§cErreur, le joueur doit être dans la partie !");
					return true;
				}

				if (!main.Spectator.contains(player.getUniqueId())) {
					player.sendMessage(main.gameTag + "§cErreur, le joueur doit être mort !");
					return true;
				}

				Bukkit.broadcastMessage(main.gameTag + "§e" + targetname + " §6viens d'être rénanimé !");
				main.randomTp(Target, main.wbMax);

				return true;
			}

			player.sendMessage(main.gameTag + "§cVous réanimer les joueurs que lors de l'épisode 1 !");
			return true;
		}

		if (args[0].equalsIgnoreCase("save")) {
			if (args.length == 1) {
				player.sendMessage("§6La commande : /config save <name>");
				return true;
			}

			if (args.length != 2) {
				player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /config save <name>");
				return true;
			}

			player.sendMessage(main.gameTag + "§aFichier de configuration sauvegardé !");
			new ConfigManager(main).addConfig(args[1]);
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

		//ORGA ------------------------------

		if (args[0].equalsIgnoreCase("restart")) {

			if (!player.getUniqueId().equals(main.host)) {
				player.sendMessage(main.gameTag + "§cSeul le Host peut utiliser cette commande !");
				return true;
			}

			if (args.length != 1) {
				player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /config restart");
				return true;
			}

			new GameManager(main).restart();
			return true;
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
