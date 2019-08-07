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

public class UHCSpeedRecipes implements Listener {
	
	@EventHandler
	public void ChangeCraft(PrepareItemCraftEvent e) {
		
		if (LGUHC.getInstance().Run == false) return;
		
		//Craft se passe dans une zone de craft
		if (e.getInventory() instanceof CraftingInventory) {
			
			CraftingInventory inv = e.getInventory();
			
			if (inv.getResult() == null) {
				return;
			}
			
			//Epée
			if (inv.getResult().getType() == Material.WOOD_SWORD || inv.getResult().getType() == Material.STONE_SWORD) {
				
				ItemStack CustomResult = new ItemStack(Material.STONE_SWORD, 1);
				ItemMeta CustomM = CustomResult.getItemMeta();
				
				CustomM.addEnchant(Enchantment.DURABILITY, 3, true);
				
				CustomResult.setItemMeta(CustomM);
				
				inv.setResult(CustomResult);
				return;
			}
			
			//PIOCHE EN BOIS
			if (inv.getResult().getType() == Material.WOOD_PICKAXE || inv.getResult().getType() == Material.STONE_SWORD) {
				
				ItemStack CustomResult = new ItemStack(Material.STONE_PICKAXE, 1);
				ItemMeta CustomM = CustomResult.getItemMeta();
				
				CustomM.addEnchant(Enchantment.DIG_SPEED, 2, true);
				CustomM.addEnchant(Enchantment.DURABILITY, 3, true);
				
				CustomResult.setItemMeta(CustomM);
				
				inv.setResult(CustomResult);
				return;
			}
			
			//PIOCHE EN FER
			if (inv.getResult().getType() == Material.IRON_PICKAXE) {
				
				ItemStack CustomResult = new ItemStack(Material.IRON_PICKAXE, 1);
				ItemMeta CustomM = CustomResult.getItemMeta();
				
				CustomM.addEnchant(Enchantment.DIG_SPEED, 2, true);
				CustomM.addEnchant(Enchantment.DURABILITY, 3, true);
				
				CustomResult.setItemMeta(CustomM);
				
				inv.setResult(CustomResult);
				return;
			}
			
			//HACHE
			if (inv.getResult().getType() == Material.WOOD_AXE || inv.getResult().getType() == Material.STONE_AXE) {
				
				ItemStack CustomResult = new ItemStack(Material.IRON_AXE, 1);
				ItemMeta CustomM = CustomResult.getItemMeta();
				
				CustomM.addEnchant(Enchantment.DIG_SPEED, 1, true);
				CustomM.addEnchant(Enchantment.DURABILITY, 3, true);
				
				CustomResult.setItemMeta(CustomM);
				
				inv.setResult(CustomResult);
				return;
			}
			
			//PELLE
			if (inv.getResult().getType() == Material.WOOD_SPADE || inv.getResult().getType() == Material.STONE_SPADE) {
				
				ItemStack CustomResult = new ItemStack(Material.IRON_SPADE, 1);
				ItemMeta CustomM = CustomResult.getItemMeta();
				
				CustomM.addEnchant(Enchantment.DIG_SPEED, 1, true);
				CustomM.addEnchant(Enchantment.DURABILITY, 3, true);
				
				CustomResult.setItemMeta(CustomM);
				
				inv.setResult(CustomResult);
				return;
			}
			
			
			
			
			
		}
	}
}
