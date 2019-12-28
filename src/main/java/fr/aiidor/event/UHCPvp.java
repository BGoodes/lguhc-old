package fr.aiidor.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Horse;
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
import org.spigotmc.event.entity.EntityMountEvent;

import fr.aiidor.LGUHC;
import fr.aiidor.effect.WorldSound;
import fr.aiidor.game.Joueur;
import fr.aiidor.game.UHCState;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;
import fr.aiidor.role.use.LGRole_IPL;
import fr.aiidor.task.LGDeath;
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
		
			if (main.Abso == 1 || main.Abso > 2) {
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
				cp.setAbsorptionHearts(main.Abso * 2);
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
			main.kill(player.getUniqueId(), player.getName(), player.getLocation(), inv(player));
			return;
		}
		
		//LES ROLES SONT DEJA DONNE
		if (main.isState(UHCState.FINISH)) {
			new LGDeath(main, player.getUniqueId(), null, loc, inv(player)).death();
			return;
		}
		
		if (!main.hasRole(player.getUniqueId())) return;	
				
		if (e.getEntity().getKiller() != null) {
			
			Joueur dead = main.getPlayer(player.getUniqueId());
			
			Player killer = e.getEntity().getKiller();
			
			if (dead.getRole() == LGRoles.Ancien && dead.getPower() == 1) {
				dead.setPower(0);
				if (main.hasRole(killer.getUniqueId())) {
					Joueur tueur = main.getPlayer(killer.getUniqueId());
					if (tueur.isLg()) {
						e.setDroppedExp(0);
						
						player.sendMessage(main.gameTag + "§aVous avez été tué par un Loup-Garou ! Par conséquent, vous êtes sauvé par votre aura. Mais attention, vous n'aurez pas deux fois cette chance !");
						main.respawn(player);
						
						return;
					}
				}
			}
			
			player.sendMessage(main.gameTag + "§bVous êtes mort mais vous avez peut être une chance d'être réssuscité ! Veuillez attendre quelques secondes.");
			player.sendMessage(" ");
			
			if (main.hasRole(killer)) {
				
				LGDeath task = new LGDeath(main, player.getUniqueId(), killer.getUniqueId(), loc, inv(player));
				task.runTaskTimer(main, 0, 20);
				
				if (main.getPlayer(killer.getUniqueId()).isLg()) {
					//INFECT
					main.getPlayer(player.getUniqueId()).setDyingState(LGCamps.LoupGarou);
					
					new LGRole_IPL(main).ReaMsg(player);
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
			if (!main.hasRole(player.getUniqueId())) return;
			
			//CUPIDON
			if (e.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow) e.getDamager();
				if (arrow.getShooter() instanceof Player) {
					
					Player damager = (Player) arrow.getShooter();
					
					if (main.hasRole(damager.getUniqueId())) {
						if (main.getPlayer(damager.getUniqueId()).getRole() == LGRoles.Cupidon) {
							if (new Random().nextInt(3) == 0) {
								damager.sendMessage(main.gameTag + "§aGrâce a votre pouvoir, les dégats de cette flèche seront augmenté.");
								
								player.damage(e.getFinalDamage() * 0.25);
							}
						}
					}
				}

			}
			
			//LGP ET PETITE FILLE
			if (e.getDamager() instanceof Player) {
				Player damager = (Player) e.getDamager();
				
				if (main.hasRole(damager.getUniqueId())) {
					Joueur damagerJ = main.getPlayer(damager.getUniqueId());
					
					if (damagerJ.getRole() == LGRoles.PetiteFille || damagerJ.getRole() == LGRoles.LGP) {
						if (damager.hasPotionEffect(PotionEffectType.INVISIBILITY) && damager.hasPotionEffect(PotionEffectType.WEAKNESS)) {
							
							damager.removePotionEffect(PotionEffectType.INVISIBILITY);
							damager.removePotionEffect(PotionEffectType.WEAKNESS);
							
							damager.sendMessage(main.gameTag + "§eVous êtes à nouveau visible !");
						}
					}
				}
			}
		}
	}
	
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		
		if (!(e.getEntity() instanceof Player)) return;
		
		Player pl = (Player) e.getEntity();

		if (main.isState(UHCState.WAITING_GAME)) {
			e.setDamage(0);
			return;
		}
		
		if (main.isState(UHCState.WAITING) || main.isState(UHCState.STARTING) || main.isState(UHCState.PREGAME)) {
			e.setCancelled(true);
			return;
		}
		
		//NO FALL
		if (e.getCause() == DamageCause.FALL) {
			
			if (main.noFall.contains(pl.getUniqueId())) {
				e.setCancelled(true);
				return;
			}
			
			if (main.hasRole(pl)) {
				if (main.getPlayer(pl.getUniqueId()).salvation) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerMount(EntityMountEvent e) {
		if (main.horseLess) {
			if (e.getEntity() instanceof Player) {
				if (e.getMount() instanceof Horse) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(main.Spawn);
	}	
}
