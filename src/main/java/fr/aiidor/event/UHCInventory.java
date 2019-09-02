package fr.aiidor.event;

import java.util.Arrays;

import org.bukkit.Bukkit;
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
import fr.aiidor.game.UHCState;
import fr.aiidor.role.LGRoles;
import fr.aiidor.task.UHCStart;

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
		
		if (main.FireEnchantLess) {
			if (clicked.getEnchantments().containsKey(Enchantment.FIRE_ASPECT)) {
				clicked.removeEnchantment(Enchantment.FIRE_ASPECT);
			}
			
			if (clicked.getEnchantments().containsKey(Enchantment.ARROW_FIRE)) {
				clicked.removeEnchantment(Enchantment.ARROW_FIRE);
			}
		}
		
		if (main.LimiteDePiece > 0) {
			if (e.getInventory().getHolder() == player) {
				
				if ((clicked.getType() == Material.DIAMOND_HELMET || clicked.getType() == Material.DIAMOND_CHESTPLATE ||
						clicked.getType() == Material.DIAMOND_LEGGINGS || clicked.getType() == Material.DIAMOND_BOOTS) && e.getSlotType() != SlotType.ARMOR) {
							
						if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
							if (!canEquipArmor(player)) {
								player.sendMessage(" ");
								player.sendMessage(main.gameTag + "§cVous ne pouvez pas vous équiper avec plus de pièce en diamant !");
								e.setCancelled(true);
							}
						}
						return;
					}
				
				if ((e.getSlotType() == SlotType.ARMOR) && e.getCursor() != null) {
					
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
		
		if (inv.getName().equalsIgnoreCase("§dComposition :")) {
			e.setCancelled(true);
			return;
		}
		
		if (inv.getName().equalsIgnoreCase("§5Menu: §dConfiguration")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				player.closeInventory();
				return;
			}
			
			if (clicked.getType() == Material.SLIME_BALL) {
				 if (main.isState(UHCState.WAITING)) {
					 if (main.getPlayers().size() >= main.PlayerMin) {
						 
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
			
			if (clicked.getType() == Material.REDSTONE_BLOCK) {
				main.cancelStart = true;
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
			
			if (clicked.getType() == Material.BEACON) {
				InvConfigWbBuilder(player);
				return;
			}
			if (clicked.getType() == Material.WATCH) {
				InvConfigTimeBuilder(player);
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
				
				if (main.HasteyBoys) main.HasteyBoys = false;
				else main.HasteyBoys = true;
				invRunBuiler(player);
				return;
			}
			
			if (e.getSlot() == 22) {
				
				if (main.bleedingSweet) main.bleedingSweet = false;
				else main.bleedingSweet = true;
				invRunBuiler(player);
				return;
			}
			
			if (e.getSlot() == 23) {
				
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
			
			return;
		}
		
		//LIMITE DE STUFF
		if (inv.getName().equalsIgnoreCase("§6Scénarios : §bLimite de Stuff")) { 
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigBuilder(player);
				return;
			}
			
			
			if (e.getSlot() == 18) {
				
				if (main.FireEnchantLess) main.FireEnchantLess = false;
				else main.FireEnchantLess = true;
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
				
				if (main.diamondLess) main.diamondLess  = false;
				else main.diamondLess  = true;
				invLimitBuiler(player);
				return;
			}
			
			if (e.getSlot() == 24) {
				
				if (main.oreLess) main.oreLess  = false;
				else main.oreLess  = true;
				invLimitBuiler(player);
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
					if (main.Abso < 2) {
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
				
				if (main.xpNerf) main.xpNerf = false;
				else main.xpNerf = true;
				invVanillaBuilder(player);
				return;
			}
			
			if (e.getSlot() == 21) {
				
				if (main.infiniteEnc) main.infiniteEnc = false;
				else main.infiniteEnc = true;
				invVanillaBuilder(player);
				return;
			}
			
			if (e.getSlot() == 22) {
				
				if (main.noHunger) main.noHunger = false;
				else main.noHunger = true;
				invVanillaBuilder(player);
				return;
			}
			
		}
		
		if (inv.getName().equalsIgnoreCase("§6Scénarios : §ePVE")) { 
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				InvConfigBuilder(player);
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
				for (LGRoles role : LGRoles.values()) {
					if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase("§6"+role.name)) {
						
						if (role == LGRoles.Soeur) {
							if (role.number == 0) role.number = 2;
							else role.number = 0;
							
						} else {
							if (role.number == 0) role.number = 1;
							else role.number = 0;
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
		
		//GESTIONS DE LA WORLDBORDER ////////////////////////
		if (inv.getName().equalsIgnoreCase("§5Menu: §6Gestion WorldBorder")) {
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				main.configInvBuilder(player);
				return;
			}
			
			if (e.getSlot() == 0) {
				if (e.getClick() == ClickType.LEFT) {
					if (main.Map < 10000) {
						main.Map = main.Map + 100;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.Map > 100) {
						main.Map = main.Map - 100;
					}
				}
				
				InvConfigWbBuilder(player);
				return;
			}
			if (e.getSlot() == 1) {
				if (e.getClick() == ClickType.LEFT) {
					if (main.wbMax < 1000) {
						main.wbMax = main.wbMax + 50;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.wbMax > 50) {
						main.wbMax = main.wbMax - 50;
					}
				}
				
				InvConfigWbBuilder(player);
				return;
			}
			
			if (e.getSlot() == 2) {
				if (e.getClick() == ClickType.LEFT) {
					if (main.wbEP < 10) {
						main.wbEP ++;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.wbEP > 1) {
						main.wbEP --;
					}
				}
				
				InvConfigWbBuilder(player);
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
				if (e.getClick() == ClickType.LEFT) {
					if (main.epTime < 60) {
						main.epTime ++;
					}
				}
				if (e.getClick() == ClickType.RIGHT) {
					if (main.epTime > 1) {
						main.epTime --;
					}
				}
				
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
		
		inv.setItem(4, getItem(Material.SKULL_ITEM, "§ePVE", 1, true, true));
		
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
		
		signM.setLore(Arrays.asList("", "§7>> §fFireEnchantLess : " + getString(main.FireEnchantLess), "§7>> §fRodLess: " + getString(main.RodLess),
				"", "§7>> §fDiamondLimit : " + diamond,  "§7>> §fPiece en diams : " + stuff, "§7>> §fBloodDiamond: " + getString(main.BloodDiamond), 
				"", "§7>> §fDiamondLess: " + getString(main.diamondLess), "§7>> §fOreLess: " + getString(main.oreLess)));
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
		+ getString(main.GoldenHead), "§7>> §fGolden Head Heal :§a " + main.HeadHeal + " Ceours", "","§7>> §fFinal Heal : " +  getString(main.FinalHeal), 
		"§7>> §fCupid : " +  getString(main.Cupid)));
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
		
		inv.setItem(3, getItem(Material.DIAMOND_PICKAXE, "§dHastey Boys", 1, true));
		inv.setItem(21, getWool(null , main.HasteyBoys));
		
		inv.setItem(4, getItem(Material.DIAMOND, "§dBleeding Sweet", 1, true));
		inv.setItem(22, getWool(null , main.bleedingSweet));
		
		inv.setItem(5, getItem(Material.QUARTZ, "§dSpeedy Miner", 1, true));
		inv.setItem(23, getConfigItemSmall(Material.EMERALD, "§bDernier Episode avec SpeedyMiner : §6" + main.SpeedyMiner, main.SpeedyMiner, true));
		
		p.openInventory(inv);
	}
	
	private void invLimitBuiler(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§6Scénarios : §bLimite de Stuff");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(3));
		}
		
		inv.setItem(0, getItem(Material.FIREBALL, "§6FireEnchantLess", 1, true));
		
		inv.setItem(18, getWool(null, main.FireEnchantLess));
		
		inv.setItem(1, getItem(Material.DIAMOND_CHESTPLATE, "§6Limite de piece en diamant", 1, true));
		inv.setItem(19, getConfigItemSmall(Material.DIAMOND, "§cLimite : §3" + main.LimiteDePiece, main.LimiteDePiece, true));
		
		inv.setItem(2, getItem(Material.INK_SACK, "§bBlood Diamond", 1, (byte)1, true));
		inv.setItem(20, getWool(null, main.BloodDiamond));
		
		inv.setItem(3, getItem(Material.DIAMOND_ORE, "§bDiamondLimit", 1, true));
		inv.setItem(21, getConfigItemSmall(Material.DIAMOND, "§cLimite : §3" + main.diamondlimit, main.diamondlimit, true));
		
		inv.setItem(4, getItem(Material.FISHING_ROD, "§6RodLess", 1, true));
		inv.setItem(22, getWool(null, main.RodLess ));
		
		inv.setItem(5, getItem(Material.DIAMOND_BLOCK, "§bDiamondLess", 1, true));
		inv.setItem(23, getWool(null, main.diamondLess ));
		
		inv.setItem(6, getItem(Material.GOLD_ORE, "§bOreLess", 1, true));
		inv.setItem(24, getWool(null, main.oreLess ));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private void invHealingBuiler(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§6Scénarios : §dHealing Info");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(6));
		}
		
		inv.setItem(0, getItem(Material.GOLD_BLOCK, "§aCoeurs d'absorbtion", 1, true));
		inv.setItem(18, getConfigItemSmall(Material.GOLD_NUGGET, "§dCoeurs : §3" + main.Abso, main.Abso, true));
		
		inv.setItem(1, getItem(Material.GOLDEN_APPLE, "§ePomme de notch", 1, (byte) 1, true));
		inv.setItem(19, getWool(null, main.Notch));
		
		inv.setItem(2, getItem(Material.SKULL_ITEM, "§eGolden Head", 1, (byte) 3, true));
		inv.setItem(20, getWool(null, main.GoldenHead));
		
		inv.setItem(3, getItem(Material.BREAD, "§dGolden Head Heal", 1, true));
		inv.setItem(21, getConfigItemSmall(Material.GOLD_NUGGET, "§dCoeurs : §3" + main.HeadHeal, main.HeadHeal, true));
		
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
		inv.setItem(18, getConfigItem(Material.REDSTONE, "§aTaux de drop : §2" + main.flint + "%", main.flint, true));
		
		inv.setItem(1, getItem(Material.APPLE, "§aDrop de Pommes", 1, true));
		inv.setItem(19, getConfigItem(Material.REDSTONE, "§aTaux de drop : §2" + main.apples + "%", main.apples, true));
		
		inv.setItem(2, getItem(Material.EXP_BOTTLE, "§aXp Nerf", 1, true));
		inv.setItem(20, getWool(null, main.xpNerf));
		
		inv.setItem(3, getItem(Material.ENCHANTMENT_TABLE, "§aInfinite Enchanter", 1, true));
		inv.setItem(21, getWool(null, main.infiniteEnc));
		
		inv.setItem(4, getItem(Material.COOKED_BEEF, "§aNo Food", 1, true));
		inv.setItem(22, getWool(null, main.noHunger));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	private void invPveBuilder(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, "§6Scénarios : §ePVE");
		
		for (int i = 9; i < 18; i ++) {
			inv.setItem(i, vitre(4));
		}
		
		
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
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	//GESTIONS DES ROLES ////////////////////////////////
	private void invConfigRolesBuiler(Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, "§5Menu: §6Rôles");
		
		inv.setItem(0, getItem(Material.EXP_BOTTLE, "§2Simple Villageois", 1, true));
		inv.setItem(1, getConfigItemSmall(Material.BONE, "§4Loup-Garou", LGRoles.LG.number, true));
		
		int slot = 2;
		for (LGRoles role : LGRoles.values()) {
			if (role != LGRoles.SV && role != LGRoles.LG) {
				if (role == LGRoles.Valentin) break;
				
				inv.setItem(slot, getWool("§6"+role.name, role.number));
				slot++;
				
			}
		}
		
		inv.setItem(35, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		p.openInventory(inv);
	}
	
	private void invConfigRolesBuilerSSS(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, "§5Menu: §6Rôles");
		
		inv.setItem(0, getWool("§6"+LGRoles.Valentin.name, LGRoles.Valentin.number));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		p.openInventory(inv);
	}

	//GESTIONS DE LA WORLDBORDER ////////////////////////
	private void InvConfigWbBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 9, "§5Menu: §6Gestion WorldBorder");
		
		inv.setItem(0, getConfigItem(Material.MAP, "§eBordure Initial : §6" + main.Map + " x " + main.Map, main.Map,  true));
		inv.setItem(1, getConfigItem(Material.GLASS, "§eBordure Finale : §6" + main.wbMax + " x " + main.wbMax, main.Map,  true));
		inv.setItem(2, getConfigItemSmall(Material.COMPASS, "§eEpisode WorldBorder : §6" + main.wbEP, main.wbEP, true));
		
		inv.setItem(8, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
		
		p.openInventory(inv);
	}
	
	//GESTIONS DU TEMPS ////////////////////////
	private void InvConfigTimeBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 9, "§5Menu: §6Gestion Temps");
		
		inv.setItem(0, getConfigItemSmall(Material.BOOK_AND_QUILL, "§9Duré d'un épisode : §3" + main.epTime + " §9Min", main.epTime,  true));
		inv.setItem(1, getConfigItem(Material.WATCH, "§9LG DailyCycle §f(x2)§9 : " + getString(main.DailyCycle), main.DailyCycle,  true));
		inv.setItem(2, getConfigItemSmall(Material.DIAMOND_SWORD, "§9Activation Pvp : §3" + main.pvp + " §9Min", main.pvp, true));
		inv.setItem(3, getConfigItemSmall(Material.BOOKSHELF, "§9Annonce des rôles : §3" + main.announceRole + " §9Min", main.announceRole, true));
		
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
	
	private static ItemStack getItem(Material material, String Name, int number, boolean enchant) {
		
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
	
	private static ItemStack getItem(Material material, String Name, int number, Byte bit, boolean enchant) {
		
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
	
	private static ItemStack getItem(Material material, String Name, int number, boolean enchant, boolean lore) {
		
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
	
	private static ItemStack getItem(Material material, String Name, int number, Byte bit, boolean enchant, boolean lore) {
		
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
	
	private ItemStack getWool(String name, int on) {
		
		if (on != 0) {
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
	
	private ItemStack getConfigItemSmall(Material material, String Name, int number, Boolean enchant) {
		
		ItemStack Item = new ItemStack(material, number);
		
		if (number <= 0) Item = getWool(null, false);
		
		ItemMeta ItemM = Item.getItemMeta();
		
		if (enchant) {
			ItemM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		}
		
		ItemM.setLore(Arrays.asList("§cClick gauche :§f +1", "§cClick droit :§f -1"));
		
		ItemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}
	
	private ItemStack getConfigItem(Material material, String Name, int number, Boolean enchant) {
		
		ItemStack Item = new ItemStack(material);
		
		if (number <= 0) Item = getWool(null, false);
		
		ItemMeta ItemM = Item.getItemMeta();
		
		if (enchant) {
			ItemM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		}
		
		ItemM.setLore(Arrays.asList("§cClick gauche :§f +1", "§cClick droit :§f -1"));
		
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
}
