package fr.aiidor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.aiidor.commands.CommandBypass;
import fr.aiidor.commands.CommandLg;
import fr.aiidor.commands.CommandTest;
import fr.aiidor.effects.Titles;
import fr.aiidor.events.EventsManager;
import fr.aiidor.game.UHCState;
import fr.aiidor.scoreboard.ScoreboardSign;

public class LGUHC extends JavaPlugin {
	
	public ArrayList<UUID> PlayerInGame = new ArrayList<>();
	public HashMap<UUID, String> PlayerName = new HashMap<>();
	public int PlayerMin = 1;
	
	public HashMap<Player, ScoreboardSign> boards = new HashMap<>();
	
	public boolean Run = true;
	
	//TITLE
	public Titles title = new Titles();
	
	//INSTANCE
	public static LGUHC instance;
	
	public WorldBorder wb = Bukkit.getWorld("world").getWorldBorder();
	
	public static LGUHC getInstance() {
		return instance;
	}
	
	//LOAD
	@Override
	public void onEnable() {
		
		System.out.println("[LOUP-GAROUS] Plugin LGUHC fonctionnel !");
		instance = this;
		
		EventsManager.registerEvents(this);
		
		UHCState.setState(UHCState.WAIT);
		Bukkit.getWorld("world").setPVP(true);
		
		//GAMERULE
		Bukkit.getWorld("world").setGameRuleValue("reducedDebugInfo", "true");
		Bukkit.getWorld("world").setGameRuleValue("naturalRegeneration", "false");
		Bukkit.getWorld("world").setGameRuleValue("announceAdvancements", "false");
		Bukkit.getWorld("world").setGameRuleValue("spawnRadius", "0");
		
		
		getCommand("bypass").setExecutor(new CommandBypass());
		getCommand("lg").setExecutor(new CommandLg());
		getCommand("test").setExecutor(new CommandTest());
		
		
		//BORDER
		wb.setCenter(0, 0);
		wb.setSize(2000);
		
		//PLATEFORME
		World world = Bukkit.getWorld("World");
		world.setSpawnLocation(new Location(world, 0, 151, 0));
	}
	
	public String getNameString(UUID uuid) {
		return PlayerName.get(uuid);
	}

}
