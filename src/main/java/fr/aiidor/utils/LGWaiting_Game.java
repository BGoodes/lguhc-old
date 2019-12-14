package fr.aiidor.utils;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import fr.aiidor.LGUHC;
import fr.aiidor.effect.Sounds;
import fr.aiidor.effect.Titles;

public class LGWaiting_Game {
	
	private LGUHC main;
	
	public LGWaiting_Game(LGUHC main) {
		this.main = main;
	}
	
	public void startGame() {
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			new Sounds(p).PlaySound(Sound.ENDERDRAGON_GROWL);
			new Titles().sendTitle(p, "§e§k!!!§e PVP §k!!!", "§aActivé", 20);
			
			
			p.getInventory().setItem(0, Stick());
			p.getInventory().setItem(1, Sugar());
		}
		
		if (main.cage) {
			Location l1 = new Location(main.world, main.Spawn.getX() - main.cageSize, main.Spawn.getY() + main.cageHeight, main.Spawn.getZ() + main.cageSize);
			Location l2 = new Location(main.world, main.Spawn.getX() + main.cageSize, main.Spawn.getY(), main.Spawn.getZ() - main.cageSize);
			
			main.wallArrounndRegion(l1, l2, Material.STAINED_GLASS);
		}
	}
	
	public void stopGame() {
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			new Sounds(p).PlaySound(Sound.CAT_MEOW);
			new Titles().sendTitle(p, "§e§k!!!§e PVP §k!!!", "§cDésactivé", 20);
			
			p.closeInventory();
			reset(p);
			
			if (main.orgas.contains(p.getUniqueId()) || main.host == p.getUniqueId()) {
				p.getInventory().setItem(4, getItem(Material.ENDER_CHEST, "§d§lConfiguration"));
			}
		}
		
		if (main.cage) {
			Location l1 = new Location(main.world, main.Spawn.getX() - main.cageSize, main.Spawn.getY() + main.cageHeight, main.Spawn.getZ() + main.cageSize);
			Location l2 = new Location(main.world, main.Spawn.getX() + main.cageSize, main.Spawn.getY(), main.Spawn.getZ() - main.cageSize);
			
			main.wallArrounndRegion(l1, l2, Material.BARRIER);
		}
	}
	
	
	private ItemStack Stick() {
		
		ItemStack item = new ItemStack(Material.STICK);
		ItemMeta itemM = item.getItemMeta();
		
		itemM.setDisplayName("§aSlime Stick");
		itemM.addEnchant(Enchantment.KNOCKBACK, 2, true);
		itemM.addEnchant(Enchantment.DAMAGE_ALL, 10, true);
		
		itemM.setLore(Arrays.asList("", "§2La joie de voir tes amis", "§f§lvoler !"));
		item.setItemMeta(itemM);
		return item;
	}
	
	public ItemStack Sugar() {
		
		ItemStack item = new ItemStack(Material.SUGAR);
		ItemMeta itemM = item.getItemMeta();
		
		itemM.setDisplayName("§dSucre Pétillant");
		itemM.addEnchant(Enchantment.THORNS, 10, true);
		
		itemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		itemM.setLore(Arrays.asList("§fMysterieux tout ça ..."));
		item.setItemMeta(itemM);
		return item;
	}
	
	private ItemStack getItem(Material mat, String Name) {
		
		ItemStack it = new ItemStack(mat, 1);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName(Name);
		
		it.setItemMeta(itM);
		return it;
	}
	
	
	private void reset(Player player) {
		
		main.clearArmor(player);
		
		for(ItemStack item:player.getInventory().getContents()){
			player.getInventory().remove(item);
			player.updateInventory();
		}
		
		for(PotionEffect effect:player.getActivePotionEffects()){player.removePotionEffect(effect.getType());}
	}
}
