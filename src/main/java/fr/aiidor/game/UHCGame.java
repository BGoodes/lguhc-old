package fr.aiidor.game;

import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;
import fr.aiidor.events.LGEndGame;
import fr.aiidor.roles.LGRoleManager;
import fr.aiidor.roles.LGRoles;
import fr.aiidor.roles.LGSetRole;
import fr.aiidor.roles.RoleDescription;
import fr.aiidor.roles.use.LG;
import fr.aiidor.roles.use.LGCupidon;
import fr.aiidor.roles.use.LGMontreur;
import fr.aiidor.roles.use.LGPermEffects;
import fr.aiidor.roles.use.LGSalvateur;
import fr.aiidor.roles.use.LGTrublion;
import fr.aiidor.roles.use.LGVoyante;
import fr.aiidor.roles.use.LGroleSolo;
import fr.aiidor.scoreboard.ScoreboardSign;
import fr.aiidor.task.LGTime;
import fr.aiidor.utils.LGDistance;
import fr.aiidor.utils.LGGroups;
import fr.aiidor.utils.LGVote;

public class UHCGame extends BukkitRunnable{
	
	public static int timer = 0;
	private int nextEP = 10; //TEMPORAIRE (Vrai valeur = 1200)
	public static int ep = 1;
	private boolean vote = false;
	public long time;
	
	public static int GroupLimit = 3;
	
	public static int wbT = 1000;
	private int nextWb;
	
	public UHCGame(LGUHC main) {
	}
	
	@Override
	public void run() {
		timer ++;
		
		//SCOREBOARD
		for (Entry<Player, ScoreboardSign> sign : LGUHC.getInstance().boards.entrySet()) {
			
			sign.getValue().setLine(0, "§2 ");
			sign.getValue().setLine(1, "§bEpisode " + ep);
			sign.getValue().setLine(2, "§c" + LGUHC.getInstance().PlayerInGame.size() + " §4Joueurs");
			if (ep> 1) {
				sign.getValue().setLine(3, "§4Groupes de §c" + GroupLimit);
			}
			
			sign.getValue().setLine(4, "§b ");
			
			int min = UHCGame.timer/60;
			int sec = UHCGame.timer%60;;
			sign.getValue().setLine(5, "§6Timer : §e" + min + " §6Min§e " + sec);
			sign.getValue().setLine(6, "§2Border : §a" + wbT);
			
		}
		
		//WB
		if (timer == nextWb && ep == 6) {
			
			if (wbT != 300) {
				wbT --;
				LGUHC.getInstance().wb.setSize(wbT * 2);
				
				nextWb = nextWb + 5;
			}
		}
		
		
		
		if (!(GroupLimit <= 3)) {
			GroupLimit = (LGUHC.getInstance().PlayerInGame.size() / 2);
		}
		else GroupLimit = 3;
		
		if (UHCState.isState(UHCState.FINISH)) {
			cancel();
			return;
		}
		
		if (ep > 1) {
			
			//Distance Spawn
			for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
				
				Player pl = Bukkit.getPlayer(uuid);
				LGDistance.getDistance(pl); //DISTANCE AU SPAWN
				LGPermEffects.PermEffect(pl); //EFFETS PERMS
				
				//CHECK WIN
				LGEndGame.CheckWin();
			}
			
		}
		
		
		//1min : plus d'invincibilité ---------------------
		if (timer == 60) {
			
			UHCState.setState(UHCState.GAME);
			Bukkit.broadcastMessage("§b§l[§6§lUHC§b§l]§3 Vous n'êtes maintenant plus §6invincible §3!");
		}
		
		//30Min PVP -----------------------------
		if (timer == 1800) {
			Bukkit.broadcastMessage("§b§l[§6§lUHC§b§l] §bPVP activé !");
			Bukkit.getWorld("world").setPVP(true);
		}
		
		if (timer == nextEP - 600 && ep > 3) {
			
			if (LGVote.cible != null) {
				
				LGVote.cible.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
				
				if (LGRoles.getRole(LGVote.cible.getUniqueId()) == LGRoles.LGB) {
					LGVote.cible.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(25);
				}
			}
		}
		
		//VOTE LIMITE --------------------------
		if (timer == nextEP - 1140 && ep > 3) {
			LGVote.Result();
		}
		
		//EPISODES ----------------------------
		if (timer == nextEP) {
			if (ep == 1) { //EPISODE 1 = ANNONCE DES ROLES +  -----------------------------------
				
				UHCState.setState(UHCState.GAMEPVP);
				Bukkit.broadcastMessage("§b§l[§6§lUHC§b§l] §bPVP activé dans 10 min !");
				Bukkit.broadcastMessage("§b-------- Fin Episode " + ep + " --------");
				
				Bukkit.getScheduler().runTaskLater(LGUHC.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						
						LGSetRole.setRole();
						
						for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
							
							Player pl = Bukkit.getPlayer(uuid);
							
							pl.sendMessage(RoleDescription.desc(uuid));
							LG.LGCompo(pl); //LG DESC
							
							LGRoleManager.setPower(uuid);
							GroupLimit = LGGroups.setGroups();
							
							//BUFF LGB
							LGroleSolo.LGBBuff(pl);
						}	
					}
				}, 60);
				
				//TIME
				LGTime start = new LGTime(LGUHC.getInstance());
				start.runTaskTimer(LGUHC.getInstance(), 0, 1);	
				
				//POUVOIRS EP1
				Bukkit.getScheduler().runTaskLater(LGUHC.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						
						LGCupidon.canChoose();
					}
				}, 40);
			}
			
			
			//EPISODE 1 -----------------------------------------------------------------------------
			
			if (ep != 1) {
				Bukkit.broadcastMessage("§b-------- Fin Episode " + ep + " --------");
			}
			
			Bukkit.getScheduler().runTaskLater(LGUHC.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					
					//MONTREUR D'OURS
					for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
						LGMontreur.Grrr(uuid);
					}
			
					//VOTES -----------------
					if (ep == 4) {
						vote = true; //ON ACTIVE LES VOTES
					}
					if (vote) {
						//LES VOTES
						LGVote.AnounceVote(); //ON ANNONCE LES VOTES
					}
					//------------------------
					
				}
			}, 40);
			
			if (ep == 2) {
				LGTrublion.canSwitch();
			}
			
			if (ep == 5) {
				
				Bukkit.getScheduler().runTaskLater(LGUHC.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						Bukkit.broadcastMessage("§b§l[§6§lUHC§b§l]§4 La World Border va commencé à diminuer ! Elle réduira la map jusqu'en 300 / -300 !");
						nextWb = timer + 5;
					}
				}, 40);
			}
			
			//POUVOIR:
			Bukkit.getScheduler().runTaskLater(LGUHC.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					LGVoyante.canSee();
					LGSalvateur.EndProtect();
					LGSalvateur.canChoose();
					
				}
			}, 40);

			
			nextEP = nextEP + 1200;
			ep ++;
			
			if (!LGUHC.getInstance().Run) {
				Bukkit.getWorld("world").setTime(23499);
			}
		}
		
		
	}
	
	
}
