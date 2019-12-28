package fr.aiidor.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.LGUHC;

public class UHCHasteyBoys implements Listener{
	
	private LGUHC main;
	public UHCHasteyBoys(LGUHC pl) {
		this.main = pl;
	}
	
	@EventHandler
	public void ChangeCraft(PrepareItemCraftEvent e) {
		
		if (main.run == false) return;
		if (main.HasteyBoys == false && main.noWodenTool == Material.WOOD) return;
		
		//Craft se passe dans une zone de craft
		if (e.getInventory() instanceof CraftingInventory) {
			
			CraftingInventory inv = e.getInventory();
			
			if (inv.getResult() == null) return;
			
			Material result = inv.getResult().getType();
			
			if (main.noWodenTool != Material.WOOD) {
				if (result == Material.WOOD_SPADE || result == Material.WOOD_PICKAXE || result == Material.WOOD_AXE || result == Material.WOOD_SWORD) {
					
					ItemStack tool = new ItemStack(result);
					
					if (main.noWodenTool == Material.COBBLESTONE) {
						if (result == Material.WOOD_SPADE) tool = new ItemStack(Material.STONE_SPADE);
						if (result == Material.WOOD_PICKAXE) tool = new ItemStack(Material.STONE_PICKAXE);
						if (result == Material.WOOD_AXE) tool = new ItemStack(Material.STONE_AXE);
						if (result == Material.WOOD_SWORD) tool = new ItemStack(Material.STONE_SWORD);
					}
					
					if (main.noWodenTool == Material.IRON_INGOT) {
						if (result == Material.WOOD_SPADE) tool = new ItemStack(Material.IRON_SPADE);
						if (result == Material.WOOD_PICKAXE) tool = new ItemStack(Material.IRON_PICKAXE);
						if (result == Material.WOOD_AXE) tool = new ItemStack(Material.IRON_AXE);
						if (result == Material.WOOD_SWORD) tool = new ItemStack(Material.IRON_SWORD);
					}
					
					if (main.noWodenTool == Material.DIAMOND) {
						if (result == Material.WOOD_SPADE) tool = new ItemStack(Material.DIAMOND_SPADE);
						if (result == Material.WOOD_PICKAXE) tool = new ItemStack(Material.DIAMOND_PICKAXE);
						if (result == Material.WOOD_AXE) tool = new ItemStack(Material.DIAMOND_AXE);
						if (result == Material.WOOD_SWORD) tool = new ItemStack(Material.DIAMOND_SWORD);
					}
					
					inv.setResult(tool);
					result = tool.getType();
				}
			}
			
			if (main.HasteyBoys == false) return;
			
			if (result == Material.WOOD_SPADE || result == Material.WOOD_PICKAXE || result == Material.WOOD_AXE ||
				result == Material.STONE_SPADE || result == Material.STONE_PICKAXE || result == Material.STONE_AXE ||
				result == Material.IRON_SPADE || result == Material.IRON_PICKAXE || result == Material.IRON_AXE ||
				result == Material.GOLD_SPADE || result == Material.GOLD_PICKAXE || result == Material.GOLD_AXE ||
				result == Material.DIAMOND_SPADE || result == Material.DIAMOND_PICKAXE || result == Material.DIAMOND_AXE) {
				
				ItemStack tool = new ItemStack(result);
				ItemMeta toolM = tool.getItemMeta();
				
				toolM.addEnchant(Enchantment.DIG_SPEED, 3, true);
				toolM.addEnchant(Enchantment.DURABILITY, 1, true);
				
				tool.setItemMeta(toolM);
				inv.setResult(tool);
			}
		}
	}
}
