package fr.aiidor.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import fr.aiidor.LGUHC;
import fr.aiidor.game.UHCState;
import fr.aiidor.roles.LGRoleManager;
import fr.aiidor.scoreboard.ScoreboardSign;
import fr.aiidor.task.LGDeathDelay;
import fr.aiidor.task.UHCStart;

public class UHCJoin implements Listener {
	
	private boolean FriendsMode = true;
	private boolean automaticStart = false;
	
	@EventHandler
	public void Join(PlayerJoinEvent e) {
		
		Player player = e.getPlayer();
		LGUHC.getInstance().PlayerName.put(player.getUniqueId(), player.getName());
		player.setCollidable(false);
		
		ScoreboardSign scoreboard = new ScoreboardSign(player, "§fLGUHC");
		scoreboard.create();
		scoreboard.setLine(0, "§c ");
		scoreboard.setLine(1, "§2Partie en Attente !");
		LGUHC.getInstance().boards.put(player, scoreboard);
		
		player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(102.4D);
		
		if (UHCState.isState(UHCState.PREGAME)) {
			LGUHC.getInstance().PlayerInGame.add(player.getUniqueId());
			return;
			
		}
		
		//GAME EN COURS
		if (!(UHCState.isState(UHCState.WAIT) || UHCState.isState(UHCState.STARTING)) && !(LGUHC.getInstance().PlayerInGame.contains(player.getUniqueId()))) {
			
			if (LGUHC.getInstance().Spectator.contains(player.getUniqueId())) {
				return;
			}
			if (FriendsMode) {
				if (LGRoleManager.PlayerRole.containsKey(player.getUniqueId())) {
					
					LGUHC.getInstance().PlayerInGame.add(player.getUniqueId());
					e.setJoinMessage("§8[§a+§8] " + player.getName() + "§7(§e"+ Bukkit.getOnlinePlayers().size() + " §7/§e" + Bukkit.getMaxPlayers() +"§7)");
					return;
				}
			}
			if (LGUHC.getInstance().PlayerInGame.contains(player.getUniqueId())) {
				LGUHC.getInstance().PlayerInGame.remove(player.getUniqueId());
			}
			
			player.sendMessage("§b§l[§6§lUHC§b§l] §cLa partie à déjà commencé ! Vous allez être mis en spectateur !");
			player.setGameMode(GameMode.SPECTATOR);
			LGUHC.getInstance().Spectator.add(player.getUniqueId());
			
			e.setJoinMessage("§8[§a+§8] " + player.getName() + "§7(§e"+ Bukkit.getOnlinePlayers().size() + " §7/§e" + Bukkit.getMaxPlayers() +"§7) §b(Spectateur)");
			
			reset(player);
			return;
		}
		
		//AJOUTER JOUEUR
		if (!(LGUHC.getInstance().PlayerInGame.contains(player.getUniqueId()))) {
			LGUHC.getInstance().PlayerInGame.add(player.getUniqueId());
		}
		
		int size = Bukkit.getOnlinePlayers().size();
		e.setJoinMessage("§8[§a+§8] " + player.getName() + "§7(§e"+ size + " §7/§e" + Bukkit.getMaxPlayers() +"§7)");
		player.setGameMode(GameMode.ADVENTURE);
		
		//PREPARATION DU JOUEUR
		reset(player);
		player.teleport(new Location(player.getWorld(), 0, 151, 0));
		
		//STARTING
		
		if (automaticStart == false) {return;}
		if (UHCState.isState(UHCState.WAIT) && LGUHC.getInstance().PlayerInGame.size() >= LGUHC.getInstance().PlayerMin) {
			
			UHCStart start = new UHCStart(LGUHC.getInstance());
			start.runTaskTimer(LGUHC.getInstance(), 0, 20);
			UHCState.setState(UHCState.STARTING);
		}
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent e) {
		
		Player player = e.getPlayer();
		
		int size = Bukkit.getOnlinePlayers().size() - 1;
		e.setQuitMessage("§8[§c-§8] " + player.getName() + "§7(§e"+ size + " §7/§e" + Bukkit.getMaxPlayers() +"§7)");
		
		
		if (LGUHC.getInstance().PlayerInGame.contains(player.getUniqueId()) && !FriendsMode) {
			LGDeathDelay.death(player, player.getLocation(), null);
		}
		
		
		if (LGUHC.getInstance().PlayerInGame.contains(player.getUniqueId())) {
			LGUHC.getInstance().PlayerInGame.remove(player.getUniqueId());
		}	
		
		if (UHCState.isState(UHCState.GAMEPVP) || UHCState.isState(UHCState.FINISH)) {
			LGUHC.getInstance().Spectator.add(player.getUniqueId());
		}

	}
	
	private void reset(Player player) {
		
		player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
		
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(20);
		
		player.setLevel(0);
		player.setExp(0);
		
		for(ItemStack item:player.getInventory().getContents()){player.getInventory().remove(item);}
		for(ItemStack item:player.getInventory().getArmorContents()){player.getInventory().remove(item);}
		
		player.getInventory().clear();
		
		for(PotionEffect effect:player.getActivePotionEffects()){player.removePotionEffect(effect.getType());}
		
	}
	
	
	
	
}
