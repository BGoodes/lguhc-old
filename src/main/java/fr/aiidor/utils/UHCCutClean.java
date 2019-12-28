package fr.aiidor.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.LGUHC;

public class UHCCutClean implements Listener {
	
	private LGUHC main;
	
	public UHCCutClean(LGUHC main) {
		this.main = main;
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void breakBlock(BlockBreakEvent e) {
		
		if (main.run == false) return;
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		
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
		
		if (main.veinMiner) {
			
			Player p = e.getPlayer();
		    
			if (p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR) {
				if (isMinerais(e.getBlock().getType())) {
					
					Vein vein = new Vein(e.getBlock());
					vein.process();
					vein.getDrops(p);
					
					e.setCancelled(true);
					
					return;
				}
			}
		}
		
		if (main.cutclean == false) return;
		
		Location loc = e.getBlock().getLocation();
		
		if (e.getBlock().getType() == Material.IRON_ORE) {
			if (main.oreLess && main.ironLess) return;
			
			Integer choose = new Random().nextInt(10);
			
			if (choose <= 7) {
				ExperienceOrb orb = (ExperienceOrb) loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
				orb.setExperience(1);
			}

			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			
			loc.getWorld().dropItem(loc.add(0.5, 0, 0.5), new ItemStack(Material.IRON_INGOT, 1));
			return;
		}
		
		if (e.getBlock().getType() == Material.GOLD_ORE) {
			if (main.oreLess && main.goldLess) return;
			
			ExperienceOrb orb = (ExperienceOrb) loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
			orb.setExperience(1);
			
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			
			loc.getWorld().dropItem(loc.add(0.5, 0, 0.5), new ItemStack(Material.GOLD_INGOT, 1));
			return;
		}
	}
	
	
	
	public Boolean isMinerais(Material mat) {
		
		if (mat == Material.IRON_ORE) return true;
		if (mat == Material.GOLD_ORE) return true;
		if (mat == Material.COAL_ORE) return true;
		if (mat == Material.DIAMOND_ORE) return true;
		if (mat == Material.REDSTONE_ORE) return true;
		if (mat == Material.LAPIS_ORE) return true;
		if (mat == Material.EMERALD_ORE) return true;
		if (mat == Material.QUARTZ_ORE) return true;
		if (mat == Material.GLOWING_REDSTONE_ORE || mat == Material.REDSTONE_ORE) return true;
		
		return false;
	}
	
	@EventHandler
	public void onMobDeath(EntityDeathEvent e) {
		
		if (main.run == false) return;
		if (main.cutclean == false) return;
			
		int index = 0;
		for (ItemStack drop : e.getDrops()) {
			if (drop != null && drop.getType() != Material.AIR) {
				
				if (drop.getType() == Material.RAW_BEEF) {
					e.getDrops().set(index, new ItemStack(Material.COOKED_BEEF, drop.getAmount()));
				}
				
				if (drop.getType() == Material.MUTTON) {
					e.getDrops().set(index, new ItemStack(Material.COOKED_MUTTON, drop.getAmount()));
				}
				
				if (drop.getType() == Material.PORK) {
					e.getDrops().set(index, new ItemStack(Material.GRILLED_PORK, drop.getAmount()));
				}
				
				if (drop.getType() == Material.RAW_CHICKEN) {
					e.getDrops().set(index, new ItemStack(Material.COOKED_CHICKEN, drop.getAmount()));
				}
				
				if (drop.getType() == Material.RABBIT) {
					e.getDrops().set(index, new ItemStack(Material.COOKED_RABBIT, drop.getAmount()));
				}
			}
			
			index++;
		}
	}
	
	
	//VEIN MINER
	private class Vein {
		
		public Block startBlock;
		public Material mat;
		public List<Location> drops = new ArrayList<>();
		private final BlockFace[] BLOCK_FACES = { BlockFace.DOWN, BlockFace.UP, BlockFace.SOUTH, BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST };
		
		public Vein(Block startBlock) {
			this.startBlock = startBlock;
			mat = startBlock.getType();
		}
		
		public void process() { getVeinBlocks(startBlock, mat, 2, 12); }
		 
		private void getVeinBlocks(Block block, Material type, int i, int maxBlocks) {
			 
			if (maxBlocks == 0)  return; 
			 if (block == null) return;
			 
			 //RESTONE
			 if (mat == Material.REDSTONE_ORE || mat == Material.GLOWING_REDSTONE_ORE) {
				 if (block.getType() == Material.REDSTONE_ORE || block.getType() == Material.GLOWING_REDSTONE_ORE) {
					 
					 drops.add(block.getLocation());
					 block.setType(Material.AIR);
					 i=2;	
					 
				 }
				 else {
					 i--;
				 }
			 }
			 //OTHER
			 else if (block.getType() == mat) {
				 
				 drops.add(block.getLocation());
				 block.setType(Material.AIR);
				 i=2;		
		       		
			 } else {
				 i--;
			 }
			 
			 if (i> 0) {
				 for (BlockFace face : BLOCK_FACES) {	
					 getVeinBlocks(block.getRelative(face), mat, i, maxBlocks - 1);
		        }
			 }
		 }
		 
		 public void getDrops(Player p) {
			 if (!drops.isEmpty()) {
				 for (Location loc : drops) {
					 spawnDrop(loc, p);
				 }
			 }
		 }
		 
		 private void spawnDrop(Location loc, Player p) {
			 
			 if (mat == Material.IRON_ORE && main.cutclean) {
						
				Integer choose = new Random().nextInt(10);
						
				if (choose <= 7) {
					xpDrop(1, p);
				}
						
				loc.getWorld().dropItem(loc.add(0.5, 0, 0.5), new ItemStack(Material.IRON_INGOT, 1));
				return;
			}
					
			if (mat == Material.GOLD_ORE && main.cutclean) {
					
				
				 xpDrop(1, p);
					
				loc.getWorld().dropItem(loc.add(0.5, 0, 0.5), new ItemStack(Material.GOLD_INGOT, 1));
				return;
			}
				
			 
			 if (mat == Material.DIAMOND_ORE) {
				 
				 
				if (main.BloodDiamond) {
					p.damage(1);
				}
				
				if (main.diamondlimit > 0) {
					
					if (main.DiamondPl.containsKey(p.getUniqueId())) {
						
						int diamond = main.DiamondPl.get(p.getUniqueId());
						main.DiamondPl.remove(p.getUniqueId());
						
						main.DiamondPl.put(p.getUniqueId(), diamond + 1);
					}
					
					else {
						main.DiamondPl.put(p.getUniqueId(), 1);
					}
					
					if (main.DiamondPl.get(p.getUniqueId()) > main.diamondlimit) {
						
						p.sendMessage(main.gameTag + "§cVous avez dépassé votre limite de Diamant !");
						return;
					}
				}
			 }
			 
			 
			 
			 if (mat == Material.QUARTZ_ORE || mat == Material.LAPIS_ORE ) {
				 xpDrop(2, 5, p);
				 if (mat == Material.QUARTZ_ORE) dropItem(Material.QUARTZ, loc);
				 if (mat == Material.LAPIS_ORE) dropItem(Material.INK_SACK, loc, 4, 9, (byte) 4);
			 }
			 
			 
			 else if (mat == Material.EMERALD_ORE || mat == Material.DIAMOND_ORE) {
				 xpDrop(3, 7, p);
				 if (mat == Material.EMERALD_ORE) dropItem(Material.EMERALD, loc);
				 if (mat == Material.DIAMOND_ORE) dropItem(Material.DIAMOND, loc);
			 }
			 
			 else if (mat == Material.REDSTONE_ORE || mat == Material.GLOWING_REDSTONE_ORE) {
				 xpDrop(1, 5, p);
				 dropItem(Material.REDSTONE, loc, 4, 5);
			 }
			 
			 else if (mat == Material.COAL_ORE) {
				 xpDrop(0, 2, p);
				 dropItem(Material.COAL, loc);
			 }
			 
			 else {
				 loc.getWorld().dropItem(loc.add(0.5, 0, 0.5), new ItemStack(mat, 1));
			 }

		 }
		 
		 private void dropItem(Material mat, Location loc, int min, int max) {
			 int drop = new Random().nextInt(max - min) + min;
			 
			 if (drop > 0) {
				 loc.getWorld().dropItem(loc.add(0.5, 0, 0.5), new ItemStack(mat, drop));
			 }
			
		 }
		 
		 private void dropItem(Material mat, Location loc, int min, int max, byte bit) {
			 int drop = new Random().nextInt(max - min) + min;
			 
			 if (drop > 0) {
				 loc.getWorld().dropItem(loc.add(0.5, 0, 0.5), new ItemStack(mat, drop, bit));
			 }
			 
		 }
		 
		 private void dropItem(Material mat, Location loc) {
			 loc.getWorld().dropItem(loc.add(0.5, 0, 0.5), new ItemStack(mat, 1));
		 }
		 
		 private void xpDrop(int min, int max, Player p) {
			 
			Integer choose = new Random().nextInt(max - min) + min;
				
				if (choose > 0) {
					p.giveExp(choose);
				}
		 }
		 
		 private void xpDrop(int value, Player p) {
			 p.giveExp(value);
		 }
		 
	}
}
