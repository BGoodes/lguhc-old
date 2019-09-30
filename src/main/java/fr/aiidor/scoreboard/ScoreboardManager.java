package fr.aiidor.scoreboard;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;

public class ScoreboardManager {

	private LGUHC main;

	public ScoreboardManager(LGUHC main) {
		this.main = main;
	}

	public void destroy() {

		for (ScoreboardSign scoreboard : main.boards.values()) {
			scoreboard.destroy();
		}

		main.boards = new HashMap<>();

	}
	public void changeName(String name) {
		main.gameName = name;
		destroy();

		for (Player p : Bukkit.getOnlinePlayers()) {

			ScoreboardSign scoreboard = new ScoreboardSign(p, main.gameName);
			scoreboard.create();
			scoreboard.setLine(0, "§c");
			scoreboard.setLine(1, "§ePartie en Attente !");

			main.boards.put(p, scoreboard);
		}
	}
}
