package fr.aiidor.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.game.UHCState;
import fr.aiidor.role.LGRoles;

public class UHCListeners implements Listener{
	
	private LGUHC main;
	private HashMap<UUID, Integer> DiamondPl = new HashMap<>();
	
	public UHCListeners(LGUHC main) {
		this.main = main;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getItem() != null) {
			ItemStack clicked = e.getItem();
			Player player = e.getPlayer();

				if (clicked.getType() == Material.ENDER_CHEST) {
					if (clicked.hasItemMeta()) {
						if (clicked.getItemMeta().getDisplayName().startsWith("§d§lConfiguration")) {
							e.setCancelled(true);
							e.getPlayer().updateInventory();
							main.configInvBuilder(e.getPlayer());
							return;
						}
					}
			}

			
			
			
			//PROTECTIONS
			if (main.FireEnchantLess) {
				if (clicked.getEnchantments().containsKey(Enchantment.FIRE_ASPECT)) {
					clicked.removeEnchantment(Enchantment.FIRE_ASPECT);
				}
				
				if (clicked.getEnchantments().containsKey(Enchantment.ARROW_FIRE)) {
					clicked.removeEnchantment(Enchantment.ARROW_FIRE);
				}
			}
			
			//PROTECTIONS
			if (main.LimiteDePiece > 0) {
				if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					
					Material mat = clicked.getType();
					if (mat == Material.DIAMOND_HELMET || mat == Material.DIAMOND_CHESTPLATE ||
							mat == Material.DIAMOND_LEGGINGS || mat == Material.DIAMOND_BOOTS) {
						
						
						if (!canEquipArmor(e.getPlayer())) {
							clicked.setType(Material.AIR);
							player.sendMessage(" ");
							player.sendMessage(main.gameTag + "§cVous ne pouvez pas vous équiper avec plus de pièce en diamant !");
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
	
	@EventHandler (priority = EventPriority.HIGH)
	public void onDamage(EntityDamageEvent e) {
		
		if (!(e.getEntity() instanceof Player)) return;
		
		if (main.isState(UHCState.WAITING) || main.isState(UHCState.STARTING) || main.isState(UHCState.PREGAME)) {
			e.setCancelled(true);
		}
		
		//NO FALL
		if (e.getCause() != DamageCause.FALL) return;
		
		Player pl = (Player) e.getEntity();
		if (main.isInGame(pl.getUniqueId())) {
			if (main.getPlayer(pl.getUniqueId()).hasNoFall()) {
				e.setCancelled(true);
			}
		}
		

	}
	
	//DIAMOND LIMIT
	
	@EventHandler
	public void entityExplode(EntityExplodeEvent e) {
		
		if (main.diamondLess || main.oreLess) {
			List<Block> blocks = e.blockList();
			Iterator<Block> b = blocks.iterator();
			
			while(b.hasNext()) {
				Block bs = b.next();
				
				if (main.oreLess) {
					if (bs.getType() == Material.COAL_ORE || bs.getType() == Material.IRON_ORE || bs.getType() == Material.LAPIS_ORE ||
						bs.getType() == Material.REDSTONE_ORE || bs.getType() == Material.GOLD_ORE || bs.getType() == Material.DIAMOND_ORE) {
						bs.setType(Material.AIR);
					}
				}
				
				if (main.diamondLess) {
					if (bs.getType() == Material.DIAMOND_ORE) {
						bs.setType(Material.AIR);
					}
				}
			}
		}

	}
	
	@EventHandler
	public void BlockBreak(BlockBreakEvent e) {
		
		if (e.getBlock().getType() == Material.BARRIER) {
			if (e.getPlayer().getItemInHand().getType() != Material.STICK) {
				e.setCancelled(true);
				return;
			}
		}
		
		if (e.getBlock().getType() == Material.COAL_ORE || e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.LAPIS_ORE ||
			e.getBlock().getType() == Material.REDSTONE_ORE || e.getBlock().getType() == Material.GOLD_ORE || e.getBlock().getType() == Material.DIAMOND_ORE) {
			if (main.oreLess) {
				if (main.canJoin()) return;
				e.setCancelled(true);
				e.getBlock().setType(Material.STONE);
				
				e.getPlayer().sendMessage(main.gameTag + "§cHo un mirage ! C'est vrai les minerais sont désactivés !");
				return;
			}
		}
		
		if (e.getBlock().getType() == Material.DIAMOND_ORE) {
			
			if (main.canJoin()) return;
			
			if (main.diamondLess) {
				e.setCancelled(true);
				e.getBlock().setType(Material.STONE);
				
				e.getPlayer().sendMessage(main.gameTag + "§cHo un mirage ! C'est vrai les diamants sont désactivés !");
				return;
			}
			
			if (main.BloodDiamond) {
				e.getPlayer().damage(1);
			}
			
			if (main.diamondlimit > 0) {
				if (DiamondPl.containsKey(e.getPlayer().getUniqueId())) {
					
					int diamond = DiamondPl.get(e.getPlayer().getUniqueId());
					DiamondPl.remove(e.getPlayer().getUniqueId());
					
					DiamondPl.put(e.getPlayer().getUniqueId(), diamond + 1);
				}
				else {
					DiamondPl.put(e.getPlayer().getUniqueId(), 1);
					return;
				}
				
				if (DiamondPl.get(e.getPlayer().getUniqueId()) > main.diamondlimit) {
					
					e.setCancelled(true);
					e.getBlock().setType(Material.AIR);
					
					e.getPlayer().sendMessage(main.gameTag + "§cVous avez dépassé votre limite de Diamant !");
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
				
				if (Choose <= main.apples) {
					e.setCancelled(true);
					e.getBlock().setType(Material.AIR);
					
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.APPLE));
				}
			}
			
		}
		
		if (e.getBlock().getType() == Material.GRAVEL) {
			if (main.vanilla == false) return;
			if (main.flint > 0) {
				
				Random ran = new Random();
				int Choose = ran.nextInt(100);
				
				
				if (Choose <= main.flint) {
					e.setCancelled(true);
					e.getBlock().setType(Material.AIR);
					
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.FLINT));
				}
			}
			
		}
		
		
	}
	
	@EventHandler
	public void LeaveDecay(LeavesDecayEvent e) {
			if (main.vanilla == false) return;
			if (main.apples > 0) {
				
				Random ran = new Random();
				int Choose = ran.nextInt(100);
				
				if (Choose <= main.apples) {
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
	public void noNether(PortalCreateEvent e) {
		e.setCancelled(true);
	}
	
	public void xpDrop(PlayerExpChangeEvent e) {
		if (main.vanilla) {
			if (main.xpNerf) {
				e.setAmount(e.getAmount() / 2);
			}
		}
		
		return;
	}
	
	  @EventHandler
	  public void onPreprocess(PlayerCommandPreprocessEvent e) {
		  
		    Player player = e.getPlayer();
		    
		    String[] args = e.getMessage().split(" ");
		    
		    if (args.length <= 1) {
		    	return;
		    }
		    
		    
		  if (!main.chat) {
			    if (args[0].equalsIgnoreCase("/tell") || args[0].equalsIgnoreCase("/me")) {
			    	e.setCancelled(true);
			      player.sendMessage(main.gameTag + "§cMessages Désactivé !");
			    } 
			  }  
		  }
	  
	//CHAT
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		
		String msg = e.getMessage();
		
		if (e.getMessage().length() >= 2) {
			String Label = msg.substring(0, 2);
			
			if (Label.equalsIgnoreCase("$$")) {
				e.setCancelled(true);
				
				Player player = e.getPlayer();
				
				if (!player.getUniqueId().equals(main.host) && !main.orgas.contains(player.getUniqueId())) {
					player.sendMessage(main.gameTag + "§cSeul les organisateurs et le Host peuvent utiliser ce chat !");
					return;
				}
				
				msg = msg.substring(2);
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getUniqueId().equals(main.host) || main.orgas.contains(p.getUniqueId())) {
						p.sendMessage("§3(Staff : §d" + player.getName() + "§3) §f" + msg);
					}
				}
				
				return;
			}
		}
		
		if (main.deathChat) {
			if (main.isInGame(e.getPlayer().getUniqueId())) {
				Joueur d = main.getPlayer(e.getPlayer().getUniqueId());
				
				if (d.isDead()) {
					e.setCancelled(true);
					for (Joueur j : main.getPlayerRoles(LGRoles.Chaman)) {
						j.getPlayer().sendMessage(main.gameTag + "§8[MORT] anonyme >> §7" + e.getMessage());
					}
					
					for (Player p : main.getSpectator()) {
						p.sendMessage(main.gameTag + "§8[MORT] "+ e.getPlayer().getName() +" >> §7" + e.getMessage());
					}
					
					return;
				}
			}
		}
		
		if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(main.gameTag + "§cLes spectateurs ne parlent pas !");
			return;
		}
		
		if (!main.chat) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(main.gameTag + "§cLe chat est désactivé !");
			return;
		}
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
	
}
