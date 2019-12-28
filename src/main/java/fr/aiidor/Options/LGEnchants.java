package fr.aiidor.Options;

import org.bukkit.enchantments.Enchantment;

public class LGEnchants {
	
	private Enchantment enchant;
	private Integer limit;
	
	private Integer MaxLevel;
	
	public LGEnchants(Enchantment enchant) {
		this.enchant = enchant;
		this.MaxLevel = enchant.getMaxLevel();
		
		this.limit = MaxLevel;
	}
	
	public LGEnchants(Enchantment enchant, Integer limit) {
		this(enchant);
		this.limit = limit;
	}
	
	public Enchantment getEnchant() {
		return enchant;
	}

	public Boolean hasLimit() {
		return limit != MaxLevel;
	}
	
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer Limit) {
		this.limit = Limit;
	}
	
	public void addLimit() {
		setLimit(getLimit() + 1);
	}
	
	public void RemoveLimit() {
		setLimit(getLimit() - 1);
	}

	public Integer getMaxLevel() {
		return MaxLevel;
	}
	
	public String getEnchantName() {
		
		if (enchant.equals(Enchantment.ARROW_DAMAGE)) return "Power";
		if (enchant.equals(Enchantment.ARROW_FIRE)) return "Flame";
		if (enchant.equals(Enchantment.ARROW_INFINITE)) return "Infinity";
		if (enchant.equals(Enchantment.ARROW_KNOCKBACK)) return "Punch";
		if (enchant.equals(Enchantment.DAMAGE_ALL)) return "Sharpness";
		if (enchant.equals(Enchantment.DAMAGE_ARTHROPODS)) return "Bane of arthropods";
		if (enchant.equals(Enchantment.DAMAGE_UNDEAD)) return "Smite";
		if (enchant.equals(Enchantment.DEPTH_STRIDER)) return "Depth Strider";
		if (enchant.equals(Enchantment.DIG_SPEED)) return "Efficiency";
		if (enchant.equals(Enchantment.DURABILITY)) return "Unbreaking";
		if (enchant.equals(Enchantment.FIRE_ASPECT)) return "Fire Aspect";
		if (enchant.equals(Enchantment.KNOCKBACK)) return "Knockback";
		if (enchant.equals(Enchantment.LOOT_BONUS_BLOCKS)) return "Fortune";
		if (enchant.equals(Enchantment.LOOT_BONUS_MOBS)) return "Looting";
		if (enchant.equals(Enchantment.LUCK)) return "Luck of the sea";
		if (enchant.equals(Enchantment.LURE)) return "Lure";
		if (enchant.equals(Enchantment.OXYGEN)) return "Respiration";
		if (enchant.equals(Enchantment.PROTECTION_ENVIRONMENTAL)) return "Protection";
		if (enchant.equals(Enchantment.PROTECTION_EXPLOSIONS)) return "Blast Protection";
		if (enchant.equals(Enchantment.PROTECTION_FALL)) return "Feather Falling";
		if (enchant.equals(Enchantment.PROTECTION_FIRE)) return "Fire Protection";
		if (enchant.equals(Enchantment.PROTECTION_PROJECTILE)) return "Projectile Protection";
		if (enchant.equals(Enchantment.SILK_TOUCH)) return "Silk Touch";
		if (enchant.equals(Enchantment.THORNS)) return "Thorns";
		if (enchant.equals(Enchantment.WATER_WORKER)) return "Aqua Affinity";
		
		return null;
	}
}
