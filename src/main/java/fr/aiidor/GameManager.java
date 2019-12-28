package fr.aiidor;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.effect.Sounds;
import fr.aiidor.effect.Titles;
import fr.aiidor.game.UHCState;
import fr.aiidor.role.LGRoles;
import fr.aiidor.scoreboard.NameManager;
import fr.aiidor.scoreboard.ScoreboardManager;
import fr.aiidor.scoreboard.ScoreboardSign;
import fr.aiidor.scoreboard.TabList;

public class GameManager {
	
	private LGUHC main;
	
	public GameManager(LGUHC main) {
		this.main = main;
	}
	
	public void restart() {
		
		//CANCEL ALL TASK
		Bukkit.getScheduler().cancelAllTasks();
		new ScoreboardManager(main).destroy();
		
		main.setState(UHCState.WAITING);
		main.world.setPVP(true);
		
		main.PlayerHasRole = false;
		main.compo = new ArrayList<>();
		
		main.Spectator = new ArrayList<>();
		main.PlayerInGame = new ArrayList<>();
		main.Players = new ArrayList<>();
		main.death = new ArrayList<>();
		
		main.deathChat = false;
		main.canVote = false;
		main.canSeeVote = false;
		
		main.cancelVote = false;
		main.canSeeList = true;
		
		main.noFall = new ArrayList<>();
		main.cannotVote = new ArrayList<>();
		
		main.ep = 1;
		if (main.start != null) {
			main.start.cancel();
			main.start = null;
		}
		
		main.DiamondPl  = new HashMap<>();
		
		main.civilisations = new ArrayList<>();
		main.addNames();
		
		for (ScoreboardSign scoreboard : main.boards.values()) {
			scoreboard.destroy();
		}
		
		main.boards = new HashMap<>();
		
		//TEAM NHIDE
		main.nhideReset();
		
		//PLAYER EFFECTS
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			new Titles().sendTitle(player, "§fLGUHC", "§dRedémarrage ", 60);
			new Sounds(player).PlaySound(Sound.CAT_PURREOW);
			
			player.teleport(main.Spawn.clone().add(0, 1, 0));
			main.reset(player);
			
			if (player.getUniqueId().equals(main.host) || main.orgas.contains(player.getUniqueId())) {
				player.getInventory().setItem(4, getItem(Material.ENDER_CHEST, "§d§lConfiguration"));
			}
			
			//NAME ------------------------
			player.setPlayerListName(player.getName());
			
			if (player.getUniqueId().equals(main.host)) {
				
				player.sendMessage(main.gameTag + "§dVous êtes le Host de la partie !");
				player.setPlayerListName("§7[§cHOST§7] §f" + player.getName());
			}
			
			if (main.orgas.contains(player.getUniqueId())) {
				
				player.sendMessage(main.gameTag + "§bVous êtes un organisateur de la partie !");
				player.setPlayerListName("§7[§9Orga§7] §f" + player.getName());
			}
			
			//SCOREBOARD ------------------------
			ScoreboardSign scoreboard = new ScoreboardSign(player, main.gameName);
			scoreboard.create();
			scoreboard.setLine(0, "§c");
			scoreboard.setLine(1, "§ePartie en Attente !");
			
			main.boards.put(player, scoreboard);
			
			//TABLIST ---------------------
			new TabList(main).set(player);
			
			//NAME
			new NameManager(player).resetName();
		}
		
		Bukkit.broadcastMessage(main.gameTag + "§6La partie vient d'être réinitialisé !");
		Bukkit.broadcastMessage(" ");
		
		for (LGRoles role : LGRoles.values()) {
			if (role == LGRoles.LG) role.number = 1;
			else role.number = 0;
		}
		
		main.wb.setSize(main.Map);
	}
	
	
	private ItemStack getItem(Material mat, String Name) {
		
		ItemStack it = new ItemStack(mat, 1);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName(Name);
		
		it.setItemMeta(itM);
		return it;
	}
}
