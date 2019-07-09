package fr.aiidor.roles.use;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.roles.LGRoles;

public class LGroleSolo {
	
	public static void LGBBuff(Player p) {
		
		if (LGRoles.getRole(p.getUniqueId()) == LGRoles.LGB) {
			p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(30);
			return;
		}
	}
	
	
	public static void AssassinBuff(Player p) {
		
		if (LGRoles.getRole(p.getUniqueId()) == LGRoles.Assassin) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), true);
			return;
		}
	}
}
