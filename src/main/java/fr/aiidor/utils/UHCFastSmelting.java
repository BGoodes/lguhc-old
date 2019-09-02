package fr.aiidor.utils;

import java.util.Random;

import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;

public class UHCFastSmelting implements Listener{

	private LGUHC main;
	public UHCFastSmelting(LGUHC pl) {
		this.main = pl;
	}
	
    private void startUpdate(final Furnace tile, final int increase)
    {
      new BukkitRunnable() {
        public void run() {
          if ((tile.getCookTime() > 0) || (tile.getBurnTime() > 0)) {
            tile.setCookTime((short)(tile.getCookTime() + increase));
            tile.update();
          }
          else {
            cancel();
          }
        }
      }
      .runTaskTimer(main, 1L, 1L);
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
    	if (main.run == false) return;
		if (main.fastSmelting == false) return;
		
      startUpdate((Furnace)event.getBlock().getState(), new Random().nextBoolean() ? 2 : 3);
    }
    
}
