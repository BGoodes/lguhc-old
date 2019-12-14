package fr.aiidor.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;

public class ScoreboardKill {
	
	private LGUHC main;
	private Player player;
	
	public ScoreboardKill(LGUHC main, Player player) {
		this.main = main;
		this.player = player;
	}
	
	public void createScoreboard() {
		
		ScoreboardManager sb = Bukkit.getScoreboardManager();
		Scoreboard board = sb.getNewScoreboard();
		
		Objective obj = board.registerNewObjective("Kills", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName("§fTop Kills");
		
		int joueurs = 0;
		
		for (Joueur j : main.Players) {
			if (j.getKills() != 0) {
				if (j.isDead() )obj.getScore("§7" + j.getName() + " ").setScore(j.getKills());
				else obj.getScore("§f" + j.getName() + " ").setScore(j.getKills());
				joueurs++;
			}
		}
		
		int PVE = 0;
		for (Joueur j : main.death) {
			if (j.Killer == null) {
				PVE++;
			}
		}
		
		if (joueurs != 0) {
			obj.getScore("§cPVE ").setScore(PVE);
			player.setScoreboard(board);
			
		} else {
			ScoreboardSign scoreboard = new ScoreboardSign(player, main.gameName);
			scoreboard.create();
			
			scoreboard.setLine(0, "§4");
			scoreboard.setLine(1, "§6Fin de partie !");
			scoreboard.setLine(2, "§b");
			if (PVE != 0) {
				if (PVE > 1) scoreboard.setLine(3, "§cPVE : §f" + PVE + " §ckills");
				else scoreboard.setLine(3, "§cPVE : §f" + PVE + " §c  kill");
			}
			
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
				
				@Override
				public void run() {
					main.boards.put(player, scoreboard);
				}
			}, 5);
			
		}
	}
}
