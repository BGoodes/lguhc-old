package fr.aiidor.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;
import fr.aiidor.effect.Sounds;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;
import fr.aiidor.role.use.LGRole_Chasseur;
import fr.aiidor.role.use.LGRole_Soeur;
import fr.aiidor.role.use.LGRole_Sorciere;
import net.minecraft.server.v1_8_R3.EntityLiving;

public class LGDeath extends BukkitRunnable {
	
	private int timer = 12;
	
	private Location grave;
	
	private LGUHC main;
	
	private UUID dead;
	private List<ItemStack> inv;
	private UUID killer;
	
	public LGDeath(LGUHC main, UUID dead, UUID killer, Location grave, List<ItemStack> inv) {
		
		this.main = main;
		this.dead = dead;
		this.grave = grave;
		this.inv = inv;
		
		if (killer == null) this.killer = null;
		else this.killer = killer;
		
		main.respawnInstant(Bukkit.getPlayer(dead));
	}

	
	@Override
	public void run() {
		
		if (main.getPlayer(dead).isDead()) {
			cancel();
			return;
		}
		
		timer --;
		
		if (timer == 6) {
			
			Joueur j = main.getPlayer(dead);
			//SORCIERE
			j.setDyingState(LGCamps.Village);
			if (!j.isRea()) {
				new LGRole_Sorciere(main).ReaMsg(j.getPlayer());
				
			} else {
				//INFECT
				revive(j, true);
				cancel();
				return;
			}
		}
		//TIMER == 0
		if (timer == 0) {
			Joueur j = main.getPlayer(dead);
			if (j.isRea()) {
				revive(j, false);
				cancel();
				return;
			}
			
			death(dead, grave, inv);
			cancel();
        }
	}
	
	
	private void death(UUID dead, Location grave, List<ItemStack> inv) {
		Joueur j = main.getPlayer(dead);
		
		//LOOT DE STUFF
		if (!inv.isEmpty()) {
			for (ItemStack item : inv) {
				if (item.getType() != Material.AIR) grave.getWorld().dropItem(grave, item);
			}
		}
		
		if (main.run) {
			if (main.bleedingSweet) {
				grave.getWorld().dropItemNaturally(grave, (new ItemStack(Material.DIAMOND)));
				grave.getWorld().dropItemNaturally(grave, (new ItemStack(Material.GOLD_INGOT, 5)));
				grave.getWorld().dropItemNaturally(grave, (new ItemStack(Material.ARROW, 16)));
				grave.getWorld().dropItemNaturally(grave, (new ItemStack(Material.STRING)));
			}
		}
		
		if (main.GoldenHead) {
			
		    ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		    SkullMeta meta = (SkullMeta)head.getItemMeta();
		    
		    meta.setOwner(j.getName());
		    meta.setDisplayName("§6Tête de §c" + Bukkit.getPlayer(dead).getName());
		    meta.setLore(Arrays.asList(new String[] { "§bCette tête a été récupéré ", "§bsur un joueur" }));
		    
		    head.setItemMeta(meta);
		}
		
		if (main.deathItem != null) {
			int slot = 0;
			for (ItemStack it : main.deathItem) {
				if (it != null) {
					grave.getWorld().dropItemNaturally(grave, it);
				}
				
				slot ++;
				
				if (slot == 18) break;
			}
			
			
		}
		
		
		j.setDead(true);
		main.Spectator.add(dead);
		
		main.death.add(j);
		
		if (main.PlayerInGame.contains(dead)) main.PlayerInGame.remove(dead);
		
		
		if (j.hasCouple() && j.getCouple().isDead()) {
			
			Bukkit.broadcastMessage("§c§l==========§4§k0§c§l==========");
			
			if (j.Rob) Bukkit.broadcastMessage("§2§l" + j.getName() + " à décidé de le rejoindre dans sa tombe §c♥ §2 il étais §o" + LGRoles.Voleur.name);
			else Bukkit.broadcastMessage("§2§l" + j.getName() + " §2à décidé de le rejoindre dans sa tombe §c♥ §2 il étais §o" + j.getRole().name);
			
			Bukkit.broadcastMessage("§c§l=====================");	
		}
		else {
			Bukkit.broadcastMessage("§c§l==========§4§k0§c§l==========");
			
			if (j.Rob) Bukkit.broadcastMessage("§2Le village à perdu un de ses membres : §l" + j.getName() + "§2 qui étais §o" + LGRoles.Voleur.name);
			else Bukkit.broadcastMessage("§2Le village à perdu un de ses membres : §l" + j.getName() + "§2 qui étais §o" + j.getRole().name);
			
			Bukkit.broadcastMessage("§c§l=====================");	
		}
		
		//HEAL + FEED
		if (j.isConnected()) {
			Player player = j.getPlayer();
			
			main.reset(player);	
			player.setGameMode(GameMode.SPECTATOR);
			
			if (j.Rob) player.setPlayerListName("§8[MORT] §7" + player.getName() + " §8(" + LGRoles.Voleur.name + ")"); 
			else player.setPlayerListName("§8[MORT] §7" + player.getName() + " §8(" + j.getRole().name + ")"); 
			
		    
			player.sendMessage("§6===============§4§k0§6===============");
			player.sendMessage("§bVous êtes spectateur : §9faites /spec §bpour accéder aux commandes des spectateurs !");
			player.sendMessage("§6===============§4§k0§6===============");
			player.sendMessage(" ");
		}

		
		for (Player pl : Bukkit.getOnlinePlayers()) {
	    	pl.playSound(pl.getLocation(), Sound.AMBIENCE_THUNDER, 8.0F, 0.8F);
	    }
		
		if (j.getRole() == LGRoles.Chasseur) {
			new LGRole_Chasseur(main).canPan(j);
		}
		
		if (j.getRole() == LGRoles.Trublion) {
			Bukkit.broadcastMessage(main.gameTag + "§cLe trublion à été tué ! Par conséquent, tous les joueurs ont été téléporté aléatoirement sur la map !");
			for (Joueur all : main.getJoueurs()) {
				if (!all.isDying()) {
					main.TpPower(all.getPlayer());
					new Sounds(all.getPlayer()).PlaySound(Sound.ENDERMAN_TELEPORT);
				}
			}
		}
	    
		if (main.compo.contains(LGRoles.EnfantS)) {
			for (Joueur es : main.getPlayerRolesOff(LGRoles.EnfantS)) {
				if (es.Model.equals(j)) {
					if (!es.isLg()) {
						
						main.addLg(es);
						
						if (es.isConnected()) {
							es.getPlayer().sendMessage(main.gameTag + "§4Votre modèle §e" + j.getName() + "§4 vous faites désormais partie du camp des Loups-Garous !");
							es.getPlayer().sendMessage(" ");
						}
					}
				}
			}
		}
		
		//KILLER -------------------------------------------
		if (killer != null) {
			
			if (main.isInGame(killer)) {
				for (Player p : main.getSpectator()) {
					p.sendMessage("§c[§6SPEC-INFO§c] §6Tué par " + main.getPlayer(killer).getName());
				} 
				
				Joueur k = main.getPlayer(killer);
				
				if (k.isConnected()) {
					j.Killer = k;
					
					if (!Bukkit.getOnlinePlayers().contains(j.getPlayer())) return;
					
					if (k.isLg()) {	
						if (k.getRole() == LGRoles.VPL) {
							k.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0, false, false));
						} else {
							
							k.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 0, false, false));
							k.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0, false, false));
							
						}
						
						EntityLiving cp = ((CraftPlayer)k.getPlayer()).getHandle();
						cp.setAbsorptionHearts(6);
					}
					
					if (k.getRole() == LGRoles.Assassin) {
						
						k.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 0, false, false));
						k.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0, false, false));
						
						EntityLiving cp = ((CraftPlayer)k.getPlayer()).getHandle();
						cp.setAbsorptionHearts(6);
					}
					
					//VOLEUR --------------------------------------
					if (k.getRole() == LGRoles.Voleur) {
						
						k.Rob = true;
						
						if (j.isLg()) {
							main.addLg(k);
						}
						
						k.setRole(j.getRole());
						k.setPower(j.getPower());
						k.setCamp(j.getCamp());
						
						k.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
						k.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0, false, false));
						
						k.getPlayer().sendMessage(main.gameTag + "§bVous avez fait votre première victime et récupérer donc le rôle et les pouvoirs de votre victime !");
						k.getPlayer().sendMessage("§bFaites /lg role pour en savoir plus !");
						k.getPlayer().sendMessage(" ");
						
						//VOTES ET HEALTH
						if (k.isVoteCible()) {
							k.setVoteCible(false);
							
						} else if (j.isVoteCible()) {
							
							double angeHeart = 0;
							if (j.getRole() == LGRoles.Ange) angeHeart = j.getPlayer().getHealth() - 10;
							
							k.getPlayer().setMaxHealth(j.getPlayer().getMaxHealth() * 2 + angeHeart);
							
						} else {
							k.getPlayer().setMaxHealth(j.getPlayer().getMaxHealth());
						}
						
						//LGA
						if (j.getRole() == LGRoles.LGA) {
							k.lglist = j.lglist;
							k.lglist.add(k);
						}
						//SOEUR
						if (j.getRole() == LGRoles.Soeur) {
							j.Soeur.Soeur = k;
							k.Soeur = j.Soeur;
							j.Soeur = null;
						}
						
						if (j.getRole() == LGRoles.EnfantS) {
							k.Model = j.Model;
						}
						
						//COUPLE
						if (j.hasCouple() && !k.hasCouple()) {
							j.getCouple().setCouple(k);
							j.setCouple(null);
							
							k.setCouple(k);
							
							k.getPlayer().sendMessage(main.gameTag + "§c♥§3 Vous êtes amoureux de §6" + k.getCouple().getName() + "§3 si il vient à mourrir, vous mourrerez aussitôt !");
						}
					}
				}
			}
				
		}
		
		if (j.getRole() == LGRoles.Soeur) {
			if (j.Soeur != null) {
				if (!j.Soeur.isDead()) {
					new LGRole_Soeur(main).canSeeMsg(j.Soeur);
				}
			}
		}
		
		if (j.hasCouple()) {
			if (j.getCouple().isConnected()) {
				if (!j.getCouple().isDead()) {
					death(j.getCouple().getUUID(), j.getCouple().getPlayer().getLocation(), inv(j.getCouple().getPlayer()));
				} else {
					
					j.getCouple().setDead(true);
					main.Spectator.add(j.getCouple().getUUID());
				}
			}

		}
		

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
	
	private void revive(Joueur Target, Boolean infect) {
		
		if (!Target.isConnected()) return;
		
		Target.setDyingState(null);
		Target.setRea(false);
		
		Player player = Target.getPlayer();
		Target.setDyingState(null);
		
		if (infect) {
			
			player.sendMessage(main.gameTag + "§aL'infect Père des Loups à décidé de vous ressusciter !");
			player.sendMessage(main.gameTag + "§4Vous faites désormais partie du camp des Loups-Garous mais gardez vos mêmes pouvoirs !");
			
			if (!Target.isLg()) {
				main.addLg(Target);
			}

			Target.setInfect(true);
			Target.setCamp(LGCamps.LoupGarou);
			
		} else {
			player.sendMessage(main.gameTag + "§aLa sorcière à décidé de vous ressusciter !");
		}
		
		if (!main.Spectator.contains(Target.getUUID())) main.Spectator.remove(Target.getUUID());
		
		Target.setNoFall(true);
		main.TpPower(player);
		
		for(PotionEffect effect:player.getActivePotionEffects()){player.removePotionEffect(effect.getType());}
		player.setGameMode(GameMode.SURVIVAL);
		
		player.setHealth(player.getMaxHealth());
	}
}
