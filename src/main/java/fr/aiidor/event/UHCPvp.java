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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
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
import fr.aiidor.game.UHCState;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;
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

					String village = "Le village";

					if (main.PlayerHasVillage(player.getUniqueId())) {
						village = main.getVillage(player.getUniqueId()).getName();
					}

					Bukkit.broadcastMessage("§c§l==========§4§k0§c§l==========");
					Bukkit.broadcastMessage("§2" + village + " à perdu un de ses membres : §l" + player.getName());
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

		if (main.isState(UHCState.FINISH)) {
			new LGDeath(main, player.getUniqueId(), null, loc, inv(player)).death();
			return;
		}

		if (!main.isInGame(player.getUniqueId())) return;
		main.getPlayer(player.getUniqueId()).setNoFall(true);

		if (e.getEntity().getKiller() != null) {

			Joueur dead = main.getPlayer(player.getUniqueId());

			Player killer = e.getEntity().getKiller();

			if (dead.getRole() == LGRoles.Ancien && dead.getPower() == 1) {
				if (main.isInGame(killer.getUniqueId())) {
					Joueur tueur = main.getPlayer(killer.getUniqueId());

					if (tueur.isLg()) {

						dead.setPower(0);
						e.setDroppedExp(0);

						player.sendMessage(main.gameTag + "§bVotre pouvoir vous a sauvé !");
						main.respawn(player);

						return;
					}
				}
			}



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

		if (main.NightMare) {
			if (e.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow) e.getDamager();

				if (arrow.getShooter() instanceof Skeleton && e.getEntity().getType().isAlive()) {
					LivingEntity damaged = (LivingEntity) e.getEntity();

					damaged.removePotionEffect(PotionEffectType.POISON);
					damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
				}
			}
		}

		if (main.Cupid) {
			if (e.getDamager() instanceof Arrow && e.getEntity() instanceof Player) {
				Arrow arrow = (Arrow) e.getDamager();

				if (arrow.getShooter() instanceof Player) {
					Player player = (Player) arrow.getShooter();

					if (player.getHealth() + 0.2 < 20) player.setHealth(player.getHealth() + 0.2);
					else player.setHealth(20);

				}
			}
		}

		if (e.getEntity() instanceof Player) {

			Player player = (Player) e.getEntity();
			if (!main.isInGame(player.getUniqueId())) return;

			Joueur j = main.getPlayer(player.getUniqueId());


			//CUPIDON
			if (e.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow) e.getDamager();
				if (arrow.getShooter() instanceof Player) {

					Player damager = (Player) arrow.getShooter();

					if (main.isInGame(damager.getUniqueId())) {
						if (main.getPlayer(damager.getUniqueId()).getRole() == LGRoles.Cupidon) {
							if (new Random().nextInt(3) == 0) {
								damager.sendMessage(main.gameTag + "§aGrâce a votre pouvoir, les dégats de cette flèche seront augmenté.");

								player.damage(e.getFinalDamage() * 0.25);
							}
						}
					}
				}

			}

			//LGA
			if (j.getRole() == LGRoles.LGA && j.getPower() == 0) {
				if (e.getDamager() instanceof Player) {
					Player damager = (Player) e.getDamager();

					if (!main.isInGame(damager.getUniqueId())) return;
					Joueur k = main.getPlayer(damager.getUniqueId());
					if (k.isLg()) {

						//LGA EFFECT
						j.getPlayer().sendMessage(main.gameTag + "§cAu contact de votre agresseur, vous êtes frappé d'une révélation ! Soudainement la mémoire vous revient :"
								+ " vous êtes Loup-Garou vous aussi !");

						j.setPower(1);

						new LGLga(main, j).runTaskTimer(main, 0, 20);
					}

				}

				return;
			}


		}
	}


	//ANCIEN
	@EventHandler
	public void onDamage(EntityDamageEvent e) {

		if (!(e.getEntity() instanceof Player)) return;

		if (main.isState(UHCState.WAITING) || main.isState(UHCState.STARTING) || main.isState(UHCState.PREGAME)) {
			e.setCancelled(true);
			return;
		}

		//NO FALL
		Player pl = (Player) e.getEntity();
		if (main.isInGame(pl.getUniqueId()) && e.getCause() == DamageCause.FALL) {
			if (main.getPlayer(pl.getUniqueId()).hasNoFall()) {
				e.setCancelled(true);
				return;
			}
		}
	}


	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(main.Spawn);
	}
}
