package fr.aiidor.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.SkullMeta;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.game.LGVote;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGDesc;
import fr.aiidor.role.LGRoles;
import fr.aiidor.role.use.LGRole_Ange;
import fr.aiidor.role.use.LGRole_Chasseur;
import fr.aiidor.role.use.LGRole_Citoyen;
import fr.aiidor.role.use.LGRole_Cupidon;
import fr.aiidor.role.use.LGRole_Detective;
import fr.aiidor.role.use.LGRole_EnfantS;
import fr.aiidor.role.use.LGRole_IPL;
import fr.aiidor.role.use.LGRole_Renard;
import fr.aiidor.role.use.LGRole_Salvateur;
import fr.aiidor.role.use.LGRole_Soeur;
import fr.aiidor.role.use.LGRole_Sorciere;
import fr.aiidor.role.use.LGRole_Trublion;
import fr.aiidor.role.use.LGRole_Voyante;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;

public class CommandLg implements CommandExecutor {
	
	private LGUHC main;
	public CommandLg(LGUHC main) {
		this.main = main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			
			System.out.println("[LOUP-GAROUS] Seul un joueur peut effectuer cette commande !");
			return true;
		}
		
		Player player = (Player) sender;
		
		//RACINE == /HELP
		if (args.length == 0) {
			player.sendMessage(main.gameTag + "§3 Les commandes :");
			player.sendMessage("§9- /lg role : §6Infos sur le rôle");
			player.sendMessage("§9- /lg compo : §6Composition de la partie");
			player.sendMessage("§9- /lg compo Chat");
			player.sendMessage("§9- /lg msg <message> : §6Permet de laisser un dernière volonté !");
			player.sendMessage("§9- /lg report: §6Permet de report les bugs, les joueurs, ...");
			
			
			if (main.compo.contains(LGRoles.Chaman)) {
				if (main.getPlayerRolesOff(LGRoles.Chaman).size() != 0) {
					if (main.hasRole(player)) {
						Joueur j = main.getPlayer(player.getUniqueId());
						
						if (j.isDead()) {
							player.sendMessage(main.gameTag + "§cVous êtes mort mais vous pouvez encore agir dans la partie ! Grâce à la commande /lg chaman <message> "
									+ "qui permet de laisser un message au chaman si vous vous déconnecter Sinon vous pouvez attendre que le chaman puisse \"écouter les mort\".");
						}
					}
				}
			}

			player.sendMessage(" ");
			
			return true;
		}
		
		//COMMANDE ROLE
		if (args[0].equalsIgnoreCase("role")) {
			
			if (args.length == 1) {
				if (main.getPlayer(player.getUniqueId()) == null) {
					player.sendMessage(main.gameTag + "§cErreur, vous devez avoir un rôle pour effectuer cette commande !");
				}
				else {
					main.getPlayer(player.getUniqueId()).sendDesc();
					
					if (main.canSeeList) {
						if (main.getPlayer(player.getUniqueId()).isLg()) {
							new LGDesc(main).sendLGList(player);
						}
					}
				}
				return true;
			}
			
			player.sendMessage(main.gameTag + "§cErreur, la commande est §e/lg role");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("msg")) {
			
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§6La commande s'écrit §e/lg msg <Message>");
				return true;
			}
			
			if (args.length < 2) {
				player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit §e/lg msg <Message>");
				return true;
			}
			
			if (!canCommand(player)) return true;
			
			Joueur j = main.getPlayer(player.getUniqueId());
			
			if (main.compo.contains(LGRoles.Chaman)) {
				if (main.getPlayerRolesOff(LGRoles.Chaman).size() > 0) {
					player.sendMessage(main.gameTag + "§cVous ne pouvez pas effectuer cette commande car un §oChaman §cest encore en vie.");
					return true;
				}
			}
			
			StringBuilder sb = new StringBuilder();
			
			Boolean i = false;
			for (String word : args) {
				if (i) {
					sb.append(word + " ");
				}
				
				i = true;
			}
			
			j.LastWill = sb.toString();
			player.sendMessage(main.gameTag + "§aVotre denière volonté a bien été enregistré !");
			return true;
		}
		
		
		//COMMANDE ROLE
		if (args[0].equalsIgnoreCase("compo")) {
			
			if (!main.PlayersHasRole()) {
				player.sendMessage(main.gameTag + "§cLes joueurs n'ont pas encore de rôles !");
				return true;
			}
			
			if (main.hidecompo && !main.Spectator.contains(player.getUniqueId())) {
				player.sendMessage(main.gameTag + "§cLa composition est caché !");
				return true;
			}
			
			if (args.length == 1) {
				int slot = 0;
				Inventory inv = Bukkit.createInventory(null, 27, "§dComposition :");
				for (LGRoles role : LGRoles.values()) {
					if (main.compo.contains(role)) {
							
						inv.setItem(slot, getRoleIcon(role, getCompo(role)));
						slot++;
					}
				}
				
				player.openInventory(inv);

				return true;
			}
			
			if (args.length == 2) {
				if (args[1].equalsIgnoreCase("chat")) {
					player.sendMessage(main.gameTag + "§d§lComposition : ");
					for (LGRoles role : LGRoles.values()) {
						if (main.compo.contains(role)) {
							player.sendMessage(getMessage(role, getCompo(role)));
						}
					}
					return true;
				}
				
				if (args[1].equalsIgnoreCase("book")) {
					newBook(player);
					return true;
				}
				
			}
			
			player.sendMessage(main.gameTag + "§cErreur, la commande est §e/lg compo OU /lg compo chat");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("report")) {	
			if (args.length == 1) {
				player.sendMessage("§6La commande : /lg report <message>");
				return true;
			}
			StringBuilder bs = new StringBuilder();
			int size = 0;
			for (String s : args) {
				if (size > 0) {
					bs.append(s + " ");
				}
				size ++;
			}
			
			player.sendMessage("§aVotre Report à bien été enregistré !");
			
			main.sendStaffMsg("§3(Staff : §6Report§3) §8Le joueur §c" + player.getName() + " §8a report :");
			main.sendStaffMsg("§7 > " + bs.toString());
			
			return true;
		}
		
		//VOTE
		if (args[0].equalsIgnoreCase("vote")) {
			
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg vote <Joueur>");
				return true;
			}
			
			if (!canCommand(player)) return true;
			
			
			if (args.length == 2) {
				new LGVote(main).vote(main.getPlayer(player.getUniqueId()), args[1]);
				return true;
			}
			
			if (args.length == 4) {
				new LGVote(main).corbeauVote(main.getPlayer(player.getUniqueId()), args[1], args[2], args[3]);
				return true;
			}
			
			player.sendMessage(main.gameTag + "§cErreur, la commande s'écrit /lg vote <Joueur>");
			
			if (main.getPlayer(player.getUniqueId()).getRole() == LGRoles.Corbeau) {
				player.sendMessage(main.gameTag + "§cMais elle peut aussi écrire /lg vote <Joueur1> <Joueur2> <Joueur3> car vous êtes le §ocorbeau §c!");
			}
			
			return true;
		}
		
		//FLAIRER
		if (args[0].equalsIgnoreCase("flairer")) {
			
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg flairer <Joueur>");
				return true;
			}
			
			if (!canCommand(player)) return true;
			
			Joueur j = main.getPlayer(player.getUniqueId());
			
			if (args.length > 2) {
				player.sendMessage(main.gameTag + "§cErreur, La commande :§e /lg flairer <Joueur>");
				return true;
			}
			
			if (args.length == 2) {
				String targetname = args[1];
				new LGRole_Renard(main, j).canFlaire(targetname);
				return true;
			}
		}
		
		//SEE
		if (args[0].equalsIgnoreCase("see")) {
			
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg see <Joueur>");
				return true;
			}
			
			if (!canCommand(player)) return true;
			
			if (args.length > 2) {
				player.sendMessage(main.gameTag + "§cErreur, La commande :§e /lg see <Joueur>");
				return true;
			}
			
			if (args.length == 2) {
				new LGRole_Voyante(main).canSee(main.getPlayer(player.getUniqueId()), args[1]);
				return true;
			}
		}
		
		//REA
		if (args[0].equalsIgnoreCase("revive")) {
			
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg revive <Joueur>");
				
				return true;
			}
			
			//CAN COMMAND
			if (main.getPlayer(player.getUniqueId()) == null) {
				player.sendMessage(main.gameTag + "§cVous devez avoir un rôle pour effectuer cette commande !");
				return true;
			} else {
				Joueur j = main.getPlayer(player.getUniqueId());
				if (j.isDead()) {
					player.sendMessage(main.gameTag + "§cVous devez être en vie pour effectuer cette commande !");
					return true;
				}
				
			}
			
			if (args.length > 2) {
				player.sendMessage(main.gameTag + "§cErreur, La commande :§e /lg revive <Joueur>");
				return true;
			}
			
			if (args.length == 2) {
				new LGRole_Sorciere(main).canRea(args[1], main.getPlayer(player.getUniqueId()));
				return true;
			}
		}
		
		//INFECT
		if (args[0].equalsIgnoreCase("infect")) {
			
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg infect <Joueur>");
				
				return true;
			}
			
			//CAN COMMAND
			if (main.getPlayer(player.getUniqueId()) == null) {
				player.sendMessage(main.gameTag + "§cVous devez avoir un rôle pour effectuer cette commande !");
				return true;
			} else {
				Joueur j = main.getPlayer(player.getUniqueId());
				if (j.isDead()) {
					player.sendMessage(main.gameTag + "§cVous devez être en vie pour effectuer cette commande !");
					return true;
				}
				
			}
			
			if (args.length > 2) {
				player.sendMessage(main.gameTag + "§cErreur, La commande :§e /lg infect <Joueur>");
				
				return true;
			}
			
			if (args.length == 2) {
				new LGRole_IPL(main).canRea(args[1], main.getPlayer(player.getUniqueId()));
				return true;
			}
		}
		
		if (args[0].equalsIgnoreCase("pan")) {
			
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg pan <Joueur>");
				
				return true;
			}
			
			if (main.getPlayer(player.getUniqueId()) == null) {
				player.sendMessage(main.gameTag + "§cVous devez avoir un rôle pour effectuer cette commande !");
				return true;
			}
			
			if (args.length > 2) {
				player.sendMessage(main.gameTag + "§cErreur, La commande :§e /lg pan <Joueur>");
				
				return true;
			}
			
			if (args.length == 2) {
				new LGRole_Chasseur(main).canPan(main.getPlayer(player.getUniqueId()), args[1]);
				return true;
			}
		}
		
		if (args[0].equalsIgnoreCase("chaman")) {
			
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg chaman <message>");
				
				return true;
			}
			
			if (main.getPlayer(player.getUniqueId()) == null) {
				player.sendMessage(main.gameTag + "§cVous devez avoir un rôle pour effectuer cette commande !");
				return true;
			}
			
			if (args.length < 2) {
				player.sendMessage(main.gameTag + "§cErreur, La commande :§e /lg chaman <message>");
				return true;
			}
			
			Joueur j = main.getPlayer(player.getUniqueId());
			
			StringBuilder bs = new StringBuilder();
			boolean size = false;
			
			for (String s : args) {
				if (size) {
					bs.append(s + " ");
				} else {
					size = true;
				}
			}
			
			j.chamanMsg = bs.toString();
			player.sendMessage(main.gameTag + "§aVotre message a bien été enregistré !");
			return true;
		}
		
		//ENFANTS
		if (args[0].equalsIgnoreCase("choose")) {
			
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg choose <Joueur>");
				
				return true;
			}
			
			if (!canCommand(player)) return true;
			
			if (args.length > 2) {
				player.sendMessage(main.gameTag + "§cErreur, La commande :§e /lg choose <Joueur>");
				
				return true;
			}
			
			if (args.length == 2) {
				new LGRole_EnfantS(main).canChoose(main.getPlayer(player.getUniqueId()), args[1]);
				return true;
			}
		}
		
		//CUPIDON
		if (args[0].equalsIgnoreCase("love")) {
			
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg love <Joueur1> <Joueur2>");
				
				return true;
			}
			
			if (!canCommand(player)) return true;
			
			if (args.length != 3) {
				player.sendMessage(main.gameTag + "§cErreur, La commande :§e /lg love <Joueur1> <Joueur2>");
				
				return true;
			}
			
			if (args.length == 3) {
				new LGRole_Cupidon(main).canChoose(main.getPlayer(player.getUniqueId()), args[1], args[2]);
				return true;
			}
		}
		
		//TRUBLION
		if (args[0].equalsIgnoreCase("switch")) {
			
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg switch <Joueur1> <Joueur2>");
				
				return true;
			}
			
			if (!canCommand(player)) return true;
			
			if (args.length != 3) {
				player.sendMessage(main.gameTag + "§cErreur, La commande :§e /lg switch <Joueur1> <Joueur2>");
				
				return true;
			}
			
			if (args.length == 3) {
				new LGRole_Trublion(main).canChoose(main.getPlayer(player.getUniqueId()), args[1], args[2]);
				return true;
			}
		}
		
		//DETECTIVE
		if (args[0].equalsIgnoreCase("inspect")) {
			
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg inspect <Joueur1> <Joueur2>");
				
				return true;
			}
			
			if (!canCommand(player)) return true;
			
			if (args.length != 3) {
				player.sendMessage(main.gameTag + "§cErreur, La commande :§e /lg inspect <Joueur1> <Joueur2>");
				
				return true;
			}
			
			if (args.length == 3) {
				new LGRole_Detective(main).Inspect(main.getPlayer(player.getUniqueId()), args[1], args[2]);
				return true;
			}
		}
		
		//CITOYEN
		if (args[0].equalsIgnoreCase("SeeVote")) {
			
			if (args.length != 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg SeeVote");
				
				return true;
			}
			
			if (!canCommand(player)) return true;
			
			if (args.length == 1) {
				new LGRole_Citoyen(main).canSee(main.getPlayer(player.getUniqueId()));
				return true;
			}
		}
		
		if (args[0].equalsIgnoreCase("cancelVote")) {
			
			if (args.length != 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg cancelVote");
				
				return true;
			}
			
			if (!canCommand(player)) return true;
			
			if (args.length == 1) {
				new LGRole_Citoyen(main).cancelVote(main.getPlayer(player.getUniqueId()));
				return true;
			}
		}
		
		if (args[0].equalsIgnoreCase("SeeKiller")) {
			
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg SeeKiller role|name");
				
				return true;
			}
			
			if (!canCommand(player)) return true;
			
			if (args.length == 2) {
				if (args[1].equalsIgnoreCase("name")) {
					new LGRole_Soeur(main).canSee(main.getPlayer(player.getUniqueId()), true);
					return true;
				}
				if (args[1].equalsIgnoreCase("role")) {
					new LGRole_Soeur(main).canSee(main.getPlayer(player.getUniqueId()), false);
					return true;
				}
				player.sendMessage(main.gameTag + "§cErreur, La commande : /lg SeeKiller role|name");
				
			}
		}
		
		if (args[0].equalsIgnoreCase("protect")) {
			
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg protect <Joueur>");
				
				return true;
			}
			
			if (!canCommand(player)) return true;
			
			if (args.length > 2) {
				player.sendMessage(main.gameTag + "§cErreur, La commande :§e /lg protect <Joueur>");
				
				return true;
			}
			
			if (args.length == 2) {
				new LGRole_Salvateur(main).canProtect(main.getPlayer(player.getUniqueId()), args[1]);
				return true;
			}
		}
		
		//ANGE
		if (args[0].equalsIgnoreCase("angeType")) {
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg angeType <déchu|gardien>");
				
				return true;
			}
			
			if (!canCommand(player)) return true;
			
			if (args.length > 2) {
				player.sendMessage(main.gameTag + "§cErreur, La commande :§e /lg angeType <déchu|gardien>");
				return true;
			}
			
			if (args.length == 2) {
				new LGRole_Ange(main).choose(main.getPlayer(player.getUniqueId()), args[1]);
				return true;
			}
		}
		
		if (args[0].equalsIgnoreCase("regen")) {
			if (args.length != 1) {
				player.sendMessage(main.gameTag + "§cLa commande : /lg regen");
				
				return true;
			}
			
			if (!canCommand(player)) return true;
			
			if (args.length == 1) {
				new LGRole_Ange(main).heal(main.getPlayer(player.getUniqueId()));
				return true;
			}
		}
		
		//DON DE VIE
		if (args[0].equalsIgnoreCase("don")) {
			
			if (args.length == 1) {
				player.sendMessage(main.gameTag + "§bLa commande : /lg don <% de vie>");
				
				return true;
			}
			
			if (!canCommand(player)) return true;
			
			if (args.length > 2) {
				player.sendMessage(main.gameTag + "§cErreur, La commande :§e /lg don <% de vie>");
				return true;
			}
			
			Joueur j = main.getPlayer(player.getUniqueId());
			
			if (!j.hasCouple()) {
				player.sendMessage(main.gameTag + "§cErreur, vous devez être en couple pour effectuer cette comande !");
				return true;
			}
			
			if (!j.getCouple().isConnected()) {
				player.sendMessage(main.gameTag + "§cErreur, votre âme-soeur n'est pas connecté !");
				return true;
			}
			if (Double.valueOf(args[1]) == null) {
				player.sendMessage(main.gameTag + "§cErreur, La commande :§e /lg don <% de vie>");
				return true;
			}
			
			double Health = Double.valueOf(args[1]);
			
			if (player.getHealth() + 1 < Health/5) {
				player.sendMessage(main.gameTag + "§cVous n'avez pas assez de point de vie pour effectuer cette action !");
				return true;
			}
			
			if (j.getCouple().getPlayer().getHealth() + Health/5 > 20) {
				player.sendMessage(main.gameTag + "§cVous ne pouvez pas donnez autant de vie car votre âme soeur ne manque pas d'autant !");
				return true;
			}
			
			player.sendMessage(main.gameTag + "§aLe transfert de " + args[1] + " à " + j.getCouple().getName() + " à bien eu lieu !");
			
			player.setHealth(player.getHealth() - Health/5);
			
			j.getCouple().getPlayer().setHealth(j.getCouple().getPlayer().getHealth() + Health/5);
			j.getCouple().getPlayer().sendMessage(main.gameTag + "§aVous avez reçut un don de " + args[1] + "% de vie de la part de " + player.getName());
			return true;
		}
		
		
		player.sendMessage(main.gameTag + "§cErreur, Les commandes :");
		player.sendMessage("§9- /lg role : §6Permet d'avoir des infos sur son rôle !");
		player.sendMessage("§9- /lg compo : §6Permet de voir la composition de la game !");
		player.sendMessage("§9- /lg compo Chat : §6Permet de voir la composition de la game !");
		player.sendMessage("§9- /lg report: §6Permet de report les bugs, les joueurs, ...");
		
		return false;
	}
	
	private boolean canCommand(Player player) {
		
		if (main.getPlayer(player.getUniqueId()) == null) {
			player.sendMessage(main.gameTag + "§cVous devez avoir un rôle pour effectuer cette commande !");
			return false;
		} else {
			Joueur j = main.getPlayer(player.getUniqueId());
			if (j.isDead() || j.isDying()) {
				player.sendMessage(main.gameTag + "§cVous devez être en vie pour effectuer cette commande !");
				return false;
			}
			
		}
		
		return true;
	}
	
	private ItemStack getRoleIcon(LGRoles role, int number) {
		
		ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (byte) 4);
		
		if (role == LGRoles.Voleur) {
			number = getVoleurs();
		}
		
		if (number == 0) it = new ItemStack(Material.SKULL_ITEM, 1, (byte) 0);
		else it = new ItemStack(Material.SKULL_ITEM, number, (byte) 3);
		
		SkullMeta itM = (SkullMeta) it.getItemMeta();
		StringBuilder name = new StringBuilder();
		
		if (role.camp == LGCamps.Village) {
			name.append("§a");
			if (number != 0) itM.setOwner("Villager");
		}
		if (role.camp == LGCamps.LoupGarou) {
			name.append("§c");
			if (number != 0) itM.setOwner("Verzide");
		}
		
		if (role.camp == LGCamps.LGB) {
			name.append("§4");
			if (number != 0) itM.setOwner("SomeWerewolf");
		}
		
		if (role.camp == LGCamps.Assassin) {
			name.append("§6");
			if (number != 0) itM.setOwner("julagamer2007");
		}
		
		if (role.camp == LGCamps.Ange) {
			name.append("§d");
			if (number != 0) itM.setOwner("K1ll3rK1tt3n");
		}
		
		if (role.camp == LGCamps.Neutre) {
			name.append("§b");
			if (number != 0) itM.setOwner("Madben");
		}
		
		if (number == 0) {
			name.append("§m");
			itM.setLore(Arrays.asList("§c§l[MORT]"));
		}
		else itM.setLore(Arrays.asList("§6Nombre : §e" + number));
		
		name.append(role.name);
		itM.setDisplayName(name.toString());
		
		
		it.setItemMeta(itM);
		return it;
	}
	
	private String getMessage(LGRoles role, int number) {
		
		StringBuilder msg = new StringBuilder();
		
		if (role == LGRoles.Voleur) {
			number = getVoleurs();
		}
		
		if (role.camp == LGCamps.Village) msg.append("§a");
		if (role.camp == LGCamps.LoupGarou) msg.append("§c");
		if (role.camp == LGCamps.LGB) msg.append("§4");
		if (role.camp == LGCamps.Assassin) msg.append("§6");
		if (role.camp == LGCamps.Assassin) msg.append("§d");
		if (role.camp == LGCamps.Neutre) msg.append("§b");
		
		if (number == 0) msg.append("§m");
		msg.append(role.name);
		
		if (number != 0) msg.append(" §f §7(" + number + ")");
		
		return msg.toString();
	}
	
	
	public void newBook(Player p) {
	   
	  ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
	  
	  BookMeta meta = (BookMeta) book.getItemMeta();
	  meta.setTitle("§dComposition");
	  meta.setAuthor(main.gameTag);
	  
	  StringBuilder sb = new StringBuilder();
	  sb.append("§d§nComposition : \n\n");
	  
		int count = 0;
		
		for (LGRoles role : LGRoles.values()) {
			if (count == 13) break;
			if (!main.compo.contains(role)) {
				sb.append("§f-" + getMessage(role, getCompo(role))+ "\n");
				count++;
			}
		}
		
	  meta.setPages(sb.toString());
	  book.setItemMeta(meta);
	  
	  openBook(book, p);
	}
	
    private void openBook(ItemStack book, Player p) {
    	
        int slot = p.getInventory().getHeldItemSlot();
        ItemStack old = p.getInventory().getItem(slot);
        p.getInventory().setItem(slot, book);

       ByteBuf buf = Unpooled.buffer(256);
       buf.setByte(0, (byte)0);
       buf.writerIndex(1);

        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        p.getInventory().setItem(slot, old);
        
     }
	
	public int getVoleurs() {
		int number = 0;
		for (Joueur j : main.Players) {
			if (!j.isDead()) {
				if (j.Rob || j.getRole() == LGRoles.Voleur) {
					number++;
				}
			}
		}
		return number;
	}
	
	public int getCompo(LGRoles role) {
		int joueurs = 0;
		for (Joueur j : main.Players) {
			if (!j.Rob) {
				if (j.getRole() == role) {
					if (!j.isDead()) {
						joueurs ++;
					}
				}
			}
		}
		return joueurs;
	}

}
