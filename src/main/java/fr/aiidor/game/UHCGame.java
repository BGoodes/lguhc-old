package fr.aiidor.game;

import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;
import fr.aiidor.effect.Sounds;
import fr.aiidor.effect.Titles;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGDesc;
import fr.aiidor.role.LGItem;
import fr.aiidor.role.LGRoles;
import fr.aiidor.role.use.LGRole_Cupidon;
import fr.aiidor.role.use.LGRole_EnfantS;
import fr.aiidor.role.use.LGRole_Salvateur;
import fr.aiidor.role.use.LGRole_Trublion;
import fr.aiidor.role.use.LGRole_Voyante;
import fr.aiidor.scoreboard.ScoreboardSign;
import fr.aiidor.scoreboard.TabList;

public final class UHCGame extends BukkitRunnable {

	private LGUHC main;
	
	public UHCGame(LGUHC main) {
		this.main = main;
		this.nextEp = main.epTime * 60;
		this.Wb = main.Map;
	}
	
	private int timer = 0;
	
	private int nextEp;
	private int Wb;
	private int nextWb;
	
	private int SkyDamage = 0;
	
	private int LootCrate = 600;
	
	@Override
	public void run() {
		
		timer ++;
		main.setLimitGroup();
		
		if (timer >= main.announceRole * 60 + 10) {
			new LGEndGame(main).isEnd();
		}
		
		if (timer >= main.pvp * 60) {
			for (Player pl : Bukkit.getOnlinePlayers()) {
				getDistance(pl);
			}
		}

		//SCOREBOARD
		for (Entry<Player, ScoreboardSign> sign : main.boards.entrySet()) {
			
			sign.getValue().setLine(0, "§2 ");
			sign.getValue().setLine(1, "§bEpisode " + main.ep);
			sign.getValue().setLine(2, "§c" + main.getPlayers().size() + " §4Joueurs");
			
			if (main.ep > 1) {
				if (main.fun && main.Civilisation) {
					
					if (main.PlayerHasVillage(sign.getKey().getUniqueId())) {
						sign.getValue().setLine(3, "§e" + main.getVillage(sign.getKey().getUniqueId()).getName());
						
					} else sign.getValue().setLine(3, "§e(Village)");
					
				} else {
					sign.getValue().setLine(3, "§4Groupes de §c" + main.GroupLimit);
				}
				
			}
			
			sign.getValue().setLine(4, "§b ");
			
			int min = timer/60;
			int sec = timer%60;;
			
			if (sec<10) {
				sign.getValue().setLine(5, "§6Timer : §e" + min + " §6:§e 0" + sec);
			}
			else {
				sign.getValue().setLine(5, "§6Timer : §e" + min + " §6:§e " + sec);
			}
			
			sign.getValue().setLine(6, "§2Border : §a" + Wb);
			
		}
		
		//EFFECT
		long time = main.world.getTime();
		
		if (time >= 13000 && time < 23500) {
			nightEffect();
		}
		
		if (time >= 23500 || time < 13000) {
			dayEffect();
		}
		
		//TIME CYCLE
		
		if (main.DailyCycle) {
			
			//20
			if (main.epTime == 20) {
				
				if (timer == nextEp - 900) {
					main.world.setTime(13000);
				}
			
				if (timer == nextEp - 600) {
					main.world.setTime(23500);
				}

				if (timer == nextEp - 300) {
					main.world.setTime(13000);
				}
			}
			
			//10
			if (main.epTime == 10) {
				
				if (timer == nextEp - 300) {
					main.world.setTime(13000);
				}
			}
		}



		
		Permeffect();
		
		//1min : plus d'invincibilité ---------------------
		if (timer == 60) {
			
			main.setState(UHCState.GAME);
			Bukkit.broadcastMessage(main.gameTag + "§3Vous n'êtes maintenant plus §6invincible §3!");
		}
		
		//30Min PVP -----------------------------
		if (timer == main.pvp * 60 - 300) {
			Bukkit.broadcastMessage(main.gameTag + "§bPVP activé dans 5 min !");
		}
		
		if (timer == main.pvp * 60) {
			Bukkit.broadcastMessage(main.gameTag + "§cPVP activé !");
			main.world.setPVP(true);
			
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
				
				@Override
				public void run() {
					if (main.FinalHeal) {
						Bukkit.broadcastMessage(main.gameTag + "§aLe Final Heal est activé ! Tous les joueurs récupèrent leurs points de vie !");
						for (Player pl : Bukkit.getOnlinePlayers()) {
							pl.setHealth(pl.getMaxHealth());
						}
					}
					
					if (main.coupleAlea) {
						if (main.PlayerHasRole) {
						
							if (!main.compo.contains(LGRoles.Cupidon)) {
								if (main.Players.size() >= 2) {
									 couple();
								}
							}
						}
						else Bukkit.broadcastMessage(main.gameTag + "§cLes rôles n'on pas été distribué, le couple aléatoire sera donc désactivé !");
					}
					
					if (main.fun) {
						if (main.skyHigh) {
							
							 SkyDamage = timer + 30;
							 
							 Bukkit.getScheduler().runTaskLater(main, new Runnable() {
								
								@Override
								public void run() {
									for (Player p : Bukkit.getOnlinePlayers()) {
										new Sounds(p).PlaySound(Sound.ENDERDRAGON_GROWL);
									}
									
									Bukkit.broadcastMessage(main.gameTag + "§cAttention ! Le scénario Sky High est activé ! Si vous êtes a moins de 175 blocs en Y, vous "
											+ "perderez 1 coeur toutes les 30 secondes !");
								}
							}, 60);
						}
					}
				}
			}, 20);

		}
		
		if (main.ep == main.wbEP - 1 && timer == nextEp - 600) {
			Bukkit.broadcastMessage(main.gameTag + "§bLa world border diminuera dans 10 min !");
		}
		
		if (main.ep == main.wbEP - 1 && timer == nextEp - 300) {
			Bukkit.broadcastMessage(main.gameTag + "§bLa world border diminuera dans 5 min !");
		}
		
		if (main.noMine && main.vanillaN) {
			if (main.noMineEp != 1) {
				if (main.ep == main.noMineEp - 1 && timer == nextEp - 300) {
					Bukkit.broadcastMessage(main.gameTag + "§bVous n'aurez plus le droit de miner dans 5 min !");
				}
			}
		}

		
		//SPEEDY MINER
		if (main.run) {
			if (main.SpeedyMiner > 0) {
				if (main.ep <= main.SpeedyMiner) {
					for (Player pl : main.getPlayers()) {
						if (pl.getLocation().getY() < 32) {
							
							pl.removePotionEffect(PotionEffectType.SPEED);
							pl.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, true, false));
						}
					}
				}
			}
		}
		
		if (main.fun && main.skyHigh) {
			if (timer == SkyDamage) {
				if (timer >= main.pvp * 60) {
				
				
					SkyDamage = timer + 30;
						
					for (Player p : Bukkit.getOnlinePlayers()) {
						if (p.getGameMode() == GameMode.SURVIVAL) {
							if (p.getLocation().getY() < 175) {
								p.setHealth(p.getHealth() - 2);
								p.damage(0);
								p.sendMessage(main.gameTag + "§cMontez aux cieux avec §6une hauteur supérieur ou égal a 175 §cafin de ne pas prendre de dégâts");
								p.sendMessage(" ");
							}
						}
					}
				}
			}
		}
		
		if (main.vanilla) {
			if (main.noHunger) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.setSaturation(20);
					p.setFoodLevel(20);
				}
			}
			if (main.infiniteEnc) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.setLevel(20000);
				}
			}
			if (main.CatEyes) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
				}
			}
		}
		
		if (main.vanillaN) {
			if (main.noMine && main.isState(UHCState.GAME)) {
				if (main.ep >= main.noMineEp) {
					for (Player pl : main.getPlayers()) {
						if (pl.getLocation().getY() < main.noMineCouche) {
							pl.damage(0.5);
						}
					}
				}
			}
		}
		
		if (main.fun && main.LootCrate) {
			if (timer == LootCrate) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getGameMode() != GameMode.SPECTATOR) {
						new Sounds(p).PlaySound(Sound.CHEST_OPEN);
						giveItemStack(p, getItem(Material.CHEST, "§aLootcrate"));
						p.sendMessage(main.gameTag + "§aVous recevez un Lootcrate !");
					}
				}
				
				LootCrate = LootCrate + 600;
			}

		}
		
		//WORLDBORDER
		if (main.ep >= main.wbEP && timer == nextWb) {
			//WB
			if (Wb > main.wbMax) {
				
				Wb = Wb - main.wbSpeed;
				main.wb.setSize(Wb * 2);
				
				nextWb = timer + main.wbSecond;
			}
		}
		
		if ((timer == nextEp && main.ep != 1) || timer == main.announceRole * 60) {
			if (main.ep == 1) {
				
				main.PlayerInGame.clear();
				
				Bukkit.broadcastMessage("§b-------- Fin Episode " + main.ep + " --------");
				
				Bukkit.getScheduler().runTaskLater(main, new Runnable() {
					
					@Override
					public void run() {
						setAllRole();
						getSoeur();
						
						main.PlayerHasRole = true;
						
						for (Joueur j : main.getJoueurs()) {
							if (j.isConnected()) {
								j.sendDesc();
								new TabList(main).set(j.getPlayer());
							}
						}
						
						
						if (!main.compo.contains(LGRoles.Trublion)) {
							for (Joueur j : main.getLg()) {	
								new LGDesc(main).sendLGList(j.getPlayer());
							}
						} else {
							main.canSeeList = false;
						}
						
						main.setLimitGroup();
					}
				}, 40);
				
				Bukkit.getScheduler().runTaskLater(main, new Runnable() {
					
					//POWER
					@Override
					public void run() {
						
						if (main.compo.contains(LGRoles.MontreurDours)) {
							Montreur();
						}
						
						if (main.compo.contains(LGRoles.Voyante) || main.compo.contains(LGRoles.VoyanteB)) {
							new LGRole_Voyante(main).canSee();
						}
						
						if (main.compo.contains(LGRoles.EnfantS)) {
							new LGRole_EnfantS(main).canChoose();
						}
						
						if (main.compo.contains(LGRoles.Cupidon)) {
							new LGRole_Cupidon(main).canChoose();
						}

						if (main.compo.contains(LGRoles.Salvateur)) {
							new LGRole_Salvateur(main).canProtect();
						}
						
					}
				}, 60); //EP1 ---------------------------------------------------------------
				
			} else 	
			
			{
				
				Bukkit.broadcastMessage("§b-------- Fin Episode " + main.ep + " --------");
				
				if (main.ep == main.wbEP) {
					//WB
					Bukkit.broadcastMessage(main.gameTag + "§4La World Border va commencé à diminuer ! Elle réduira la map jusqu'en " + main.wbMax + " / -" + main.wbMax);
					nextWb = timer + 5;
				}
				
				if (main.vanillaN && main.noMineEp > 1) {
					if (main.ep == main.noMineEp) {
						Bukkit.broadcastMessage(main.gameTag + "§4Vous n'avez désormais plus le droit de miner ! Si vous restez en a une couche infèrieur a 32 vous perderez de la vie !");
					}
				}
				
				if (main.ep == 2) {
					if (main.compo.contains(LGRoles.Trublion)) {
						new LGRole_Trublion(main).canChoose();
					}
					
					
				}

				if (main.ep >= main.voteEp) {
					Bukkit.getScheduler().runTaskLater(main, new Runnable() {
						
						@Override
						public void run() {
							
							if (main.getJoueursOff().size() < 10) {
								Bukkit.broadcastMessage(main.gameTag + "§4Il reste moins de 10 joueurs ! Les votes sont donc désactivés !");
							} else {
								new LGVote(main).canVote();
							}
						}
					}, 20);
				}
				
				Bukkit.getScheduler().runTaskLater(main, new Runnable() {
					
					@Override
					public void run() {
						
						//POWER
						
						if (main.compo.contains(LGRoles.MontreurDours)) {
							Montreur();
						}
						
						if (main.compo.contains(LGRoles.Voyante) || main.compo.contains(LGRoles.VoyanteB)) {
							new LGRole_Voyante(main).canSee();
						}
						
						if (main.compo.contains(LGRoles.Salvateur)) {
							new LGRole_Salvateur(main).canProtect();
							
							for (Joueur j : main.Players) {
								j.salvation = false;
							}
								
							for (Player p : Bukkit.getOnlinePlayers()) {
								p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
							}
						}
						
						if (main.compo.contains(LGRoles.Chaman) && main.getPlayerRolesOff(LGRoles.Chaman).size() > 0) {
							if (main.ep%2 != 0) {
								Chaman();
							}
						}
					}
				}, 60);
				
				
			}
			
			if (main.DailyCycle) {
				main.world.setTime(23500);
			}
			
			nextEp = timer + main.epTime*60;
			main.ep++;
		}
	}
	
	private void setAllRole() {
		
		//JOUEUR
		for (int i = 0; i < main.getPlayers().size(); i++) {
			
			int random = new Random().nextInt(Bukkit.getServer().getOnlinePlayers().size());
			Player pl = (Player) Bukkit.getServer().getOnlinePlayers().toArray()[random];
			
			//TOUS LES JOUEURS IN GAME
			while (main.getPlayer(pl.getUniqueId()) != null || main.getSpectator().contains(pl)) {
				
				random = new Random().nextInt(Bukkit.getServer().getOnlinePlayers().size());
				pl = (Player) Bukkit.getServer().getOnlinePlayers().toArray()[random];
				
			}
			
			//ROLE	
			Random ran = new Random();
			Integer Choose = ran.nextInt(main.allRoles().size());
			LGRoles role = main.allRoles().get(Choose);
				
			if (main.RoleIsEmpty()) {
				setRole(pl, LGRoles.SV);
					
			} else {
					
				while (role.number <= 0) {
					Choose = ran.nextInt(main.allRoles().size());
					role = main.allRoles().get(Choose);
				}
					
				role.number --;
				setRole(pl, role);
			}
		}
	}
	
	private void setRole(Player p, LGRoles role) {
		
		main.Players.add(new Joueur(p.getUniqueId(), role, role.camp, main));
		
		main.PlayerInGame.add(p.getUniqueId());
		new LGItem(main.getPlayer(p.getUniqueId())).giveItem();
		
		if(role == LGRoles.LGB) {
			p.setMaxHealth(30);
		}
		
		if (!main.compo.contains(role)) main.compo.add(role);
	}
	
	private void getSoeur() {
		if (main.compo.contains(LGRoles.Soeur)) {
			if (main.getPlayerRoles(LGRoles.Soeur).size() < 2) return;
			
			for (Joueur j : main.getPlayerRoles(LGRoles.Soeur)) {
				if (j.Soeur == null) {
					for (Joueur s : main.getPlayerRoles(LGRoles.Soeur)) {
						if (s.Soeur == null) {
							if (!j.equals(s)) {
								j.Soeur = s;
								s.Soeur = j;
							}
						}
					}
				}
			}
		}
	}
	
	private void Permeffect() {
		if (main.PlayersHasRole()) {
			
			for (Joueur j : main.getJoueurs()) {
				
				if (!j.noPower && j.getCamp() == LGCamps.LoupGarou || j.getCamp() == LGCamps.LGB ||j.getRole() == LGRoles.Voyante || j.getRole() == LGRoles.VoyanteB || j.getRole() == LGRoles.PetiteFille) {
					j.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
				}
				
				if (!j.noPower && j.getRole() == LGRoles.Renard) {
					j.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
				}
				
				if (!j.noPower && j.getRole() == LGRoles.Ancien) {
					j.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
				}
				
				if (!j.noPower && j.getRole() == LGRoles.Voleur) {
					j.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
				}
				
				if (!j.noPower && j.getRole() == LGRoles.Mineur) {
					j.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1, false, false));
				}
				
				if (!j.noPower && j.getRole() == LGRoles.Pyromane) {
					j.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
				}
				
				if (j.salvation) {
					j.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
				}
				
				if (!j.noPower && j.getRole() == LGRoles.Soeur) {
					if (j.Soeur != null) {
						if (!j.Soeur.isDead() && j.Soeur.isConnected()) {
							if (j.getPlayer().getLocation().distance(j.Soeur.getPlayer().getLocation()) <= 10) {
								j.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
								j.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 0, false, false));
							}
						}
					}
				}
			}
		}
	}
	
	private void nightEffect() {
		for (Joueur j : main.getJoueurs()) {
			if (j.isConnected() && !j.noPower) {
				if (j.getCamp() == LGCamps.LoupGarou || j.getCamp() == LGCamps.LGB) {
					j.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
				}
				
				if (j.getRole() == LGRoles.PetiteFille) {
					j.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
					j.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false));
				}
				
				if (j.getRole() == LGRoles.VPL) {
					j.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
				}
				
				//REMOVE ------------------------
				if (!j.isLg()) {
					j.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				}
			}
		}
	}
	
	private void dayEffect() {
		for (Joueur j : main.getJoueurs()) {
			if (j.isConnected() && !j.noPower) {
				
				if (j.getRole() == LGRoles.Assassin) {
					j.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
				}
				
				
				//REMOVE ------------------------
				if (j.isLg()) {
					if (j.getRole() == LGRoles.VPL) {
						j.getPlayer().removePotionEffect(PotionEffectType.SPEED);
					}
					j.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				}
				
				if (j.getRole() == LGRoles.PetiteFille) {
					j.getPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
					j.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
				}
			}
		}
	}
	
	private void Montreur() {
		for (Joueur j : main.getPlayerRoles(LGRoles.MontreurDours)) {
			if (Bukkit.getOnlinePlayers().contains(j.getPlayer())) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (main.isInGame(p.getUniqueId())) {
						Joueur j2 = main.getPlayer(p.getUniqueId());
						
						if (j2.isLg() && !j2.isDead()) {
							if (p.getLocation().distance(j.getPlayer().getLocation()) <= 50) {
								Bukkit.broadcastMessage(main.gameTag + "§6Grrrrrrrr");
								
								for (Player p1 : Bukkit.getOnlinePlayers()) {
									new Sounds(p1).PlaySound(Sound.WOLF_GROWL);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void Chaman() {
		Bukkit.broadcastMessage(main.gameTag + "§bLe chaman va pouvoir écouter les morts ! Durant les 30 prochaines secondes, les joueurs morts "
				+ "pourront envoyer des messages anonymes !");
		
		main.deathChat = true;
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				
				main.deathChat = false;
				for (Joueur j : main.getPlayerRoles(LGRoles.Chaman)) {
					j.getPlayer().sendMessage(main.gameTag + "§cVous n'entendez plus les morts !");
				}
				
				for (Joueur j : main.Players) {
					if (j.isDead() && j.isConnected()) {
						j.getPlayer().sendMessage(main.gameTag + "Vous ne pouvez plus envoyer de messages au Chaman !");
					}
				}
			}
		}, 600);
	}
	
	private void couple() {
		
		if (main.PlayerHasRole) {
			if (main.getJoueursOff().size() >= 2) {
				
				Joueur a = main.getJoueursOff().get(new Random().nextInt(main.getJoueursOff().size()));
				Joueur b = main.getJoueursOff().get(new Random().nextInt(main.getJoueursOff().size()));
				
				while (a.equals(b)) {
					a = main.Players.get(new Random().nextInt(main.Players.size()));
				}
				
				new LGRole_Cupidon(main).choose(null, a, b);
				
			}
			else {
				Bukkit.broadcastMessage(main.gameTag + "§cIl n'y a pas assez de joueurs, le couple aléatoire sera donc désactivé !");
				return;
			}

		}
	}
	

	public void getDistance(Player player) {
		
		
		double distance = Math.sqrt(Math.pow((main.Spawn.getX()-player.getLocation().getBlockX()), 2) 
				+ Math.pow((main.Spawn.getZ()-player.getLocation().getBlockZ()), 2));
		
		if (distance <= 300) {
			
			new Titles().sendActionText(player, "§5Distance au centre : §6✈ Entre 0 et 300 blocs ");
			return;
		}
		
		if (distance > 300 && distance <= 600) {
			new Titles().sendActionText(player, "§5Distance au centre : §e✈ Entre 300 et 600 blocs");
			return;
		}
		
		if (distance > 600 && distance <= 900) {
			new Titles().sendActionText(player, "§5Distance au centre : §a✈ Entre 600 et 900 blocs");
			return;
		}
		
		if (distance > 900 && distance <= 1200) {
			new Titles().sendActionText(player, "§5Distance au centre : §3✈ Entre 900 et 1200 blocs");
			return;
		}
		
		if (distance > 1200 && distance <= 1500) {
			new Titles().sendActionText(player, "§5Distance au centre : §9✈ Entre 1200 et 1500 blocs");
			return;
		}
		
		if (distance > 1500 && distance <= 1800) {
			new Titles().sendActionText(player, "§5Distance au centre : §1✈ Entre 1500 et 1800 blocs");
			return;
		}
		
		if (distance > 1800) {
			new Titles().sendActionText(player, "§5Distance au centre : §4✈ + de 1800 blocs");
			return;
		}
	}
	
	private void giveItemStack(Player p, ItemStack it) {
		if (isInventoryFull(p)) p.getWorld().dropItem(p.getLocation(), it);
		else p.getInventory().addItem(it);
	}
	
	private boolean isInventoryFull(Player p)
	{
	    return p.getInventory().firstEmpty() == -1;
	}
	
	private ItemStack getItem(Material mat, String Name) {
		
		ItemStack it = new ItemStack(mat, 1);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName(Name);
		
		it.setItemMeta(itM);
		return it;
	}
}
