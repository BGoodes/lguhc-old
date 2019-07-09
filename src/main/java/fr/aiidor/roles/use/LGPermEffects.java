package fr.aiidor.roles.use;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.roles.LGCamps;
import fr.aiidor.roles.LGRoleManager;
import fr.aiidor.roles.LGRoles;

public class LGPermEffects {
	
	public static void PermEffect(Player p) {
		
		//NIGHT VISION
		if ( LGCamps.getCamp(p.getUniqueId()) == LGCamps.LOUPGAROUS ||LGRoles.getRole(p.getUniqueId()) == LGRoles.Voyante || LGRoles.getRole(p.getUniqueId()) == LGRoles.Petite) {
			if (LGRoles.getRole(p.getUniqueId()) == LGRoles.LGA && !LGRoleManager.Power.containsKey(p.getUniqueId())) {return;} //LGA
			//if (LGRoleManager.Infect.contains(p.getUniqueId()) && !LGRoles.isLG(p.getUniqueId())) {return;} //INFECT
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false), true);
			return;
		}
		
		//RENARD SPPED
		if (LGRoles.getRole(p.getUniqueId()) == LGRoles.Renard) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), true);
			return;
		}
		
		//ANCIEN + VOLEUR RESISTANCE 
		if ((LGRoles.getRole(p.getUniqueId()) == LGRoles.Ancien || LGRoles.getRole(p.getUniqueId()) == LGRoles.Voleur) && (LGRoleManager.Power.containsKey(p.getUniqueId()))) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), true);
			return;
		}
		
		//SALVATEUR
		if (LGSalvateur.protect.contains(p.getUniqueId())) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), true);
			return;
		}
		
		//MINEUR
		if (LGRoles.getRole(p.getUniqueId()) == LGRoles.Mineur) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1, false, false), true);
			return;
		}
		
	}
}
