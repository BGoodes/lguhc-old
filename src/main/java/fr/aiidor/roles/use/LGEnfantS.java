package fr.aiidor.roles.use;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.roles.LGRoleManager;
import fr.aiidor.roles.LGRoles;
import fr.aiidor.roles.RoleDescription;
import fr.aiidor.task.LGA;

public class LGEnfantS {
public static void canChoose() {
		
		for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
			if (LGRoles.getRole(uuid) == LGRoles.EnfantS) {
					Bukkit.getPlayer(uuid).sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§b Vous avez 5 minutes pour choisir un modèle avec la commande /lg choose <pseudo> !");
				}
			}
	}
	
	public static void choose(Player player, Player model) {
		
	LGRoleManager.model.put(model.getUniqueId(), player.getUniqueId());
	player.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§a Vous avez bien choisit §6" + model.getName() + " §a comme modèle !");
		
		Bukkit.getScheduler().runTaskLater(LGUHC.getInstance(), new Runnable() {
			@Override
			public void run() {
				LGEnfantS.Cannotchoose();
			}
		}, 20*60*5);
	}
	
	public static void Cannotchoose() {
		
		for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
			if (LGRoles.getRole(uuid) == LGRoles.EnfantS) {
				
				if (LGRoleManager.Power.containsKey(uuid)) {
					
					Player pl =Bukkit.getPlayer(uuid);
					LGRoleManager.Power.remove(uuid);
					pl.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c Vous avez attendu plus de 5 min, vous ne pourrez donc plus utiliser votre pouvoir et resterez donc villageois toute la partie !");
				}
				
			}
		}
	}
	
	public static void Transform(Player p) {
		
		LGRoleManager.lg.add(p.getName());
		
		for (String name : LGRoleManager.lg) {
			Player lg = Bukkit.getPlayer(name);
			lg.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c Un nouveau Loup à rejoint votre camp ! Faite /lg role pour plus de détail !");  //NOUVEAU JOUEUR
		}
		
		for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
			
			if (LGRoles.getRole(uuid) == LGRoles.LGA && LGRoleManager.Power.containsKey(uuid)) {
				if (LGRoleManager.Power.containsKey(uuid)) {
					LGA.lgM.add(p.getName());
				}
			}
		}
		
		
		p.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§9 [Privé] Vous êtes §o" + LGRoles.getRoleName(p.getUniqueId()) + " §4Transformé.");
		p.sendMessage(RoleDescription.desc(p.getUniqueId()));
		LG.LGCompo(p);
	}
}
