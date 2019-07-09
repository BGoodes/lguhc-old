package fr.aiidor.roles;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LGItem {
	
	public static void GiveItem(Player p, LGRoles role) {
		
		if (role == LGRoles.Voyante) {
			
			p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.BOOKSHELF, 4));
			p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.OBSIDIAN, 4));
			return;
		}
		
		if (role == LGRoles.Petite) {
			
			p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.TNT, 5));
			return;
		}
		
		if (role == LGRoles.Assassin) {
			
			//LIVRE SHARP 3
			ItemStack Sharp = new ItemStack(Material.ENCHANTED_BOOK);
			EnchantmentStorageMeta SharpM = (EnchantmentStorageMeta) Sharp.getItemMeta();
			SharpM.addStoredEnchant(Enchantment.DAMAGE_ALL, 3, true);
			
			Sharp.setItemMeta(SharpM);
			p.getWorld().dropItem(p.getLocation(), Sharp);
			
			//LIVRE PROT 3
			ItemStack Prot = new ItemStack(Material.ENCHANTED_BOOK);
			EnchantmentStorageMeta ProtM = (EnchantmentStorageMeta) Prot.getItemMeta();
			ProtM.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
			
			Prot.setItemMeta(ProtM);
			p.getWorld().dropItem(p.getLocation(), Prot);
			
			//LIVRE POWER 3
			ItemStack Power = new ItemStack(Material.ENCHANTED_BOOK);
			EnchantmentStorageMeta PowerM = (EnchantmentStorageMeta) Power.getItemMeta();
			PowerM.addStoredEnchant(Enchantment.ARROW_DAMAGE, 3, true);
			
			Power.setItemMeta(PowerM);
			p.getWorld().dropItem(p.getLocation(), Power);
			return;
		}
		
		
		if (role == LGRoles.Sorcière) {
			
			//INSTANT HEALTH
			ItemStack Health = new ItemStack(Material.SPLASH_POTION, 3);
			PotionMeta HealthM = (PotionMeta) Health.getItemMeta();
			
			HealthM.setColor(Color.RED);
			HealthM.setDisplayName("§aPotion de Heal");
			HealthM.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 0, 0), true);
			Health.setItemMeta(HealthM);
			p.getWorld().dropItem(p.getLocation(), Health);
			//REGEN
			
			ItemStack regen = new ItemStack(Material.SPLASH_POTION, 1);
			PotionMeta regenM = (PotionMeta) regen.getItemMeta();
			
			regenM.setColor(Color.FUCHSIA);
			regenM.setDisplayName("§dPotion de Régenération");
			regenM.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 900, 0), true);
			regen.setItemMeta(regenM);
			p.getWorld().dropItem(p.getLocation(), regen);
			
			//DEG
			ItemStack damage = new ItemStack(Material.SPLASH_POTION, 3);
			PotionMeta damageM = (PotionMeta) damage.getItemMeta();
			
			damageM.setColor(Color.MAROON);
			damageM.setDisplayName("§cPotion de Dégat");
			damageM.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 0, 0), true);
			damage.setItemMeta(damageM);
			p.getWorld().dropItem(p.getLocation(), damage);
			
			return;
		}
		
		if (role == LGRoles.Salvateur) {
			
			ItemStack Health = new ItemStack(Material.SPLASH_POTION, 2);
			PotionMeta HealthM = (PotionMeta) Health.getItemMeta();
			
			HealthM.setColor(Color.RED);
			HealthM.setDisplayName("§aPotion de Heal");
			HealthM.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 0, 0), true);
			Health.setItemMeta(HealthM);
			p.getWorld().dropItem(p.getLocation(), Health);
			
			return;
		}
		
		if (role == LGRoles.Mineur) {
			
			ItemStack Pioche = new ItemStack(Material.DIAMOND_PICKAXE);
			ItemMeta PiocheM = Pioche.getItemMeta();
			
			PiocheM.addEnchant(Enchantment.DIG_SPEED, 2, true);
			PiocheM.setDisplayName("§6Pioche du mineur");
			Pioche.setItemMeta(PiocheM);
			
			p.getWorld().dropItem(p.getLocation(), Pioche);
			return;
		}
		
		if (role == LGRoles.Cupidon) {
			
			ItemStack Bow = new ItemStack(Material.BOW);
			ItemMeta BowM = Bow.getItemMeta();
			
			BowM.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
			BowM.setDisplayName("§dArc de cupidon");
			Bow.setItemMeta(BowM);
			
			p.getWorld().dropItem(p.getLocation(), Bow);
			p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.ARROW, 64));
			return;
		}
	}
}
