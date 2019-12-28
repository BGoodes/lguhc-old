package fr.aiidor.game;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;

import fr.aiidor.GameManager;
import fr.aiidor.LGUHC;
import fr.aiidor.files.StatAction;
import fr.aiidor.files.StatManager;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;
import fr.aiidor.role.use.LGAnge;
import fr.aiidor.scoreboard.ScoreboardKill;
import fr.aiidor.scoreboard.ScoreboardManager;

public class LGEndGame {
	
	private LGUHC main;
	
	public LGEndGame(LGUHC main) {
		this.main = main;
	}
	
	public void isEnd() {
		
		if (main.isState(UHCState.FINISH)) return;
		
		int vil = 0;
		int lg = 0;
		int lgb = 0;
		
		int assassin = 0;
		int ange = 0;
		
		int couple = 0;
		int cupidon = 0;
		int coupleOp = 0;
		
		if (main.getJoueurs().size() == 0) {
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(main.gameTag + "§e§lVictoire de personne car tout le monde est mort ^^'");
			stopGame();
			return;
		}
		
		for (Joueur j : main.getJoueurs()) {
				
				LGCamps camp = j.getCamp();
				LGRoles role = j.getRole();
				
				if (j.hasCouple() && (j.getCamp() != j.getCouple().getCamp())) {
					couple++;
					
				} else {
					
					if (j.hasCouple()) {
						coupleOp++;
					}
					
					if (camp == LGCamps.Village) {
						vil++;
						if (role == LGRoles.Cupidon) {
							cupidon++;
						}
					}
					if (camp == LGCamps.LoupGarou) {
						lg++;
					}
					if (camp == LGCamps.LGB) {
						lgb++;
					}
					if (camp == LGCamps.Assassin) {
						assassin++;
					}
					
					if (camp == LGCamps.Ange) {
						ange++;
					}
				}
		}
		
		if (couple + lg + assassin + vil + ange + lgb == 0) {
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(main.gameTag + "§fPersonne ne gagne car il ne reste plus aucun représentant d'un camp.");
			stopGame();
			return;
		}
		
		//WIN DU COUPLE
		if (couple == 2 && 
				lg + assassin + vil + ange + lgb - cupidon == 0) {
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(main.gameTag + "§5§lVictoire du couple ! Bravo à eux !");
			stopGame();
			return;
		}
		
		if (coupleOp == 2 && 
				lg + assassin + vil + ange + lgb - cupidon - coupleOp == 0) {
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(main.gameTag + "§5§lVictoire du couple ! Bravo à eux !");
			stopGame();
			return;
		}
		
		//WIN DU VILLAGE
		if (vil > 0 &&
				lg + assassin + lgb + couple + ange == 0) {
			Bukkit.broadcastMessage(" ");
				Bukkit.broadcastMessage(main.gameTag + "§2§lVictoire du village ! Bravo à eux !");
				stopGame();
				return;
			}
		
			//WIN DES LG
			if (lg > 0 &&
					vil + assassin + lgb + couple + ange == 0) {
				Bukkit.broadcastMessage(" ");
				Bukkit.broadcastMessage(main.gameTag + "§c§lVictoire des Loups-Garous ! Bravo à eux !");
				stopGame();
				return;
			}
			
			//WIN DE L'ASSASSIN
			if (assassin == 1 &&
					vil + lg + lgb + couple + ange == 0) {
				Bukkit.broadcastMessage(" ");
				Bukkit.broadcastMessage(main.gameTag + "§e§lVictoire de l'Assassin ! Bravo à lui !");
				stopGame();
				return;
			}	
			
			//WIN DU LGB
			if (lgb == 1 &&
					lg + assassin + vil + couple  + ange == 0) {
				Bukkit.broadcastMessage(" ");
				Bukkit.broadcastMessage(main.gameTag + "§4§lVictoire du Loup-Garou Blanc ! Bravo à lui !");
				stopGame();
				}	
			
			if (ange == 1) {
				
				int cible = 0;
				
				for (Joueur angeJ : main.getPlayerRoles(LGRoles.Ange)) {
					if (angeJ.ange == LGAnge.Gardien) {
						if (!angeJ.angeTarget.isDead()) {
							cible = 1;
						}
					}
				}
				
				//VICTOIRE DE L'ANGE
				if (ange == 1 &&
						lg + assassin + vil + couple + lgb - cible == 0) {
					Bukkit.broadcastMessage(" ");
					Bukkit.broadcastMessage(main.gameTag + "§a§lVictoire de L'ange ! Bravo à lui !");
					stopGame();
					return;
				}
			}

	}
	
	private void stopGame() {
		
		if (main.isState(UHCState.FINISH)) {
			return;
		}
		
		main.setState(UHCState.FINISH);
		Bukkit.getScheduler().cancelAllTasks();
		
		StringBuilder msg = new StringBuilder();
		
		for (Joueur j : main.death) {
				
			if (j.Rob) msg.append("§d§l§m" + j.getName() + "§d§m : " + LGRoles.Voleur.name + " (" + j.getRole().name + ")");
			else msg.append("§d§l§m" + j.getName() + "§d§m : " + j.getRole().name);
			
			if (!j.Rob && j.ange == LGAnge.Déchu) msg.append(" §d§mDéchu");
			if (!j.Rob && j.ange == LGAnge.Gardien) msg.append(" §d§mGardien");
				
			if (j.isInfect()) msg.append(" §d§m(Infecté)");
			else if (j.getRole() == LGRoles.EnfantS && j.getCamp() == LGCamps.LoupGarou) msg.append(" §d§m(Transformé)");
			
			if (j.trublion) msg.append(" §b⇋");
			if (j.hasCouple()) msg.append(" §c♥");
			
			Bukkit.broadcastMessage(msg.toString());
			msg = new StringBuilder();
		}
		
		msg = new StringBuilder();
		
		for (Player p : main.getPlayers()) {
			if (main.getPlayer(p.getUniqueId()) != null) {
				Joueur j = main.getPlayer(p.getUniqueId());
				
				if (!j.isDead()) {
					if (j.Rob) msg.append("§d§l" + j.getName() + "§d : " + LGRoles.Voleur.name + " (" + j.getRole().name + ")");
					else msg.append("§d§l" + j.getName() + "§d : " + j.getRole().name);
					
					if (j.ange == LGAnge.Déchu) msg.append(" §dDéchu");
					if (j.ange == LGAnge.Gardien) msg.append(" §dGardien");
					
					if (j.isInfect()) msg.append(" §d(Infecté)");
					else if (j.getRole() == LGRoles.EnfantS && j.getCamp() == LGCamps.LoupGarou) msg.append(" §d(Transformé)");
					if (j.trublion) msg.append(" §b⇋");
					if (j.hasCouple()) msg.append(" §c♥");
					
					Bukkit.broadcastMessage(msg.toString());
					msg = new StringBuilder();
					
					if (main.stats) {
						if (j.getCamp() != LGCamps.Neutre) {
							new StatManager(main).changeState(j.getUUID(), "nombre_victoire", StatAction.Increase);
						}
					}
				}
			}
		}
		
		new ScoreboardManager(main).destroy();
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			new ScoreboardKill(main, p).createScoreboard();
			
			for(PotionEffect effect:p.getActivePotionEffects()){p.removePotionEffect(effect.getType());}
		}
		
		for (Joueur j : main.getJoueurs()) {
			if (j.isConnected()) {
				FireWork(j.getPlayer());
			}
		}
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				new GameManager(main).restart();
			}
		}, 20*60*5);
		
	}
	
	private void FireWork(Player p) {
		
		Firework f = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
		f.detonate();
		
		FireworkMeta fM = f.getFireworkMeta();
		
		FireworkEffect effect = FireworkEffect.builder()
				.flicker(true)
				.withColor(Color.PURPLE)
				.withFade(Color.ORANGE)
				.with(Type.BALL)
				.trail(true)
				.build();
		
		fM.addEffect(effect);
		fM.setPower(1);
		f.setFireworkMeta(fM);
		
	}
}
