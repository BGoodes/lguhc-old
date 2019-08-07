package fr.aiidor.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.events.UHCListeners;
import fr.aiidor.game.UHCGame;
import fr.aiidor.roles.LGCamps;
import fr.aiidor.roles.LGRoleManager;
import fr.aiidor.roles.LGRoles;
import fr.aiidor.roles.RoleDescription;
import fr.aiidor.roles.use.LG;
import fr.aiidor.roles.use.LGCupidon;
import fr.aiidor.roles.use.LGEnfantS;
import fr.aiidor.roles.use.LGRenard;
import fr.aiidor.roles.use.LGSalvateur;
import fr.aiidor.roles.use.LGSorciere;
import fr.aiidor.roles.use.LGTrublion;
import fr.aiidor.roles.use.LGVoyante;
import fr.aiidor.utils.LGVote;

public class CommandLg implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			
			System.out.println("[LOUP-GAROUS] Seul un joueur peut effectuer cette commande !");
			return true;
		}
		
		Player player = (Player) sender;
		
		
		//RACINE == /HELP
		if (args.length == 0) {
			player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�3 Les commandes :");
			player.sendMessage("�3- /lg role : �6Permet d'avoir des infos sur sont r�le ! (Commandes et description)");
			player.sendMessage("�3- /lg info : �6Permet d'avoir des infos sur la partie en cours");
			player.sendMessage("�3- /lg compo : �6Permet d'avoir des infos sur la composition de la partie");
			player.sendMessage("�3- /lg rules : �6Permet d'avoir les r�gles de la partie");
			player.sendMessage(" ");
			
			return true;
		}
		
		if (args.length == 1) {
			//LG ROLE -----------------------------------------------
			if (args[0].equalsIgnoreCase("role")) {
				
				if (LGRoles.getRole(player.getUniqueId()) == null) {
					
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Erreur, Vous devez avoir un r�le pour effectuer cette commande !");
					return true;
				}
				//LOUP-GAROUS AMNESIQUE
				if (LGRoles.getRole(player.getUniqueId()) == LGRoles.LGA && !LGRoleManager.Power.containsKey(player.getUniqueId())) {
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�9 [Priv�] Vous �tes �o" + "Simple Villageois" + ".");
				}
				
				else {
					//INFECT
					if (LGRoleManager.Infect.contains(player.getUniqueId())) {
						player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�9 [Priv�] Vous �tes �o" + LGRoles.getRoleName(player.getUniqueId()) + " �4Infect�.");
					}
					
					else if (LGRoles.getRole(player.getUniqueId()) == LGRoles.EnfantS && LGCamps.getCamp(player.getUniqueId()) == LGCamps.LOUPGAROUS) {
						player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�9 [Priv�] Vous �tes �o" + LGRoles.getRoleName(player.getUniqueId()) + " �4Transform�.");
					}
					else {
						player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�9 [Priv�] Vous �tes �o" + LGRoles.getRoleName(player.getUniqueId()) + ".");
					}
				}
				player.sendMessage(RoleDescription.desc(player.getUniqueId()));
				
				LG.LGCompo(player);
				player.sendMessage("");
				return true;
			}
			//-----------------------------------------------
			
			if (args[0].equalsIgnoreCase("info")) {
				
				player.sendMessage("�2  --- Information ---");
				
				int min = UHCGame.timer/60;
				int sec = UHCGame.timer%60;;
				player.sendMessage("�cTimer : �6" + min + " �eMin�6 " + sec);
				
				player.sendMessage("�cJoueur : �6" + LGUHC.getInstance().PlayerInGame.size());
				player.sendMessage("�cLimite de groupe : �6" + UHCGame.GroupLimit);
				
				return true;
			}
			//-----------------------------------------------
			
			if (args[0].equalsIgnoreCase("compo")) {
				
				player.sendMessage("�2  --- Composition ---");
				
				StringBuilder compo = new StringBuilder();
				
				for (UUID uuid : LGRoleManager.PlayerRole.keySet()) {
					
					if (LGUHC.getInstance().PlayerInGame.contains(uuid)) {
						
						compo.append("�6" + LGRoles.getRoleName(uuid) + "�6   ");
					}
					else {
						compo.append("�c�m" + LGRoles.getRoleName(uuid) + "�c   ");
					}
				}
				
				player.sendMessage(compo.toString());
				return true;
			}
			//-----------------------------------------------
			
			if (args[0].equalsIgnoreCase("rules")) {
				
				player.sendMessage("�2  --- R�gles ---");
				if (UHCListeners.DiamondLimit) {
					player.sendMessage("�6Limite de diamant : �b" + UHCListeners.DiamondMax);
				}
				player.sendMessage("�6Limite de stuff : �b2 pi�ces diamant max �3(Armure)");
				player.sendMessage("�6Limite d'enchant : �bP2 sur les armures �3(en diamants)�b, T4 pour les �p�es");
				player.sendMessage("�6Enchants : �bFlame et Fire Aspect Interdit !");
				player.sendMessage("�6Items : �cBouclier + Rod interdit en pvp !");
				player.sendMessage("�cInterdit aux msg priv�s �3(Sauf pour des Questions aux admins)");
				
				player.sendMessage("�2  --- Sc�nario ---");
				//RUN
				if (LGUHC.getInstance().Run) {
					player.sendMessage("�6Run : �aActiv� ");
				}
				else {
					player.sendMessage("�6Run : �cD�sactiv� ");
				}
				player.sendMessage("�6FastSmelting : �cD�sactiv� ! ");
				return true;
			}
			
		}
		
		
		//CUPIDON -------------------------------------------
		if (args[0].equalsIgnoreCase("love")) {
			
			if (LGRoles.getRole(player.getUniqueId()) != LGRoles.Cupidon) {
				player.sendMessage(" ");
				player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous devez �tre �oCupidon �cpour effectuer cette commande !");
				return true;
			}
			
			
			if (args.length < 3) {
				player.sendMessage(" ");
				player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous devez choisir 2 personnes !");
			}	
			
			
			if (!LGRoleManager.Power.containsKey(player.getUniqueId())) {
				player.sendMessage(" ");
				player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous avez d�ja utilis� votre capacit� !");
				return true;
			}
			
			String targetName1 = args[1];
			String targetName2 = args[2];
			
			if (targetName1 == targetName2) {
				player.sendMessage(" ");
				player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l] �cVous ne pouvez pas mettre 2 fois la m�me personne dans le couple !");
				
				return true;
			}
			if (Bukkit.getPlayer(targetName1) == null || Bukkit.getPlayer(targetName2) == null) {
				
				player.sendMessage(" ");
				player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l] �cErreur, l'un des joueurs n'est pas en partie �fou �cn'existe pas !");
				return true;
			}
			
			Player Target1 = Bukkit.getPlayer(targetName1);
			Player Target2 = Bukkit.getPlayer(targetName1);
			
			LGCupidon.choose(Target1, Target2);
			
			player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�d Vous avez bien mis �6" + targetName1 + "�d et �6" + targetName2 + "�d en couple ! Si l'un meurt, l'autre ne pourrais supporter "
					+ "cette souffrance et se suiciderais imm�diatement !");
			player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�2 Vous pouvez gagnez avec eux ou avec le village !");
			
			return true;
		}
		
		//TRUBLION -------------------------------------------
		if (args[0].equalsIgnoreCase("switch")) {
			
			if (LGRoles.getRole(player.getUniqueId()) != LGRoles.Trublion) {
				player.sendMessage(" ");
				player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous devez �tre �oTrublion �cpour effectuer cette commande !");
				return true;
			}
			
			
			if (args.length < 3) {
				player.sendMessage(" ");
				player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous devez choisir 2 personnes !");
			}	
			
			
			if (!LGRoleManager.Power.containsKey(player.getUniqueId())) {
				player.sendMessage(" ");
				player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous avez d�ja utilis� votre capacit� ou avez attendu trop longtemps !");
				return true;
			}
			
			String targetName1 = args[1];
			String targetName2 = args[2];
			
			
			if (targetName1 == player.getName() || targetName2 == player.getName()) {
				player.sendMessage(" ");
				player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l] �cVous ne pouvez pas vous choisir vous m�me !");
				return true;
			}
			if (targetName1 == targetName2) {
				player.sendMessage(" ");
				player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l] �cVous ne pouvez pas choisir 2 fois la m�me personne !");
				return true;
			}
			if (Bukkit.getPlayer(targetName1) == null || Bukkit.getPlayer(targetName2) == null) {
				
				player.sendMessage(" ");
				player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l] �cErreur, l'un des joueurs n'est pas en partie �fou �cn'existe pas !");
				return true;
			}
			
			Player Target1 = Bukkit.getPlayer(targetName1);
			Player Target2 = Bukkit.getPlayer(targetName1);
			
			LGTrublion.Switch(Target1, Target2);
			
			player.sendMessage(" ");
			player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�3 Vous avez bien mis �6" + targetName1 + "�3 et �6" + targetName2 +  "�3 !");
			return true;
		}
		
		
		
		
		
		
		
		if (args.length == 2) {
			if (!LGUHC.getInstance().PlayerInGame.contains(player.getUniqueId())) { 
				player.sendMessage(" ");
				player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous devez �tre en partie pour effectuer cette commande !");
				return true;
				}
			
			//VOTE----------------------------------------------------------------------
			if (args[0].equalsIgnoreCase("vote")) {
				
				if (!LGVote.canVote) {
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous ne pouvez pas effectuer cette commandes actuellement !");
					return true;
				}
				
				if (LGVote.aVote.contains(player.getUniqueId())) {
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous avez d�j� vot� ! Attendez le prochaine �pisode !");
					return true;
				}
				
				String targetName = args[1];
				if (Bukkit.getPlayer(targetName) == null) {
					
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l] �cErreur, le joueur �e" + targetName + " �c n'est pas connect� �fou �cn'existe pas !");
					return true;
				}
				
				Player Target = Bukkit.getPlayer(targetName);
				
				if (!LGUHC.getInstance().PlayerInGame.contains(Target.getUniqueId())) {
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l] �cErreur, le joueur �e" + targetName + " �cest mort ou n'est pas dans la partie !");
					return true;
				}
				
				LGVote.AddVote(Target.getUniqueId());
				
				LGVote.aVote.add(player.getUniqueId());
				player.sendMessage(" ");
				player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l] �aVous avez bien vot� pour �f" + targetName);
			}
			
			
			//RENARD ---------------------------------------
			if (args[0].equalsIgnoreCase("flairer")) {
				
				if (LGRoles.getRole(player.getUniqueId()) != LGRoles.Renard) {
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous devez �tre �oRenard �cpour effectuer cette commande !");
					return true;
				}
				
				String targetName = args[1];
				if (Bukkit.getPlayer(targetName) == null) {
					
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l] �cErreur, le joueur �e" + targetName + " �c n'est pas connect� �fou �cn'existe pas !");
					return true;
				}
				
				Player Target = Bukkit.getPlayer(targetName);
				
				if (!LGUHC.getInstance().PlayerInGame.contains(Target.getUniqueId())) {
					
				}
				LGRenard.Renard(player, Target);
			}
			
			//VOYANTE -----------------------------------------
			if (args[0].equalsIgnoreCase("see")) {
				
				if (LGRoles.getRole(player.getUniqueId()) != LGRoles.Voyante) {
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous devez �tre �oVoyante �cour effectuer cette commande !");
					return true;
				}
				
				String targetName = args[1];
				if (Bukkit.getPlayer(targetName) == null) {
					
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�cErreur, le joueur �e" + targetName + " �c n'est pas connect� �fou �cn'existe pas !");
					return true;
				}
				
				Player Target = Bukkit.getPlayer(targetName);
				LGVoyante.SeeRole(player, Target);
			}
			
			//INFECT PERE DES LOUPS ------------------------------
			if (args[0].equalsIgnoreCase("infect")) {
				
				if (LGRoles.getRole(player.getUniqueId()) != LGRoles.LgInfect) {
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous devez �tre �oInfect P�re des loups �cpour effectuer cette commande !");
					return true;
				}
				
				if (!LGRoleManager.Power.containsKey(player.getUniqueId())) {
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous avez d�ja utilis� votre capacit� !");
					return true;
				}
				
				String targetName = args[1];
				if (Bukkit.getPlayer(targetName) == null) {
					
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�cErreur, le joueur �e" + targetName + " �c n'est pas connect� �fou �cn'existe pas !");
					return true;
				}
				
				Player Target = Bukkit.getPlayer(targetName);
				
				if (LGRoleManager.mortD.containsKey(Target.getUniqueId())) {
					if (LGRoleManager.mortD.get(Target.getUniqueId()) == LGCamps.LOUPGAROUS) {
						
						LGRoleManager.Power.remove(player.getUniqueId());
						LG.Infect(Target.getUniqueId());
						return true;
					}
				}
				
				player.sendMessage(" ");
				player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Le joueur �e" + targetName + " �cne peut pas �tre infect� !");
				return true;
			}
			//SORCIERE ------------------------------
			if (args[0].equalsIgnoreCase("revive")) {
				
				if (LGRoles.getRole(player.getUniqueId()) != LGRoles.Sorciere) {
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous devez �tre �oSorci�re �cpour effectuer cette commande !");
					return true;
				}
				
				if (!LGRoleManager.Power.containsKey(player.getUniqueId())) {
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous avez d�ja utilis� votre capacit� !");
					return true;
				}
				
				String targetName = args[1];
				if (Bukkit.getPlayer(targetName) == null) {
					
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�cErreur, le joueur �e" + targetName + " �c n'est pas connect� �fou �cn'existe pas !");
					return true;
				}
				
				Player Target = Bukkit.getPlayer(targetName);
				
				if (LGRoleManager.mortD.containsKey(Target.getUniqueId())) {
					if (LGRoleManager.mortD.get(Target.getUniqueId()) == LGCamps.VILLAGEOIS) {
						
						LGRoleManager.Power.remove(player.getUniqueId());
						LGSorciere.Revive(Target.getUniqueId());
						return true;
					}
				}
				
				player.sendMessage(" ");
				player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Le joueur �e" + targetName + " �cne peut pas �tre r�anim� !");
				return true;
			}
			
			//SALVATEUR ------------------------------------------------
			if (args[0].equalsIgnoreCase("protect")) {
				
				if (LGRoles.getRole(player.getUniqueId()) != LGRoles.Salvateur) {
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous devez �tre �oSalvateur �cpour effectuer cette commande !");
					return true;
				}
				
				if (!LGRoleManager.Power.containsKey(player.getUniqueId())) {
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous avez d�ja utilis� votre capacit� ou attendu plus de 2 minutes dans cette �pisode ! Attendez le prochain !");
					return true;
				}
				
				String targetName = args[1];
				if (Bukkit.getPlayer(targetName) == null) {
					
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�cErreur, le joueur �e" + targetName + " �c n'est pas connect� �fou �cn'existe pas !");
					return true;
				}
				
				Player Target = Bukkit.getPlayer(targetName);
				
				if (LGSalvateur.Aprotect.contains(Target.getUniqueId())) {
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous ne pouvez pas prot�ger la m�me personne deux �pisodes d'affil� !");
					return true;
				}
				
				LGSalvateur.choose(Target.getUniqueId());
				
				LGRoleManager.Power.remove(player.getUniqueId());
				player.sendMessage(" ");
				player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l] �aVous avez bien prot�g� " + targetName);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("choose")) {
				
				if (LGRoles.getRole(player.getUniqueId()) != LGRoles.EnfantS) {
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous devez �tre �oEnfant Sauvage �cpour effectuer cette commande !");
					return true;
				}
				
				if (!LGRoleManager.Power.containsKey(player.getUniqueId())) {
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous avez d�j� utilis� votre capacit� ou avez attendu plus de 5 min !");
					return true;
				}
				
				String targetName = args[1];
				if (Bukkit.getPlayer(targetName) == null) {
					
					player.sendMessage(" ");
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�cErreur, le joueur �e" + targetName + " �c n'est pas connect� �fou �cn'existe pas !");
					return true;
				}
				
				Player Target = Bukkit.getPlayer(targetName);
				if (Target == player) {
					player.sendMessage("�b�l[�6�lLOUP-GAROUS�b�l]�c Vous ne pouvez pas vous choisir comme mod�le !");
					return true;
				}
				
				LGRoleManager.Power.remove(player.getUniqueId());
				LGEnfantS.choose(player, Target);
				
				return true;
			}
		}
		
		
		return true;
	}

}
