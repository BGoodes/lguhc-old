package fr.aiidor.task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;
import fr.aiidor.effect.Sounds;
import fr.aiidor.files.StatAction;
import fr.aiidor.files.StatManager;
import fr.aiidor.game.Joueur;
import fr.aiidor.game.UHCState;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;
import fr.aiidor.role.use.LGAnge;
import fr.aiidor.role.use.LGRole_Chasseur;
import fr.aiidor.role.use.LGRole_Soeur;
import fr.aiidor.role.use.LGRole_Sorciere;
import fr.aiidor.scoreboard.TabList;

public class LGDeath extends BukkitRunnable {
	
	private int timer = 12;
	
	private Location grave;
	
	private LGUHC main;
	
	private UUID dead;
	private List<ItemStack> inv;
	private UUID killer;
	
	public LGDeath(LGUHC main, UUID dead, UUID killer, Location grave, List<ItemStack> inv) {
		
		this.main = main;
		this.dead = dead;
		this.grave = grave;
		this.inv = inv;
		
		if (killer == null) this.killer = null;
		else this.killer = killer;
		
		main.respawnInstant(Bukkit.getPlayer(dead));
		
		if (!main.getPlayer(dead).isDying()) {
			main.getPlayer(dead).setDyingState(LGCamps.Neutre);
		}
	}

	
	@Override
	public void run() {
		
		if (main.getPlayer(dead).isDead()) {
			cancel();
			return;
		}
		
		timer --;
		
		if (timer == 6) {
			
			Joueur j = main.getPlayer(dead);
			//SORCIERE
			j.setDyingState(LGCamps.Village);
			if (!j.isRea()) {
				new LGRole_Sorciere(main).ReaMsg(j.getPlayer());
				
			} else {
				//INFECT
				revive(j, true);
				cancel();
				return;
			}
		}
		//TIMER == 0
		if (timer == 0) {
			Joueur j = main.getPlayer(dead);
			if (j.isRea()) {
				revive(j, false);
				cancel();
				return;
			}
			
			death();
			cancel();
			return;
        }
	}
	
	public void death() {
		
		if (main.PlayerHasRole) {
			if (main.hasRole(dead)) {
				death(dead, grave, inv, killer);
			}
		}
	}
	
	private void death(UUID dead, Location grave, List<ItemStack> inv, UUID killer) {
		Joueur j = main.getPlayer(dead);
		
		main.dropInventory(dead, grave, inv);
		
		if (main.deathItem != null) {
			int slot = 0;
			for (ItemStack it : main.deathItem) {
				if (it != null) {
					if (it.getType() != Material.AIR) {
						grave.getWorld().dropItemNaturally(grave, it);
					}
				}
				
				slot ++;
				
				if (slot == 18) break;
			}
			
			
		}
		
		j.setDead(true);
		main.Spectator.add(dead);
		if (j.isConnected()) new TabList(main).set(j.getPlayer());
		
		if (main.stats) new StatManager(main).changeState(dead, "mort", StatAction.Increase);
		
		if (main.nhide.hasEntry(j.getName())) {
			main.nhide.removeEntry(j.getName());
		}
		
		if (!main.isState(UHCState.FINISH)) main.death.add(j);
		
		String village = "Le village";
		
		if (main.PlayerInGame.contains(dead)) main.PlayerInGame.remove(dead);
		if (main.PlayerHasVillage(dead)) {
			village = main.getVillage(dead).getName();
		}
		
		if (j.LastWill != null) {
			Bukkit.broadcastMessage("§8[MORT] §o" + j.getName() + "§8 >> §7" + j.LastWill);
		}
		
		if (j.hasCouple() && j.getCouple().isDead()) {
			
			Bukkit.broadcastMessage("§c§l==========§4§k0§c§l==========");
			
			if (j.Rob) Bukkit.broadcastMessage("§2§l" + j.getName() + " à décidé de rejoindre §l" + j.getCouple().getName() + " §2dans sa tombe §c♥ §2 il étais §o" + LGRoles.Voleur.name);
			else Bukkit.broadcastMessage("§2§l" + j.getName() + " §2à décidé de le rejoindre dans sa tombe §c♥ §2 il étais §o" + j.getRole().name);
			
			Bukkit.broadcastMessage("§c§l=====================");	
		}
		else {
			Bukkit.broadcastMessage("§c§l==========§4§k0§c§l==========");
			
			if (j.Rob) Bukkit.broadcastMessage("§2" + village + " à perdu un de ses membres : §l" + j.getName() + "§2 qui étais §o" + LGRoles.Voleur.name);
			else Bukkit.broadcastMessage("§2" + village + " à perdu un de ses membres : §l" + j.getName() + "§2 qui étais §o" + j.getRole().name);
			
			Bukkit.broadcastMessage("§c§l=====================");	
		}
		
		//HEAL + FEED
		if (j.isConnected()) {
			Player player = j.getPlayer();
			
			main.reset(player);	
			player.setGameMode(GameMode.SPECTATOR);
			
			if (j.Rob) player.setPlayerListName("§8[MORT] §7" + player.getName() + " §8(" + LGRoles.Voleur.name + ")"); 
			else player.setPlayerListName("§8[MORT] §7" + player.getName() + " §8(" + j.getRole().name + ")"); 
			
			player.sendMessage("§cVotre aventure au sein du village s'arrête ici !");
		    
			player.sendMessage("§6===============§4§k0§6===============");
			player.sendMessage("§bVous êtes spectateur : §9faites /spec §bpour accéder aux commandes des spectateurs !");
			player.sendMessage("§6===============§4§k0§6===============");
			player.sendMessage(" ");
		}

		
		for (Player pl : Bukkit.getOnlinePlayers()) {
	    	pl.playSound(pl.getLocation(), Sound.AMBIENCE_THUNDER, 8.0F, 0.8F);
	    }
		
		if (j.getRole() == LGRoles.Chasseur) {
			new LGRole_Chasseur(main).canPan(j);
		}
		
		if (j.getRole() == LGRoles.Trublion) {
			Bukkit.broadcastMessage(main.gameTag + "§cLe trublion à été tué ! Par conséquent, tous les joueurs ont été téléporté aléatoirement sur la map !");
			for (Joueur all : main.getJoueurs()) {
				if (!all.isDying()) {
					main.TpPower(all.getPlayer());
					new Sounds(all.getPlayer()).PlaySound(Sound.ENDERMAN_TELEPORT);
				}
			}
		}
	    
		if (main.compo.contains(LGRoles.EnfantS)) {
			if (!main.getPlayerRolesOff(LGRoles.EnfantS).isEmpty()) {
				for (Joueur es : main.getPlayerRolesOff(LGRoles.EnfantS)) {
					if (es.Model != null) {
						if (es.Model.equals(j)) {
							
							if (!es.isLg() && es.isConnected()) {
								
								es.getPlayer().sendMessage(main.gameTag + "§6Votre modèle est mort ! Vous devenez donc un membre des loups garous et héritez de leurs pouvoirs !");
								es.getPlayer().sendMessage(" ");
							}
							
							main.addLg(es);
						}
					}
		
				}
			}
		}
		
		if (main.compo.contains(LGRoles.Ange)) {
			if (!main.getPlayerRolesOff(LGRoles.Ange).isEmpty()) {
				
				for (Joueur ange : main.getPlayerRolesOff(LGRoles.Ange)) {
					if (ange.angeTarget.equals(j)) {
						if (ange.ange == LGAnge.Gardien) {
							
							if (ange.isConnected()) {
								
								ange.getPlayer().sendMessage(main.gameTag + "§cVotre protégé est mort ! Vous serez donc à 10 coeurs et vous obtenez l'effect Weakness I jusqu'a la fin de la partie !");
								
								if (!ange.isVoteCible()) ange.getPlayer().setMaxHealth(20);
								else ange.getPlayer().setMaxHealth(10);
							}
						}
						
						if (ange.ange == LGAnge.Déchu) {
							
							Boolean haskill = false;
							
							if (killer != null) {
								if (main.hasRole(killer) && main.getPlayer(killer).equals(ange)) {
									haskill = true;
								}
							}
							
							if (!haskill) {
								if (ange.isConnected()) {
									ange.getPlayer().sendMessage(main.gameTag + "§cVotre cible est morte et vous n'avez pas réussi à la tuer ! Vous resterez donc à 12 coeurs");
								} 
							}
						}
					}
				}
			}
		}
		
		//KILLER -------------------------------------------
		if (killer != null) {
			
			if (main.hasRole(killer)) {
				for (Player p : main.getSpectator()) {
					p.sendMessage("§c[§6SPEC-INFO§c] §6Tué par " + main.getPlayer(killer).getName());
				} 
				
				Joueur k = main.getPlayer(killer);
				if (!main.isState(UHCState.FINISH)) {
					k.addkill();
					if (main.stats) new StatManager(main).changeState(killer, "kill", StatAction.Increase);
				}
				
				if (k.isConnected()) {
					j.Killer = k;
					
					if (!Bukkit.getOnlinePlayers().contains(j.getPlayer())) return;
					
					k.getPlayer().removePotionEffect(PotionEffectType.ABSORPTION);
					
					if (k.isLg()) {	
						if (k.getRole() == LGRoles.VPL || k.getRole() == LGRoles.LGA && k.getPower() < 2) {
							k.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0, false, false));
						} else {
							
							k.getPlayer().removePotionEffect(PotionEffectType.SPEED);
							
							k.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 0, false, false));
							k.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0, false, false));
							
						}
					}
					
					if (j.getRole() == LGRoles.Ancien && k.getCamp() == LGCamps.Village) {
						if (k.isConnected()) {
							
							k.getPlayer().setHealth(k.getPlayer().getHealth() / 2);
							k.getPlayer().damage(0);
							k.getPlayer().sendMessage(main.gameTag + "§cVous avez tué l'ancien et perdez par conséquent la moitié de votre vie ainsi que votre pouvoir si vous en aviez un ");
							
							for(PotionEffect effect:k.getPlayer().getActivePotionEffects()) {
								if (effect.getType() != PotionEffectType.REGENERATION && effect.getType() != PotionEffectType.ABSORPTION) {
									k.getPlayer().removePotionEffect(effect.getType());
								}
							}
						}
						
						k.setPower(0);
						k.noPower = true;
						
					}
					
					if (k.getRole() == LGRoles.Assassin) {
						if (k.isConnected()) {
							k.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 0, false, false));
							k.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0, false, false));
						}
					}
					
					if (k.getRole() == LGRoles.LGA) {
						
						if (k.getPower() == 0) {
							new LGLgaWait(main, k);
							
							k.setPower(1);
							
							if (k.isConnected()) {		
								k.getPlayer().sendMessage(main.gameTag + "§bVous avez tué un villageois mais les loups-garous ne vous reconnaissent pas encore ! Il vous reconnaitrons que dans 15 minutes !");
							}
						}
					}
					
					if (k.getRole() == LGRoles.Ange) {
						if (k.ange == LGAnge.Déchu) {
							if (k.angeTarget.equals(j)) {
								
								if (k.isConnected()) {
									
									k.getPlayer().sendMessage(main.gameTag + "§bVous avez tué votre cible ! Vous aurez par conséquent 15 Coeurs !");
									
									if (!k.isVoteCible()) k.getPlayer().setMaxHealth(30);
									else k.getPlayer().setMaxHealth(15);
									
									k.setPower(1);
								}
							}
						}
					}
					
					//VOLEUR --------------------------------------
					if (k.getRole() == LGRoles.Voleur) {
						
						k.Rob = true;
						
						k.setRole(j.getRole());
						k.setPower(j.getPower());
						
						if (!k.isInfect()) k.setCamp(j.getCamp());
						
						if (j.isInfect()) {
							k.setInfect(true);
						}
						
						k.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
						k.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0, false, false));
						
						k.getPlayer().sendMessage(main.gameTag + "§bVous avez fait votre première victime et récupérer donc le rôle et les pouvoirs de votre victime !");
						k.getPlayer().sendMessage("§bFaites /lg role pour en savoir plus !");
						k.getPlayer().sendMessage(" ");
						
						//VOTES ET HEALTH
						if (k.isVoteCible()) {
							k.getPlayer().setMaxHealth(j.getPlayer().getMaxHealth() / 2);
						} else if (j.isVoteCible()) {
							k.getPlayer().setMaxHealth(j.getPlayer().getMaxHealth() * 2);
							
						} else {
							k.getPlayer().setMaxHealth(j.getPlayer().getMaxHealth());
						}
						
						//SOEUR
						if (j.getRole() == LGRoles.Soeur) {
							j.Soeur.Soeur = k;
							k.Soeur = j.Soeur;
							j.Soeur = null;
						}
						
						if (j.getRole() == LGRoles.EnfantS) {
							k.Model = j.Model;
						}
						
						if (j.getRole() == LGRoles.LGA) {
							k.LgList = j.LgList;
							
							k.LgList.add(k);
							k.LgList.remove(j);
							
							k.setPower(2);
						}
						
						if (j.getRole() == LGRoles.Ange) {
							k.ange = j.ange;
							k.angeTarget = j.angeTarget;
						}
						
						//COUPLE
						if (j.hasCouple() && !k.hasCouple()) {
							if (!j.getCouple().equals(k)) {
								
								j.getCouple().setCouple(k);
								j.setCouple(null);
								
								k.setCouple(j.getCouple());
								
								k.getPlayer().sendMessage(main.gameTag + "§c♥§3 Vous êtes amoureux de §6" + k.getCouple().getName() + "§3 si il vient à mourrir, vous mourrerez aussitôt !");
							}
						}
						
						if (k.isLg()) {
							main.revealLg(k);
						}
					}
				}
				
				if (k.isConnected()) new TabList(main).set(k.getPlayer());
			}
		}
		
		if (j.getRole() == LGRoles.Soeur) {
			if (j.Soeur != null) {
				if (!j.Soeur.isDead()) {
					new LGRole_Soeur(main).canSeeMsg(j.Soeur);
				}
			}
		}
		
		if (j.hasCouple() && !j.getCouple().isDead()) {
			
			if (j.getCouple().isConnected()) {
				death(j.getCouple().getUUID(), j.getCouple().getPlayer().getLocation(), inv(j.getCouple().getPlayer()), killer);
						
			} else {
				death(j.getCouple().getUUID(), j.getPlayer().getLocation(), main.DecoInv.get(j.getCouple().getUUID()), killer);
			}
		}
	}
	
	private List<ItemStack> inv(Player player) {
		
		List<ItemStack> inv = new ArrayList<>();
		
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null) inv.add(item);
			
		}
		for (ItemStack item : player.getInventory().getArmorContents()) {
			if (item != null) inv.add(item);
		}
		return inv;
	}
	
	private void revive(Joueur Target, Boolean infect) {
		
		if (!Target.isConnected()) return;
		
		Target.setDyingState(null);
		Target.setRea(false);
		
		Player player = Target.getPlayer();
		
		if (infect) {
			
			player.sendMessage(main.gameTag + "§aL'infect Père des Loups à décidé de vous ressusciter !");
			player.sendMessage(main.gameTag + "§4Vous faites désormais partie du camp des Loups-Garous mais gardez vos mêmes pouvoirs !");
			
			if (!Target.isLg()) {
				main.addLg(Target);
			}

			Target.setInfect(true);
			
		} else {
			player.sendMessage(main.gameTag + "§aLa sorcière à décidé de vous ressusciter !");
		}
		
		if (main.Spectator.contains(Target.getUUID())) main.Spectator.remove(Target.getUUID());
		
		main.TpPower(player);
		
		for(PotionEffect effect:player.getActivePotionEffects()){player.removePotionEffect(effect.getType());}
		player.setGameMode(GameMode.SURVIVAL);
		
		player.setHealth(player.getMaxHealth());
	}
}
