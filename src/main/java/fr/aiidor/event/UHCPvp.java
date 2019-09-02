package fr.aiidor.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.LGUHC;
import fr.aiidor.effect.WorldSound;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;
import fr.aiidor.role.use.LGRole_Ancien;
import fr.aiidor.role.use.LGRole_IPL;
import fr.aiidor.task.LGDeath;
import fr.aiidor.task.LGLga;
import net.minecraft.server.v1_8_R3.EntityLiving;

public class UHCPvp implements Listener{

	private LGUHC main;
	public UHCPvp(LGUHC main) {
		this.main = main;
	}

	//GAPPLE
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		if (e.getItem().getType() == Material.GOLDEN_APPLE) {
			Player player = e.getPlayer();

			ItemStack hand = player.getItemInHand();
			int amount = hand.getAmount();


			if (!main.Notch) {
				if (hand.getDurability() == (byte) 1) {
					e.setCancelled(true);

					if (amount > 1) {
						hand.setAmount(amount - 1);
						player.setItemInHand(hand);
					} else {
						player.setItemInHand(new ItemStack(Material.AIR));
					}

					player.setSaturation((float) (player.getSaturation() + 9.6));
					player.setFoodLevel(player.getFoodLevel() + 4);

					new WorldSound(player.getLocation()).PlaySound(Sound.BURP);

					player.sendMessage("");
					player.sendMessage(main.gameTag + "§cPommes de Notch désactivé !");
					return;
				}
			}


			if (main.Abso == 0) {
				e.setCancelled(true);

				if (amount > 1) {
					hand.setAmount(amount - 1);
					player.setItemInHand(hand);
				} else {
					player.setItemInHand(new ItemStack(Material.AIR));
				}

				effect(player);
			}

			if (main.Abso == 1) {
				e.setCancelled(true);

				if (amount > 1) {
					hand.setAmount(amount - 1);
					player.setItemInHand(hand);
				} else {
					player.setItemInHand(new ItemStack(Material.AIR));
				}

				effect(player);
				player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0));

				EntityLiving cp = ((CraftPlayer)player).getHandle();
				cp.setAbsorptionHearts(2);
			}

			if (main.Abso == 2) {
				e.setCancelled(true);

				if (amount > 1) {
					hand.setAmount(amount - 1);
					player.setItemInHand(hand);
				} else {
					player.setItemInHand(new ItemStack(Material.AIR));
				}

				effect(player);
				player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0));
			}

			if (main.GoldenHead) {
				if (hand.hasItemMeta()) {
					if (hand.getItemMeta().getDisplayName().equalsIgnoreCase("§6Golden Head")) {

						player.removePotionEffect(PotionEffectType.REGENERATION);

						player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50 * main.HeadHeal, 1));
						return;
					}
				}
			}
		}

		if (e.getItem().getType() == Material.POTION) {
			if (main.getPlayer(e.getPlayer().getUniqueId()) != null) {
				if (main.getPlayer(e.getPlayer().getUniqueId()).getRole() == LGRoles.Valentin) {
					Valentin(e.getPlayer());
				}
			}
		}
	}

	private void effect(Player player) {

		player.updateInventory();

		player.setSaturation((float) (player.getSaturation() + 9.6));
		player.setFoodLevel(player.getFoodLevel() + 4);

		new WorldSound(player.getLocation()).PlaySound(Sound.BURP);

		player.removePotionEffect(PotionEffectType.ABSORPTION);
		player.removePotionEffect(PotionEffectType.REGENERATION);
		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
	}


	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player player = e.getEntity().getPlayer();
		Location loc = e.getEntity().getPlayer().getLocation();


		if (!main.PlayerHasRole) {

			player.setPlayerListName("§7[Spec] §f" + player.getName());

			if (!main.Spectator.contains(player.getUniqueId())) main.Spectator.add(player.getUniqueId());

			main.respawnInstant(player);

			Bukkit.getScheduler().runTaskLater(main, new Runnable() {

				@Override
				public void run() {

					Bukkit.broadcastMessage("§c§l==========§4§k0§c§l==========");
					Bukkit.broadcastMessage("§2Le village à perdu un de ses membres : §l" + player.getName());
					Bukkit.broadcastMessage("§c§l=====================");

					for (Player pl : Bukkit.getOnlinePlayers()) {
						pl.playSound(pl.getLocation(), Sound.AMBIENCE_THUNDER, 8.0F, 0.8F);
					}

					player.sendMessage("§6===============§4§k0§6===============");
					player.sendMessage("§bVous êtes spectateur : §9faites /spec §bpour accéder aux commandes des spectateurs !");
					player.sendMessage("§6===============§4§k0§6===============");
					player.sendMessage(" ");

					player.teleport(main.Spawn);
					main.reset(e.getEntity());
					player.setGameMode(GameMode.SPECTATOR);
				}
			}, 5);
			return;
		}

		if (main.getPlayer(player.getUniqueId()) == null) return;
		main.getPlayer(player.getUniqueId()).setNoFall(true);

		if (e.getEntity().getKiller() != null) {

			Player killer = e.getEntity().getKiller();
			player.sendMessage(main.gameTag + "§bVous êtes mort mais vous avez peut être une chance d'être réssuscité ! Veuillez attendre quelques secondes.");
			player.sendMessage(" ");

			if (main.isInGame(killer.getUniqueId())) {
				LGDeath task = new LGDeath(main, player.getUniqueId(), killer.getUniqueId(), loc, inv(player));
				task.runTaskTimer(main, 0 , 20);

				if (main.getPlayer(killer.getUniqueId()).isLg()) {
					//INFECT
					main.getPlayer(player.getUniqueId()).setDyingState(LGCamps.LoupGarou);
					for (Joueur j : main.Players) {
						if (j.getRole() == LGRoles.IPL) {
							new LGRole_IPL(main).ReaMsg(player);
						}
					}

				}
				return;
			}
		}

		LGDeath task = new LGDeath(main, player.getUniqueId(), null, loc, inv(player));
		task.runTaskTimer(main, 0 , 20);
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


	//ANCIEN
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent e) {
		//ANTI ROD
		if (main.RodLess) {
			if (e.getDamager() instanceof FishHook) {
				e.setCancelled(true);
				return;
			}
		}

		if (main.Cupid) {
			if (e.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow) e.getDamager();

				if (arrow.getShooter() instanceof Player && e.getEntity() instanceof Player) {
					Player player = (Player) arrow.getShooter();

					if (player.getHealth() + 1/5 < 20) player.setHealth(player.getHealth() + 1/5);
					else player.setHealth(20);

				}
			}
		}

		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if (main.getPlayer(player.getUniqueId()) == null) return;

			Joueur j = main.getPlayer(player.getUniqueId());

			//LGA
			if (j.getRole() == LGRoles.LGA) {
				if (e.getDamager() instanceof Player) {
					Player damager = (Player) e.getDamager();

					if (!main.isInGame(damager.getUniqueId())) return;
					Joueur k = main.getPlayer(damager.getUniqueId());
					if (k.isLg()) {
						//LGA EFFECT
						j.getPlayer().sendMessage(main.gameTag + "§cAu contact de votre agresseur, vous êtes frappé d'une révélation ! Soudainement la mémoire vous revient :"
								+ " vous êtes Loup-Garou vous aussi !");
						j.setPower(1);

						Bukkit.getScheduler().runTaskLater(main, new Runnable() {

							@Override
							public void run() {
								j.sendDesc();
							}
						}, 120);

						new LGLga(main, j).runTaskTimer(main, 0, 20);
					}

				}

				return;
			}



			//MORT
			if((player.getHealth()-e.getFinalDamage()) <= 0) {

				//INGAME
				if (main.isInGame(player.getUniqueId())) {
					//ANCIEN
					if (j.getRole() == LGRoles.Ancien) {

						//POWER
						if (j.getPower() == 0) return;

						e.setCancelled(true);
						main.getPlayer(player.getUniqueId()).setPower(0);

						//ASSASSINE
						if (e.getDamager() instanceof Player) {
							Player killer = (Player) e.getDamager();

							if (main.isInGame(killer.getUniqueId())) {
								Joueur k = main.getPlayer(killer.getUniqueId());

								if (!k.isLg()) {
									main.getPlayer(player.getUniqueId()).setPower(0);
									player.setMaxHealth(10);
								}
							}


						} else {

							//ARROW
							if (e.getDamager() instanceof Projectile) {
								Projectile arrow = (Projectile) e.getDamager();

								if (arrow.getShooter() instanceof Player) {
									Player killer = (Player) arrow.getShooter();

									if (main.isInGame(killer.getUniqueId())) {
										Joueur k = main.getPlayer(killer.getUniqueId());

										if (!k.isLg()) {
											main.getPlayer(player.getUniqueId()).setPower(0);
											player.setMaxHealth(10);
										}
									}
								}
							}

							else main.getPlayer(player.getUniqueId()).setPower(0);
						}

						new LGRole_Ancien(main, player.getUniqueId()).Revive();
						return;
					}
				}
			}
		}
	}


	//ANCIEN
	@EventHandler
	public void onDamage(EntityDamageEvent e) {

		if (e.getCause() == DamageCause.ENTITY_ATTACK) return;
		if (e.getCause() == DamageCause.ENTITY_EXPLOSION) return;
		if (e.getCause() == DamageCause.MAGIC) return;
		if (e.getCause() == DamageCause.PROJECTILE) return;
		if (e.getCause() == DamageCause.THORNS) return;

		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();

			if((player.getHealth()-e.getFinalDamage()) <= 0) {

				if (main.isInGame(player.getUniqueId())) {
					if (main.getPlayer(player.getUniqueId()).getRole() == LGRoles.Ancien) {

						if (main.getPlayer(player.getUniqueId()).getPower() == 0) return;

						main.getPlayer(player.getUniqueId()).setPower(0);
						player.setMaxHealth(10);
						new LGRole_Ancien(main, player.getUniqueId()).Revive();
						e.setCancelled(true);
					}
				}
			}

		}
	}


	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(main.Spawn);
	}

	private void Valentin(Player p) {
		int Choose = new Random().nextInt(10);

		switch (Choose) {
			case 0:
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 6000, 0, false, false));
				break;
			case 1:
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6000, 0, false, false));
				break;
			case 5:
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 0, false, false));
				break;
			case 6:
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 6000, 0, false, false));
				break;
			case 7:
				p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6000, 0, false, false));
				break;
			case 8:
				p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 6000, 0, false, false));
				break;
			case 9:
				p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 6000, 0, false, false));
				break;
			case 10:
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 6000, 1, false, false));
				break;
		}
	}
}
