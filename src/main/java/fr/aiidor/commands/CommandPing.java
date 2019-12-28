package fr.aiidor.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;

public class CommandPing implements CommandExecutor {

	private LGUHC main;
	
	public CommandPing(LGUHC main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0) {
			if (sender instanceof Player) {
				
				Player player = (Player) sender;
			    int ping = ((CraftPlayer) player).getHandle().ping;
			   
			    player.sendMessage(main.gameTag + "§eVous avez §l" + getPingMessage(ping) + "ms §ede ping.");

			} else {
				System.out.println("[LOUP-GAROU] Vous devez etre un joueur pour voir votre ping !");
				return true;
			}
			
			return true;
		} 
		
		if (args.length != 1) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.sendMessage(main.gameTag + "§cLa commande s'écrit /ping OU /ping <pseudo>.");
				
			} else {
				System.out.println("[LOUP-GAROU] La commande s'ecrit /ping <pseudo>");
			}
			
			return true;
		}
		
		if (args.length == 1) {
			
			String targetname = args[0];
			
			if (Bukkit.getPlayer(targetname) == null) {
				if (sender instanceof Player) {
					
					Player player = (Player) sender;
					player.sendMessage(main.gameTag + "§cLe joueur n'est pas connecté OU n'existe pas.");
					
				} else {
					System.out.println("[LOUP-GAROU] Le joueur n'est pas connecte OU n'existe pas.");
				}
				return true;
			}
			
			Player Target = Bukkit.getPlayer(targetname);
		    int ping = ((CraftPlayer) Target).getHandle().ping;
		   
			if (sender instanceof Player) {
				
				Player player = (Player) sender;
				player.sendMessage(main.gameTag + "§e" + Target.getName() + " à §l" + getPingMessage(ping) + "ms §ede ping.");
				
			} else {
				System.out.println("[LOUP-GAROU] " + Target.getName() + " à " + ping + "ms de ping.");
			}
			
			return true;
		}
		
		return false;
	}
	
	
	private String getPingMessage(int ping) {
		
		 String pingMsg = "§f" + ping;
		 
	    if (ping > 200) {
	    	pingMsg = "§4" + ping;
	    }
	    
	    if (ping <= 200) {
	    	pingMsg = "§c" + ping;
	    }
	    
	    if (ping <= 100) {
	    	pingMsg = "§e" + ping;
	    }
	    
	    if (ping <= 50) {
	    	pingMsg = "§a" + ping;
	    }
	    
	    return pingMsg;
	}

}
