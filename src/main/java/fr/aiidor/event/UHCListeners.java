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
import org.bukkit.Sound;
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
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.PortalCreateEvent.CreateReason;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.ConfigManager;
import fr.aiidor.LGUHC;
import fr.aiidor.effect.WorldSound;
import fr.aiidor.game.Joueur;
import fr.aiidor.game.UHCState;
import fr.aiidor.role.LGRoles;
import fr.aiidor.utils.LGChat;
import fr.aiidor.utils.LGOption;
import fr.aiidor.utils.LGWaiting_Game;
import fr.aiidor.utils.UHCLootCrate;

public class UHCListeners implements Listener{
	
	private LGUHC main;
	private HashMap<UUID, Integer> DiamondPl = new HashMap<>();
	
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
							e.setCancelled(true);
							e.getPlayer().updateInventory();
							main.configInvBuilder(e.getPlayer());
							return;
						}
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
			if (main.FireEnchantLess) {
				if (clicked.getEnchantments().containsKey(Enchantment.FIRE_ASPECT)) {
					clicked.removeEnchantment(Enchantment.FIRE_ASPECT);
				}
				
				if (clicked.getEnchantments().containsKey(Enchantment.ARROW_FIRE)) {
					clicked.removeEnchantment(Enchantment.ARROW_FIRE);
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
			
			if (e.getBlock().getType() == Material.REDSTONE_ORE && main.redstoneLess) {
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
		
		//ORELESS -----------------------------------------------
		
		if (e.getBlock().getType() == Material.DIAMOND_ORE) {
			
			if (main.canJoin()) return;
			
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
				
				if (Choose <= main.apples - 1) {
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
				
				
				if (Choose <= main.flint - 1) {
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
	
	public void xpDrop(PlayerExpChangeEvent e) {
		if (main.vanillaN) {
			if (main.xpNerf) {
				e.setAmount(e.getAmount() / 2);
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
	  public void onPreprocess(PlayerCommandPreprocessEvent e) {
		  
		    Player player = e.getPlayer();
		    
		    String[] args = e.getMessage().split(" ");
		    
		    if (args.length <= 1) {
		    	return;
		    }
		    
		    if (!main.msg) {
		    	if (args[0].equalsIgnoreCase("/tell") || args[0].equalsIgnoreCase("/me")) {
		    		e.setCancelled(true);
		    		player.sendMessage(main.gameTag + "§cMessages Désactivé !");
		    		return;
		    	}
		    }
		    
		    if (args[0].equalsIgnoreCase("/gamerule")) {
		    	if (args[1].equalsIgnoreCase("doDaylightCycle") || args[1].equalsIgnoreCase("showDeathMessages") || args[1].equalsIgnoreCase("keepInventory")) {
		    		e.setCancelled(true);
		    		player.sendMessage(main.gameTag + "§cCette Gamerule ne peut pas être modfié !");
		    		return;
		    	}
		    }
	  }
    
	//CHAT ------------------------------------------------------------------------
    
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		
		String msg = e.getMessage();
		Player player = e.getPlayer();
		
		//OPTIONS CHAT ------------------------------------------------------------------------
		if (main.hasOptionChat(player.getUniqueId())) {
			e.setCancelled(true);
			player.sendMessage("§f  > " + e.getMessage());
			
			LGOption op = main.getOptionChat(player.getUniqueId());
			
			if (op == LGOption.specAdd || op == LGOption.specRemove) {
				
				if (e.getMessage().equalsIgnoreCase("Annuler")) {
					main.removeOptionChat(player.getUniqueId());
					return;
				}
				
				String targetname = e.getMessage();
				if (Bukkit.getPlayer(targetname) == null) {
					player.sendMessage(main.gameTag + "§cErreur, le joueur "+ targetname + " n'est pas connecté ou n'existe pas !");
					return;
				}
				
				Player Target = Bukkit.getPlayer(targetname);
				
				if (op == LGOption.specAdd) {
					
					if (main.Spectator.contains(Target.getUniqueId())) {
						player.sendMessage(main.gameTag + "§cErreur, le joueur "+ targetname + " est déjà spectateur !");
					} else {
						main.Spectator.add(Target.getUniqueId());
						player.sendMessage(main.gameTag + "§aLe joueur "+ targetname + " est désormais spectateur !");
					}
					main.removeOptionChat(player.getUniqueId(), false);
					main.getSpectatorInv(player);
					return;
				}
				
				if (op == LGOption.specRemove) {
					
					if (!main.Spectator.contains(Target.getUniqueId())) {
						player.sendMessage(main.gameTag + "§cErreur, le joueur "+ targetname + " n'est pas spectateur !");
					} else {
						main.Spectator.remove(Target.getUniqueId());
						player.sendMessage(main.gameTag + "§aLe joueur "+ targetname + " n'est désormais plus spectateur !");
					}
					main.removeOptionChat(player.getUniqueId(), false);
					main.getSpectatorInv(player);
					return;
				}
				
				return;
			}
			
			if (op == LGOption.addConfig) {
				
				if (e.getMessage().equalsIgnoreCase("Annuler")) {
					main.removeOptionChat(player.getUniqueId());
					main.InvConfigSaveBuilder(player);
					return;
				}
				
				String Name = msg;
				
				new ConfigManager(main).addConfig(Name);
				player.sendMessage(main.gameTag + "§aLe fichier de configuration a été sauvegardé !");
				
				main.InvConfigSaveBuilder(player);
				return;
			}
			
			
			if (op == LGOption.addOrga || op == LGOption.removeOrga || op == LGOption.setHost) {
				if (e.getMessage().equalsIgnoreCase("Annuler")) {
					main.removeOptionChat(player.getUniqueId());
					main.InvStaffBuilder(player);
					return;
				}
				
				String targetname = e.getMessage();
				if (Bukkit.getPlayer(targetname) == null) {
					player.sendMessage(main.gameTag + "§cErreur, le joueur "+ targetname + " n'est pas connecté ou n'existe pas !");
					return;
				}
				
				Player Target = Bukkit.getPlayer(targetname);
				
				if (Target.equals(player)) {
					player.sendMessage(main.gameTag + "§cVous êtes déjà le HOST");
					
					main.removeOptionChat(player.getUniqueId());
					main.InvStaffBuilder(player);
					return;
				}
				
				if (op == LGOption.addOrga) {
					
					if (main.orgas.contains(Target.getUniqueId())) {
						player.sendMessage(main.gameTag + "§cErreur, le joueur "+ targetname + " est déjà organisateur !");
						
						main.removeOptionChat(player.getUniqueId());
						main.InvStaffBuilder(player);
					} else {
						main.orgas.add(Target.getUniqueId());
						player.sendMessage(main.gameTag + "§aLe joueur "+ targetname + " est désormais organisateur !");
						Target.setPlayerListName("§7[§9Orga§7] §f" + Target.getName());
						
						main.removeOptionChat(player.getUniqueId());
						main.InvStaffBuilder(player);
					}
					main.removeOptionChat(player.getUniqueId(), false);
					main.InvStaffBuilder(player);
					return;
				}
				
				if (op == LGOption.removeOrga) {
					
					if (!main.orgas.contains(Target.getUniqueId())) {
						player.sendMessage(main.gameTag + "§cErreur, le joueur "+ targetname + " n'est pas organisateur !");
						
						main.removeOptionChat(player.getUniqueId());
						main.InvStaffBuilder(player);
					} else {
						main.orgas.remove(Target.getUniqueId());
						player.sendMessage(main.gameTag + "§aLe joueur "+ targetname + " n'est désormais plus organisateur !");
						Target.setPlayerListName(Target.getName());
						
						main.removeOptionChat(player.getUniqueId());
						main.InvStaffBuilder(player);
					}
					main.removeOptionChat(player.getUniqueId(), false);
					main.InvStaffBuilder(player);
					return;
				}
				
				if (op == LGOption.setHost) {
					
					if (player.getUniqueId() != main.host) {
						player.sendMessage(main.gameTag +"§cErreur, vous n'êtes pas le host !");
						main.removeOptionChat(player.getUniqueId());
						main.InvStaffBuilder(player);
						return;
					}
					
					main.host = Target.getUniqueId();
					Target.setPlayerListName("§7[§cHOST§7] §f" + Target.getName());
					player.setPlayerListName(player.getName());
					
					player.sendMessage(main.gameTag + "§aLe joueur " + Target.getName() + " est désormais le HOST.");
					main.removeOptionChat(player.getUniqueId());
					return;
				}
				
			}
			
			if (op == LGOption.setWbmaxSize || op == LGOption.setWbSize) {
				
				if (e.getMessage().equalsIgnoreCase("Annuler")) {
					main.removeOptionChat(player.getUniqueId());
					return;
				}
				
				String Message = e.getMessage();
				
				if (Message.split(" ").length != 1) {
					player.sendMessage(main.gameTag + "§cErreur, vous devez rentrer une seule valeur !");
					return;
				}
				
				int num = 0;
				
				try {
					  num = Integer.valueOf(Message);
					  
				} catch (NumberFormatException ex){
					 player.sendMessage(main.gameTag + "§cErreur, vous devez entrer un chiffre !");
					return;
				}
				
				if (num > 10000) {
					num = 10000;
				}
				
				if (op == LGOption.setWbSize) {
					if (num < 50) {
						num = 50;
					}
					
					main.Map = num;
				}
				
				if (op == LGOption.setWbmaxSize) {
					if (num < 0) {
						num = 0;
					}
					
					main.wbMax = num;
				}
				
				main.removeOptionChat(player.getUniqueId(), false);
				player.sendMessage(main.gameTag + "§aLes paramètres on été enregistrés !");
				main.configInvBuilder(player);
				
				return;
			}
			
			
			
			
			
			
			return;
		}
		
		
		//CHAT ------------------------------------------------------------------------
		
		if (e.getMessage().length() >= 2) {
			String Label = msg.substring(0, 2);
			
			if (Label.equalsIgnoreCase("$$")) {
				e.setCancelled(true);
				
				
				
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
						j.getPlayer().sendMessage("§8[MORT] §oanonyme §8>> §7" + e.getMessage());
					}
					
					for (Player p : main.getSpectator()) {
						if (!main.isInGame(p.getUniqueId())) {
							p.sendMessage("§8[MORT] §o"+ e.getPlayer().getName() +" §8>> §7" + e.getMessage());
						}
					}
					
					return;
				}
			}
		}
		
		if (e.getPlayer().getGameMode() == GameMode.SPECTATOR && main.isState(UHCState.GAME)) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(main.gameTag + "§cLes spectateurs ne parlent pas !");
			return;
		}
		
		if (main.chat == LGChat.Off) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(main.gameTag + "§cLe chat est désactivé !");
			return;
		}
		
		if (main.chat == LGChat.Off_IG && main.isState(UHCState.GAME)) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(main.gameTag + "§cLe chat est désactivé !");
			return;
		}
		
		if (main.chat == LGChat.Region) {
			e.setCancelled(true);
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (player.getLocation().distance(p.getLocation()) <= main.chatRegion) {
					p.sendMessage("<" + player.getName() + "> " + e.getMessage());
				}
			}
			return;
		}
	}
	
}
