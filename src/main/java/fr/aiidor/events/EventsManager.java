package fr.aiidor.events;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import fr.aiidor.LGUHC;
import fr.aiidor.roles.use.LGCupidon;
import fr.aiidor.utils.UHCAutoLeafDecay;
import fr.aiidor.utils.UHCBlockDrops;
import fr.aiidor.utils.UHCSpeedLoot;
import fr.aiidor.utils.UHCSpeedRecipes;

public class EventsManager {

	public static void registerEvents(LGUHC pl) {
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new UHCJoin(), pl);
		
		pm.registerEvents(new UHCListeners(), pl);
		pm.registerEvents(new LGDeath(), pl);
		
		pm.registerEvents(new LGCupidon(), pl);
		
		
		if (LGUHC.getInstance().Run) {
			pm.registerEvents(new UHCBlockDrops(), pl);
			pm.registerEvents(new UHCSpeedRecipes(), pl);
			pm.registerEvents(new UHCAutoLeafDecay(), pl);
			pm.registerEvents(new UHCSpeedLoot(), pl);
		}
	}

}
