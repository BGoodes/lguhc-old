package fr.aiidor.events;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import fr.aiidor.LGUHC;
import fr.aiidor.game.UHCState;
import fr.aiidor.roles.LGCamps;
import fr.aiidor.roles.LGRoleManager;
import fr.aiidor.roles.LGRoles;
import fr.aiidor.roles.RoleDescription;
import fr.aiidor.roles.use.LG;
import fr.aiidor.roles.use.LGRevive;
import fr.aiidor.roles.use.LGSalvateur;
import fr.aiidor.task.LGA;
import fr.aiidor.task.LGDeathDelay;

public class LGDeath implements Listener {
	
	public static HashMap<UUID, Integer> kills = new HashMap<>();
	
	//DISABLE AXE
	@EventHandler
	public void AXE(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player)) return;
		
		Player damager = (Player) e.getDamager();
		if (damager.getItemOnCursor().getType() == Material.WOOD_AXE ||
			damager.getItemOnCursor().getType() == Material.STONE_AXE ||
			damager.getItemOnCursor().getType() == Material.IRON_AXE ||
			damager.getItemOnCursor().getType() == Material.DIAMOND_AXE) {
			
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void Pvp(EntityDamageByEntityEvent e) {
		
		if (!(e.getEntity() instanceof Player)) return;
		
		if (UHCState.isState(UHCState.WAIT) || UHCState.isState(UHCState.PREGAME) || UHCState.isState(UHCState.STARTING)) {
			e.setCancelled(true);
			return;
		}
		
		Player damaged = (Player) e.getEntity();
		
		//LOUP GAROU AMNESIQUE
		if (LGRoles.getRole(damaged.getUniqueId()) == LGRoles.LGA && !(LGRoleManager.Power.containsKey(damaged.getUniqueId()))) {
			if((damaged.getHealth()-e.getDamage()) > 0) {
				
				if (!(e.getDamager() instanceof Player)) {return; }
				
				Player loup = (Player) e.getDamager();
				
				if (LGCamps.getCamp(loup.getUniqueId()) == LGCamps.LOUPGAROUS) {
					
					LGRoleManager.Power.put(damaged.getUniqueId(), 1); //POWER
					damaged.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§9 [Privé] Vous êtes §oLoup-Garous Amnésique.");
					damaged.sendMessage(RoleDescription.desc(damaged.getUniqueId()));
					//TASK
					LGA start = new LGA(LGUHC.getInstance(), damaged.getUniqueId());
					start.runTaskTimer(LGUHC.getInstance(), 0, 20);
					
					return;
					}
				}
		}
		
		///////MORT
		
		if((damaged.getHealth()-e.getDamage()) <= 0) {
			
			damaged.setGameMode(GameMode.SPECTATOR);
			e.setCancelled(true);
			//MORT
			Location grave = damaged.getLocation();
			
			//PARTICLE
			Particle(grave);

			
			//PVE -----------------------------------
			if (!(e.getDamager() instanceof Player)) {	
				
				ReviveVillage(damaged, grave);
				return;
			}
			
			//PVP -----------------------------------
			
			Player damager = (Player) e.getDamager();
			AddKill(damager.getUniqueId());
			
			//ANCIEN
	    	if (LGRoles.getRole(damaged.getUniqueId()) == LGRoles.Ancien && LGRoleManager.Power.containsKey(damaged.getUniqueId())) {
	    		LGRevive.ancienRevive(damaged, damager);
	    		return;
	    	}
	    	
			//EFFETS DE KILLL
			if (LGCamps.getCamp(damager.getUniqueId()) == LGCamps.LOUPGAROUS) {
				LG.LgKill(damager);
				
				//INFECT ------------------
				for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
					
					if (LGRoles.getRole(uuid) == LGRoles.LgInfect && LGRoleManager.Power.containsKey(uuid)) {
						LG.InfectMsg(uuid, damaged);
					}
				}
			}
			
			//VOLEUR
			if (LGRoles.getRole(damager.getUniqueId()) == LGRoles.Voleur) {
				
				LGDeathDelay start = new LGDeathDelay(LGUHC.getInstance(), damaged, grave, damager);
				start.runTaskTimer(LGUHC.getInstance(), 0, 20);
				
				//MODE SPECATATEUR
				damaged.setGameMode(GameMode.SPECTATOR);
		    	damaged.teleport(new Location(damaged.getWorld(), 0, 200, 0));
		    	
		    	damaged.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§b Vous êtes mort mais vous avez peut être une chance d'être réssuscité ! Veuillez attendre quelques secondes.");
		    	LGUHC.getInstance().title.sendTitle(damaged, "§cVous êtes mort !", "§6UHC", 60);
		    	
		    	return;
				}
			
		    	FakeDeath(damaged, grave);
			}
	}
	
	
	@EventHandler
	public void PVE(EntityDamageEvent e) {
		
		if (!(e.getEntity() instanceof Player)) return;	
		
		if (e.getCause() == DamageCause.ENTITY_ATTACK || 
			e.getCause() == DamageCause.ENTITY_EXPLOSION ||
			e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK ||
			e.getCause() == DamageCause.THORNS) return;
		
		if (UHCState.isState(UHCState.WAIT) || UHCState.isState(UHCState.PREGAME) || UHCState.isState(UHCState.STARTING)) {
				
				e.setCancelled(true);
				return;
			}
		
		if (e.getCause() == DamageCause.FALL) {
			Player p = (Player) e.getEntity();
			
			if (LGSalvateur.protect.contains(p.getUniqueId())) {
				e.setCancelled(true);
				return;
			}
		}
		
		Player damaged = (Player) e.getEntity();
		
		//PAS INVINCIBLE
		
		//MORT
		if((damaged.getHealth()-e.getDamage()) <= 0) {
			
        	e.setCancelled(true);
        	Location grave = damaged.getLocation();
        	
        	Particle(grave);
        	ReviveVillage(damaged, grave);
		}
		
		
	}
	
	
	//FONCTION ===================================
	private void ReviveVillage(Player damaged, Location grave) {
		
		//ANCIEN
    	if (LGRoles.getRole(damaged.getUniqueId()) == LGRoles.Ancien && LGRoleManager.Power.containsKey(damaged.getUniqueId())) {
    		LGRevive.ancienRevive(damaged, null);
    		return;
    	}
    	
    	FakeDeath(damaged, grave);
	}
	
	//FakeDeath
	private void FakeDeath(Player damaged, Location grave) {
		
		//MODE SPECATATEUR
		damaged.setGameMode(GameMode.SPECTATOR);
    	damaged.teleport(new Location(damaged.getWorld(), 0, 200, 0));
    	
    	//AJOUTE A LA LISTE DES MORTS
    	
    	
		LGDeathDelay start = new LGDeathDelay(LGUHC.getInstance(), damaged, grave, null);
		start.runTaskTimer(LGUHC.getInstance(), 0, 20);
    	
    	damaged.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§b Vous êtes mort mais vous avez peut être une chance d'être réssuscité ! Veuillez attendre quelques secondes.");
    	LGUHC.getInstance().title.sendTitle(damaged, "§cVous êtes mort !", "§6UHC", 60);
	}
	
	//AJOUTER UN KILL
	private void AddKill(UUID p) {
		
		if (!(kills.containsKey(p))) {
			kills.put(p, 0);
		}
		
		kills.put(p, kills.get(p) + 1);
	}
	
	private void Particle(Location grave) {
		
		for (int i = 0; i < 5; i++) {
			grave.getWorld().playEffect(grave, Effect.SMOKE, 15);
		}
	}
}
