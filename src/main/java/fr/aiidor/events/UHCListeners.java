package fr.aiidor.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;

public class UHCListeners implements Listener {
	
	public static boolean DiamondLimit = true;
	public static int DiamondMax = 17;
	public HashMap<UUID, Integer> DiamondPl = new HashMap<>();
	
	//NETHER NERF
	@EventHandler
	public void Portal(PortalCreateEvent e) {
		e.setCancelled(true);
	}
	

	//DISABLE AXE
	@SuppressWarnings("deprecation")
	@EventHandler
	public void Pvp(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player)) return;
		
		Player damager = (Player) e.getDamager();
		if (damager.getItemInHand().getType() == Material.WOOD_AXE ||
				damager.getItemInHand().getType() == Material.IRON_AXE ||
				damager.getItemInHand().getType() == Material.STONE_AXE ||
				damager.getItemInHand().getType() == Material.DIAMOND_AXE) {
			
			e.setCancelled(true);
		}
		
	}
    
    
	//DIAMOND LIMIT
	@EventHandler
	public void Diamond(BlockBreakEvent e) {
		if (DiamondLimit = false) { return; }
		if (e.getBlock().getType() != Material.DIAMOND_ORE) { return;}
		
		if (DiamondPl.containsKey(e.getPlayer().getUniqueId())) {
			
			int diamond = DiamondPl.get(e.getPlayer().getUniqueId());
			DiamondPl.remove(e.getPlayer().getUniqueId());
			
			DiamondPl.put(e.getPlayer().getUniqueId(), diamond + 1);
		}
		else {
			DiamondPl.put(e.getPlayer().getUniqueId(), 1);
			return;
		}
		
		if (DiamondPl.get(e.getPlayer().getUniqueId()) > DiamondMax) {
			
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			
			e.getPlayer().sendMessage("§b§l[§6§lUHC§b§l]§c Vous avez dépassé votre limite de Diamant !");
		}
	}
	
	//DISABLE FLAMME AND FIRE ASPECT

	
	@EventHandler
	public static void onAnvil(PrepareAnvilEvent e) {
		
		Map<Enchantment, Integer> enchants = e.getResult().getEnchantments();
		ItemStack item = e.getResult();
		if (enchants.containsKey(Enchantment.FIRE_ASPECT) || enchants.containsKey(Enchantment.ARROW_FIRE)) {
			
			if (item.getEnchantments().containsKey(Enchantment.FIRE_ASPECT)) {
				
				item.removeEnchantment(Enchantment.FIRE_ASPECT);
			}
			
			if (item.getEnchantments().containsKey(Enchantment.ARROW_FIRE)) {
				
				item.removeEnchantment(Enchantment.ARROW_FIRE);
			}
			
			
			e.setResult(item);
		}
	}
	
	
	//FAST SMELTING
	
}
