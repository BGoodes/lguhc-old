package fr.aiidor.role;

import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;

public class LGDecription {
	
	private Joueur j;
	private Player p;
	
	private String gameTag = "§b§l[§6§lLOUP-GAROUS§b§l] §r";
	
	public LGDecription(Joueur j, LGUHC main) {
		this.j = j;
		this.p = j.getPlayer();
		this.gameTag = main.gameTag;
	}
	
	public void sendDescription() {
		
		StringBuilder roleName = new StringBuilder();
		
		if (j.getRole() == LGRoles.LGA && j.getPower() == 0) {
			roleName.append(gameTag + "§9[Privé] Vous êtes §o§9" + LGRoles.SV.name);
		} else {
			roleName.append(gameTag + "§9[Privé] Vous êtes §o§9" + j.getRole().name);
		}
		
		if (j.isInfect()) roleName.append(" §4Infecté");
		
		else if (j.getRole() == LGRoles.EnfantS && j.getCamp() == LGCamps.LoupGarou) {
			roleName.append(" §4Transformé");
		}
		
		roleName.append(". ");
		p.sendMessage(roleName.toString());
		
		
		LGRoles role = j.getRole();
		LGCamps camp = j.getCamp();
		
		StringBuilder desc = new StringBuilder();
		
		if (camp == LGCamps.Village && role != LGRoles.LGA) desc.append("§9Votre objectif est d'éliminer les Loups-Garous. ");
		if (camp == LGCamps.LoupGarou && role != LGRoles.LGA) desc.append("§9Votre objectif est de tuer tous les Villageois ! ");
		if (camp == LGCamps.Voleur) desc.append("§9Vous n'appartenez à aucun camps ! Votre objectif est de récuperer un rôle un joueur en le tuant ! ");
		if (camp == LGCamps.LGB || camp == LGCamps.Assassin ) desc.append("§9Votre objectif est de gagner seul ! ");
		
		if (role == LGRoles.LGA) {
			
			if (j.getRole() == LGRoles.LGA && j.getPower() == 0) {
				desc.append("§9Votre objectif est d'éliminer les Loups-Garous. ");
				desc.append("§9Mais vous ne disposez d'aucun pouvoir particulier !");
				j.getPlayer().sendMessage(desc.toString());
				return;
				
			} else {
				desc.append("§9Votre objectif est de tuer tous les Villageois ! ");
			}
		}
		
		if (role == LGRoles.SV) {
			
			desc.append("§9Mais vous ne disposez d'aucun pouvoir particulier !");
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Bouc) {
			
			desc.append("§9Mais vous ne disposez d'aucun pouvoir particulier ! Si les villageois se retrouvent indécis, c'est vous qui prendrez le vote !");
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Voyante || role == LGRoles.VoyanteB) {
			
			String role_Use = "§9Pour cela, vous disposez de l'effet de Night vision ainsi que d'une capacité : "
					+ "au début de chaque épisode vous pourrez connaître le rôle d'un joueur choisit grâce a la commande"
					+ "/lg see <Pseudo> ! ";
			
			String stuff = "\n§6Items de départ : §94 obsidienne, 4 biblio";
			
			desc.append(role_Use);
			desc.append(stuff);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Renard) {
			
			String role_Use = "§9Pour cela, vous disposez de l'effet Speed I ainsi qu'une capacité : "
					+ "Vous pouvez effectuer 3 fois dans la partie la commande /lg flairer <Pseudo> "
					+ "si la personne ciblée est située à moins de 10 blocs. "
					+ "Cette commande permet de savoir si celle ci est Loup ou Innocent ! ";

			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.MontreurDours) {
			
			String role_Use = "§9Afin de réussir vous disposez d'une capacité : "
					+ "Au début de chaque épidode, pour chaque loups dans un rayon de 50 blocs, "
					+ "un grognement s'affichera dans le chat !";
			
			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Mineur) {
			
			String role_Use = "§9Pour cela vous pourrez vous faire un équippement plus facilement grâce à un effet permanent de Haste II ! ";
			String stuff = "\n§6Item de départ : §9Pioche en diamant efficacité II";
			
			desc.append(role_Use);
			desc.append(stuff);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.PetiteFille) {
			
			String role_Use = "§9Pour cela, vous serez invisible la nuit mais aussi plus faible (Weakness I) ! ";
			String power = "§9Une fois par nuit, vous verrez les pseudos des joueurs situés à moins de 100 blocs ! ";
			String stuff = "\n§6Items de départ : §95 tnt !";
			
			desc.append(role_Use);
			desc.append(power);
			desc.append(stuff);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Ancien) {
			
			String role_Use = "§9Pour cela, vous disposez d'un effet de Resistance I ainsi que d'un pouvoir :\n"
					+ "- Si vous mourrez par un LG, vous survivrez (une fois)\n" 
					+ "- Si vous mourrez par un non-LG (ou PVE), vous mourrerrez directement !\n"
					+ "- Si en plus votre meurtrier est du Village celui ci perdra sa vie de moitié ainsi que ses pouvoirs !";
			
			
			
			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Sorciere) {
			
			String role_Use = "§9Afin d'y arriver, vous avez le pouvoir de ressusciter un joueur mort dans la partie durant un lapse de temps définit."
					+ "Pour se faire, vous pourrez utiliser la commande /lg Revive <Pseudo> ou cliquer sur un message message spécifique !";
			String stuff = "\n§6Items de départ : §94 Potion en spalsh : 3 Potions d'instant health, 3 Potions de dégats, et 1 Potion de régenération";
				
			desc.append(role_Use);
			desc.append(stuff);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Chasseur) {
			
			String role_Use = "§9Pour ce faire, à votre mort vous pouvez choisir un joueur qui perdra la moitié de sa vie grâce à la commande"
					+ " /lg pan <Pseudo>";
			String stuff = "\n§6Items de départ : §9Arc Power IV, 128 flèches, 3 oeufs de loups, 15 os";
				
			desc.append(role_Use);
			desc.append(stuff);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Cupidon) {
			
			String role_Use = "§9Pour cela vous disposez d'un stuff de départ ! "
					+ "De plus, vous pourrez mettre 2 personnes en couple et donc de choisir de gagnez avec le Village ou "
					+ "gagnez avec le couple !";
			
			String stuff = "\n§6Items de départ : §9Arc Punch I, 64 flèches";
				
			desc.append(role_Use);
			desc.append(stuff);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Ange) {
			
			String role_Use = "§9Vous disposez d'un avantage lors du vote. En effet, tous les deux votes orientés contre vous permetterons de gagner "
					+ "1 coeur potentiel ! Mais attention à ne pas recevoir trop de vote et par conséquent de perdre la moitié de votre vie !";
				
			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Trublion) {
			
			String role_Use = "§9Pour ce faire, vous pourrez échanger le rôle de 2 joueurs. De plus, si vous mourrez, "
					+ "tous les joueurs seront téléportés individuellement à des endroits aléatoire de la map !";
			
			
			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Citoyen) {
			
			String role_Use = "§9Pour ce faire, vous avez la possiblité de consulterles votes des joueurs 2 fois dans la partie. Pour cela,"
					+ " vous devrez cliquer sur le message qui vous y invitera, entre la clotûre des votes et la révélation du résultat !";
			
			
			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Salvateur) {
			
			String role_Use = "§9Pour y arriver, à chaque début d'épisode, vous pouvez choisir un joueur (y compris vous même) a qui vous conférez "
					+ "NoFall et résistance pendant 20min, à l'aide la commande /lg protect. Vous ne pouvez pas choisir 2 fois d'afilée la même personne !";
			
			String stuff = "\n§6Items de départ : §92 Potions de Heal";
				
			desc.append(role_Use);
			desc.append(stuff);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Soeur) {
			
			String role_Use = "§9Pour y parvenir, vous diposez d'un allié qui détient le même rôle que vous : votre soeur ! Si vous êtes à moins de 10 blocs "
					+ "l'un de l'autre, vous disposerez de l'effet de resistance. De plus, si votre soeur meurt, vous pouvez choisir de voir le Pseudo OU le Rôle du "
					+ "Joueur qui l'a tué !";
				
			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			
			if (j.Soeur != null)  {
				
				String soeur = j.Soeur.getName();
				if (j.Soeur.isDead()) soeur = "§m" + j.Soeur.getName();
				j.getPlayer().sendMessage("§b§l[§6§lLOUP-GAROUS§b§l] §dLe pseudo de votre soeur est §l" + soeur);
				
			}
			else j.getPlayer().sendMessage("§b§l[§6§lLOUP-GAROUS§b§l] §dMalheuresement, vous n'avez pas de Soeur !");
			return;
		}
		
		if (role == LGRoles.Chaman) {
			
			String role_Use = "§9Pour ce faire, vous pouvez \"écouter les morts\" pendant 30 secondes 1 nuit sur 2 ! Les messages des morts seront anonymes, a vous de "
					+ "démêler le vrai du faux !";
				
			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Corbeau) {
			
			desc.append("§9Pour ce faire, tous les jours, vous pourrez jeter votre malédiction sur un joueur : en effet, lorsque "
					+ "vous effectuez un vote, celui-ci compte triple !");
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Pyromane) {
			
			String role_Use = "§9Pour y parvenir, vous disposez d'un équippement de départ et de l'effet de résistance au feu !";
			String stuff = "\n§6Items de départ : §91 Livre Fire Aspect I, 1 Livre Flame, 2 Seaux de lave, 1 Briquet";
				
			desc.append(role_Use);
			desc.append(stuff);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		
		//SOLO
		
		if (camp == LGCamps.Voleur) {
			
			String role_use ="Pour cela, vous avez l'effet de résistance I jusqu'à votre premier meurtre !";
			desc.append(role_use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		
		if (camp == LGCamps.Assassin) {
			
			String role_use = "§9Votre Objectif est de gagner seul, "
					+ "vous ne faite partie d'aucun camp ! "
					+ "Pour y arriver vous disposez de l'effet Force I le jour ainsi que d'un stuff de départ ! ";
			String stuff = "\n§6Items de départ : §91 Livre Sharpness III, 1 Livre Protection III et 1 Livre Power III";
			
			desc.append(role_use);
			desc.append(stuff);
					
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.EnfantS) {
			
			String role_use = "§9Vous pourrez choisir un modèle parmis les joueurs. "
				  	+"Ci celui-ci meurt, vous deviendrez un membre du camp des Loups-Garous !";
			
			desc.append(role_use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		
		//LG
		if (j.isLg()) {
			String kill = "§9A chaque kill vous gagnez l'effet speed I pendant 1 minute et deux coeurs d'absorption "
					+ "pendant 1 minute. ";
			
			String effet = "§9Afin de réussir vous disposez de l'effet de Force I (la nuit) et "
					+ "de Night vision. ";
			
			
			if (role == LGRoles.LGB) {
				
				String role_use = "§9Afin de réussir vous disposez de l'effet de Force I (la nuit) et "
						+ "de Night vision, ainsi que de 5 coeurs suplémentaires ! ";
				
				desc.append(role_use);
				desc.append(kill);
			}
			
			if (role == LGRoles.LG) {
				
				desc.append(effet);
				desc.append(kill);
			}
			
			if (role == LGRoles.LGA) {
				
				desc.append(effet);
				desc.append(kill);
				if (j.getPower() == 1) {
					desc.append("§cFaites attention cependant, vos aliés ne vous reconnaitrons que dans 5 min !");
				}
				
				if (j.getPower() == 2) {
					desc.append("§9Toutes les 5 minutes, le nom d'un des Loups-Garous vous sera révélé !");
				}
			}
			
			if (role == LGRoles.VPL) {
				
				String role_use = "§9Mais contrairement aux autres Loups-Garous vous disposez aussi de Speed I durant la nuit !" +
				" Attention à ne pas vous faire repèrer ! ";
				
				desc.append(effet);
				desc.append(role_use);
			}
			
			if (role == LGRoles.IPL) {
				
				
				String power = "§9Une fois dans la partie, vous pouvez ressusciter un joueur mort de la main d'un Loup, et le faire passer dans votre camp durant un lapse de temps. "
						+ "Pour se faire, vous pourrez utiliser la commande /lg Infect <Pseudo> ou cliquer sur un message message spécifique ! "
						+ "L'infecté conserve son pouvoir.";
				
				desc.append(effet);
				desc.append(kill);
				desc.append(power);
			}
			
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
	}

}
