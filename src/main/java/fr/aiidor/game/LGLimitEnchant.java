package fr.aiidor.game;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.LGUHC;

public class LGLimitEnchant {
	
	private LGUHC main;
	private ItemStack clicked;
	
	public LGLimitEnchant(LGUHC main, ItemStack clicked) {
		this.main = main;
		this.clicked = clicked;
	}
	
	public void limit(Player player) {
		
		if (!main.LimiteEnchant) return;
		if (clicked.getEnchantments().isEmpty()) return;
		
		for (Enchantment ench : clicked.getEnchantments().keySet()) {
			
			if (main.getEnchant(ench) != null) {
				if (main.getEnchant(ench).hasLimit()) {
					
					if (main.getEnchant(ench).getLimit() == 0) {
						
						clicked.removeEnchantment(ench);
						
					} else if (clicked.getEnchantmentLevel(ench) > main.getEnchant(ench).getLimit()) {
						
						clicked.removeEnchantment(ench);
						
						ItemMeta meta = clicked.getItemMeta();
						meta.addEnchant(ench, main.getEnchant(ench).getLimit(), true);
						clicked.setItemMeta(meta);
					}
				}

			}
		}
		
	}
}
