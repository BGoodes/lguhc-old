package fr.aiidor.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.LGUHC;
import fr.aiidor.game.UHCState;
import fr.aiidor.roles.LGCamps;
import fr.aiidor.roles.LGRoleManager;
import fr.aiidor.roles.LGRoles;
import fr.aiidor.task.UHCStart;

public class CommandBypass implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			
			System.out.println("[LOUP-GAROUS] Seul un joueur peut effectuer cette commande !");
			return true;
		}
		
		Player player = (Player) sender;
		
		//RACINE == /HELP
		if (args.length == 0) {
			player.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§3 Les commandes :");
			player.sendMessage("§3- /bypass role <role> §6: Permet de se donner un rôle !");
			player.sendMessage(" ");
			
			return true;
		}
		
		if (args.length == 1) {
			
			
			 if (args[0].equalsIgnoreCase("start")) {
				 
				UHCStart start = new UHCStart(LGUHC.getInstance());
				start.runTaskTimer(LGUHC.getInstance(), 0, 20);
				UHCState.setState(UHCState.STARTING);
				
				player.sendMessage("§b§l[§6§lUHC§b§l]§6 Lancement de la partie !");
				return true;
			}
			 
			 if (args[0].equalsIgnoreCase("config")) {
				 
				Inventory inv = Bukkit.createInventory(null, 27, "§5Menu: §dConfiguration");
				
				inv.setItem(11, getItem(Material.DIAMOND_SWORD, "§6Scénarios"));
				inv.setItem(15, getItem(Material.BOOKSHELF, "§6Rôles"));
				inv.setItem(26, getItem(Material.BARRIER, "§cExit =>"));
				
				player.openInventory(inv);
				return true;
			}
			 
			 
		}
		
		//ROLE
		if(args[0].equalsIgnoreCase("role")) {
			
			//RACINE ++ /HELP ROLE
			if (args.length == 1) {
				player.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§3 Les roles :");
				player.sendMessage("§3- sv §6: Simple villageois");
				player.sendMessage("§3- lg §6: Loup Garous");
				player.sendMessage("§3- vpl §6: Vilain Petit Loup");
				player.sendMessage("§3- lgb §6: Loup Garou Blanc");
				player.sendMessage("§3- ipl §6: Infect Père Des Loups");
				player.sendMessage("§3- ancien §6: Ancien");
				player.sendMessage("§3- Assassin §6: Assassin");
				player.sendMessage(" ");
				
				return true;
			}
			
			if (args.length == 2) {
				
				if (args[1].equalsIgnoreCase("sv")) {
					
					LGRoles.SetRole(player.getUniqueId(), LGRoles.SV);
					LGCamps.SetCamp(player.getUniqueId(), LGCamps.VILLAGEOIS);
					player.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§3 Vous êtes désormais §6" + LGRoles.getName(LGRoles.SV));
					return true;
				}
				
				if (args[1].equalsIgnoreCase("lg")) {
					
					LGRoles.SetRole(player.getUniqueId(), LGRoles.LG);
					LGCamps.SetCamp(player.getUniqueId(), LGCamps.LOUPGAROUS);
					player.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§3 Vous êtes désormais §6" + LGRoles.getName(LGRoles.LG));
					return true;
				}
				
				if (args[1].equalsIgnoreCase("vpl")) {
					
					LGRoles.SetRole(player.getUniqueId(), LGRoles.VilainPL);
					LGCamps.SetCamp(player.getUniqueId(), LGCamps.LOUPGAROUS);
					
					player.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§3 Vous êtes désormais §6" + LGRoles.getName(LGRoles.VilainPL));
					return true;
				}
				
				if (args[1].equalsIgnoreCase("lgb")) {
					
					LGRoles.SetRole(player.getUniqueId(), LGRoles.LGB);
					LGCamps.SetCamp(player.getUniqueId(), LGCamps.LOUPGAROUS);
					
					player.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§3 Vous êtes désormais §6" + LGRoles.getName(LGRoles.LGB));
					return true;
				}
				
				if (args[1].equalsIgnoreCase("ipl")) {
					
					LGRoles.SetRole(player.getUniqueId(), LGRoles.LgInfect);
					LGCamps.SetCamp(player.getUniqueId(), LGCamps.LOUPGAROUS);
					
					player.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§3 Vous êtes désormais §6" + LGRoles.getName(LGRoles.LgInfect));
					return true;
				}
				
				if (args[1].equalsIgnoreCase("ancien")) {
					
					LGRoles.SetRole(player.getUniqueId(), LGRoles.Ancien);
					LGCamps.SetCamp(player.getUniqueId(), LGCamps.VILLAGEOIS);
					
					LGRoleManager.Power.put(player.getUniqueId(), 1);
					player.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§3 Vous êtes désormais §6" + LGRoles.getName(LGRoles.Ancien));
					return true;
				}
				
				if (args[1].equalsIgnoreCase("assassin")) {
					
					LGRoles.SetRole(player.getUniqueId(), LGRoles.Assassin);
					LGCamps.SetCamp(player.getUniqueId(), LGCamps.ASSASSIN);
					
					player.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§3 Vous êtes désormais §6" + LGRoles.getName(LGRoles.Assassin));
					return true;
				}
				
				
				player.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l] §cErreur,§3 Les roles :");
				player.sendMessage("§3- sv §6: Simple villageois");
				player.sendMessage("§3- lg §6: Loup Garous");
				player.sendMessage("§3- vpl §6: Vilain Petit Loup");
				player.sendMessage("§3- lgb §6: Loup Garou Blanc");
				player.sendMessage("§3- ipl §6: Infect Père Des Loups");
				player.sendMessage("§3- ancien §6: Ancien");
				player.sendMessage("§3- Assassin §6: Assassin");
				player.sendMessage(" ");
				return true;
			}
		}

		return false;
	}
	
	private static ItemStack getItem(Material material, String Name) {
		
		ItemStack Item = new ItemStack(material);
		ItemMeta ItemM = Item.getItemMeta();
		
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}

}
