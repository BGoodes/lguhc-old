package fr.aiidor.role;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import fr.aiidor.game.Joueur;

public class LGItem {

	private Joueur j;
	private Player p;

	public LGItem(Joueur j) {
		this.j = j;
		this.p = j.getPlayer();
	}

	public void giveItem() {

		LGRoles role = j.getRole();

		if (role == LGRoles.Voyante || role == LGRoles.VoyanteB) {

			giveItemStack(new ItemStack(Material.BOOKSHELF, 4));
			giveItemStack(new ItemStack(Material.OBSIDIAN, 4));

			return;
		}

		if (role == LGRoles.PetiteFille) {

			giveItemStack(new ItemStack(Material.TNT, 5));
			return;
		}

		if (role == LGRoles.Assassin) {

			//LIVRE SHARP 3
			ItemStack Sharp = new ItemStack(Material.ENCHANTED_BOOK);
			EnchantmentStorageMeta SharpM = (EnchantmentStorageMeta) Sharp.getItemMeta();
			SharpM.addStoredEnchant(Enchantment.DAMAGE_ALL, 3, true);

			Sharp.setItemMeta(SharpM);
			giveItemStack(Sharp);

			//LIVRE PROT 3
			ItemStack Prot = new ItemStack(Material.ENCHANTED_BOOK);
			EnchantmentStorageMeta ProtM = (EnchantmentStorageMeta) Prot.getItemMeta();
			ProtM.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);

			Prot.setItemMeta(ProtM);
			giveItemStack(Prot);

			//LIVRE POWER 3
			ItemStack Power = new ItemStack(Material.ENCHANTED_BOOK);
			EnchantmentStorageMeta PowerM = (EnchantmentStorageMeta) Power.getItemMeta();
			PowerM.addStoredEnchant(Enchantment.ARROW_DAMAGE, 3, true);

			Power.setItemMeta(PowerM);
			giveItemStack(Power);

			return;
		}

		if (role == LGRoles.Sorciere) {

			ItemStack item = new ItemStack(Material.POTION, 3);
			Potion pot = new Potion(1);

			pot.setType(PotionType.INSTANT_HEAL);
			pot.setSplash(true);
			pot.apply(item);

			ItemStack item2 = new ItemStack(Material.POTION, 3);
			Potion pot2 = new Potion(1);
			pot2.setType(PotionType.INSTANT_DAMAGE);
			pot2.setSplash(true);
			pot2.apply(item2);

			ItemStack item3 = new ItemStack(Material.POTION);
			Potion pot3 = new Potion(1);
			pot3.setType(PotionType.REGEN);
			pot3.setSplash(true);
			pot3.apply(item3);

			giveItemStack(item);
			giveItemStack(item2);
			giveItemStack(item3);
		}

		if (role == LGRoles.Salvateur) {

			ItemStack item = new ItemStack(Material.POTION, 2);
			Potion pot = new Potion(1);

			pot.setType(PotionType.INSTANT_HEAL);
			pot.setSplash(true);
			pot.apply(item);

			giveItemStack(item);
		}

		if (role == LGRoles.Mineur) {

			ItemStack Pioche = new ItemStack(Material.DIAMOND_PICKAXE);
			ItemMeta PiocheM = Pioche.getItemMeta();

			PiocheM.addEnchant(Enchantment.DIG_SPEED, 2, true);
			PiocheM.setDisplayName("§6Pioche du mineur");
			Pioche.setItemMeta(PiocheM);

			giveItemStack(Pioche);
			return;
		}

		if (role == LGRoles.Chasseur) {

			ItemStack bow = new ItemStack(Material.BOW);
			ItemMeta bowM = bow.getItemMeta();

			bowM.addEnchant(Enchantment.ARROW_DAMAGE, 4, true);
			bowM.setDisplayName("§aArc du chasseur");
			bow.setItemMeta(bowM);

			giveItemStack(bow);
			giveItemStack(new ItemStack(Material.ARROW, 64));
			giveItemStack(new ItemStack(Material.ARROW, 64));
			giveItemStack(new ItemStack(Material.MONSTER_EGG, 3, (byte) 95));
			giveItemStack(new ItemStack(Material.BONE, 15));
			return;
		}

		if (role == LGRoles.Pyromane) {

			//LIVRE FIRE A
			ItemStack fa = new ItemStack(Material.ENCHANTED_BOOK);
			EnchantmentStorageMeta faM = (EnchantmentStorageMeta) fa.getItemMeta();
			faM.addStoredEnchant(Enchantment.FIRE_ASPECT, 1, true);

			fa.setItemMeta(faM);
			giveItemStack(fa);

			//LIVRE FLAME
			ItemStack flame = new ItemStack(Material.ENCHANTED_BOOK);
			EnchantmentStorageMeta flameM = (EnchantmentStorageMeta) flame.getItemMeta();
			flameM.addStoredEnchant(Enchantment.ARROW_FIRE, 1, true);

			flame.setItemMeta(flameM);
			giveItemStack(flame);

			giveItemStack(new ItemStack(Material.LAVA_BUCKET, 1));
			giveItemStack(new ItemStack(Material.LAVA_BUCKET, 1));
			giveItemStack(new ItemStack(Material.FLINT_AND_STEEL, 1));
			return;
		}

		if (role == LGRoles.Cupidon) {

			ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
			EnchantmentStorageMeta bookM = (EnchantmentStorageMeta) book.getItemMeta();
			bookM.addStoredEnchant(Enchantment.ARROW_DAMAGE, 2, true);
			bookM.addStoredEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);

			book.setItemMeta(bookM);
			giveItemStack(book);

			giveItemStack(new ItemStack(Material.STRING, 3));
			giveItemStack(new ItemStack(Material.ARROW, 64));
			return;
		}
	}

	private void giveItemStack(ItemStack it) {
		if (isInventoryFull()) p.getWorld().dropItem(p.getLocation(), it);
		else p.getInventory().addItem(it);
	}

	public boolean isInventoryFull()
	{
		return p.getInventory().firstEmpty() == -1;
	}
}
