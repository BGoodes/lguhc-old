package fr.aiidor.roles;

import java.util.UUID;

public class RoleDescription {
	
	public static String desc(UUID p) {
		
		StringBuilder desc = new StringBuilder();
		
		//VILLAGEOIS ------------------------------------------------------------------
		if (LGCamps.getCamp(p) == LGCamps.VILLAGEOIS || (LGRoles.getRole(p) == LGRoles.LGA && !LGRoleManager.Power.containsKey(p)) || LGRoleManager.Infect.contains(p)) {
			String camp = "§9Votre objectif est d'éliminer les Loups-Garous. ";
			desc.append(camp);
			
			if (LGRoles.getRole(p) == LGRoles.Voyante) {
				
				String role = "§9Pour cela, vous disposez de l'effet de Night vision ainsi que d'une capacité : "
						+ "au début de chaque épisode vous pourrez connaître le rôle d'un joueur choisit grâce a la commande"
						+ "/lg see <Pseudo> ! ";
				
				String stuff = "\n§6Items de départ : §94 obsidienne, 4 biblio";
				
				desc.append(role);
				desc.append(stuff);
				return desc.toString(); 
			}
			
			if (LGRoles.getRole(p) == LGRoles.Renard) {
				
				String role = "§9Pour cela, vous disposez de l'effet Speed I ainsi qu'une capacité : "
						+ "Vous pouvez effectuer 3 fois dans la partie la commande /lg flairer <Pseudo> "
						+ "si la personne ciblé est situé à moins de 10 blocs. "
						+ "cette commande permet de savoir si la personne est Loup ou Innocent ! ";
				
				desc.append(role);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.Montreur) {
				
				String role = "§9Afin de réussir vous disposez d'une capacité : "
						+ "Au début de chaque épidode, pour chaque loups dans un rayon de 50 blocks, "
						+ "un grognement s'affichera dans le chat !";
				
				desc.append(role);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.Petite) {
				
				String role = "§9Pour cela, vous serez invisible la nuit mais aussi plus faible (Weakness I) ! ";
				String power = "§9Une fois par nuit, vous verrez les pseudos des joueurs situés à moins de 100 blocs ! ";
				String stuff = "\n§6Items de départ : §95 tnt !";
				
				desc.append(role);
				desc.append(power);
				desc.append(stuff);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.Ancien) {
				
				String role = "§9Pour cela, vous disposez d'un effet de Resitance I ainsi que d'un pouvoir : "
						+ "Si vous mourrez par un LG, vous survivrez (une fois) " 
						+ "Si vous mourrez par un non-LG (ou PVE), vous perdrez 1/2 de votre vie et votre effet de resistance ";
				
				
				desc.append(role);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.Sorciere) {
				
				String role = "§9Afin d'y arriver, vous avez le pouvoir de ressusciter un joueur mort dans la partie durant un lapse de temps définit."
						+ "Pour se faire, vous pourrez utiliser la commande /lg Revive <Pseudo> ou cliquer sur un message message spécifique !";
				String stuff = "\n§6Items de départ : §94 Potion en spalsh : 3 Potions d'instant health, 3 Potions de dégats, et 1 Potion de régenération";
					
				desc.append(role);
				desc.append(stuff);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.Salvateur) {
				
				String role = "§9Une fois par épisode, vous pourrez donner NoFall & Resistance I (20min) au joueur de votre choix (y compris vous-même). "
						+ "Vous ne pourrez juste pas donnez l'effet deux fois sur la même personne sur deux épisodes de suite !";
				String stuff = "\n§6Item de départ : §92 Potions splash de Heal I";
					
				desc.append(role);
				desc.append(stuff);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.Mineur) {
				
				String role = "§9Pour cela vous pourrez vous stuff plus facilement grâce à un effet permanent de Haste II ! ";
				String stuff = "\n§6Item de départ : §9Pioche en diamant efficacité II";
					
				desc.append(role);
				desc.append(stuff);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.Cupidon) {
				
				String role = "§9Pour cela vous disposez d'un stuff de départ ! "
						+ "De plus, vous pourrez mettre 2 personnes en couple et donc de choisir de gagnez avec le Village ou "
						+ "gagnez avec le couple !";
				String stuff = "\n§6Item de départ : §9Arc Punch I, 64 Flèches";
					
				desc.append(role);
				desc.append(stuff);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.EnfantS) {
				
				String role = "§9Vous pourrez choisir un modèle parmis les joueurs. "
						  	+"Ci celui-ci meurt, vous deviendrez un membre du camps des Loups-Garous !";
					
				desc.append(role);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.SV || LGRoles.getRole(p) == LGRoles.LGA) {
				
				String role = "§9Mais vous ne disposez d'aucun pouvoir particulier !";
				
				desc.append(role);
				return desc.toString();
			}
			
		}
		
		//LG -------------------------------------------------
		if (LGCamps.getCamp(p) == LGCamps.LOUPGAROUS) {
			
			String kill = "§9A chaque kill vous gagnez l'effet speed I pendant 1 minute et deux coeurs d'absorption "
					+ "pendant 1 minute.";
			
			String effet = "§9Afin de réussir vous disposez de l'effet de Force I (la nuit) et "
					+ "de Night vision. ";
			
			
			//LGB
			if (LGRoles.getRole(p) == LGRoles.LGB) {
				String camp = "§9Votre objectif est de gagner seul ! ";
				
				String role = "§9Afin de réussir vous disposez de l'effet de Force I (la nuit) et "
						+ "de Night vision, ainsi que de 5 coeurs suplémentaires ! ";
				
				desc.append(camp);
				desc.append(role);
				desc.append(kill);
				return desc.toString();
			}
			
			String camp = "§9Votre objectif est de tuer tous les Villageois ! ";
			
			
			if (LGRoles.getRole(p) == LGRoles.LG) {
				
				desc.append(camp);
				desc.append(effet);
				desc.append(kill);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.LgInfect) {
				
				
				String power = "§9Une fois dans la partie, vous pouvez ressusciter un joueur mort de la main d'un Loup, et le faire passer dans votre camp durant un lapse de temps. "
						+ "Pour se faire, vous pourrez utiliser la commande /lg Infect <Pseudo> ou cliquer sur un message message spécifique ! "
						+ "L'infecté conserve son pouvoir.";
				
				desc.append(effet);
				desc.append(kill);
				desc.append(power);
				return desc.toString();
			}
			
			if (LGRoles.getRole(p) == LGRoles.VilainPL) {
				
				
				kill = "§9A chaque kill vous gagnez deux ceours d'absorption "
						+ "pendant 2 minutes. ";
				
				String power = "§9Vous disposez aussi  de l'effet Speed I (la nuit). Attention à ne pas vous faire repérer !";
				
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
					
					String power = "§9Toutes les 5 min, le nom d'un des membres des Loups-Garous vous sera révélé !";
					desc.append(power);
				}
				return desc.toString();
			}
			
			return desc.toString();
		}
		
		
		
		
		
		
		if (LGCamps.getCamp(p) == LGCamps.VOLEUR) {
			
			String role = "§9Vous n'appartenez à aucun camps ! Votre objectif est de récuperer un rôle un joueur en le tuant !"
					+ "Pour cela, vous avez l'effet de résistance I jusqu'à votre premier meurtre !";
			
			return role;
		}
		
		if (LGCamps.getCamp(p) == LGCamps.ASSASSIN) {
			
			String role = "§9Votre Objectif est de gagner seul, "
					+ "vous ne faite partie d'aucun camp ! "
					+ "Pour y arriver vous disposez de l'effet Force I le jour ainsi que d'un stuff de départ ! ";
			String stuff = "\n§6Items de départ : §91 Livre Sharpness III, 1 Livre Protection III et 1 Livre Power III";
			
			desc.append(role);
			desc.append(stuff);
					
			return desc.toString();
		}
		return null;
	}
}
