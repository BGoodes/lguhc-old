package fr.aiidor.event;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.LGUHC;
import fr.aiidor.Options.ChatOption;
import fr.aiidor.Options.LGEnchants;
import fr.aiidor.Options.LGOption;
import fr.aiidor.files.ConfigManager;
import fr.aiidor.game.LGLimitEnchant;
import fr.aiidor.game.UHCState;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;
import fr.aiidor.task.UHCStart;
import fr.aiidor.utils.LGWaiting_Game;

public class UHCInventory implements Listener{
	
	private LGUHC main;
	public UHCInventory(LGUHC main) {
		this.main = main;
	}
	
	@EventHandler
	public void ItemClick(InventoryClickEvent e) {
		
		if (!(e.getWhoClicked() instanceof Player)) return;
		if (e.getCurrentItem() == null) return;
		
		Inventory inv = e.getInventory();
		Player player = (Player) e.getWhoClicked();	
		ItemStack clicked = e.getCurrentItem();
		
		
		//INVENTAIRESs ----------------------------------------------------------------------------------------------------------
		
		if (inv.getName().equalsIgnoreCase("§dComposition :")) {
			e.setCancelled(true);
			return;
		}
		
		if (inv.getName().equalsIgnoreCase("§6Spectateurs :")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigPlBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.WOOL && clicked.getDurability() == 13) {
				
				player.closeInventory();
				
				if (!main.isState(UHCState.WAITING) && !main.isState(UHCState.WAITING_GAME)) {
					player.sendMessage(main.gameTag + "§cVous pouvez ajouter des spectateurs que quand la partie est en attente !");
					return;
				}
				
				main.setOptionChat(player.getUniqueId(), LGOption.specAdd);
				player.sendMessage(main.gameTag + "§2Ecrivez le nom du joueur que vous voulez ajouter à la liste des spectateurs (écrivez 'Annuler' pour quitter)");
				return;
			}
			
			if (clicked.getType() == Material.WOOL && clicked.getDurability() == 14) {
				
				player.closeInventory();
				
				if (!main.isState(UHCState.WAITING) && !main.isState(UHCState.WAITING_GAME)) {
					player.sendMessage(main.gameTag + "§cVous pouvez ajouter des spectateurs que quand la partie est en attente !");
					return;
				}
				
				main.setOptionChat(player.getUniqueId(), LGOption.specRemove);
				player.sendMessage(main.gameTag + "§2Ecrivez le nom du joueur que vous voulez retirer de la liste des spectateurs (écrivez 'Annuler' pour quitter)");
				return;
			}
			
			return;
		}
		
		if (inv.getName().equalsIgnoreCase("§6Staff :")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigPlBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.WOOL && clicked.getDurability() == 13) {
				
				player.closeInventory();
				
				main.setOptionChat(player.getUniqueId(), LGOption.addOrga);
				player.sendMessage(main.gameTag + "§2Ecrivez le nom du joueur que vous voulez ajouter à la liste des Organisateurs (écrivez 'Annuler' pour quitter)");
				return;
			}
			
			if (clicked.getType() == Material.WOOL && clicked.getDurability() == 14) {
				
				player.closeInventory();
				
				main.setOptionChat(player.getUniqueId(), LGOption.removeOrga);
				player.sendMessage(main.gameTag + "§2Ecrivez le nom du joueur que vous voulez retirer de la liste des Organisateurs (écrivez 'Annuler' pour quitter)");
				return;
			}
			
			if (clicked.getType() == Material.ENDER_PEARL) {
				
				player.closeInventory();
				
				main.setOptionChat(player.getUniqueId(), LGOption.setHost);
				player.sendMessage(main.gameTag + "§2Ecrivez le nom du joueur à qui vous voulez transmettre vos permissions [HOST] (écrivez 'Annuler' pour quitter)");
				return;
			}
			
			if (clicked.getType() == Material.ANVIL) {
				
				player.closeInventory();
				
				main.setOptionChat(player.getUniqueId(), LGOption.leaveHost);
				player.sendMessage(main.gameTag + "§6Etes vous sûr de ne plus vouloir être Host ? (écrivez 'OUI' ou 'NON')");
				return;
			}
		}
		
		if (inv.getName().equalsIgnoreCase("§5Menu: §dConfiguration")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				player.closeInventory();
				return;
			}
			
			if (clicked.getType() == Material.SLIME_BALL) {
				 if (main.isState(UHCState.WAITING) || main.isState(UHCState.WAITING_GAME)) {
					 if (main.getPlayers().size() >= main.PlayerMin) {
						 
						if (main.isState(UHCState.WAITING_GAME)) {
							new LGWaiting_Game(main).stopGame();
						}
							
						main.setState(UHCState.STARTING);
						
						UHCStart start = new UHCStart(main);
						start.runTaskTimer(main, 0, 20);
							
						player.sendMessage(main.gameTag + "§3Lancement de la partie !");
						player.closeInventory();
						return;
					 } else {
						 player.sendMessage(main.gameTag + "§cIl n'y a pas assez de joueurs pour lancer la partie !");
						 return;
					 }

				 } else {
					 player.sendMessage(main.gameTag + "§cLa partie est déjà lancé ou est en cours de lancement !");
				 }
				return;
			}
			
			if (clicked.getType() == Material.INK_SACK) {
				if (main.isState(UHCState.WAITING)) {
					
					main.setState(UHCState.WAITING_GAME);
					player.closeInventory();
					Bukkit.broadcastMessage(main.gameTag + "§9Le Mini jeu est lancé !");
					
					new LGWaiting_Game(main).startGame();
					return;
				}
				
				if (main.isState(UHCState.WAITING_GAME)) {
					
					main.setState(UHCState.WAITING);
					player.closeInventory();
					Bukkit.broadcastMessage(main.gameTag + "§cLe Mini jeu est terminé !");
					
					new LGWaiting_Game(main).stopGame();
					return;
				}
				
				player.sendMessage(main.gameTag + "§cLe mini jeu ne peut être lancé que durant le temps d'attente !");
				return;
			}
			
			if (clicked.getType() == Material.REDSTONE_BLOCK) {
				if (main.start != null) {
					main.start.stopStarting();
				}
				
				player.closeInventory();
				return;
			}
			
			
			if (clicked.getType() == Material.DIAMOND_SWORD) {
				InvConfigBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.BOOKSHELF) {
				invConfigRolesBuiler(player);
				return;
			}
			
			
			if (clicked.getType() == Material.CHEST) {
				InvConfigStuffBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.ANVIL) {
				InvConfigRulesBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.SKULL_ITEM) {
				InvConfigPlBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.BEACON) {
				InvConfigWbBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.WATCH) {
				InvConfigTimeBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.ENDER_PEARL) {
				main.InvConfigSaveBuilder(player);
				return;
			}
			
			
			return;
		}
		
		if (inv.getName().equalsIgnoreCase("§5Menu: §6Scénarios")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				main.configInvBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.DIAMOND_AXE) {
				invRunBuiler(player);
				return;
			}
			
			if (clicked.getType() == Material.DIAMOND_SWORD) {
				invLimitBuiler(player);
				return;
			}
			
			if (clicked.getType() == Material.GOLDEN_APPLE) {
				invHealingBuiler(player);
				return;
			}
			
			if (clicked.getType() == Material.LOG) {
				invVanillaBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.WOOD) {
				invVanillaNBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.TNT) {
				invFunBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.SKULL_ITEM) {
				invPveBuilder(player);
				return;
			}
			
			
			if (e.getSlot() == 18) {
				
				if (main.run) main.run = false;
				else main.run = true;
				InvConfigBuilder(player);
				return;
			}
			
			if (e.getSlot() == 21) {
				
				if (main.vanilla) main.vanilla = false;
				else main.vanilla = true;
				InvConfigBuilder(player);
				return;
			}
			
			if (e.getSlot() == 22) {
				
				if (main.vanillaN) main.vanillaN = false;
				else main.vanillaN = true;
				InvConfigBuilder(player);
				return;
			}
			
			if (e.getSlot() == 25) {
				
				if (main.fun) main.fun = false;
				else main.fun = true;
				InvConfigBuilder(player);
				return;
			}
			
			return;
		}
		
		
		if (inv.getName().equalsIgnoreCase("§6Scénarios : §2Scénarios Run")) { 
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigBuilder(player);
				return;
			}
			
			if (e.getSlot() == 18) {
				
				if (main.cutclean) main.cutclean = false;
				else main.cutclean = true;
				invRunBuiler(player);
				return;
			}
			
			
			if (e.getSlot() == 19) {
				
				if (main.fastSmelting) main.fastSmelting = false;
				else main.fastSmelting = true;
				invRunBuiler(player);
				return;
			}
			
			if (e.getSlot() == 20) {
				
				if (main.timber) main.timber = false;
				else main.timber = true;
				invRunBuiler(player);
				return;
			}
			
			if (e.getSlot() == 21) {
				
				if (main.veinMiner) main.veinMiner = false;
				else main.veinMiner = true;
				invRunBuiler(player);
				return;
			}
			
			if (e.getSlot() == 22) {
				
				if (main.HasteyBoys) main.HasteyBoys = false;
				else main.HasteyBoys = true;
				invRunBuiler(player);
				return;
			}
			
			if (e.getSlot() == 23) {
				
				if (main.bleedingSweet) main.bleedingSweet = false;
				else main.bleedingSweet = true;
				invRunBuiler(player);
				return;
			}
			
			if (e.getSlot() == 24) {
				
				if (e.getClick() == ClickType.LEFT) {
					if (main.SpeedyMiner < 5) {
						main.SpeedyMiner ++;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.SpeedyMiner > 0) {
						main.SpeedyMiner --;
					}
				}
				
				invRunBuiler(player);
				return;
			}
			
			if (e.getSlot() == 25) {
				
				if (e.getClick() == ClickType.LEFT) {
					
					if (main.noWodenTool == Material.WOOD) {
						main.noWodenTool = Material.COBBLESTONE;
						invRunBuiler(player);
						return;
					}
					
					if (main.noWodenTool == Material.COBBLESTONE) {
						main.noWodenTool = Material.IRON_INGOT;
						invRunBuiler(player);
						return;
					}
					
					if (main.noWodenTool == Material.IRON_INGOT) {
						main.noWodenTool = Material.DIAMOND;
						invRunBuiler(player);
						return;
					}
					
					if (main.noWodenTool == Material.DIAMOND) {
						main.noWodenTool = Material.WOOD;
						invRunBuiler(player);
						return;
					}
					
					invRunBuiler(player);
					return;
				}
				
				if (e.getClick() == ClickType.RIGHT) {
					
					if (main.noWodenTool == Material.WOOD) {
						main.noWodenTool = Material.DIAMOND;
						invRunBuiler(player);
						return;
					}
					
					if (main.noWodenTool == Material.DIAMOND) {
						main.noWodenTool = Material.IRON_INGOT;
						invRunBuiler(player);
						return;
					}
					
					if (main.noWodenTool == Material.IRON_INGOT) {
						main.noWodenTool = Material.COBBLESTONE;
						invRunBuiler(player);
						return;
					}
					
					if (main.noWodenTool == Material.COBBLESTONE) {
						main.noWodenTool = Material.WOOD;
						invRunBuiler(player);
						return;
					}
					
					invRunBuiler(player);
					return;
				}
			}
			
			return;
		}
		
		//LIMITE DE STUFF
		if (inv.getName().equalsIgnoreCase("§6Scénarios : §bLimite de Stuff")) { 
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.GOLD_ORE) {
				invOreLimitBuiler(player);
				return;
			}
			
			if (clicked.getType() == Material.ENCHANTED_BOOK) {
				invEnchantLimitBuilder(player);
				return;
			}
			
			
			
			if (e.getSlot() == 18) {
				
				if (main.LimiteEnchant) main.LimiteEnchant = false;
				else main.LimiteEnchant = true;
				invLimitBuiler(player);
				return;
			}
			
			if (e.getSlot() == 19) {
				
				if (e.getClick() == ClickType.LEFT) {
					if (main.LimiteDePiece < 4) {
						main.LimiteDePiece ++;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.LimiteDePiece > 0) {
						main.LimiteDePiece --;
					}
				}
				
				invLimitBuiler(player);
				return;
			}
			
			if (e.getSlot() == 20) {
				
				if (main.BloodDiamond) main.BloodDiamond = false;
				else main.BloodDiamond = true;
				invLimitBuiler(player);
				return;
			}
			
			if (e.getSlot() == 21) {
				
				if (e.getClick() == ClickType.LEFT) {
					if (main.diamondlimit < 50) {
						main.diamondlimit ++;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.diamondlimit > 0) {
						main.diamondlimit --;
					}
				}
				
				invLimitBuiler(player);
				return;
			}
			
			if (e.getSlot() == 22) {
				
				if (main.RodLess ) main.RodLess  = false;
				else main.RodLess  = true;
				invLimitBuiler(player);
				return;
			}
			
			
			if (e.getSlot() == 23) {
				
				if (main.oreLess) main.oreLess  = false;
				else main.oreLess  = true;
				invLimitBuiler(player);
				return;
			}
			
			if (e.getSlot() == 24) {
				
				if (main.enchantLess) main.enchantLess  = false;
				else main.enchantLess  = true;
				invLimitBuiler(player);
				return;
			}
		}
		
		if (inv.getName().equalsIgnoreCase("§bLimite de Stuff : §dEnchants")) { 
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				invLimitBuiler(player);
				return;
			}
			
			if (e.getInventory().equals(e.getClickedInventory())) {
				if (clicked.getType() == Material.ENCHANTED_BOOK || clicked.getType() == Material.WOOL) {
					
					LGEnchants enchant = main.getEnchant(clicked.getItemMeta().getDisplayName()
							.substring(4)
							.replace(" §f*", ""));
					
					if (e.getClick() == ClickType.LEFT) {
						if (enchant.getLimit() < enchant.getMaxLevel()) {
							enchant.addLimit();
						}
					}
					if (e.getClick() == ClickType.RIGHT) {
						if (enchant.getLimit() > 0) {
							enchant.RemoveLimit();
						}
					}
					
					invEnchantLimitBuilder(player);
				}
			}

			
		}
		
		if (inv.getName().equalsIgnoreCase("§bLimite de Stuff : §eOreLess")) { 
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				invLimitBuiler(player);
				return;
			}
			
			if (e.getSlot() == 18) {
				
				if (main.diamondLess) main.diamondLess = false;
				else main.diamondLess = true;
				invOreLimitBuiler(player);
				return;
			}
			
			if (e.getSlot() == 19) {
				
				if (main.goldLess) main.goldLess = false;
				else main.goldLess = true;
				invOreLimitBuiler(player);
				return;
			}
			
			if (e.getSlot() == 20) {
				
				if (main.ironLess) main.ironLess = false;
				else main.ironLess = true;
				invOreLimitBuiler(player);
				return;
			}
			
			if (e.getSlot() == 21) {
				
				if (main.lapisLess) main.lapisLess = false;
				else main.lapisLess = true;
				invOreLimitBuiler(player);
				return;
			}
			
			if (e.getSlot() == 22) {
				
				if (main.coalLess) main.coalLess = false;
				else main.coalLess = true;
				invOreLimitBuiler(player);
				return;
			}
			
			if (e.getSlot() == 23) {
				
				if (main.redstoneLess) main.redstoneLess = false;
				else main.redstoneLess = true;
				invOreLimitBuiler(player);
				return;
			}	
			
			if (e.getSlot() == 24) {
				
				if (main.quartzLess) main.quartzLess = false;
				else main.quartzLess = true;
				invOreLimitBuiler(player);
				return;
			}	
		}
		
		//HEALING
		if (inv.getName().equalsIgnoreCase("§6Scénarios : §dHealing Info")) { 
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigBuilder(player);
				return;
			}
			
			if (e.getSlot() == 18) {
				
				if (e.getClick() == ClickType.LEFT) {
					if (main.Abso < 20) {
						main.Abso ++;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.Abso > 0) {
						main.Abso --;
					}
				}
				
				invHealingBuiler(player);
				return;
			}
			
			if (e.getSlot() == 19) {
				
				if (main.Notch) main.Notch = false;
				else main.Notch = true;
				invHealingBuiler(player);
				return;
			}
			
			if (e.getSlot() == 20) {
				
				if (main.GoldenHead) main.GoldenHead = false;
				else main.GoldenHead = true;
				invHealingBuiler(player);
				return;
			}
			
			if (e.getSlot() == 21) {
				
				if (e.getClick() == ClickType.LEFT) {
					if (main.HeadHeal < 10) {
						main.HeadHeal ++;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.HeadHeal > 0) {
						main.HeadHeal --;
					}
				}
				
				invHealingBuiler(player);
				return;
			}
			
			if (e.getSlot() == 22) {
				
				if (main.FinalHeal) main.FinalHeal = false;
				else main.FinalHeal = true;
				invHealingBuiler(player);
				return;
			}
			
			if (e.getSlot() == 23) {
				
				if (main.Cupid) main.Cupid = false;
				else main.Cupid = true;
				invHealingBuiler(player);
				return;
			}
		}
		//VANILLA +
		if (inv.getName().equalsIgnoreCase("§6Scénarios : §aVanilla Boost")) { 
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigBuilder(player);
				return;
			}
			
			if (e.getSlot() == 18) {
				
				if (e.getClick() == ClickType.LEFT) {
					if (main.flint < 100) {
						main.flint = main.flint + 1;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.flint > 0) {
						main.flint = main.flint - 1;
					}
				}
				
				invVanillaBuilder(player);
				return;
			}
			
			if (e.getSlot() == 19) {
				
				if (e.getClick() == ClickType.LEFT) {
					if (main.apples < 100) {
						main.apples = main.apples + 1;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.apples > 0) {
						main.apples = main.apples - 1;
					}
				}
				
				invVanillaBuilder(player);
				return;
			}
			
			
			if (e.getSlot() == 20) {
				
				if (main.infiniteEnc) main.infiniteEnc = false;
				else main.infiniteEnc = true;
				invVanillaBuilder(player);
				return;
			}
			
			if (e.getSlot() == 21) {
				
				if (main.noHunger) main.noHunger = false;
				else main.noHunger = true;
				invVanillaBuilder(player);
				return;
			}
			
			if (e.getSlot() == 22) {
				
				if (main.CatEyes) main.CatEyes = false;
				else main.CatEyes = true;
				invVanillaBuilder(player);
				return;
			}
			
			if (e.getSlot() == 25) {
				
				if (main.Utils) main.Utils = false;
				else main.Utils = true;
				invVanillaBuilder(player);
				return;
			}
			
		}
		
		if (inv.getName().equalsIgnoreCase("§6Scénarios : §cVanilla Nerf")) { 
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.COBBLESTONE) {
				noMineBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.EXP_BOTTLE) {
				InvConfigXpNerf(player);
				return;
			}
			
			if (e.getSlot() == 18) {
				
				if (main.xpNerf) main.xpNerf = false;
				else main.xpNerf = true;
				invVanillaNBuilder(player);
				return;
			}
			
			if (e.getSlot() == 19) {
				
				if (main.noNether) main.noNether = false;
				else main.noNether = true;
				invVanillaNBuilder(player);
				return;
			}
			
			if (e.getSlot() == 20) {
				
				if (main.noEnd) main.noEnd = false;
				else main.noEnd = true;
				invVanillaNBuilder(player);
				return;
			}
			
			if (e.getSlot() == 21) {
				
				if (main.noMine) main.noMine = false;
				else main.noMine = true;
				invVanillaNBuilder(player);
				return;
			}
			
			if (e.getSlot() == 22) {
				if (main.horseLess) main.horseLess = false;
				else main.horseLess = true;
				invVanillaNBuilder(player);
				return;
			}
			
			if (e.getSlot() == 23) {
				
				if (!main.canJoin()) {
					player.sendMessage(main.gameTag + "§cCe scénario ne peut pas être modifier après le lancement de la partie !");
					return;
				}
				
				if (main.noNametag) main.noNametag = false;
				else main.noNametag = true;
				
				invVanillaNBuilder(player);
				return;
			}
		}
		
		if (inv.getName().equalsIgnoreCase("§cVanilla Nerf : §4No Mine")) { 
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				invVanillaNBuilder(player);
				return;
			}
			
			if (e.getSlot() == 0 || e.getSlot() == 18) {
				
				noMineTimeBuilder(player);
				return;
			}
			
			if (e.getSlot() == 19) {
				if (e.getClick() == ClickType.LEFT) {
					if (main.noMineCouche < 64) {
						main.noMineCouche++;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.noMineCouche > 5) {
						main.noMineCouche--;
					}
				}
				noMineBuilder(player);
				return;
			}
		}
		
		if (inv.getName().equalsIgnoreCase("§4No Mine : §aTime")) { 
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				noMineBuilder(player);
				return;
			}
			
			if (e.getInventory().getHolder() != player) {
				if (clicked.getType() == Material.BANNER) {
					
					main.noMineTime = main.noMineTime + Integer.valueOf(clicked.getItemMeta().getDisplayName().substring(2));
					
					if (main.noMineTime < 0) main.noMineTime = 0;
					if (main.noMineTime > 60) main.noMineTime = 60;
					
					
					noMineTimeBuilder(player);
					return;
				}
				
				if (clicked.getType() == Material.SIGN) {
					player.closeInventory();
					
					main.setOptionChat(player.getUniqueId(), LGOption.setNoMineTime);
					player.sendMessage(main.gameTag + "§2Ecrivez le temps avant l'activation du scénario noMine (écrivez 'Annuler' pour quitter)");
				}
			}
		}
		
		if (inv.getName().equalsIgnoreCase("§cVanilla Nerf : §4Xp Nerf")) { 
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				invVanillaNBuilder(player);
				return;
			}
			
			if (e.getInventory().getHolder() != player) {
				if (clicked.getType() == Material.BANNER) {
					
					main.xpNerfVar = main.xpNerfVar + Double.valueOf(clicked.getItemMeta().getDisplayName().substring(2));
					
					if (main.xpNerfVar < 1) main.xpNerfVar = 1.00;
					if (main.xpNerfVar > 10) main.xpNerfVar = 10.00;
					
					main.xpNerfVar = (double)Math.round(main.xpNerfVar * 100d) / 100d;
					
					InvConfigXpNerf(player);
					return;
				}
				
				if (clicked.getType() == Material.SIGN) {
					player.closeInventory();
					
					main.setOptionChat(player.getUniqueId(), LGOption.setXpNerf);
					player.sendMessage(main.gameTag + "§2Ecrivez par combien vous souhaiter diviser le drop d'Xp (écrivez 'Annuler' pour quitter)");
				}
			}
		}
		
		
		if (inv.getName().equalsIgnoreCase("§6Scénarios : §ePVE")) { 
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigBuilder(player);
				return;
			}
			
			if (e.getSlot() == 18) {
				
				if (main.NightMare) main.NightMare = false;
				else main.NightMare = true;
				invPveBuilder(player);
				return;
			}
			
			if (e.getSlot() == 19) {
				
				if (main.PoisonLess) main.PoisonLess = false;
				else main.PoisonLess = true;
				invPveBuilder(player);
				return;
			}
			
			
		}
		
		if (inv.getName().equalsIgnoreCase("§6Scénarios : §cFUN")) { 
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigBuilder(player);
				return;
			}
			
			if (e.getSlot() == 18) {
				
				if (main.goneFishin) main.goneFishin = false;
				else main.goneFishin = true;
				invFunBuilder(player);
				return;
			}
			
			if (e.getSlot() == 19) {
				
				if (main.skyHigh) main.skyHigh = false;
				else main.skyHigh = true;
				invFunBuilder(player);
				return;
			}
			
			if (e.getSlot() == 20) {
				
				if (main.eggs) main.eggs = false;
				else main.eggs = true;
				invFunBuilder(player);
				return;
			}
			
			if (e.getSlot() == 21) {
				
				if (main.LootCrate) main.LootCrate = false;
				else main.LootCrate = true;
				invFunBuilder(player);
				return;
			}
			
			if (e.getSlot() == 4 || e.getSlot() == 22) {
				if (main.canJoin()) {
					if (e.getSlot() == 4) {
						invCivBuilder(player);
						return;
					}
					
					if (e.getSlot() == 22) {
						
						if (main.Civilisation) main.Civilisation = false;
						else main.Civilisation = true;
						invFunBuilder(player);
						return;
					} else {
						if (!main.Civilisation) player.sendMessage(main.gameTag + "§cLa partie est déjà lancé ! Vous ne pouvez pas activer ce plugin !");
						else player.sendMessage(main.gameTag + "§cLa partie est déjà lancé ! Vous ne pouvez pas désactiver ce plugin !");
						
						player.closeInventory();
						return;
					}
				}
			}
		}
		
		if (inv.getName().equalsIgnoreCase("§cFUN : §eCivilisation")) { 
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				invFunBuilder(player);
				return;
			}
			
			if (e.getSlot() == 18) {
				if (e.getClick() == ClickType.LEFT) {
					if (main.CivSize < 50) {
						main.CivSize = main.CivSize + 1;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.CivSize > 1) {
						main.CivSize = main.CivSize - 1;
					}
				}
				
				invCivBuilder(player);
				return;
			}
			
			if (e.getSlot() == 19) {
				if (e.getClick() == ClickType.LEFT) {
					if (main.vilSize < 200) {
						main.vilSize = main.vilSize + 5;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.vilSize > 10) {
						main.vilSize = main.vilSize - 5;
					}
				}
				
				invCivBuilder(player);
				return;
			}
		}
		
		
		
		//ROLES /////////////////////////////////////////
		if (inv.getName().equalsIgnoreCase("§5Menu: §6Rôles")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.MELON_BLOCK) {
				invConfigRolesBuilerSSS(player);
				return;
			}
			
			if (clicked.getType() == Material.BARRIER) {
				main.configInvBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.WOOL) {
				
				if (e.getClick() == ClickType.LEFT || e.getClick() == ClickType.RIGHT || e.getClick() == ClickType.DOUBLE_CLICK) {
					for (LGRoles role : LGRoles.values()) {
						String name = clicked.getItemMeta().getDisplayName();
						
						
						if (name.substring(2)
								.replace(" §f(x2)", "")
								.equals(role.name)) {
							
							//SOEUR
							if (role == LGRoles.Soeur) {
								if (role.number == 0) role.number = 2;
								else role.number = 0;
								
							//AUTRE
							} else {
								if (role.number == 0) role.number = 1;
								else role.number = 0;
							}
							
							
						}
					}
				}
				
				if (e.getClick() == ClickType.SHIFT_LEFT) {
					for (LGRoles role : LGRoles.values()) {
						String name = clicked.getItemMeta().getDisplayName();
						
						
						if (name.substring(2)
								.replace(" §f(x2)", "")
								.equals(role.name)) {
							
							//SOEUR
							if (role == LGRoles.Soeur) {
								if (role.number < 20) {
									role.number = role.number + 2;
								}
								
							//AUTRE
							} else {
								if (role.number < 20) {
									role.number++;
								}
								
							}
							
							
						}
					}
				}
				
				if (e.getClick() == ClickType.SHIFT_RIGHT) {
					for (LGRoles role : LGRoles.values()) {
						String name = clicked.getItemMeta().getDisplayName();
						
						
						if (name.substring(2)
								.replace(" §f(x2)", "")
								.equals(role.name)) {
							
							//SOEUR
							if (role == LGRoles.Soeur) {
								if (role.number > 0) {
									role.number = role.number - 2;
								}
								
								
							//AUTRE
							} else {
								if (role.number > 0) {
									role.number--;
								}
							}
						}
					}
				}
				
				invConfigRolesBuiler(player);
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getOpenInventory() != null) {
						if (p.getOpenInventory().getTitle().equalsIgnoreCase("§5Menu: §6Rôles")) {
							if (!p.equals(player)) {
								invConfigRolesBuiler(p);
							}
							
						}
					}
				}
				return;
			}
			
			if (clicked.getType() == Material.BONE) {
				
				LGRoles role = LGRoles.LG;
				if (e.getClick() == ClickType.LEFT) {
					if (role.number < 50) {
						role.number ++;
					}
				}
				
				if (e.getClick() == ClickType.RIGHT) {
					if (role.number > 1) {
						role.number --;
					}
				}
				
				invConfigRolesBuiler(player);
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getOpenInventory() != null) {
						if (p.getOpenInventory().getTitle().equalsIgnoreCase("§5Menu: §6Rôles")) {
							if (!p.equals(player)) {
								invConfigRolesBuiler(p);
							}
							
						}
					}
				}
				
				return;
			}
		}
		
		//REGLES LGUHC ////////////////////////
		if (inv.getName().equalsIgnoreCase("§5Menu: §6Règles LGUHC")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				main.configInvBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.BOOK) {
				InvConfigChatBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.WORKBENCH) {
				InvConfigGamerulesBuilder(player);
				return;
			}
			
			if (e.getSlot() == 18) {
				if (main.meetup) main.meetup = false;
				else main.meetup = true;
				InvConfigRulesBuilder(player);
				return;
			}
			
			if (e.getSlot() == 19) {
				if (main.hidecompo) main.hidecompo = false;
				else main.hidecompo = true;
				InvConfigRulesBuilder(player);
				return;
			}
			
			if (e.getSlot() == 20) {
				if (main.coupleAlea) main.coupleAlea = false;
				else main.coupleAlea = true;
				InvConfigRulesBuilder(player);
				return;
			}
			
			if (e.getSlot() == 21) {
				if (main.voteProtect) main.voteProtect = false;
				else main.voteProtect = true;
				InvConfigRulesBuilder(player);
				return;
			}
			
			if (e.getSlot() == 24) {
				if (main.msg) main.msg = false;
				else main.msg = true;
				InvConfigRulesBuilder(player);
				return;
			}
			
			if (e.getSlot() == 25) {
				
				if (e.getClick() == ClickType.LEFT) {
					
					if (main.chat == ChatOption.On) {
						main.chat = ChatOption.Off;
						InvConfigRulesBuilder(player);
						return;
					}
					if (main.chat == ChatOption.Off) {
						main.chat = ChatOption.Off_IG;
						InvConfigRulesBuilder(player);
						return;
					}
					if (main.chat == ChatOption.Off_IG) {
						main.chat = ChatOption.Region;
						InvConfigRulesBuilder(player);
						return;
					}
					if (main.chat == ChatOption.Region) {
						main.chat = ChatOption.On;
						InvConfigRulesBuilder(player);
						return;
					}
					
					InvConfigRulesBuilder(player);
					return;
				}
				
				if (e.getClick() == ClickType.RIGHT) {
					
					if (main.chat == ChatOption.On) {
						main.chat = ChatOption.Region;
						InvConfigRulesBuilder(player);
						return;
					}
					if (main.chat == ChatOption.Off) {
						main.chat = ChatOption.On;
						InvConfigRulesBuilder(player);
						return;
					}
					if (main.chat == ChatOption.Off_IG) {
						main.chat = ChatOption.Off;
						InvConfigRulesBuilder(player);
						return;
					}
					if (main.chat == ChatOption.Region) {
						main.chat = ChatOption.Off_IG;
						InvConfigRulesBuilder(player);
						return;
					}
					
					InvConfigRulesBuilder(player);
					return;
				}
			}
			
		}
		
		if (inv.getName().equalsIgnoreCase("§6Règles LGUHC : §2Gamerules")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigRulesBuilder(player);
				return;
			}
			
			if (e.getSlot() == 18) {
				if (main.reduced) main.reduced = false;
				else main.reduced = true;
				
				InvConfigGamerulesBuilder(player);
				return;
			}
			
			if (e.getSlot() == 19) {
				if (main.announceAdv) main.announceAdv = false;
				else main.announceAdv = true;
				
				InvConfigGamerulesBuilder(player);
				return;
			}
			
			InvConfigGamerulesBuilder(player);
			return;
		}
		
		if (inv.getName().equalsIgnoreCase("§6Règles LGUHC : §aChat")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigRulesBuilder(player);
				return;
			}
			
			if (e.getSlot() == 18) {
				
				if (e.getClick() == ClickType.LEFT) {
					if (main.chatRegion < 100) {
						main.chatRegion = main.chatRegion + 5;
					}
				}
				
				if (e.getClick() == ClickType.RIGHT) {
					if (main.chatRegion > 10) {
						main.chatRegion = main.chatRegion - 5;
					}
				}
				
				InvConfigChatBuilder(player);
				return;
			}
		}
		
		//GESTIONS DE LA WORLDBORDER ////////////////////////
		if (inv.getName().equalsIgnoreCase("§5Menu: §6Gestion WorldBorder")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				main.configInvBuilder(player);
				return;
			}
			
			if (e.getSlot() == 0) {
				InvConfigMapBuilder(player);
				return;
			}
			if (e.getSlot() == 1) {
				InvConfigWbMaxBuilder(player);
				return;
			}
			
			if (e.getSlot() == 2) {
				InvConfigWbTimeBuilder(player);
				return;
			}
			
			if (e.getSlot() == 7) {
				InvConfigSpeedBuilder(player);
				return;
			}
		}
		
		if (inv.getName().equalsIgnoreCase("§6Gestion Wb :§e Bordure Départ")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigWbBuilder(player);
				return;
			}
			
			if (e.getInventory().getHolder() != player) {
				if (clicked.getType() == Material.BANNER) {
					
					main.Map = main.Map + Integer.valueOf(clicked.getItemMeta().getDisplayName().substring(2));
					
					if (main.Map < 50) main.Map = 50;
					if (main.Map > 10000) main.Map = 10000;
					
					InvConfigMapBuilder(player);
					return;
				}
				
				if (clicked.getType() == Material.SIGN) {
					player.closeInventory();
					
					main.setOptionChat(player.getUniqueId(), LGOption.setWbSize);
					player.sendMessage(main.gameTag + "§2Ecrivez la taille de la bordure initiale (écrivez 'Annuler' pour quitter)");
				}
			}
		}
		
		if (inv.getName().equalsIgnoreCase("§6Gestion Wb :§e Bordure Finale")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigWbBuilder(player);
				return;
			}
			
			if (e.getInventory().getHolder() != player) {
				if (clicked.getType() == Material.BANNER) {
					
					main.wbMax = main.wbMax + Integer.valueOf(clicked.getItemMeta().getDisplayName().substring(2));
					
					if (main.wbMax < 0) main.wbMax = 0;
					if (main.wbMax > 10000) main.wbMax = 10000;
					
					InvConfigWbMaxBuilder(player);
					return;
				}
				
				if (clicked.getType() == Material.SIGN) {
					player.closeInventory();
					
					main.setOptionChat(player.getUniqueId(), LGOption.setWbmaxSize);
					player.sendMessage(main.gameTag + "§2Ecrivez la taille de la bordure finale (écrivez 'Annuler' pour quitter)");
				}
			}
		}
		
		if (inv.getName().equalsIgnoreCase("§6Gestion Wb :§e Temps Bordure")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigWbBuilder(player);
				return;
			}
			
			if (e.getInventory().getHolder() != player) {
				if (clicked.getType() == Material.BANNER) {
					
					main.wbTime = main.wbTime + Integer.valueOf(clicked.getItemMeta().getDisplayName().substring(2));
					
					if (main.wbTime < 0) main.wbTime = 0;
					if (main.wbTime > 300) main.wbTime = 300;
					
					InvConfigWbTimeBuilder(player);
					return;
				}
				
				if (clicked.getType() == Material.SIGN) {
					player.closeInventory();
					
					main.setOptionChat(player.getUniqueId(), LGOption.setWbTime);
					player.sendMessage(main.gameTag + "§2Ecrivez le temps avant que la bordure s'active [en minute] (écrivez 'Annuler' pour quitter)");
				}
			}
		}
		
		if (inv.getName().equalsIgnoreCase("§6Gestion Wb :§e Vitesse Bordure")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigWbBuilder(player);
				return;
			}
			
			if (e.getSlot() == 0) {
				if (e.getClick() == ClickType.LEFT) {
					if (main.wbSpeed < 30) {
						main.wbSpeed ++;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.wbSpeed > 1) {
						main.wbSpeed --;
					}
				}
				
				InvConfigSpeedBuilder(player);
				return;
			}
			
			if (e.getSlot() == 1) {
				if (e.getClick() == ClickType.LEFT) {
					if (main.wbSecond < 60) {
						main.wbSecond ++;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.wbSecond > 1) {
						main.wbSecond --;
					}
				}
				
				InvConfigSpeedBuilder(player);
				return;
			}
		}
		
		//GESTION JOUEURS ////////////////////////
		if (inv.getName().equalsIgnoreCase("§5Menu: §6Gestion Joueurs")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				main.configInvBuilder(player);
				return;
			}
			
			if (e.getSlot() == 1) {
				main.getSpectatorInv(player);
				return;
			}
			
			if (e.getSlot() == 0) {
				main.InvStaffBuilder(player);
				return;
			}
			
			if (e.getSlot() == 7) {
				if (!main.getConfig().getBoolean("Stats.state")) {
					
					player.sendMessage(main.gameTag + "§cLes statistiques on été désactivé depuis le fichier de configuration ! Vous ne pourrez donc pas les activer.");
					player.closeInventory();
					return;
				}
				
				if (main.stats) main.stats = false;
				else main.stats = true;
				
				InvConfigPlBuilder(player);
				return;
			}
			
			
		}
		
		//GESTIONS DU TEMPS ////////////////////////
		if (inv.getName().equalsIgnoreCase("§5Menu: §6Gestion Temps")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				main.configInvBuilder(player);
				return;
			}
			
			if (e.getSlot() == 0) {
				if (main.epTime == 20) main.epTime = 10;
				else main.epTime = 20;
				
				InvConfigTimeBuilder(player);
				return;
			}
			
			if (e.getSlot() == 1) {
				
				if (main.DailyCycle ) main.DailyCycle  = false;
				else main.DailyCycle  = true;
				InvConfigTimeBuilder(player);
				return;
			}
			
			if (e.getSlot() == 2) {
				if (e.getClick() == ClickType.LEFT) {
					if (main.pvp < 60) {
						main.pvp ++;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.pvp > 1) {
						main.pvp --;
					}
				}
				
				InvConfigTimeBuilder(player);
				return;
			}
			
			if (e.getSlot() == 3) {
				if (e.getClick() == ClickType.LEFT) {
					if (main.announceRole < 60) {
						main.announceRole ++;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.announceRole > 1) {
						main.announceRole --;
					}
				}
				
				InvConfigTimeBuilder(player);
				return;
			}
			
			if (e.getSlot() == 4) {
				if (e.getClick() == ClickType.LEFT) {
					if (main.voteEp < 10) {
						main.voteEp ++;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.voteEp > 2) {
						main.voteEp --;
					}
				}
				
				InvConfigTimeBuilder(player);
				return;
			}
		}
		
		//GESTIONS DES STUFFS ////////////////////////
		if (inv.getName().equalsIgnoreCase("§5Menu: §6Gestion Stuff")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				main.configInvBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.CHEST) {
				InvStartItemBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.ENDER_CHEST) {
				InvDeathItemBuilder(player);
				return;
			}
		}
		
		if (inv.getName().equalsIgnoreCase("§6Gestion Stuff : §aStart")) {
			
			
			if (clicked.getType() == Material.STAINED_GLASS_PANE) {
				e.setCancelled(true);
				return;
			}
			
			if (clicked.getType() == Material.BARRIER) {
				e.setCancelled(true);
				InvConfigStuffBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.REDSTONE_TORCH_ON) {
				if (clicked.hasItemMeta()) {
					if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase("§2Sauvegarder ?")) {
						//SAVE
						e.setCancelled(true);
						main.startItem = inv.getContents();
					}
				}
				return;
			}
		}
		
		if (inv.getName().equalsIgnoreCase("§6Gestion Stuff : §aDeath")) {
			
			
			if (clicked.getType() == Material.STAINED_GLASS_PANE) {
				e.setCancelled(true);
				return;
			}
			
			if (clicked.getType() == Material.BARRIER) {
				e.setCancelled(true);
				InvConfigStuffBuilder(player);
				return;
			}
			
			if (clicked.getType() == Material.REDSTONE_TORCH_ON) {
				if (clicked.hasItemMeta()) {
					if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase("§2Sauvegarder ?")) {
						//SAVE
						e.setCancelled(true);
						main.deathItem = inv.getContents();
					}
				}
				return;
			}
		}
		
		
		if (inv.getName().equalsIgnoreCase("§5Menu: §6Configurations")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				main.configInvBuilder(player);
				return;
			}
			
			//Configs
			if (e.getSlot() < 27) {
				if (clicked != null) {
					if (clicked.getType() != Material.AIR) {
						
						if(e.getClick() == ClickType.LEFT) {
							
							File fichier = new File(main.getDataFolder() + File.separator + "Configurations" + File.separator + clicked.getItemMeta().getDisplayName().substring(2) + ".yml");
							if (!fichier.exists()) {
								player.closeInventory();
								player.sendMessage(main.gameTag + "§cErreur, ce fichier n'existe plus !");
								return;
							}
							
							new ConfigManager(main).loadConfig(fichier);
							player.sendMessage(main.gameTag + "§aLa configuration est chargé !");
							
							player.closeInventory();
							return;
						}
						
						if (e.getClick() == ClickType.RIGHT) {
							
							File fichier = new File(main.getDataFolder() + File.separator + "Configurations" + File.separator + clicked.getItemMeta().getDisplayName().substring(2) + ".yml");
							
							new ConfigManager(main).removeConfig(fichier);
							player.sendMessage(main.gameTag + "§aLa configuration à été suprimé !");
							
							main.configInvBuilder(player);
							return;
						}
						
						if (e.getClick() == ClickType.MIDDLE) {
							
							File fichier = new File(main.getDataFolder() + File.separator + "Configurations" + File.separator + clicked.getItemMeta().getDisplayName().substring(2) + ".yml");
							
							new ConfigManager(main).saveConfig(fichier);
							player.sendMessage(main.gameTag + "§aLa configuration à été écrasé !");
							player.closeInventory();
							return;
						}
					}
				}
			}
			
			if (e.getSlot() == 31) {
				main.setOptionChat(player.getUniqueId(), LGOption.addConfig);
				player.sendMessage(main.gameTag + "§2Ecrivez le nom que vous voulez donner à votre configuration (écrivez 'Annuler' pour quitter)");
				player.closeInventory();
				return;
			}
		}
		
		if (e.isCancelled()) return; //IS CANCELLED ----------------------------------------------------
		
		if (main.LimiteDePiece != 4) {
			if (e.getInventory().getHolder() == player) {
				
				if ((clicked.getType() == Material.DIAMOND_HELMET || clicked.getType() == Material.DIAMOND_CHESTPLATE ||
						clicked.getType() == Material.DIAMOND_LEGGINGS || clicked.getType() == Material.DIAMOND_BOOTS) && e.getSlotType() != SlotType.ARMOR) {
							
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							if (!canEquipArmor(player) && !isFullArmor(player)) {
								player.sendMessage(" ");
								player.sendMessage(main.gameTag + "§cVous ne pouvez pas vous équiper avec plus de pièce en diamant !");
								e.setCancelled(true);
							}
						}
						return;
					}
				
				if ((e.getSlotType() == SlotType.ARMOR)) {
					
					if (e.getCursor() != null) {
						if (e.getCursor().getType() == Material.DIAMOND_HELMET || e.getCursor().getType() == Material.DIAMOND_CHESTPLATE ||
								e.getCursor().getType() == Material.DIAMOND_LEGGINGS || e.getCursor().getType() == Material.DIAMOND_BOOTS) {
								
								if (!canEquipArmor(player)) {
									e.setCancelled(true);
									player.sendMessage(" ");
									player.sendMessage(main.gameTag + "§cVous ne pouvez pas vous équiper avec plus de pièce en diamant !");
									return;
								}
								return;
							}
					}
					
				}
				

			}
		}
		
		if (main.isState(UHCState.GAME)) {
			if (player.getGameMode() == GameMode.SURVIVAL) {
				new LGLimitEnchant(main, clicked).limit(player);
			}
 		}
	}
	
	private void InvConfigBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§5Menu: §6Scénarios");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(2));
		}
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		inv.setItem(0, getItem(Material.DIAMOND_AXE, "§2Mode Run", 1, true, true));
		inv.setItem(18, getWool(null, main.run));
		
		inv.setItem(1, getItem(Material.DIAMOND_SWORD, "§bLimite de Stuff", 1, true, true));
		inv.setItem(19, getSign2());
		
		inv.setItem(2, getItem(Material.GOLDEN_APPLE, "§dHealing Info", 1, (byte) 1, true, true));
		inv.setItem(20, getSign1());
		
		inv.setItem(3, getItem(Material.LOG, "§aVanilla Boost", 1, true, true));
		inv.setItem(21, getWool(null, main.vanilla));
		
		inv.setItem(4, getItem(Material.WOOD, "§cVanilla Nerf", 1, true, true));
		inv.setItem(22, getWool(null, main.vanillaN));
		
		inv.setItem(6, getItem(Material.SKULL_ITEM, "§ePVE", 1, true, true));
		inv.setItem(24, getSign3());
		
		inv.setItem(7, getItem(Material.TNT, "§cFUN", 1, true, true));
		inv.setItem(25, getWool(null, main.fun));
		
		p.openInventory(inv);
	}
	
	private ItemStack getSign2() {
		
		ItemStack sign = new ItemStack(Material.SIGN, 1);
		ItemMeta signM = sign.getItemMeta();
		
		signM.setDisplayName("§b<<§6Informations§b>>");
		
		String diamond = "§b" + main.diamondlimit;
		if (main.diamondlimit == 0) diamond = "§cDésactivé";
		
		String stuff = "§b" + main.LimiteDePiece;
		if (main.LimiteDePiece == 0 || main.LimiteDePiece == 4) stuff = "§cDésactivé";
		
		signM.setLore(Arrays.asList("", "§7>> §fLimite d'enchant : " + getString(main.LimiteEnchant), "§7>> §fEnchantLess: " + getString(main.enchantLess), "", "§7>> §fRodLess: " + getString(main.RodLess),
				"", "§7>> §fDiamondLimit : " + diamond,  "§7>> §fPiece en diams : " + stuff, "§7>> §fBloodDiamond: " + getString(main.BloodDiamond), 
				"", "§7>> §fOreLess: " + getString(main.oreLess)));
		sign.setItemMeta(signM);
		
		return sign;
	}
	
	private ItemStack getSign1() {
		
		ItemStack sign = new ItemStack(Material.SIGN, 1);
		ItemMeta signM = sign.getItemMeta();
		
		signM.setDisplayName("§b<<§6Informations§b>>");
		
		String Abso = "§a" + main.Abso + " Coeur";
		if (main.Abso == 0) Abso = "§cDésactivé";
		
		signM.setLore(Arrays.asList("", "§7>> §fAbsorbtion : " + Abso, "§7>> §fPomme de notch : " + getString(main.Notch), "", "§7>> §fGolden Head : " 
		+ getString(main.GoldenHead), "§7>> §fGolden Head Heal :§a " + main.HeadHeal + " Coeur", "","§7>> §fFinal Heal : " +  getString(main.FinalHeal), 
		"§7>> §fCupid : " +  getString(main.Cupid)));
		sign.setItemMeta(signM);
		
		return sign;
	}
	
	private ItemStack getSign3() {
		
		ItemStack sign = new ItemStack(Material.SIGN, 1);
		ItemMeta signM = sign.getItemMeta();
		
		signM.setDisplayName("§b<<§6Informations§b>>");
		
		signM.setLore(Arrays.asList("", "§7>> §fMode Nightmare : " + getString(main.NightMare),"§7>> §fPoisonLess : " + getString(main.PoisonLess)));
		sign.setItemMeta(signM);
		
		return sign;
	}
	
	private String getString(Boolean b) {
		String state = "";
		if (b) {
			state = "§aActivé";
		} else {
			state = "§cDésativé";
		}
		return state;
	}
	
	private void invRunBuiler(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§6Scénarios : §2Scénarios Run");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(5));
		}
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		inv.setItem(0, getItem(Material.COOKED_BEEF, "§dCutClean", 1, true));
		inv.setItem(18, getWool(null , main.cutclean));
		
		inv.setItem(1, getItem(Material.FURNACE, "§dFast Smelting", 1, true));
		inv.setItem(19, getWool(null , main.fastSmelting));
		
		inv.setItem(2, getItem(Material.LOG, "§dTimber", 1, true));
		inv.setItem(20, getWool(null , main.timber));
		
		inv.setItem(3, getItem(Material.DIAMOND_ORE, "§dVeinMiner", 1, true));
		inv.setItem(21, getWool(null , main.veinMiner));
		
		inv.setItem(4, getItem(Material.DIAMOND_PICKAXE, "§dHastey Boys", 1, true));
		inv.setItem(22, getWool(null , main.HasteyBoys));
		
		inv.setItem(5, getItem(Material.DIAMOND, "§dBleeding Sweet", 1, true));
		inv.setItem(23, getWool(null , main.bleedingSweet));
		
		inv.setItem(6, getItem(Material.QUARTZ, "§dSpeedy Miner", 1, true));
		inv.setItem(24, getConfigItemSmall(Material.EMERALD, "§bDernier Episode avec SpeedyMiner : §6" + main.SpeedyMiner, main.SpeedyMiner, true, 1));
		
		inv.setItem(7, getItem(Material.WOOD_SPADE, "§dNo Wooden Tool", 1, true));
		inv.setItem(25, getTool());
		
		p.openInventory(inv);
	}
	
	private ItemStack getTool() {
		
		ItemStack item = new ItemStack(main.noWodenTool);	
		
		if (main.noWodenTool == Material.WOOD) {
			item = getWool(null, false);
			return item;
		}
		
		ItemMeta itemM = item.getItemMeta();
		
		if (main.noWodenTool == Material.COBBLESTONE) {
			itemM.setDisplayName("§bOutils en pierre");
		}
		
		if (main.noWodenTool == Material.IRON_INGOT) {
			itemM.setDisplayName("§bOutils en fer");
		}
		
		if (main.noWodenTool == Material.DIAMOND) {
			itemM.setDisplayName("§bOutils en diamant");
		}
		
		item.setItemMeta(itemM);
		return item;
	}
	private void invLimitBuiler(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§6Scénarios : §bLimite de Stuff");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(3));
		}
		
		inv.setItem(0, getItem(Material.ENCHANTED_BOOK, "§dLimite d'enchant", 1, true, true));
		inv.setItem(18, getWool(null, main.LimiteEnchant));
		
		inv.setItem(1, getItem(Material.DIAMOND_CHESTPLATE, "§6Limite de pièce en diamant", 1, true));
		inv.setItem(19, getConfigItemSmall(Material.DIAMOND, "§bLimite : §3" + main.LimiteDePiece, main.LimiteDePiece, true, 1));
		
		inv.setItem(2, getItem(Material.INK_SACK, "§cBlood Diamond", 1, (byte)1, true));
		inv.setItem(20, getWool(null, main.BloodDiamond));
		
		inv.setItem(3, getItem(Material.DIAMOND_ORE, "§bDiamondLimit", 1, true));
		inv.setItem(21, getConfigItemSmall(Material.DIAMOND, "§bLimite : §3" + main.diamondlimit, main.diamondlimit, true, 1));
		
		inv.setItem(4, getItem(Material.FISHING_ROD, "§6RodLess", 1, true));
		inv.setItem(22, getWool(null, main.RodLess ));
		
		inv.setItem(5, getItem(Material.GOLD_ORE, "§eOreLess", 1, true, true));
		inv.setItem(23, getWool(null, main.oreLess ));
		
		inv.setItem(6, getItem(Material.ENCHANTMENT_TABLE, "§5EnchantLess", 1, true));
		inv.setItem(24, getWool(null, main.enchantLess));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	private void invOreLimitBuiler(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§bLimite de Stuff : §eOreLess");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(3));
		}
		
		inv.setItem(0, getItem(Material.DIAMOND, "§bDiamondLess", 1, true));
		inv.setItem(18, getWool(null, main.diamondLess));
		
		inv.setItem(1, getItem(Material.GOLD_INGOT, "§eGoldLess", 1, true));
		inv.setItem(19, getWool(null, main.goldLess));
		
		inv.setItem(2, getItem(Material.IRON_INGOT, "§7IronLess", 1, true));
		inv.setItem(20, getWool(null, main.ironLess));
		
		inv.setItem(3, getItem(Material.INK_SACK, "§9LapisLess", 1, (byte) 4, true));
		inv.setItem(21, getWool(null, main.lapisLess));
		
		inv.setItem(4, getItem(Material.COAL, "§8CoalLess", 1, true));
		inv.setItem(22, getWool(null, main.coalLess));
		
		inv.setItem(5, getItem(Material.REDSTONE, "§cRedstoneLess", 1, true));
		inv.setItem(23, getWool(null, main.redstoneLess));
		
		inv.setItem(6, getItem(Material.QUARTZ, "§fQuartzLess", 1, true));
		inv.setItem(24, getWool(null, main.quartzLess));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private void invEnchantLimitBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 36, "§bLimite de Stuff : §dEnchants");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(3));
		}
		
		int slot = 0;
		
		for (LGEnchants ench : main.enchantLimit) {
			inv.setItem(slot, getEnchantedBook(ench));
			slot++;
		}
		
		inv.setItem(35, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private ItemStack getEnchantedBook(LGEnchants enchant) {
		
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, enchant.getLimit());
		
		if (enchant.getLimit() == 0) item = getWool(null, false);
		ItemMeta itemM = item.getItemMeta();
		
		if (!enchant.hasLimit()) {
			itemM.setDisplayName("§2§l" + enchant.getEnchantName());
		} else {
			itemM.setDisplayName("§2§l" + enchant.getEnchantName() + " §f*");
		}
		
		
		if (enchant.getLimit() == 0) {
			itemM.setLore(Arrays.asList("§aLimite : §c§l[Désactivé]"));
		} else {
			itemM.setLore(Arrays.asList("§aLimite : §f§l[" + enchant.getLimit() + "]"));
		}
		
		
		item.setItemMeta(itemM);
		return item;
	}
	
	private void invHealingBuiler(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§6Scénarios : §dHealing Info");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(6));
		}
		
		inv.setItem(0, getItem(Material.GOLD_BLOCK, "§aCoeurs d'absorbtion", 1, true));
		inv.setItem(18, getConfigItemSmall(Material.GOLD_NUGGET, "§dCoeurs : §3" + main.Abso, main.Abso, true, 1));
		
		inv.setItem(1, getItem(Material.GOLDEN_APPLE, "§ePomme de notch", 1, (byte) 1, true));
		inv.setItem(19, getWool(null, main.Notch));
		
		inv.setItem(2, getItem(Material.SKULL_ITEM, "§eGolden Head", 1, (byte) 3, true));
		inv.setItem(20, getWool(null, main.GoldenHead));
		
		inv.setItem(3, getItem(Material.BREAD, "§dGolden Head Heal", 1, true));
		inv.setItem(21, getConfigItemSmall(Material.GOLD_NUGGET, "§dCoeurs : §3" + main.HeadHeal, main.HeadHeal, true, 1));
		
		inv.setItem(4, getItem(Material.BEACON, "§dFinal Heal", 1, true));
		inv.setItem(22, getWool(null, main.FinalHeal));
		
		inv.setItem(5, getItem(Material.ARROW, "§dCupid", 1, true));
		inv.setItem(23, getWool(null, main.Cupid));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private void invVanillaBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§6Scénarios : §aVanilla Boost");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(5));
		}
		
		inv.setItem(0, getItem(Material.FLINT, "§aDrop du Flint", 1, true));
		inv.setItem(18, getConfigItem(Material.REDSTONE, "§aTaux de drop : §2" + main.flint + "%", main.flint, true, 1));
		
		inv.setItem(1, getItem(Material.APPLE, "§aDrop de Pommes", 1, true));
		inv.setItem(19, getConfigItem(Material.REDSTONE, "§aAugmentation : §2+" + main.apples + "%", main.apples, true, 1));
		
		inv.setItem(2, getItem(Material.ENCHANTMENT_TABLE, "§2Infinite Enchanter", 1, true));
		inv.setItem(20, getWool(null, main.infiniteEnc));
		
		inv.setItem(3, getItem(Material.COOKED_BEEF, "§2No Food", 1, true));
		inv.setItem(21, getWool(null, main.noHunger));
		
		inv.setItem(4, getItem(Material.EYE_OF_ENDER, "§5CatEyes", 1, true));
		inv.setItem(22, getWool(null, main.CatEyes));
		
		inv.setItem(7, getItem(Material.CHEST, "§bUtils", 1, true));
		inv.setItem(25, getWool(null, main.Utils));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private void invVanillaNBuilder(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, "§6Scénarios : §cVanilla Nerf");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(12));
		}
		
		inv.setItem(0, getItem(Material.EXP_BOTTLE, "§4Xp Nerf", 1, true, true));
		inv.setItem(18, getWool(null, main.xpNerf));
		
		inv.setItem(1, getItem(Material.NETHERRACK, "§4No Nether", 1, true));
		inv.setItem(19, getWool(null, main.noNether));
		
		inv.setItem(2, getItem(Material.ENDER_STONE, "§4No End", 1, true));
		inv.setItem(20, getWool(null, main.noEnd));
		
		inv.setItem(3, getItem(Material.COBBLESTONE, "§4No Mine", 1, true, true));
		inv.setItem(21, getWool(null, main.noMine));
		
		inv.setItem(4, getItem(Material.SADDLE, "§4HorseLess", 1, true));
		inv.setItem(22, getWool(null, main.horseLess));
		
		inv.setItem(5, getItem(Material.NAME_TAG, "§aNo Name Tag", 1, true));
		inv.setItem(23, getWool(null, main.noNametag));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private void InvConfigXpNerf(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§cVanilla Nerf : §4Xp Nerf");
		
		for (int i = 0; i < 9; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		for (int i = 18; i < 27; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		inv.setItem(9, vitre(15));
		inv.setItem(17, vitre(15));
		
		List<String> lore = Arrays.asList("§eL'Xp est divisé par §l" + main.xpNerfVar);
		
		inv.setItem(10, getItem(Material.BANNER, "§c-1", 1, (byte) 1, false, lore));
		inv.setItem(11, getItem(Material.BANNER, "§c-0.1", 1, (byte) 1, false, lore));
		inv.setItem(12, getItem(Material.BANNER, "§c-0.01", 1, (byte) 1, false, lore));
		
		inv.setItem(13, getItem(Material.SIGN, "§eL'Xp est divisé par §l" + main.xpNerfVar, 1, false, Arrays.asList("§bCliquez pour choisir une valeur")));
		
		inv.setItem(14, getItem(Material.BANNER, "§a+0.01", 1, (byte) 2, false, lore));
		inv.setItem(15, getItem(Material.BANNER, "§a+0.1", 1, (byte) 2, false, lore));
		inv.setItem(16, getItem(Material.BANNER, "§a+1", 1, (byte) 2, false, lore));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private void noMineBuilder(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, "§cVanilla Nerf : §4No Mine");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(12));
		}
		
		inv.setItem(0, getItem(Material.WATCH, "§aActivation", 1, true));
		inv.setItem(18, getItem(Material.GOLD_BLOCK, "§c" + main.noMineTime + "§4 Minute" , main.noMineTime, true));
		
		inv.setItem(1, getItem(Material.STONE, "§aCouche Minimal", 1, true));
		inv.setItem(19, getConfigItemSmall(Material.GOLD_BLOCK, "§4Couche : §c" + main.noMineCouche, main.noMineCouche, true, 1));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private void noMineTimeBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§4No Mine : §aTime");
		
		for (int i = 0; i < 9; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		for (int i = 18; i < 27; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		List<String> lore = Arrays.asList("§eActivation au bout de " + main.noMineTime + "min");
		
		inv.setItem(9, getItem(Material.BANNER, "§c-20", 1, (byte) 1, false, lore));
		inv.setItem(10, getItem(Material.BANNER, "§c-10", 1, (byte) 1, false, lore));
		inv.setItem(11, getItem(Material.BANNER, "§c-5", 1, (byte) 1, false, lore));
		inv.setItem(12, getItem(Material.BANNER, "§c-1", 1, (byte) 1, false, lore));
		
		inv.setItem(13, getItem(Material.SIGN, "§eActivation : " + main.noMineTime + "min", 1, false, Arrays.asList("§bCliquez pour choisir une valeur")));
		
		inv.setItem(14, getItem(Material.BANNER, "§a+1", 1, (byte) 2, false, lore));
		inv.setItem(15, getItem(Material.BANNER, "§a+5", 1, (byte) 2, false, lore));
		inv.setItem(16, getItem(Material.BANNER, "§a+10", 1, (byte) 2, false, lore));
		inv.setItem(17, getItem(Material.BANNER, "§a+20", 1, (byte) 2, false, lore));
		
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private void invPveBuilder(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, "§6Scénarios : §ePVE");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(4));
		}
		
		inv.setItem(0, getItem(Material.BED, "§cNightmare", 1, true));
		inv.setItem(18, getWool(null, main.NightMare));
		
		inv.setItem(1, getItem(Material.POISONOUS_POTATO, "§2PoisonLess", 1, true));
		inv.setItem(19, getWool(null, main.PoisonLess));
		
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private void invFunBuilder(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, "§6Scénarios : §cFUN");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(14));
		}
		
		inv.setItem(0, getItem(Material.FISHING_ROD, "§cGone Fishin'", 1, true));
		inv.setItem(18, getWool(null, main.goneFishin));
		
		inv.setItem(1, getItem(Material.WOOL, "§cSky High", 1, true));
		inv.setItem(19, getWool(null, main.skyHigh));
		
		inv.setItem(2, getItem(Material.EGG, "§cEggs", 1, true));
		inv.setItem(20, getWool(null, main.eggs));
		
		inv.setItem(3, getItem(Material.CHEST, "§2LootCrate", 1, true, false));
		inv.setItem(21, getWool(null, main.LootCrate));
		
		inv.setItem(4, getItem(Material.HAY_BLOCK, "§aCivilisation", 1, true, true));
		inv.setItem(22, getWool(null, main.Civilisation));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private void invCivBuilder(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, "§cFUN : §eCivilisation");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(4));
		}
		
		inv.setItem(0, getItem(Material.BREAD, "§aTaille des villages", 1, true));
		inv.setItem(18, getConfigItemSmall(Material.WHEAT, "§eJoueurs : §f" + main.CivSize, main.CivSize, true, 1));
		
		inv.setItem(1, getItem(Material.COBBLE_WALL, "§aTerritoire des villages", 1, true));
		inv.setItem(19, getConfigItem(Material.WHEAT, "§eTailles : §f" + main.vilSize, 1, true, 5));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	//GESTIONS DES ROLES ////////////////////////////////
	private void invConfigRolesBuiler(Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, "§5Menu: §6Rôles");
		
		inv.setItem(1, getConfigItemSmall(Material.BONE, "§cLoup-Garou", LGRoles.LG.number, true, 1));
		
		int slot = 2;
		int compoNumber = 0;
		
		for (LGRoles role : LGRoles.values()) {
			if (role == LGRoles.SV) break;
			
			compoNumber = compoNumber + role.number;
			if (role != LGRoles.LG) {
								
				if (role == LGRoles.Soeur) inv.setItem(slot, getWool("§a"+role.name + " §f(x2)", role.number, true));
				else if (role == LGRoles.EnfantS) inv.setItem(slot, getWool("§e"+role.name, role.number, true));
				else if (role.camp == LGCamps.Village) inv.setItem(slot, getWool("§a"+role.name, role.number, true));
				else if (role.camp == LGCamps.LoupGarou) inv.setItem(slot, getWool("§c"+role.name, role.number, true));
				else if (role.camp == LGCamps.LGB) inv.setItem(slot, getWool("§4"+role.name, role.number, true));
				else if (role.camp == LGCamps.Assassin) inv.setItem(slot, getWool("§6"+role.name, role.number, true));
				else if (role.camp == LGCamps.Neutre) inv.setItem(slot, getWool("§b"+role.name, role.number, true));
				
				else inv.setItem(slot, getWool("§e" + role.name, role.number, true));	
				slot++;
			}
		}

		int SVNumber = main.getPlayers().size() - compoNumber;
		
		if (SVNumber > 0) inv.setItem(0, getItem(Material.EXP_BOTTLE, "§2Simple Villageois §f(" + SVNumber + ") ", SVNumber, true));
		else inv.setItem(0, getWool("§2Simple Villageois  §c(" + SVNumber + ") ", false));
		
		
		inv.setItem(35, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		p.openInventory(inv);
	}
	
	private void invConfigRolesBuilerSSS(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9, "§5Menu: §6Rôles");
		
		
		
		inv.setItem(8, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		p.openInventory(inv);
	}
	
	//GESTIONS DES Règles ////////////////////////
	private void InvConfigRulesBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§5Menu: §6Règles LGUHC");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(0));
		}
		
		inv.setItem(0, getItem(Material.EXP_BOTTLE, "§bMeetup", 1, true));
		inv.setItem(18, getWool(null, main.meetup));
		
		inv.setItem(1, getItem(Material.VINE, "§eComposition caché", 1, true));
		inv.setItem(19, getWool(null, main.hidecompo));
		
		inv.setItem(2, getItem(Material.ARROW, "§dCouple Aléatoire", 1, true));
		inv.setItem(20, getWool(null, main.coupleAlea));
		
		inv.setItem(3, getItem(Material.PAPER, "§2Protection Vote", 1, true, Arrays.asList("§aLes joueurs ne pourront prendre les votes", "§f1 fois dans la partie")));
		inv.setItem(21, getWool(null, main.voteProtect));
		
		inv.setItem(4, getItem(Material.WORKBENCH, "§2Gamerules", 1, true, true));
		inv.setItem(22, getGameruleIcon());
		
		inv.setItem(7, getItem(Material.BOOK, "§9Paramètre de Chat", 1, true, true));
		inv.setItem(25, getChatState());
		
		inv.setItem(6, getItem(Material.BOOK_AND_QUILL, "§aMessages Privés", 1, true, false));
		inv.setItem(24, getWool(null, main.msg));
		
		inv.setItem(7, getItem(Material.BOOK, "§6Paramètre de Chat", 1, true, true));
		inv.setItem(25, getChatState());
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private void InvConfigGamerulesBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§6Règles LGUHC : §2Gamerules");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(13));
		}
		
		inv.setItem(0, getItem(Material.MAP, "§2Reduced Debug Info", 1, true));
		inv.setItem(18, getWool(null, main.reduced));
		
		inv.setItem(1, getItem(Material.NOTE_BLOCK, "§2Announce Advancements", 1, true));
		inv.setItem(19, getWool(null, main.announceAdv));
		
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private ItemStack getGameruleIcon() {
		ItemStack sign = new ItemStack(Material.SIGN, 1);
		ItemMeta signM = sign.getItemMeta();
		
		signM.setDisplayName("§b<<§6Informations§b>>");
		
		signM.setLore(Arrays.asList("", "§7>> §fReduced Debug Info : " + getString(main.reduced), "§7>> §fAnnounce Advancements : " + getString(main.announceAdv)));
		sign.setItemMeta(signM);
		
		return sign;
	}
	
	private void InvConfigChatBuilder(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, "§6Règles LGUHC : §aChat");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(5));
		}
		
		inv.setItem(0, getItem(Material.EMPTY_MAP, "§2Taille région de chat", 1, true));
		inv.setItem(18, getConfigItem(Material.SNOW_BALL, "§2Taille : §a" + main.chatRegion, 1, true, 5));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private ItemStack getChatState() {
		if (main.chat == ChatOption.On) return getItem(Material.WOOL, "State : §aActivé", 1, (byte) 5, false);
		if (main.chat == ChatOption.Off) return getItem(Material.WOOL, "State : §cDésactivé", 1, (byte) 14, false);
		if (main.chat == ChatOption.Off_IG) return getItem(Material.WOOL, "State : §6Activé (sauf en jeu)", 1, (byte) 1, false);
		if (main.chat == ChatOption.Region) return getItem(Material.WOOL, "State : §bActivé (Région)", 1, (byte) 9, false);
		return getItem(Material.BARRIER, "§cError", 1, false);
	}
	
	//GESTIONS DE LA WORLDBORDER ////////////////////////
	private void InvConfigWbBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 9, "§5Menu: §6Gestion WorldBorder");
		
		inv.setItem(0, getItem(Material.MAP, "§eBordure Initiale : §6" + main.Map + " x " + main.Map, 1, true, true));
		inv.setItem(1, getItem(Material.EMPTY_MAP, "§eBordure Finale : §6" + main.wbMax + " x " + main.wbMax, 1, true, true));
		inv.setItem(2, getItem(Material.COMPASS, "§eActivation de la WorldBorder : §6" + main.wbTime + " min", 1, true, true));
		
		StringBuilder speed = new StringBuilder();
		if (main.wbSpeed == 1) speed.append("1 bloc /");
		else speed.append(main.wbSpeed + " blocs /");
		
		if (main.wbSecond == 1) speed.append("s");
		else speed.append(" " + main.wbSecond + "s");
		
		inv.setItem(7, getItem(Material.QUARTZ, "§eVitesse : §6" + speed.toString(), 1, true, true));
		inv.setItem(8, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private void InvConfigMapBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§6Gestion Wb :§e Bordure Départ");
		
		for (int i = 0; i < 9; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		for (int i = 18; i < 27; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		List<String> lore = Arrays.asList("§eBordure Initiale : §6" + main.Map + " x " + main.Map);
		
		inv.setItem(9, getItem(Material.BANNER, "§c-100", 1, (byte) 1, false, lore));
		inv.setItem(10, getItem(Material.BANNER, "§c-25", 1, (byte) 1, false, lore));
		inv.setItem(11, getItem(Material.BANNER, "§c-10", 1, (byte) 1, false, lore));
		inv.setItem(12, getItem(Material.BANNER, "§c-1", 1, (byte) 1, false, lore));
		
		inv.setItem(13, getItem(Material.SIGN, "§eBordure Initiale : §6" + main.Map + " x " + main.Map, 1, false, Arrays.asList("§bCliquez pour choisir une valeur")));
		
		inv.setItem(14, getItem(Material.BANNER, "§a+1", 1, (byte) 2, false, lore));
		inv.setItem(15, getItem(Material.BANNER, "§a+10", 1, (byte) 2, false, lore));
		inv.setItem(16, getItem(Material.BANNER, "§a+25", 1, (byte) 2, false, lore));
		inv.setItem(17, getItem(Material.BANNER, "§a+100", 1, (byte) 2, false, lore));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private void InvConfigWbTimeBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§6Gestion Wb :§e Temps Bordure");
		
		for (int i = 0; i < 9; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		for (int i = 18; i < 27; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		List<String> lore = Arrays.asList("§eActivation de la WorldBorder : §6" + main.wbTime + " min");
		
		inv.setItem(9, getItem(Material.BANNER, "§c-50", 1, (byte) 1, false, lore));
		inv.setItem(10, getItem(Material.BANNER, "§c-25", 1, (byte) 1, false, lore));
		inv.setItem(11, getItem(Material.BANNER, "§c-10", 1, (byte) 1, false, lore));
		inv.setItem(12, getItem(Material.BANNER, "§c-1", 1, (byte) 1, false, lore));
		
		inv.setItem(13, getItem(Material.SIGN, "§eActivation de la WorldBorder : §6" + main.wbTime + " min", 1, false, Arrays.asList("§bCliquez pour choisir une valeur")));
		
		inv.setItem(14, getItem(Material.BANNER, "§a+1", 1, (byte) 2, false, lore));
		inv.setItem(15, getItem(Material.BANNER, "§a+10", 1, (byte) 2, false, lore));
		inv.setItem(16, getItem(Material.BANNER, "§a+25", 1, (byte) 2, false, lore));
		inv.setItem(17, getItem(Material.BANNER, "§a+50", 1, (byte) 2, false, lore));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private void InvConfigSpeedBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 9, "§6Gestion Wb :§e Vitesse Bordure");
		
		inv.setItem(0, getConfigItemSmall(Material.BRICK, "§eBlocs par cycle : §6" + main.wbSpeed, main.wbSpeed, true, 1));
		inv.setItem(1, getConfigItemSmall(Material.WATCH, "§eIntervalle cycle : §6" + main.wbSecond + "s", main.wbSecond, true, 1));
		
		inv.setItem(8, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		p.openInventory(inv);
	}
	
	private void InvConfigWbMaxBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§6Gestion Wb :§e Bordure Finale");
		
		for (int i = 0; i < 9; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		for (int i = 18; i < 27; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		List<String> lore = Arrays.asList("§eBordure Finale : §6" + main.wbMax + " x " + main.wbMax);
		
		inv.setItem(9, getItem(Material.BANNER, "§c-100", 1, (byte) 1, false, lore));
		inv.setItem(10, getItem(Material.BANNER, "§c-25", 1, (byte) 1, false, lore));
		inv.setItem(11, getItem(Material.BANNER, "§c-10", 1, (byte) 1, false, lore));
		inv.setItem(12, getItem(Material.BANNER, "§c-1", 1, (byte) 1, false, lore));
		
		inv.setItem(13, getItem(Material.SIGN, "§eBordure Finale : §6" + main.wbMax + " x " + main.wbMax, 1, false, Arrays.asList("§bCliquez pour choisir une valeur")));
		
		inv.setItem(14, getItem(Material.BANNER, "§a+1", 1, (byte) 2, false, lore));
		inv.setItem(15, getItem(Material.BANNER, "§a+10", 1, (byte) 2, false, lore));
		inv.setItem(16, getItem(Material.BANNER, "§a+25", 1, (byte) 2, false, lore));
		inv.setItem(17, getItem(Material.BANNER, "§a+100", 1, (byte) 2, false, lore));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	//GESTIONS DES JOUEURS ////////////////////////
	private void InvConfigPlBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 9, "§5Menu: §6Gestion Joueurs");
		
		inv.setItem(0, getItem(Material.ENDER_CHEST, "§6Staff", 1, true));
		inv.setItem(1, getItem(Material.EYE_OF_ENDER, "§aSpectateurs", 1, true));
		
		inv.setItem(7, getStatIcon());
		inv.setItem(8, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private ItemStack getStatIcon() {
		ItemStack item = new ItemStack(Material.BOOK);
		ItemMeta itemM = item.getItemMeta();
		
		itemM.setDisplayName("§2Statistiques");
		
		if (main.stats) {
			itemM.setLore(Arrays.asList("Etat : §a[Activé]"));
		} else {
			itemM.setLore(Arrays.asList("Etat : §c[Désactivé]"));
		}
		
		item.setItemMeta(itemM);
		
		return item;
	}
	
	//GESTIONS DU TEMPS ////////////////////////
	private void InvConfigTimeBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 9, "§5Menu: §6Gestion Temps");
		
		inv.setItem(0, getItem(Material.BOOK_AND_QUILL, "§9Duré d'un épisode : §3" + main.epTime + " §9min" , main.epTime, true));
		inv.setItem(1, getConfigItem(Material.WATCH, "§9LG DailyCycle §f(x2)§9 : " + getString(main.DailyCycle), main.DailyCycle,  true));
		inv.setItem(2, getConfigItemSmall(Material.DIAMOND_SWORD, "§9Activation Pvp : §3" + main.pvp + " §9min", main.pvp, true, 1));
		inv.setItem(3, getConfigItemSmall(Material.BOOKSHELF, "§9Annonce des rôles : §3" + main.announceRole + " §9min", main.announceRole, true, 1));
		inv.setItem(4, getConfigItemSmall(Material.PAPER, "§9Episode vote : §3" + main.voteEp, main.voteEp, true, 1));
		
		inv.setItem(8, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	//GESTIONS DES STUFFS ////////////////////////
	private void InvConfigStuffBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 9, "§5Menu: §6Gestion Stuff");
		
		inv.setItem(0, getItem(Material.CHEST, "§aStuff de départ", 1, true));
		inv.setItem(1, getItem(Material.ENDER_CHEST, "§aStuff à la mort", 1, true));
		
		inv.setItem(8, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private void InvStartItemBuilder(Player p) {
		Inventory inv = Bukkit.createInventory(null, 45, "§6Gestion Stuff : §aStart");
		
		if (main.startItem != null) inv.setContents(main.startItem);
		
		for (int i = 40; i < 43; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		inv.setItem(43, getItem(Material.REDSTONE_TORCH_ON, "§2Sauvegarder ?", 1, false));
		inv.setItem(44, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		p.openInventory(inv);
	}
	
	public void InvDeathItemBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§6Gestion Stuff : §aDeath");

		for (int i = 18; i < 27; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		if (main.deathItem != null) inv.setContents(main.deathItem);
		
		inv.setItem(25, getItem(Material.REDSTONE_TORCH_ON, "§2Sauvegarder ?", 1, false));
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	//ITEMS -----------------------------
	private ItemStack vitre(int color) {
		
		ItemStack vitre = new ItemStack(Material.STAINED_GLASS_PANE, 1 , (byte) color);
		ItemMeta vitreM = vitre.getItemMeta();
		
		vitreM.setDisplayName(" ");
		vitre.setItemMeta(vitreM);
		
		return vitre;
	}
	
	private ItemStack getItem(Material material, String Name, int number, boolean enchant) {
		
		ItemStack Item = new ItemStack(material, number);
		ItemMeta ItemM = Item.getItemMeta();
		
		if (enchant) {
			ItemM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		}
		ItemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}
	
	private ItemStack getItem(Material material, String Name, int number, Boolean enchant, List<String> lore) {
		
		ItemStack Item = new ItemStack(material, number);
		ItemMeta ItemM = Item.getItemMeta();
		
		if (enchant) {
			ItemM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		}
		
		ItemM.setLore(lore);
		ItemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}
	
	private ItemStack getItem(Material material, String Name, int number, Byte bit, boolean enchant) {
		
		ItemStack Item = new ItemStack(material, number, bit);
		ItemMeta ItemM = Item.getItemMeta();
		
		if (enchant) {
			ItemM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		}
		ItemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}
	
	private ItemStack getItem(Material material, String Name, int number, Byte bit, boolean enchant, List<String> lore) {
		
		ItemStack Item = new ItemStack(material, number, bit);
		ItemMeta ItemM = Item.getItemMeta();
		
		if (enchant) {
			ItemM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		}
		
		ItemM.setLore(lore);
		ItemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}
	
	private ItemStack getItem(Material material, String Name, int number, boolean enchant, boolean lore) {
		
		ItemStack Item = new ItemStack(material, number);
		ItemMeta ItemM = Item.getItemMeta();
		
		if (enchant) {
			ItemM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		}
		
		if (lore) {
			ItemM.setLore(Arrays.asList("§bCliquez pour configurer"));
		}
		ItemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}
	
	private ItemStack getItem(Material material, String Name, int number, Byte bit, boolean enchant, boolean lore) {
		
		ItemStack Item = new ItemStack(material, number, bit);
		ItemMeta ItemM = Item.getItemMeta();
		
		if (enchant) {
			ItemM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		}
		
		if (lore) {
			ItemM.setLore(Arrays.asList("§bCliquez pour configurer"));
		}
		ItemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}
	
	private ItemStack getWool(String name, boolean on) {
		
		if (on) {
			ItemStack Item = new ItemStack(Material.WOOL, 1, (byte) 5);
			ItemMeta ItemM = Item.getItemMeta();
			
			ItemM.setDisplayName("§aActivé");
			if (name != null) {
				ItemM.setLore(Arrays.asList("§a[ON]"));
				ItemM.setDisplayName(name);
			}
			
			Item.setItemMeta(ItemM);
			return Item;
		}
		else {
			ItemStack Item = new ItemStack(Material.WOOL, 1, (byte) 14);
			ItemMeta ItemM = Item.getItemMeta();
			
			ItemM.setDisplayName("§cDésactivé");
			if (name != null) {
				ItemM.setLore(Arrays.asList("§c[OFF]"));
				ItemM.setDisplayName(name);
			}
			
			Item.setItemMeta(ItemM);
			return Item;
		}
	}
	
	private ItemStack getWool(String name, int on, Boolean number) {
		
		if (on != 0) {
			ItemStack Item = new ItemStack(Material.WOOL, 1, (byte) 5);
			if (number) Item = new ItemStack(Material.WOOL, on, (byte) 5);
			
			ItemMeta ItemM = Item.getItemMeta();
			
			ItemM.setDisplayName("§aActivé");
			if (name != null) {
				ItemM.setLore(Arrays.asList("§a[ON]"));
				ItemM.setDisplayName(name);
			}
			
			Item.setItemMeta(ItemM);
			return Item;
		}
		else {
			ItemStack Item = new ItemStack(Material.WOOL, 1, (byte) 14);
			
			ItemMeta ItemM = Item.getItemMeta();
			
			ItemM.setDisplayName("§cDésactivé");
			if (name != null) {
				ItemM.setLore(Arrays.asList("§c[OFF]"));
				ItemM.setDisplayName(name);
			}
			
			Item.setItemMeta(ItemM);
			return Item;
		}
	}
	
	private ItemStack getConfigItemSmall(Material material, String Name, int number, Boolean enchant, Integer increment) {
		
		ItemStack Item = new ItemStack(material, number);
		
		if (number <= 0) Item = getWool(null, false);
		
		ItemMeta ItemM = Item.getItemMeta();
		
		if (enchant) {
			ItemM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		}
		
		ItemM.setLore(Arrays.asList("§cClick gauche :§f +" + increment, "§cClick droit :§f -" + increment));
		
		ItemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}
	
	private ItemStack getConfigItem(Material material, String Name, int number, Boolean enchant, Integer increment) {
		
		ItemStack Item = new ItemStack(material);
		
		if (number <= 0) Item = getWool(null, false);
		
		ItemMeta ItemM = Item.getItemMeta();
		
		if (enchant) {
			ItemM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		}
		
		ItemM.setLore(Arrays.asList("§cClick gauche :§f +" + increment, "§cClick droit :§f -" + increment));
		
		ItemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}
	
	private ItemStack getConfigItem(Material material, String Name, Boolean on, Boolean enchant) {
		
		ItemStack Item = new ItemStack(material);
		
		if (!on) Item = getWool(null, false);
		
		ItemMeta ItemM = Item.getItemMeta();
		
		if (enchant) {
			ItemM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		}
		
		ItemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}
	
	private Boolean canEquipArmor(Player player) {
		
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
	
	private Boolean isFullArmor(Player player) {
		if (player.getInventory().getHelmet() == null) return false;
		if (player.getInventory().getChestplate() == null) return false;
		if (player.getInventory().getLeggings() == null) return false;
		if (player.getInventory().getBoots() == null) return false;
		
		return true;
	}
}
