package fr.aiidor.roles.use;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import fr.aiidor.LGUHC;
import fr.aiidor.roles.LGRoleManager;
import fr.aiidor.roles.LGRoles;

public class LGSalvateur {
	
	public static ArrayList<UUID> protect = new ArrayList<>();
	public static ArrayList<UUID> Aprotect = new ArrayList<>();
	
	public static void canChoose() {
		
		for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
			if (LGRoles.getRole(uuid) == LGRoles.Salvateur) {
				
				LGRoleManager.Power.put(uuid, 1);
				Bukkit.getPlayer(uuid).sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�b Vous avez 2 minutes pour prot�ger un joueur gr�ce � la commande /lg protect <Pseudo>");
			}
		}
		
		Bukkit.getScheduler().runTaskLater(LGUHC.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				
				cannotChoose();
			}
		}, 20*60*2);	
	}
	
	public static void cannotChoose() {
		
		Aprotect.clear();
		Aprotect = protect;
		
		for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
			if (LGRoles.getRole(uuid) == LGRoles.Salvateur) {
				
				if (LGRoleManager.Power.containsKey(uuid)) {
					
					Player pl = Bukkit.getPlayer(uuid);
					LGRoleManager.Power.remove(uuid);
					
					pl.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous avez attendu plus de 2 minutes, vous pourrez donc utiliser votre pouvoir qu'� partir du prochaine �pisode !");
				}
				
			}
		}	
	}
	
	public static void choose(UUID cible) {
		
		protect.add(cible);
		
		Bukkit.getPlayer(cible).sendMessage(" ");
		Bukkit.getPlayer(cible).sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�a Le salvateur � d�cid� de vous prot�ger !"
				+ " Vous ne prendrez plus de d�g�ts de chute et obtenez l'effet de r�sistance pendant un �pisode !");
	}
	
	public static void EndProtect() {
		
		protect.clear();
		
		for (UUID p : protect) {
			
			for(PotionEffect effect: Bukkit.getPlayer(p).getActivePotionEffects()){
				Bukkit.getPlayer(p).removePotionEffect(effect.getType());
			}
		}	
	}
}
