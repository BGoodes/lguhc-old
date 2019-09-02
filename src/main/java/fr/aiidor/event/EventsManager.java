package fr.aiidor.event;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import fr.aiidor.LGUHC;
import fr.aiidor.utils.UHCCutClean;
import fr.aiidor.utils.UHCFastSmelting;
import fr.aiidor.utils.UHCHasteyBoys;
import fr.aiidor.utils.UHCTimber;

public class EventsManager {
	
	public static void registerEvents(LGUHC pl) {
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new UHCJoin(pl), pl);
		pm.registerEvents(new UHCListeners(pl), pl);
		pm.registerEvents(new UHCInventory(pl), pl);
		pm.registerEvents(new UHCPvp(pl), pl);
		
		pm.registerEvents(new UHCTimber(pl), pl);
		pm.registerEvents(new UHCCutClean(pl), pl);
		pm.registerEvents(new UHCFastSmelting(pl), pl);
		pm.registerEvents(new UHCHasteyBoys(pl), pl);
	}
}
