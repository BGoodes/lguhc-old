package fr.aiidor;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.events.UHCListeners;
import fr.aiidor.roles.LGRoles;

public class LGconfig implements Listener{
	
	private static HashMap<Integer, LGRoles> roleS = new HashMap<>();
	
	@EventHandler
	public static void ItemClick(InventoryClickEvent e) {
		
		Inventory inventory = e.getInventory();
		
		if (e.getCurrentItem() == null) {
			return;
		}
		
		//MAIN /////////////////////////////////////////////////////////////////
		if (inventory.getName() == "§5Menu: §dConfiguration") {
			
			e.setCancelled(true);
			Player player = (Player) e.getWhoClicked();	
			ItemStack clicked = e.getCurrentItem();
				
			if (clicked.getType() == Material.BARRIER) {
				player.closeInventory();
			}
			
			//SCENARIOS -------------------------------------------------
			if (clicked.getType() == Material.DIAMOND_SWORD) {
				player.closeInventory();
				
				 
				Inventory inv = Bukkit.createInventory(null, 27, "§5Menu: §6Scénarios");
				
				for (int i = 9; i < 18; i ++) {
					inv.setItem(i, vitre(10));
				}
				
				//RUN
				inv.setItem(0, getItem(Material.GOLDEN_APPLE, "§2UHCRun", 1, true));
				inv.setItem(18, getWool(null, LGUHC.getInstance().Run));
				
				//DIAMOND LIMIT
				inv.setItem(7, getItem(Material.DIAMOND_ORE, "§bDiamondLimit", 1, true));
				inv.setItem(25, getItem(Material.DIAMOND, "§cLimite : §3" + UHCListeners.DiamondMax, UHCListeners.DiamondMax, UHCListeners.DiamondLimit));
				
				inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
				inv.setItem(8, getItem(Material.REDSTONE, "§2Actualiser", 1, false));
				
				player.openInventory(inv);
				return;
				
			}
			
			//ROLES    ////////////////////////////////////////////////////////////////////////////
			if (clicked.getType() == Material.BOOKSHELF) {
				player.closeInventory();
				
				Inventory inv = Bukkit.createInventory(null, 27, "§5Menu: §6Rôles");
				
				if (LGRoles.SV.number < 1) LGRoles.SV.number = 1;
				if (LGRoles.LG.number < 1) LGRoles.LG.number = 1;
				
				inv.setItem(0, getItem(Material.EXP_BOTTLE, "§2Simple Villageois", LGRoles.SV.number, true));
				inv.setItem(1, getItem(Material.RED_ROSE, "§cLoup-Garou", LGRoles.LG.number, true));
				
				int slot = 2;
				for (LGRoles role : LGRoles.values()) {
					
					if (role != LGRoles.SV && role != LGRoles.LG) {
						inv.setItem(slot, getRole(LGRoles.getName(role), role.number));
						
						roleS.put(slot, role);
						slot++;
					}
				}
				inv.setItem(26, getItem(Material.BARRIER, "§cRevenir en arrière", 1, false));
				player.openInventory(inv);
			}	
			return;
		}
		
		
		
		
		
		
		//SCENARIOS MENU -------------------------------------------------
		if (inventory.getName() == "§5Menu: §6Scénarios") {
			
			Player player = (Player) e.getWhoClicked();	
			ItemStack clicked = e.getCurrentItem();
			
			e.setCancelled(true);
			
			if (clicked.getType() == Material.BARRIER) {
				
				player.closeInventory();
				
				Inventory inv = Bukkit.createInventory(null, 27, "§5Menu: §dConfiguration");
				
				inv.setItem(11, getItemS(Material.DIAMOND_SWORD, "§6Scénarios"));
				inv.setItem(15, getItemS(Material.BOOKSHELF, "§6Rôles"));
				inv.setItem(26, getItemS(Material.BARRIER, "§cExit =>"));
				
				player.openInventory(inv);
				return;
			}
			
			if (clicked.getType() == Material.REDSTONE) {
				player.updateInventory();
				
				return;
			}
			
			if (e.getSlot() == 18) {
				
				if (LGUHC.getInstance().Run) LGUHC.getInstance().Run = false;
				else LGUHC.getInstance().Run = true;
				
				e.getInventory().setItem(0, getItem(Material.GOLDEN_APPLE, "§2UHCRun", 1, true));
				e.getInventory().setItem(18, getWool(null, LGUHC.getInstance().Run));
				
				player.updateInventory();
				return;
			}
			
			if (e.getSlot() == 25) {
				
				if (e.getClick() == ClickType.RIGHT) {
					
					if (UHCListeners.DiamondMax == 0) UHCListeners.DiamondMax = 32;
					else UHCListeners.DiamondMax --;
				}
				
				if (e.getClick() == ClickType.LEFT) {
					
					if (UHCListeners.DiamondMax == 32) UHCListeners.DiamondMax = 0;
					else UHCListeners.DiamondMax ++;
				}
				if (UHCListeners.DiamondMax == 0) UHCListeners.DiamondLimit = false;
				else UHCListeners.DiamondLimit = true;
				
				
				e.getInventory().setItem(7, getItem(Material.DIAMOND_ORE, "§bDiamondLimit", 1, true));
				e.getInventory().setItem(25, getItem(Material.DIAMOND, "§cLimite : §3" + UHCListeners.DiamondMax, UHCListeners.DiamondMax, UHCListeners.DiamondLimit));
				
				player.updateInventory();
				return;
			}
		}
		
		
		
		
		
		
		//ROLE MENU -------------------------------------------------
		if (inventory.getName() == "§5Menu: §6Rôles") {
			
			e.setCancelled(true);
			Player player = (Player) e.getWhoClicked();	
			
			//SV
			if (e.getSlot() == 0) {
				if (e.getClick() == ClickType.RIGHT) {
					LGRoles.SV.number --;
				}
				if (e.getClick() == ClickType.LEFT) {
					LGRoles.SV.number ++;
				}
				
				if (LGRoles.SV.number < 1) LGRoles.SV.number = 1;
				e.getInventory().setItem(0, getItem(Material.EXP_BOTTLE, "§2Simple Villageois", LGRoles.SV.number, true));
				
				player.updateInventory();
				return;
			}
			
			//LG
			if (e.getSlot() == 1) {
				if (e.getClick() == ClickType.RIGHT) {
					LGRoles.LG.number --;				
				}
				if (e.getClick() == ClickType.LEFT) {
					
					LGRoles.LG.number ++;
				}
				
				if (LGRoles.LG.number < 1) LGRoles.LG.number = 1;
				e.getInventory().setItem(1, getItem(Material.RED_ROSE, "§cLoup-Garou", LGRoles.LG.number, true));
				
				player.updateInventory();
				return;
			}
			
			ItemStack clicked = e.getCurrentItem();
			
			if (clicked.getType() == Material.WOOL) {
				LGRoles role = roleS.get(e.getSlot());
				
				if (role.number == 0) role.number = 1;
				else role.number = 0;
				
				e.getInventory().setItem(e.getSlot(), getRole(LGRoles.getName(role), role.number));
				player.updateInventory();
				return;
			}
			
			if (clicked.getType() == Material.BARRIER) {
				
				player.closeInventory();
				
				Inventory inv = Bukkit.createInventory(null, 27, "§5Menu: §dConfiguration");
				
				inv.setItem(11, getItemS(Material.DIAMOND_SWORD, "§6Scénarios"));
				inv.setItem(15, getItemS(Material.BOOKSHELF, "§6Rôles"));
				inv.setItem(26, getItemS(Material.BARRIER, "§cExit =>"));
				
				player.openInventory(inv);
				return;
			}
		}
		
		Player player = (Player) e.getWhoClicked();	
		ItemStack clicked = e.getCurrentItem();
		
		if (clicked.getType() == Material.WOOL) {
			LGRoles role = roleS.get(e.getSlot());
			
			if (role.number == 0) role.number = 1;
			else role.number = 0;
			
			e.getInventory().setItem(e.getSlot(), getRole(LGRoles.getName(role), role.number));
			player.updateInventory();
			return;
		}
		
		if (clicked.getType() == Material.BARRIER) {
			
			player.closeInventory();
			
			Inventory inv = Bukkit.createInventory(null, 27, "§5Menu: §dConfiguration");
			
			inv.setItem(11, getItemS(Material.DIAMOND_SWORD, "§6Scénarios"));
			inv.setItem(15, getItemS(Material.BOOKSHELF, "§6Rôles"));
			inv.setItem(26, getItemS(Material.BARRIER, "§cExit =>"));
			
			player.openInventory(inv);
			return;
		}
		
		
	}
	
	
	
	
	private static ItemStack vitre(int color) {
		
		ItemStack vitre = new ItemStack(Material.STAINED_GLASS_PANE, 1 , (byte) color);
		ItemMeta vitreM = vitre.getItemMeta();
		
		vitreM.setDisplayName("");
		vitre.setItemMeta(vitreM);
		
		return vitre;
	}
	
	private static ItemStack getItem(Material material, String Name, int number, boolean enchant) {
		
		ItemStack Item = new ItemStack(material, number);
		ItemMeta ItemM = Item.getItemMeta();
		
		if (enchant) {
			ItemM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		}
		
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}
	
	
	private static ItemStack getItemS(Material material, String Name) {
		
		ItemStack Item = new ItemStack(material);
		ItemMeta ItemM = Item.getItemMeta();
		
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}
	
	private static ItemStack getWool(String name, boolean on) {
		
		if (on) {
			ItemStack Item = new ItemStack(Material.WOOL, 1, (byte) 5);
			ItemMeta ItemM = Item.getItemMeta();
			
			ItemM.setDisplayName("§aActivé");
			if (name != null) {
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
				ItemM.setDisplayName(name);
			}
			
			Item.setItemMeta(ItemM);
			return Item;
		}
	}
	
	private static ItemStack getRole(String name, int on) {
		
		if (on == 1) {
			ItemStack Item = new ItemStack(Material.WOOL, 1, (byte) 5);
			ItemMeta ItemM = Item.getItemMeta();
			
			ItemM.setDisplayName(name);
			
			Item.setItemMeta(ItemM);
			return Item;
		}
		else {
			ItemStack Item = new ItemStack(Material.WOOL, 1, (byte) 14);
			ItemMeta ItemM = Item.getItemMeta();
			
			ItemM.setDisplayName(name);
			
			Item.setItemMeta(ItemM);
			return Item;
		}
	}
}
