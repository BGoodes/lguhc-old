package fr.aiidor.utils;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import fr.aiidor.LGUHC;
import fr.aiidor.effect.Sounds;

public class UHCLootCrate {

	private LGUHC main;

	public UHCLootCrate(LGUHC main) {
		this.main = main;
	}


	public void setLoots() {

		main.Loots.add(getItem(Material.GOLDEN_APPLE, null, 1));
		main.Loots.add(getItem(Material.GOLDEN_APPLE, null, 2));
		main.Loots.add(getItem(Material.GOLDEN_APPLE, null, 3));

		main.Loots.add(getItem(Material.DIAMOND, null, 1));
		main.Loots.add(getItem(Material.GOLD_INGOT, null, 8));
		main.Loots.add(getItem(Material.GOLD_INGOT, null, 16));
		main.Loots.add(getItem(Material.IRON_INGOT, null, 64));

		main.Loots.add(getItem(Material.OBSIDIAN, null, 2));
		main.Loots.add(getItem(Material.OBSIDIAN, null, 4));

		main.Loots.add(getItem(Material.ARROW, null, 16));
		main.Loots.add(getItem(Material.ARROW, null, 64));

		main.Loots.add(getItem(Material.DIAMOND_PICKAXE, null, 1));

		main.Loots.add(getItem(Material.BONE, "§aBon Toutou", 1));

		main.Loots.add(getItem(Material.BOW, null, 1));

		main.Loots.add(getItem(Material.WEB, null, 5));
		main.Loots.add(getItem(Material.BUCKET, null, 2));
		main.Loots.add(getItem(Material.SLIME_BLOCK, null, 10));

		main.Loots.add(getItem(Material.ENDER_PEARL, null, 1));
		main.Loots.add(getItem(Material.ENDER_PEARL, null, 2));

		main.Loots.add(getPotion(1, PotionType.SPEED, 2, false));
		main.Loots.add(getPotion(2, PotionType.INSTANT_HEAL, 1, true));
		main.Loots.add(getPotion(1, PotionType.SLOWNESS, 1, true));

		main.Loots.add(getItem(Material.ENCHANTED_BOOK, "§aLivre enchanté aléatoire !", 1));
		main.Loots.add(getItem(Material.ENCHANTED_BOOK, "§aLivre enchanté aléatoire !", 1));
		main.Loots.add(getItem(Material.ENCHANTED_BOOK, "§aLivre enchanté aléatoire !", 1));
		main.Loots.add(getItem(Material.ENCHANTED_BOOK, "§aLivre enchanté aléatoire !", 1));
		main.Loots.add(getItem(Material.ENCHANTED_BOOK, "§aLivre enchanté aléatoire !", 1));
	}

	private ItemStack getPotion(int number, PotionType effect, int level, Boolean splash) {
		ItemStack item = new ItemStack(Material.POTION, number);
		Potion pot = new Potion(1);

		pot.setType(effect);
		pot.setLevel(level);
		pot.setSplash(splash);
		pot.apply(item);

		return item;
	}

	public void openLootCrate(Player p) {

		ItemStack item = main.Loots.get(new Random().nextInt(main.Loots.size()));

		if (item.getType() == Material.ENCHANTED_BOOK) {
			if (item.hasItemMeta()) {
				if (item.getItemMeta().getDisplayName().contains("aléatoire") || item.getItemMeta().getDisplayName().contains("random")) {

					item = new ItemStack(Material.ENCHANTED_BOOK);
					EnchantmentStorageMeta bookM = (EnchantmentStorageMeta) item.getItemMeta();

					Enchantment Enchant = getEnchant();

					int level = new Random().nextInt(Enchant.getMaxLevel());
					if (level == 0) level ++;

					bookM.addEnchant(Enchant, level, true);

					if (new Random().nextInt(5) == 0) {
						Enchant = getEnchant();

						level = new Random().nextInt(Enchant.getMaxLevel());
						if (level == 0) level ++;

						bookM.addEnchant(Enchant, level, true);
					}

					item.setItemMeta(bookM);
				}
			}
		}

		giveItemStack(p, item);
		new Sounds(p).PlaySound(Sound.CLICK);
	}

	private void giveItemStack(Player p, ItemStack it) {
		if (isInventoryFull(p)) p.getWorld().dropItem(p.getLocation(), it);
		else p.getInventory().addItem(it);
	}

	private boolean isInventoryFull(Player p)
	{
		return p.getInventory().firstEmpty() == -1;
	}

	private ItemStack getItem(Material material, String Name, int number) {

		ItemStack Item = new ItemStack(material, number);
		ItemMeta ItemM = Item.getItemMeta();

		ItemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		if (Name != null) ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);

		return Item;
	}

	private Enchantment getEnchant() {

		Enchantment[] Enchants = Enchantment.values();
		int choose = new Random().nextInt(Enchants.length);

		int i = 1;
		Enchantment Enchant = null;

		for (Enchantment ench : Enchants) {
			if (i == choose) break;
			Enchant = ench;
			i++;
		}

		return Enchant;
	}
}
