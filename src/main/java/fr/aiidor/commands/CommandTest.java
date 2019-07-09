package fr.aiidor.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTest implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			
			System.out.println("[LOUP-GAROUS] Seul un joueur peut effectuer cette commande !");
			return true;
		}
		
		return false;
	}

}
