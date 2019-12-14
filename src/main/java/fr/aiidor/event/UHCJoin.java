package fr.aiidor.event;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import fr.aiidor.LGUHC;
import fr.aiidor.game.UHCState;
import fr.aiidor.role.LGRoles;
import fr.aiidor.scoreboard.ScoreboardKill;
import fr.aiidor.scoreboard.ScoreboardSign;
import fr.aiidor.scoreboard.TabList;

public class UHCJoin implements Listener{
	
	private LGUHC main;
	
	public UHCJoin(LGUHC pl) {
		this.main = pl;
	}	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		Player player = e.getPlayer();
		
		if (!main.world.equals(player.getWorld())) {
			player.teleport(main.Spawn);
		}
		
		player.setOp(false);
		
		for(PotionEffect effect:player.getActivePotionEffects()){player.removePotionEffect(effect.getType());}
		
		player.setCompassTarget(main.Spawn);
		
		if (main.cage) {
			if (main.world.getBlockAt(main.Spawn.getBlockX(), main.Spawn.getBlockY(), main.Spawn.getBlockZ()).getType() != Material.BARRIER) {
				main.createCage();
			}
		}
		
		if (main.host == null) {
			if (main.canHost != null) {
				if (main.canHost.contains(player.getName())) {
					
					main.setHost(player.getUniqueId());
					player.sendMessage(main.gameTag + "§dVous êtes le Host de la partie !");
					player.setPlayerListName("§7[§cHOST§7] §f" + player.getName());
					
				}
				
			} else {
				
				main.setHost(player.getUniqueId());
				player.sendMessage(main.gameTag + "§dVous êtes le Host de la partie !");
				player.setPlayerListName("§7[§cHOST§7] §f" + player.getName());
				
			}

		} else if (player.getUniqueId().equals(main.host)) {
			
			player.sendMessage(main.gameTag + "§dVous êtes le Host de la partie !");
			player.setPlayerListName("§7[§cHOST§7] §f" + player.getName());
		}
		
		
		if (main.orgas.contains(player.getUniqueId())) {
			
			player.sendMessage(main.gameTag + "§bVous êtes un organisateur de la partie !");
			player.setPlayerListName("§7[§9Orga§7] §f" + player.getName());
		}
		

		
		//TABLIST
		new TabList(main).set(player);
		
		//SCOREBOARD
		
		if (!main.isState(UHCState.FINISH)) {
			ScoreboardSign scoreboard = new ScoreboardSign(player, main.gameName);
			scoreboard.create();
			scoreboard.setLine(0, "§c");
			scoreboard.setLine(1, "§ePartie en Attente !");
			
			main.boards.put(player, scoreboard);
		} else {
			new ScoreboardKill(main, player).createScoreboard();
		}

		
		if (!main.canJoin()) {
			
			
			if (main.Spectator.contains(player.getUniqueId())) {
				spec(player);	
				e.setJoinMessage("§8[§a+§8] " + player.getName() + "§7(§e"+ Bukkit.getOnlinePlayers().size() + " §7/§e" + Bukkit.getMaxPlayers() +"§7) §b(Spectateur)");
				return;
			}
			 
			if (!main.PlayersHasRole()) {
				if (!main.PlayerInGame.contains(player.getUniqueId())) {
					spec(player);	
					e.setJoinMessage("§8[§a+§8] " + player.getName() + "§7(§e"+ Bukkit.getOnlinePlayers().size() + " §7/§e" + Bukkit.getMaxPlayers() +"§7) §b(Spectateur)");
					return;
				}
			} else {
				
				if (!main.isInGame(player.getUniqueId())) {
					spec(player);	
					e.setJoinMessage("§8[§a+§8] " + player.getName() + "§7(§e"+ Bukkit.getOnlinePlayers().size() + " §7/§e" + Bukkit.getMaxPlayers() +"§7) §b(Spectateur)");
					return;
				}
				
			}
			
			e.setJoinMessage("§8[§a+§8] " + player.getName() + "§7(§e"+ Bukkit.getOnlinePlayers().size() + " §7/§e" + Bukkit.getMaxPlayers() +"§7)");
			return;
			}
		
		player.teleport(main.Spawn.clone().add(0, 6, 0));
		
		e.setJoinMessage("§8[§a+§8] " + player.getName() + "§7(§e"+ Bukkit.getOnlinePlayers().size() + " §7/§e" + Bukkit.getMaxPlayers() +"§7)");
		main.reset(player);
		
		if (player.getUniqueId().equals(main.host) || main.orgas.contains(player.getUniqueId())) {
			player.getInventory().setItem(4, getItem(Material.ENDER_CHEST, "§d§lConfiguration"));
		}
	}
	

	
	private void spec (Player player) {
		player.teleport(main.Spawn);
		
		main.reset(player);
		
		player.setPlayerListName("§8[Spectateur] §7" + player.getName());
		
		if (main.PlayerHasRole) {
			if (main.isInGame(player.getUniqueId())) {
				if (main.getPlayer(player.getUniqueId()).Rob) player.setPlayerListName("§8[MORT] §7" + player.getName() + " §8(" + LGRoles.Voleur.name + ")"); 
				else player.setPlayerListName("§8[MORT] §7" + player.getName() + " §8(" + main.getPlayer(player.getUniqueId()).getRole().name + ")"); 
				
				player.sendMessage(main.gameTag + "§cVous êtes mort ! Vous serez donc un spectateur !");
			}
		}
		
		if (!main.PlayerHasRole || !main.isInGame(player.getUniqueId())) {
			player.sendMessage(main.gameTag + "§cLa partie à déjà commencé ! Vous serez donc un spectateur !");
		}
		
		
		player.sendMessage("§6===============§4§k0§6===============");
		player.sendMessage("§bVous êtes spectateur : §9faites /spec §bpour accéder aux commandes des spectateurs !");
		player.sendMessage("§6===============§4§k0§6===============");
		player.sendMessage(" ");
		player.sendMessage("§cVous n'avez pas le droit de donner d'informations aux joueurs dans la partie !");
		
		if (!main.Spectator.contains(player.getUniqueId())) main.Spectator.add(player.getUniqueId());
		
		player.setOp(false);
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				player.setGameMode(GameMode.SPECTATOR);
			}
		}, 5);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		
		int size = Bukkit.getOnlinePlayers().size() - 1;
		e.setQuitMessage("§8[§c-§8] " + e.getPlayer().getName() + "§7(§e"+ size + " §7/§e" + Bukkit.getMaxPlayers() +"§7)");
		
		if (main.boards.containsKey(e.getPlayer())) main.boards.remove(e.getPlayer());
		
		main.removeOptionChat(e.getPlayer().getUniqueId(), false);
	}
	

	
	private ItemStack getItem(Material mat, String Name) {
		
		ItemStack it = new ItemStack(mat, 1);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName(Name);
		
		it.setItemMeta(itM);
		return it;
	}
}
