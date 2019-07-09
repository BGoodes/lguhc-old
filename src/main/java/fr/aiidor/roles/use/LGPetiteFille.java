package fr.aiidor.roles.use;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.LGUHC;
import fr.aiidor.roles.LGRoles;

public class LGPetiteFille {
	
	public static void NightEffect(Player p) {
		
		if (LGRoles.getRole(p.getUniqueId()) != LGRoles.Petite) {return;}
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false), true);
		p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false), true);
	}
	
	
	
	public static void Scan(Player p) {
		
		if (LGRoles.getRole(p.getUniqueId()) != LGRoles.Petite) {return;}
		
		StringBuilder msg = new StringBuilder();
		
		int Joueur = 0;
		
		for (UUID pl : LGUHC.getInstance().PlayerInGame) {
			
			if (Bukkit.getPlayer(pl) != p) {
				
				if (p.getLocation().distance(Bukkit.getPlayer(pl).getLocation()) <= 100) {
					msg.append("§6" +Bukkit.getPlayer(pl).getName());
					
					Joueur ++;
				}
			}
		}

		
		if (Joueur == 0) {
			p.sendMessage("§cAucun joueur ne se trouve dans un rayon de 100 blocs !");
			return;
		}
		
		if (Joueur == 1) {
			p.sendMessage("§3Le Joueur suivant se trouve dans un rayon de 100 blocs : ");
			p.sendMessage(msg.toString());
			return;
		}
		
		p.sendMessage("§3Les Joueurs suivants se trouvent dans un rayon de 100 blocs : ");
		p.sendMessage(msg.toString());
		return;
	}
}
