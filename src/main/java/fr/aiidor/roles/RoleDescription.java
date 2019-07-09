package fr.aiidor.roles;

import java.util.UUID;

public class RoleDescription {
	
	public static String desc(UUID p) {
		
		StringBuilder desc = new StringBuilder();
		
		//VILLAGEOIS ------------------------------------------------------------------
		if (LGCamps.getCamp(p) == LGCamps.VILLAGEOIS || (LGRoles.getRole(p) == LGRoles.LGA && !LGRoleManager.Power.containsKey(p)) || LGRoleManager.Infect.contains(p)) {
			String camp = "�9Votre objectif est d'�liminer les Loups-Garous. ";
			desc.append(camp);
			
			if (LGRoles.getRole(p) == LGRoles.Voyante) {
				
				String role = "�9Pour cela, vous disposez de l'effet de Night vision ainsi que d'une capacit� : "
						+ "au d�but de chaque �pisode vous pourrez conna�tre le r�le d'un joueur choisit gr�ce a la commande"
						+ "/lg see <Pseudo> ! ";
				
				String stuff = "\n�6Items de d�part : �94 obsidienne, 4 biblio";
				
				desc.append(role);
				desc.append(stuff);
				return desc.toString(); 
			}
			
			if (LGRoles.getRole(p) == LGRoles.Renard) {
				
				String role = "�9Pour cela, vous disposez de l'effet Speed I ainsi qu'une capacit� : "
						+ "Vous pouvez effectuer 3 fois dans la partie la commande /lg flairer <Pseudo> "
						+ "si la personne cibl� est situ� � moins de 10 blocs. "
						+ "cette commande permet de savoir si la personne est Loup ou Innocent ! ";
				
				desc.append(role);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.Montreur) {
				
				String role = "�9Afin de r�ussir vous disposez d'une capacit� : "
						+ "Au d�but de chaque �pidode, pour chaque loups dans un rayon de 50 blocks, "
						+ "un grognement s'affichera dans le chat !";
				
				desc.append(role);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.Petite) {
				
				String role = "�9Pour cela, vous serez invisible la nuit mais aussi plus faible (Weakness I) ! ";
				String power = "�9Une fois par nuit, vous verrez les pseudos des joueurs situ�s � moins de 100 blocs ! ";
				String stuff = "\n�6Items de d�part : �95 tnt !";
				
				desc.append(role);
				desc.append(power);
				desc.append(stuff);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.Ancien) {
				
				String role = "�9Pour cela, vous disposez d'un effet de Resitance I ainsi que d'un pouvoir : "
						+ "Si vous mourrez par un LG, vous survivrez (une fois) " 
						+ "Si vous mourrez par un non-LG (ou PVE), vous perdrez 1/2 de votre vie et votre effet de resistance ";
				
				
				desc.append(role);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.Sorciere) {
				
				String role = "�9Afin d'y arriver, vous avez le pouvoir de ressusciter un joueur mort dans la partie durant un lapse de temps d�finit."
						+ "Pour se faire, vous pourrez utiliser la commande /lg Revive <Pseudo> ou cliquer sur un message message sp�cifique !";
				String stuff = "\n�6Items de d�part : �94 Potion en spalsh : 3 Potions d'instant health, 3 Potions de d�gats, et 1 Potion de r�gen�ration";
					
				desc.append(role);
				desc.append(stuff);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.Salvateur) {
				
				String role = "�9Une fois par �pisode, vous pourrez donner NoFall & Resistance I (20min) au joueur de votre choix (y compris vous-m�me). "
						+ "Vous ne pourrez juste pas donnez l'effet deux fois sur la m�me personne sur deux �pisodes de suite !";
				String stuff = "\n�6Item de d�part : �92 Potions splash de Heal I";
					
				desc.append(role);
				desc.append(stuff);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.Mineur) {
				
				String role = "�9Pour cela vous pourrez vous stuff plus facilement gr�ce � un effet permanent de Haste II ! ";
				String stuff = "\n�6Item de d�part : �9Pioche en diamant efficacit� II";
					
				desc.append(role);
				desc.append(stuff);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.Cupidon) {
				
				String role = "�9Pour cela vous disposez d'un stuff de d�part ! "
						+ "De plus, vous pourrez mettre 2 personnes en couple et donc de choisir de gagnez avec le Village ou "
						+ "gagnez avec le couple !";
				String stuff = "\n�6Item de d�part : �9Arc Punch I, 64 Fl�ches";
					
				desc.append(role);
				desc.append(stuff);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.EnfantS) {
				
				String role = "�9Vous pourrez choisir un mod�le parmis les joueurs. "
						  	+"Ci celui-ci meurt, vous deviendrez un membre du camps des Loups-Garous !";
					
				desc.append(role);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.SV || LGRoles.getRole(p) == LGRoles.LGA) {
				
				String role = "�9Mais vous ne disposez d'aucun pouvoir particulier !";
				
				desc.append(role);
				return desc.toString();
			}
			
		}
		
		//LG -------------------------------------------------
		if (LGCamps.getCamp(p) == LGCamps.LOUPGAROUS) {
			
			String kill = "�9A chaque kill vous gagnez l'effet speed I pendant 1 minute et deux coeurs d'absorption "
					+ "pendant 1 minute.";
			
			String effet = "�9Afin de r�ussir vous disposez de l'effet de Force I (la nuit) et "
					+ "de Night vision. ";
			
			
			//LGB
			if (LGRoles.getRole(p) == LGRoles.LGB) {
				String camp = "�9Votre objectif est de gagner seul ! ";
				
				String role = "�9Afin de r�ussir vous disposez de l'effet de Force I (la nuit) et "
						+ "de Night vision, ainsi que de 5 coeurs supl�mentaires ! ";
				
				desc.append(camp);
				desc.append(role);
				desc.append(kill);
				return desc.toString();
			}
			
			String camp = "�9Votre objectif est de tuer tous les Villageois ! ";
			
			
			if (LGRoles.getRole(p) == LGRoles.LG) {
				
				desc.append(camp);
				desc.append(effet);
				desc.append(kill);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.LgInfect) {
				
				
				String power = "�9Une fois dans la partie, vous pouvez ressusciter un joueur mort de la main d'un Loup, et le faire passer dans votre camp durant un lapse de temps. "
						+ "Pour se faire, vous pourrez utiliser la commande /lg Infect <Pseudo> ou cliquer sur un message message sp�cifique ! "
						+ "L'infect� conserve son pouvoir.";
				
				desc.append(effet);
				desc.append(kill);
				desc.append(power);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.VilainPL) {
				
				
				kill = "�9A chaque kill vous gagnez deux ceours d'absorption "
						+ "pendant 2 minutes. ";
				
				String power = "�9Vous disposez aussi  de l'effet Speed I (la nuit). Attention � ne pas vous faire rep�rer !";
				
				desc.append(effet);
				desc.append(kill);
				desc.append(power);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.LGA && LGRoleManager.Power.containsKey(p)) {
				
				desc.append(camp);
				desc.append(effet);
				desc.append(kill);
				
				if (LGRoleManager.Power.containsKey(p)) {
					
					String power = "�9Toutes les 5 min, le nom d'un des membres des Loups-Garous vous sera r�v�l� !";
					desc.append(power);
				}
				return desc.toString();
			}
			
			return desc.toString();
		}
		
		
		
		
		
		
		if (LGCamps.getCamp(p) == LGCamps.VOLEUR) {
			
			String role = "�9Vous n'appartenez � aucun camps ! Votre objectif est de r�cuperer un r�le un joueur en le tuant !"
					+ "Pour cela, vous avez l'effet de r�sistance I jusqu'� votre premier meurtre !";
			
			return role;
		}
		
		if (LGCamps.getCamp(p) == LGCamps.ASSASSIN) {
			
			String role = "�9Votre Objectif est de gagner seul, "
					+ "vous ne faite partie d'aucun camp ! "
					+ "Pour y arriver vous disposez de l'effet Force I le jour ainsi que d'un stuff de d�part ! ";
			String stuff = "\n�6Items de d�part : �91 Livre Sharpness III, 1 Livre Protection III et 1 Livre Power III";
			
			desc.append(role);
			desc.append(stuff);
					
			return desc.toString();
		}
		return null;
	}
}
