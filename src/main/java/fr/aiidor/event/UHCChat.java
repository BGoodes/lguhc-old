package fr.aiidor.event;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import fr.aiidor.LGUHC;
import fr.aiidor.Options.ChatOption;
import fr.aiidor.Options.LGOption;
import fr.aiidor.effect.Sounds;
import fr.aiidor.files.ConfigManager;
import fr.aiidor.game.Joueur;
import fr.aiidor.game.UHCState;
import fr.aiidor.role.LGRoles;

public class UHCChat implements Listener {

	private LGUHC main;
	
	public UHCChat(LGUHC main) {
		this.main = main;	
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
		    
		    if (args[0].equalsIgnoreCase("/op")) {
			    if (!main.host.equals(player.getUniqueId())) {
		    		e.setCancelled(true);
		    		player.sendMessage(main.gameTag + "§cSeul le Host peut effectuer cette commande !");
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
					
					main.removeOptionChat(player.getUniqueId(), false);
					main.InvStaffBuilder(player);
					return;
				}
				
				if (op == LGOption.addOrga) {
					
					if (main.orgas.contains(Target.getUniqueId())) {
						player.sendMessage(main.gameTag + "§cErreur, le joueur "+ targetname + " est déjà organisateur !");
						
						main.removeOptionChat(player.getUniqueId(), false);
						main.InvStaffBuilder(player);
					} else {
						main.orgas.add(Target.getUniqueId());
						player.sendMessage(main.gameTag + "§aLe joueur "+ targetname + " est désormais organisateur !");
						Target.setPlayerListName("§7[§9Orga§7] §f" + Target.getName());
						
						Target.sendMessage(main.gameTag + "§aVous êtes désormais un Organisateur !");
						new Sounds(Target).PlaySound(Sound.LEVEL_UP);
						
						main.removeOptionChat(player.getUniqueId(), false);
						main.InvStaffBuilder(player);
						return;
					}
					main.removeOptionChat(player.getUniqueId(), false);
					return;
				}
				
				if (op == LGOption.removeOrga) {
					
					if (!main.orgas.contains(Target.getUniqueId())) {
						player.sendMessage(main.gameTag + "§cErreur, le joueur "+ targetname + " n'est pas organisateur !");
						
						main.removeOptionChat(player.getUniqueId(), false);
						main.InvStaffBuilder(player);
						return;
					} else {
						main.orgas.remove(Target.getUniqueId());
						player.sendMessage(main.gameTag + "§aLe joueur "+ targetname + " n'est désormais plus organisateur !");
						Target.setPlayerListName(Target.getName());
						
						if (!main.isState(UHCState.GAME) && !main.isState(UHCState.PREGAME)) {
							main.clearInventory(Target);
						}
						
						player.setOp(false);
						main.removeOptionChat(player.getUniqueId(), false);
						main.InvStaffBuilder(player);
						return;
					}
				}
				
				if (op == LGOption.setHost) {
					
					if (player.getUniqueId() != main.host) {
						player.sendMessage(main.gameTag +"§cErreur, vous n'êtes pas le host !");
						main.removeOptionChat(player.getUniqueId(), false);
						main.InvStaffBuilder(player);
						return;
					}
					
					main.host = Target.getUniqueId();
					Target.setPlayerListName("§7[§cHOST§7] §f" + Target.getName());
					player.setPlayerListName(player.getName());
					
					player.sendMessage(main.gameTag + "§aLe joueur " + Target.getName() + " est désormais le HOST.");
					
					Target.sendMessage(main.gameTag + "§aVous êtes désormais le HOST !");
					new Sounds(Target).PlaySound(Sound.LEVEL_UP);
					
					if (!main.isState(UHCState.GAME) && !main.isState(UHCState.PREGAME)) {
						main.clearInventory(player);
					}
					
					player.setOp(false);
					main.removeOptionChat(player.getUniqueId(), false);
					return;
				}
				
			}
			
			if (op == LGOption.leaveHost) {
				
				String Message = e.getMessage();
				
				if (Message.split(" ").length != 1) {
					player.sendMessage(main.gameTag + "§cErreur, vous devez rentrer OUI ou NON !");
					return;
				}
				
				if (Message.equalsIgnoreCase("NON")) {
					main.InvStaffBuilder(player);
					main.removeOptionChat(player.getUniqueId(), false);
					return;
				}
				
				if (Message.equalsIgnoreCase("OUI")) {
					
					player.setPlayerListName(player.getName());
					
					player.sendMessage(main.gameTag + "§aVous n'êtes plus le HOST de la partie !");
					
					main.host = null;
					
					if (!main.isState(UHCState.GAME) && !main.isState(UHCState.PREGAME)) {
						main.clearInventory(player);
					}
					
					main.removeOptionChat(player.getUniqueId(), false);
					
					new Sounds(player).PlaySound(Sound.ANVIL_USE);
					return;
				}
				
			}
			
			if (op == LGOption.setWbmaxSize || op == LGOption.setWbSize || op == LGOption.setWbTime) {
				
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
					 player.sendMessage(main.gameTag + "§cErreur, vous devez entrer un chiffre (entier) !");
					return;
				}
				
				if (op == LGOption.setWbTime) {
					if (num < 0) {
						num = 0;
					}
					
					if (num > 300) {
						num = 300;
					}
					
					main.wbTime = num;
					
					main.removeOptionChat(player.getUniqueId(), false);
					player.sendMessage(main.gameTag + "§aLes paramètres on été enregistrés !");
					main.configInvBuilder(player);
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
			
			
			if (op == LGOption.setXpNerf) {
				if (e.getMessage().equalsIgnoreCase("Annuler")) {
					main.removeOptionChat(player.getUniqueId());
					return;
				}
				
				String Message = e.getMessage();
				
				if (Message.split(" ").length != 1) {
					player.sendMessage(main.gameTag + "§cErreur, vous devez rentrer une seule valeur !");
					return;
				}
				
				Double num = 0.0;
				
				try {
					  num = Double.valueOf(Message.replace(',', '.'));
					  
				} catch (NumberFormatException ex){
					 player.sendMessage(main.gameTag + "§cErreur, vous devez entrer un chiffre !");
					return;
				}
				
				if (num < 0.0) {
					num = 0.0;
				}
					
				if (num > 10.0) {
					num = 10.0;
				}
					
				main.xpNerfVar = num;
				main.xpNerfVar = (double)Math.round(main.xpNerfVar * 100d) / 100d;
				
				main.removeOptionChat(player.getUniqueId(), false);
				player.sendMessage(main.gameTag + "§aLes paramètres on été enregistrés !");
				main.configInvBuilder(player);
			}
			
			if (op == LGOption.setNoMineTime) {
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
					 player.sendMessage(main.gameTag + "§cErreur, vous devez entrer un chiffre (entier) !");
					return;
				}
				
				if (num < 0) {
					num = 0;
				}
					
				if (num > 60) {
					num = 60;
				}
					
				main.noMineTime = num;
					
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
				if (player.getUniqueId().equals(main.host)) {
					main.sendStaffMsg("§3(Staff : §c" + player.getName() + "§3) §f" + msg);
				} else {
					main.sendStaffMsg("§3(Staff : §9" + player.getName() + "§3) §f" + msg);
				}
			
				
				return;
			}
		}
		
		if (main.deathChat) {
			if (main.hasRole(e.getPlayer())) {
				Joueur d = main.getPlayer(e.getPlayer().getUniqueId());
				
				if (d.isDead()) {
					e.setCancelled(true);
					for (Joueur j : main.getPlayerRoles(LGRoles.Chaman)) {
						j.getPlayer().sendMessage("§8[MORT] §oanonyme §8>> §7" + e.getMessage());
					}
					
					for (Player p : main.getSpectator()) {
						if (!main.hasRole(p)) {
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
		
		if (main.chat == ChatOption.Off) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(main.gameTag + "§cLe chat est désactivé !");
			return;
		}
		
		if (main.chat == ChatOption.Off_IG && main.isState(UHCState.GAME)) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(main.gameTag + "§cLe chat est désactivé !");
			return;
		}
		
		
		String name = "§7§l" + player.getName();
		
		if (player.getUniqueId().equals(main.host)) {
			name = "§c§l" + player.getName();
		}
		
		if (!main.orgas.isEmpty() && main.orgas.contains(player.getUniqueId())) {
			name = "§9§l" + player.getName();
		}
		
		if (main.canHost != null && !main.canHost.isEmpty() && main.canHost.contains(player.getName())) {
			name = " §a✔ " + name;
		}
		
		e.setCancelled(true);
		
		if (main.chat == ChatOption.Region) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (player.getLocation().distance(p.getLocation()) <= main.chatRegion) {
					p.sendMessage(name + " §8> §f" + e.getMessage());
				}
			}
			return;
		}
		
		if (e.getMessage().contains("@")) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				
				if (e.getMessage().contains("@" + p.getName())) {
					
					e.setMessage(e.getMessage().replace("@" + p.getName(), "§6@" + p.getName() + "§f"));
					new Sounds(player).PlaySound(Sound.CLICK);
					
				}
			}
		}
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getWorld().equals(player.getWorld())) {
				p.sendMessage(name + " §8> §f" + e.getMessage());
			}
		}
	}	
}
