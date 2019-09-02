package fr.aiidor.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;

public class CommandSpec implements CommandExecutor {

	private LGUHC main;
	public CommandSpec(LGUHC main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {

			System.out.println("[LOUP-GAROUS] Seul un joueur peut effectuer cette commande !");
			return true;
		}

		Player player = (Player) sender;

		//RACINE == /HELP
		if (args.length == 0) {
			player.sendMessage(main.gameTag + "§b Les commandes :");
			player.sendMessage("§9- /spec watch <Joueur> : §6Regarder les infos d'un joueurs");
			player.sendMessage("§9- /spec tp <Joueur> : §6Tp sur un joueur");
			player.sendMessage("§9- /spec SeeVote : §6Permet de voir les votes");
			player.sendMessage(" ");

			return true;
		}


		//PROTECTION
		if (args.length > 0) {

			if (!main.getSpectator().contains(player)) {
				player.sendMessage(main.gameTag + "§cVous devez être un spectateur pour effectuer ceci");
				return true;
			}

			if (player.getGameMode() != GameMode.SPECTATOR) {
				player.sendMessage(main.gameTag + "§cVous devez être en gamemode Spectateur pour effectuer ceci");
				return true;
			}
		}

		//COMMANDE OI
		if (args[0].equalsIgnoreCase("watch")) {

			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bCette commande permet de regarder les infos d'un joueurs");
				player.sendMessage(" ");
				return true;
			}

			if (args.length > 2) {
				player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit : §e/spec watch <Joueur>");
				return true;
			}

			if (args.length == 2) {
				String targetName = args[1];

				if (Bukkit.getPlayer(targetName) == null) {
					player.sendMessage(main.gameTag + "§cErreur, le joueur "+ targetName + " n'est pas connecté ou n'existe pas !");
					return true;
				} else {

					Player target = Bukkit.getPlayer(targetName);
					if (main.getPlayer(target.getUniqueId()) == null) {
						player.sendMessage(main.gameTag + "§cLe joueur §e" + targetName + " §cne participe pas à la partie !");
						return true;
					}

					player.sendMessage(main.gameTag + "§aVous ouvrez l'inventaire de §2" + targetName);

					Inventory inv = Bukkit.createInventory(null, 54, "§2Inventaire : §a" + targetName);
					inv.setContents(target.getInventory().getContents());

					for (int i = 36; i < 45; i ++) {
						inv.setItem(i, vitre(11));
					}

					int i = 45;
					for (ItemStack it : target.getInventory().getArmorContents()) {
						if (it != null) {
							inv.setItem(i, it);
						}
						i++;
					}

					int health = (int) target.getHealth();
					int food = (int) target.getFoodLevel();

					inv.setItem(50, getItem(Material.BREAD, "§5Vie : §d" + health + " §5/§d " + target.getMaxHealth(), health, true));
					inv.setItem(51, getItem(Material.BOOK, "§9Rôle : §b" + main.getPlayer(target.getUniqueId()).getRole().name , true));
					inv.setItem(52, getItem(Material.EXP_BOTTLE, "§eExperience : §a" + target.getLevel() , target.getLevel(), true));
					inv.setItem(53, getItem(Material.COOKED_CHICKEN, "§6Faim : §e" + food , food,  true));

					player.sendMessage(" ");
					player.openInventory(inv);
					return true;
				}
			}
		}

		//COMMANDE TP
		if (args[0].equalsIgnoreCase("tp")) {

			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bCette commande permet de se tp à un joueur");
				player.sendMessage(" ");
				return true;
			}

			if (args.length > 2) {
				player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit : §e/spec tp <Joueur>");
				return true;
			}

			if (args.length == 2) {
				String targetName = args[1];

				if (Bukkit.getPlayer(targetName) == null) {
					player.sendMessage(main.gameTag + "§cErreur, le joueur "+ targetName + " n'est pas connecté ou n'existe pas !");
					return true;
				} else {

					Player target = Bukkit.getPlayer(targetName);
					player.sendMessage(main.gameTag + "§aVous vous téléporter sur §2" + targetName);
					player.sendMessage(" ");

					player.teleport(target.getLocation());
					return true;
				}
			}
		}

		//COMMANDE SEE VOTE
		if (args[0].equalsIgnoreCase("SeeVote")) {
			if (args.length == 1) {
				if (!main.canSeeVote) {
					player.sendMessage(main.gameTag + "§cVous ne pouvez pas encore voir les votes !");
					return true;
				}

				player.sendMessage(main.gameTag + "§bLes résultats du vote : ");
				for (Joueur all : main.Players) {
					if (!all.isDead()) {
						String cible;

						if (all.whoVote == null) cible = "---";
						else cible = all.whoVote.getName();

						player.sendMessage("§7" + all.getName() + " §b✉➔§7 " + cible);
					}
				}

				return true;
			}

			player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit : §e/spec seeVote");
			return true;
		}


		return false;
	}

	private ItemStack getItem(Material material, String Name, Boolean enchant) {

		ItemStack Item = new ItemStack(material);
		ItemMeta ItemM = Item.getItemMeta();

		if (enchant) {
			ItemM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		}

		ItemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);

		return Item;
	}

	private ItemStack getItem(Material material, String Name, int number, Boolean enchant) {

		ItemStack Item = new ItemStack(material, number);
		ItemMeta ItemM = Item.getItemMeta();

		if (enchant) {
			ItemM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		}

		ItemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);

		return Item;
	}

	private ItemStack vitre(int color) {

		ItemStack vitre = new ItemStack(Material.STAINED_GLASS_PANE, 1 , (byte) color);
		ItemMeta vitreM = vitre.getItemMeta();

		vitreM.setDisplayName(" ");
		vitre.setItemMeta(vitreM);

		return vitre;
	}
}
