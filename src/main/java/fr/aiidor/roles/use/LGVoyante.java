package fr.aiidor.roles.use;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.roles.LGRoleManager;
import fr.aiidor.roles.LGRoles;

public class LGVoyante {
	
	private static boolean VoyanteBavarde = true;

	public static void canSee() {
		for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
			if (LGRoles.getRole(uuid) == LGRoles.Voyante) {
				
				LGRoleManager.Power.put(uuid, 1);
				Bukkit.getPlayer(uuid).sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�b Vous avez 5 minutes pour conna�tre le r�le d'un joueur gr�ce � la commande /lg see <Pseudo> !");
			}
		}
		
		Bukkit.getScheduler().runTaskLater(LGUHC.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				
				cannotSee();
			}
		}, 20*60*5);
	}
	
	public static void cannotSee() {
		for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
			if (LGRoles.getRole(uuid) == LGRoles.Voyante) {
				
				if (LGRoleManager.Power.containsKey(uuid)) {
					
					Player pl =Bukkit.getPlayer(uuid);
					LGRoleManager.Power.remove(uuid);
					pl.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous avez attendu plus de 5 min, vous pourrez donc utiliser votre pouvoir qu'� partir du prochaine �pisode !");
				}
				
			}
		}	
	}
	
	public static void SeeRole(Player p, Player Target) {
		
		if (!LGRoleManager.Power.containsKey(p.getUniqueId())) {
			p.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous avez d�j� utilis� votre pouvoir ou avez attendu trop longtemps (5min) ! Attendez le prochaine �pisode avant de pouvoir le r�utiliser !");
			return;
		}
		
		p.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�a Le joueur que vous avez espionn� est " + LGRoles.getRoleName(Target.getUniqueId()));
		
		LGRoleManager.Power.remove(p.getUniqueId());
		if (VoyanteBavarde) {
			Bukkit.broadcastMessage("�b�l[�6�lLOUP-GAROUS�b�l]�4 La voyante Bavarde � espionn� un joueur qui est... �l" + LGRoles.getRoleName(Target.getUniqueId()));
		}
	}
}
