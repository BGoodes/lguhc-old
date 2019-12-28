package fr.aiidor.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.PortalCreateEvent.CreateReason;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import fr.aiidor.LGUHC;
import fr.aiidor.effect.WorldSound;
import fr.aiidor.game.Joueur;
import fr.aiidor.game.LGLimitEnchant;
import fr.aiidor.game.UHCState;
import fr.aiidor.utils.LGWaiting_Game;
import fr.aiidor.utils.UHCLootCrate;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class UHCListeners implements Listener{
	
	private LGUHC main;
	
	public UHCListeners(LGUHC main) {
		this.main = main;
	}
	
	@EventHandler
	public void onLevelFoodChanged(FoodLevelChangeEvent e) {
		if (main.canJoin()) {
			e.setFoodLevel(20);
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		
		ItemStack clicked = e.getItemDrop().getItemStack();
		
		if (main.isState(UHCState.WAITING_GAME)) {
			e.setCancelled(true);
		}
		
		if (clicked.getType() == Material.ENDER_CHEST) {
			if (clicked.hasItemMeta()) {
				if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase("§d§lConfiguration")) {
					e.setCancelled(true);
					
					Bukkit.getScheduler().runTaskLater(main, new Runnable() {	
						@Override
						public void run() {
							if (e.getPlayer().getItemInHand().equals(clicked)) {
								e.getPlayer().setItemInHand(null);
							}
						}
					}, 1);
					
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK ) {
			if (main.enchantLess) {
				if (e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE || e.getClickedBlock().getType() == Material.ANVIL) {
					e.setCancelled(true);
					e.getPlayer().sendMessage(main.gameTag + "§cLe scénario enchantLess est activé ! Vous ne pouvez pas vous enchantez !");
					return;
				}
			}
		}
		
		if (e.getItem() != null) {
			ItemStack clicked = e.getItem();
			Player player = e.getPlayer();
			
				if (clicked.getType() == Material.ENDER_CHEST) {
					if (clicked.hasItemMeta()) {
						if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase("§d§lConfiguration")) {
							
							if (!player.getUniqueId().equals(main.host) && !main.orgas.contains(player.getUniqueId())) {
								e.setCancelled(true);
								player.sendMessage(main.gameTag + "§cVous ne pouvez pas utiliser ce coffre !");
								return;
							}

							e.setCancelled(true);
							e.getPlayer().updateInventory();
							main.configInvBuilder(e.getPlayer());
							return;
						}
					}
				}
				
				if (main.isState(UHCState.GAME)) {
					if (player.getGameMode() == GameMode.SURVIVAL) {
						new LGLimitEnchant(main, clicked).limit(player);
					}
		 		}
				
				if (clicked.getType() == Material.SUGAR) {
					if (clicked.hasItemMeta()) {
						if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase("§dSucre Pétillant")) {
							e.setCancelled(true);
							player.setItemInHand(null);
							
							new WorldSound(player.getLocation()).PlaySound(Sound.CAT_HISS);
							player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
							
							if (main.isState(UHCState.WAITING_GAME)) {
								Bukkit.getScheduler().runTaskLater(main, new Runnable() {
									@Override
									public void run() {
										if (main.isState(UHCState.WAITING_GAME)) {
											player.getInventory().setItem(1, new LGWaiting_Game(main).Sugar());
											new WorldSound(player.getLocation()).PlaySound(Sound.ITEM_PICKUP);
										}
									}
								}, 300);
							}
							return;
						}
					}
				}
			
				if (clicked.getType() == Material.CHEST) {
					if (clicked.hasItemMeta()) {
						if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase("§aLootcrate")) {
							e.setCancelled(true);
							
							ItemStack hand = player.getItemInHand();
							int amount = hand.getAmount();

							
							if (amount > 1) {
							    hand.setAmount(amount - 1);
							    player.setItemInHand(hand);
							} else {
							    player.setItemInHand(new ItemStack(Material.AIR));
							}
							
							new UHCLootCrate(main).openLootCrate(player);
							return;
						}
					}
			}
			
			//PROTECTIONS
			if (main.LimiteDePiece != 4) {
				if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					
					Material mat = clicked.getType();
					if (mat == Material.DIAMOND_HELMET || mat == Material.DIAMOND_CHESTPLATE ||
							mat == Material.DIAMOND_LEGGINGS || mat == Material.DIAMOND_BOOTS) {
						
						
						if (!canEquipArmor(e.getPlayer())) {
							clicked.setType(Material.AIR);
							player.sendMessage(" ");
							if (main.LimiteDePiece != 0) player.sendMessage(main.gameTag + "§cVous ne pouvez pas vous équiper avec plus de pièce en diamant !");
							else player.sendMessage(main.gameTag + "§cArmure en diamant désactivé !");
							e.setCancelled(true);
							
							Bukkit.getScheduler().runTaskLater(main, new Runnable() {
								
								@Override
								public void run() {		
									if (mat == Material.DIAMOND_HELMET) {
										player.getInventory().setHelmet(new ItemStack(Material.AIR));
									}
									if (mat == Material.DIAMOND_CHESTPLATE) {
										player.getInventory().setChestplate(new ItemStack(Material.AIR));
									}
									if (mat == Material.DIAMOND_LEGGINGS) {
										player.getInventory().setLeggings(new ItemStack(Material.AIR));
									}
									if (mat == Material.DIAMOND_BOOTS) {
										player.getInventory().setBoots(new ItemStack(Material.AIR));
									}
									
									player.getInventory().setItemInHand(new ItemStack(mat));
								}
							}, 1);
							return;
						}
					}
				}
			}
		}
	}
	
	private Boolean canEquipArmor(Player player){
		
		if (main.LimiteDePiece == 0) return false;
		
		int Piece = 0;
		for (ItemStack it : player.getInventory().getArmorContents()) {
			if (it.getType() == Material.DIAMOND_HELMET || it.getType() == Material.DIAMOND_CHESTPLATE ||
				it.getType() == Material.DIAMOND_LEGGINGS || it.getType() == Material.DIAMOND_BOOTS) {
				Piece ++;
				
				if (Piece >= main.LimiteDePiece) {
					return false;
				}
			}
		}
		return true;
	}
	
	//DIAMOND LIMIT
	
	@EventHandler
	public void entityExplode(EntityExplodeEvent e) {
		
		if (main.oreLess) {
			List<Block> blocks = e.blockList();
			Iterator<Block> b = blocks.iterator();
			
			while(b.hasNext()) {
				Block bs = b.next();
				
				if (bs.getType() == Material.DIAMOND_ORE && main.diamondLess) {
					bs.setType(Material.AIR);
				}
					
				if (bs.getType() == Material.COAL_ORE && main.coalLess) {
					bs.setType(Material.AIR);
				}
					
				if (bs.getType() == Material.IRON_ORE && main.ironLess) {
					bs.setType(Material.AIR);
				}
					
				if (bs.getType() == Material.LAPIS_ORE && main.lapisLess) {
					bs.setType(Material.AIR);
				}
				
				if (bs.getType() == Material.GOLD_ORE && main.goldLess) {
					bs.setType(Material.AIR);
				}
					
				if (bs.getType() == Material.REDSTONE_ORE && main.redstoneLess) {
					bs.setType(Material.AIR);
				}
				
				if (bs.getType() == Material.QUARTZ_ORE && main.quartzLess) {
					bs.setType(Material.AIR);
				}
			}
		}

	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public void BlockBreak(BlockBreakEvent e) {
		
		if (e.getBlock().getType() == Material.BARRIER) {
			if (e.getPlayer().getItemInHand().getType() != Material.STICK) {
				e.setCancelled(true);
				return;
			}
		}
		
		
		//ORELESS -----------------------------------------------
		if (main.oreLess) {
			if (e.getBlock().getType() == Material.DIAMOND_ORE && main.diamondLess) {
				if (main.canJoin()) return;
				e.setCancelled(true);
				e.getBlock().setType(Material.STONE);
				
				e.getPlayer().sendMessage(main.gameTag + "§cHo un mirage ! C'est vrai le diamant est désactivé !");
				return;
			}
			
			if (e.getBlock().getType() == Material.COAL_ORE && main.coalLess) {
				if (main.canJoin()) return;
				e.setCancelled(true);
				e.getBlock().setType(Material.STONE);
				
				e.getPlayer().sendMessage(main.gameTag + "§cHo un mirage ! C'est vrai le charbon est désactivé !");
				return;
			}
			
			if (e.getBlock().getType() == Material.IRON_ORE && main.ironLess) {
				if (main.canJoin()) return;
				e.setCancelled(true);
				e.getBlock().setType(Material.STONE);
				
				e.getPlayer().sendMessage(main.gameTag + "§cHo un mirage ! C'est vrai le fer est désactivé !");
				return;
			}
			
			if (e.getBlock().getType() == Material.LAPIS_ORE && main.lapisLess) {
				if (main.canJoin()) return;
				e.setCancelled(true);
				e.getBlock().setType(Material.STONE);
				
				e.getPlayer().sendMessage(main.gameTag + "§cHo un mirage ! C'est vrai le lapis est désactivé !");
				return;
			}
			
			if (e.getBlock().getType() == Material.GOLD_ORE && main.goldLess) {
				if (main.canJoin()) return;
				e.setCancelled(true);
				e.getBlock().setType(Material.STONE);
				
				e.getPlayer().sendMessage(main.gameTag + "§cHo un mirage ! C'est vrai l'or est désactivé !");
				return;
			}
			
			if (e.getBlock().getType() == Material.REDSTONE_ORE || e.getBlock().getType() == Material.GLOWING_REDSTONE_ORE && main.redstoneLess) {
				if (main.canJoin()) return;
				e.setCancelled(true);
				e.getBlock().setType(Material.STONE);
				
				e.getPlayer().sendMessage(main.gameTag + "§cHo un mirage ! C'est vrai la redstone est désactivé !");
				return;
			}
			
			if (e.getBlock().getType() == Material.QUARTZ_ORE && main.quartzLess) {
				if (main.canJoin()) return;
				e.setCancelled(true);
				e.getBlock().setType(Material.STONE);
				
				e.getPlayer().sendMessage(main.gameTag + "§cHo un mirage ! C'est vrai le quartz est désactivé !");
				return;
			}
		}
		
		if (e.getBlock().getType() == Material.DIAMOND_ORE) {
			
			if (main.canJoin()) return;
			
			if (main.BloodDiamond) {
				e.getPlayer().damage(1);
			}
			
			if (main.diamondlimit > 0) {
				if (main.DiamondPl.containsKey(e.getPlayer().getUniqueId())) {
					
					int diamond = main.DiamondPl.get(e.getPlayer().getUniqueId());
					main.DiamondPl.remove(e.getPlayer().getUniqueId());
					
					main.DiamondPl.put(e.getPlayer().getUniqueId(), diamond + 1);
				}
				else {
					main.DiamondPl.put(e.getPlayer().getUniqueId(), 1);
				}
				
				if (main.DiamondPl.get(e.getPlayer().getUniqueId()) > main.diamondlimit) {
					
					e.setCancelled(true);
					e.getBlock().setType(Material.AIR);
					
					e.getPlayer().sendMessage(main.gameTag + "§cVous avez dépassé votre limite de Diamant !");
					return;
				}
			}
		}
		
		if (e.getBlock().getType() == Material.LEAVES) {
			if (main.vanilla == false) return;
			if (main.apples > 0) {
				
				Player pl = e.getPlayer();
				if (pl.getItemInHand().getType() == Material.SHEARS) return;
				
				Random ran = new Random();
				int Choose = ran.nextInt(100);
				
				if (Choose <= main.apples - 1) {
					e.setCancelled(true);
					e.getBlock().setType(Material.AIR);
					
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation().add(0.5,0,0.5), new ItemStack(Material.APPLE));
				}
			}
			
		}
		
		if (e.getBlock().getType() == Material.GRAVEL) {
			if (main.vanilla == false) return;
			if (main.flint > 0) {
				
				Random ran = new Random();
				int Choose = ran.nextInt(100);
				
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
				
				if (Choose <= main.flint - 1) {
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation().add(0.5,0,0.5), new ItemStack(Material.FLINT));
				} else {
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation().add(0.5,0,0.5), new ItemStack(Material.GRAVEL));
				}
			}
			
		}
		
		
	}
	
	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		
		if (main.PlayerHasRole && main.hasRole(p)) {
			
			Joueur j = main.getPlayer(p.getUniqueId());
			
			if (!j.isDying()) return;
			
			e.setCancelled(true);
			
			particle(e.getBlock().getLocation().add(0, 0.9, 0), p);
			e.getBlock().getLocation().getWorld().playEffect(e.getBlock().getLocation().add(0.5,0,0.5), Effect.STEP_SOUND, e.getBlock().getType());
		}

	}
	
	private void particle(Location loc, Player p) {
		loc.add(new Vector(0.5, 0, 0.5));
		
		for (int i = 0; i != 15; i ++) {
			loc.add(new Vector(0, 0.05, 0));
			PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.SMOKE_NORMAL, true,(float) loc.getX(),(float) loc.getY(), (float) loc.getZ(), 0, 0, 0, 0 , 0, 0);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(particles);
		}
	}
	
	@EventHandler
	public void LeaveDecay(LeavesDecayEvent e) {
			if (main.vanilla == false) return;
			if (main.apples > 0) {
				
				Random ran = new Random();
				int Choose = ran.nextInt(100);
				
				if (Choose <= main.apples - 1) {
					e.setCancelled(true);
					e.getBlock().setType(Material.AIR);
					
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.APPLE));
				}
			}
	}
	
	
	@EventHandler
	public void ChangeCraft(PrepareItemCraftEvent e) {
		
		//Craft se passe dans une zone de craft
		if (e.getInventory() instanceof CraftingInventory) {
			
			CraftingInventory inv = e.getInventory();
			
			if (inv.getResult() == null) return;
			
			if (main.GoldenHead == false) {
			
				if (inv.getResult().hasItemMeta()) {
					if (inv.getResult().getItemMeta().getDisplayName().equalsIgnoreCase("§6Golden Head")) {
						inv.setResult(new ItemStack(Material.AIR));
						return;
					}
				}
			}
			
			if (main.Notch == false) {
				
				if (inv.getResult().getType() == Material.GOLDEN_APPLE) {
					if (inv.getResult().getDurability() == (byte) 1) {
						inv.setResult(new ItemStack(Material.AIR));
						return;
					}
				}
			}
		}
	}
	
	//Portal
	@EventHandler
	public void noPortal(PortalCreateEvent e) {
		if (main.vanillaN) {
			if (main.noNether) {
				if (e.getReason() == CreateReason.FIRE) {
					e.setCancelled(true);
				}
			}
			
			if (main.noEnd) {
				if (e.getReason() == CreateReason.OBC_DESTINATION) {
					e.setCancelled(true);
				}
			}
		}
		
	}
	
	@EventHandler
	public void xpDrop(PlayerExpChangeEvent e) {
		
		if (main.vanillaN) {
			if (main.xpNerf) {
				
				int value = (int) main.xpNerfVar;
				int choose = (int) ((main.xpNerfVar%1) * 100);
				
				if (choose == 0) {
					e.setAmount(e.getAmount() / (value));
					return;
				}
				
				if (new Random().nextInt(100) < choose) {
					e.setAmount(e.getAmount() / value + 1);
					
				} else {
					e.setAmount(e.getAmount() / (value));
				}
			}
		}
		
		return;
	}
	  
	  
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity().getType() == EntityType.EGG) {
        	if (!main.eggs || !main.fun) return;
            Egg egg = (Egg) e.getEntity();
            
            List<EntityType> ents = new ArrayList<>();
            for (EntityType ent : EntityType.values()) {
            	if (ent.isSpawnable() && ent.isAlive()) {
            		ents.add(ent);
            	}
            }
            
            ents.add(EntityType.ARROW);
            ents.add(EntityType.FIREWORK);
            ents.add(EntityType.FIREBALL);
            ents.add(EntityType.SMALL_FIREBALL);
            ents.add(EntityType.THROWN_EXP_BOTTLE);
            ents.add(EntityType.SPLASH_POTION);
            ents.add(EntityType.ARMOR_STAND);
            ents.add(EntityType.ENDER_CRYSTAL);
            ents.add(EntityType.LIGHTNING);
            ents.add(EntityType.PRIMED_TNT);
            ents.add(EntityType.MINECART_MOB_SPAWNER);
            ents.add(EntityType.MINECART);
            ents.add(EntityType.MINECART_TNT);
            ents.add(EntityType.EXPERIENCE_ORB);
            
            EntityType choose = ents.get(new Random().nextInt(ents.size()));
            
            if (choose == null) return;
            
            Entity spawn = egg.getWorld().spawnEntity(egg.getLocation(), choose);
            
            if (choose == EntityType.SKELETON) {
            	Skeleton skel = (Skeleton) spawn;
            	if (new Random().nextBoolean()) {
            		skel.setSkeletonType(SkeletonType.WITHER);
            	}
            }
        }
    }
    
    
    
	  @EventHandler
	  public void onAchievement(PlayerAchievementAwardedEvent e) {
		  if (!main.announceAdv) {
			  e.setCancelled(true);
		  } 
	  }
	  
	  @EventHandler
	  public void onMobDeath(EntityDeathEvent e) {
		  
		  if (!main.vanilla || !main.Utils) return;
		  
		  if (e.getEntity().getType() == EntityType.HORSE) {
			  
			  Horse horse = (Horse) e.getEntity();
			  
			  if (horse.isAdult()) {
				  int choose = new Random().nextInt(3);
				  
				  if (choose > 0) {
					  if (!main.run) {
						  
						  e.getDrops().add(main.getItem(Material.RAW_BEEF, choose, "§fFindus"));
					  } else {
						  e.getDrops().add(main.getItem(Material.COOKED_BEEF, choose, "§fFindus"));
					  } 
				  }	
			  }
		  }
	  }
	  
	 
}
