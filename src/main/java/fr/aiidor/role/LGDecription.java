package fr.aiidor.role;

import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.use.LGAnge;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

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
		
		roleName.append(gameTag + "§9[Privé] Vous êtes §o§9" + j.getRole().name);
		
		
		if (j.getRole() == LGRoles.Ange) {
			if (j.ange != null) {
				
				if (j.ange == LGAnge.Déchu) {
					roleName.append(" §cDéchu");
				}
				if (j.ange == LGAnge.Gardien) {
					roleName.append(" §aGardien");
				}
			}
		}
		if (j.Rob) roleName.append(" §c(Role volé)");
			
		if (j.isInfect()) roleName.append(" §4Infecté");
		
		else if (j.getRole() == LGRoles.EnfantS && j.getCamp() == LGCamps.LoupGarou) {
			roleName.append(" §4Transformé");
		}
		
		roleName.append(". ");
		p.sendMessage(roleName.toString());
		
		
		LGRoles role = j.getRole();
		LGCamps camp = j.getCamp();
		
		StringBuilder desc = new StringBuilder();
		
		if (camp == LGCamps.LGB || camp == LGCamps.Assassin) desc.append("§9Votre objectif est de gagner seul ! ");
		if (camp == LGCamps.Neutre) desc.append("§9Vous ne faites partie d'aucun camp ! ");
		if (camp == LGCamps.Ange) {
			
			if (j.ange == LGAnge.Gardien) {
				desc.append("§9Votre objectif est de gagner seul ou avec votre protégé !");
			}
			else if (j.ange == LGAnge.Déchu) {
				desc.append("§9Votre objectif est de gagner seul ! ");
				
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
					+ "/lg see <Pseudo>. Mais faites attention cependant, si jamais vous utilisez votre pouvoir sur un membre du village, "
					+ "vous perdrez 3 coeurs et vous ne pourrez pas utiliser votre pouvoir l'épisode suivant !";
			
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
					+ "Cette commande permet de savoir si celle ci est Loup ou Innocent ! "
					+ "Mais attention, vous ne pouvez flairer que la nuit et les joueurs flairés en sont informés au début de l'épisode qui suit.";

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
			
			String role_Use = "§9Pour cela, vous serez invisible lorsque votre armure est retiré la nuit mais vous êtes aussi plus faible (Weakness I) ! "
					+ "Lorque que vous êtes invisible, le Loup garous Perfide peut voir des particules sous vos pied et quand celui ci est invisible vous voyez aussi les même particules sous ses pied. "
					+ "§9De plus, à la tombé de la nuit, vous verrez les pseudos des joueurs situés à moins de 100 blocs ! ";
			
			String stuff = "\n§6Items de départ : §95 tnt !";
			
			desc.append(role_Use);
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
					+ "Pour se faire, vous pourrez choisir de cliquer sur un message spécial qui apparaîtra avant la mort d'un joueur. Vous ne pouvez "
					+ "cependant pas vous réanimer vous même !";
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
					+ "De plus, vous pourrez mettre 2 personnes en couple et donc de choisir de gagner avec le Village ou "
					+ "gagner avec le couple !";
			
			String stuff = "\n§6Items de départ : §9Arc Punch I, 64 flèches";
				
			desc.append(role_Use);
			desc.append(stuff);
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
			
			String role_Use = "§9Pour ce faire, vous avez la possiblité de consulter les votes des joueurs 2 fois dans la partie. Pour cela, "
					+ "vous devrez cliquer sur le message qui vous y invitera, entre la fermeture des votes et la révélation du résultat ! "
					+ "De plus, vous pourrez une fois dans la partie annuler la sentence d'un vote afin que celui ci soit annulé grâce a la commande /lg cancelVote "
					+ "ou en cliquant sur un message qui vous propose d'annuler celle ci.";
			
			
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
			
			String role_Use = "§9Pour y parvenir, vous diposez d'un allié qui détient le même rôle que vous : votre soeur ! Si vous êtes à moins de 20 blocs "
					+ "l'une de l'autre, vous disposerez de l'effet de resistance. De plus, si votre soeur meurt, vous pouvez choisir de voir le Pseudo OU le Rôle du "
					+ "Joueur qui l'a tué !";
				
			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			
			if (j.Soeur != null)  {
				
				String soeur = j.Soeur.getName();
				if (j.Soeur.isDead()) soeur = "§m" + j.Soeur.getName();
				j.getPlayer().sendMessage(gameTag + "§dLe pseudo de votre soeur est §l" + soeur);
				
			}
			else j.getPlayer().sendMessage(gameTag + "§dMalheuresement, vous n'avez pas de Soeur !");
			return;
		}
		
		if (role == LGRoles.Detective) {
			
			String role_use ="§9Pour cela, à chaque épisode, vous pourrez choisir deux joueurs. "
					+ "Vous saurez alors si oui ou non les joueurs choisis sont dans le même camp (sans connaître le nom du camp)."
					+ "Vous ne pouvez cependant pas tester 2 fois le même joueur";
			desc.append(role_use);
			j.getPlayer().sendMessage(desc.toString());
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
			
			desc.append("§9Pour ce faire, tous les jours, vous pourrez jeter votre malédiction sur plusieurs joueurs : en effet, lorsque "
					+ "vous effectuez un vote, vous avez la possiblilité d'écrire plusieurs pseudos qui peuvent être différents comme identiques.");
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
		
		if (role == LGRoles.Voleur) {
			
			String role_use ="Votre objectif est de récuperer un rôle un joueur en le tuant ! Pour cela, vous avez l'effet de résistance I jusqu'à votre premier meurtre !";
			desc.append(role_use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		
		if (role == LGRoles.Assassin) {
			

			String role_use = "Pour y arriver vous disposez de l'effet Force I le jour ainsi que d'un stuff de départ ! ";
			String stuff = "\n§6Items de départ : §91 Livre Sharpness III, 1 Livre Protection III et 1 Livre Efficacité III / Unbreaking III";
			
			desc.append(role_use);
			desc.append(stuff);
					
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		
		if (role == LGRoles.Ange) {
			if (j.ange == null) {
				
				String role_use = "§9Mais avant, vous allez devoir choisir entre devenir un Ange Déchu ou un Ange Gardien ! ";
				
				desc.append(role_use);
				j.getPlayer().sendMessage(desc.toString());
				
				TextComponent base = new TextComponent("§9Vous allez devoir choisir entre devenir avant les 10 prochaines minutes si vous serez ");
				
				TextComponent msg = new TextComponent("§a§lAnge Gardien");
				
				msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
						"§aVotre objectif est de gagner seul ou avec votre allié. En effet, un joueur vous sera attribué aléatoirement. "
						+ "Vous pouvez lui donner une seule fois dans la partie Regeneration I pendant 1 minute grâce à la commande /lg regen. "
						+ "Mais si il meurt, vous repasserez à 10 coeurs et vous obtiendrez Weakness I pour le reste de la partie."
						
						).create()));
				
				msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg angeType gardien"));
				
				TextComponent msg2 = new TextComponent("§c§lAnge Déchu");
				
				msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
						"§cVotre objectif est de gagner seul. Pour cela, un joueur vous sera attribué aléatoirement et sera par conséquent votre sible. "
						+ "Si vous tuez votre cible vous passerez à 15 coeurs jusqu’à la fin de la partie."
						
						).create()));
				
				msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg angeType dechu"));
				
				TextComponent space = new TextComponent(" §9ou ");
				
				j.getPlayer().spigot().sendMessage(base, msg, space, msg2);
				j.getPlayer().sendMessage("");
				return;
			}
			
			String name = "null";
			
			if (j.angeTarget != null) {
				name = j.angeTarget.getName();
			}
			
				
			//ANGE TYPE
			if (j.ange == LGAnge.Déchu) {
				
				
				String target = "\n" + gameTag + "§dLe joueur que vous devez tuer est §l" + name;
				
				if (name == "null") {
					target = "§eVous n'avez pas de cible !";
				}
				
				String role_use;
				
				if (!j.angeTarget.isDead()) {
					role_use = "§9Pour ce faire, vous disposez 12 coeurs. Votre avez une cible et si vous le tuez, vous passerez à 15 coeurs jusqu’à la fin de la partie. ";
				} else {
					if (j.getPower() == 1) {
						role_use = "§9Vous avez tué votre cible ! Vous avez donc désormais 15 coeurs ! ";
					} else {
						role_use = "§9Votre cible est morte et vous n'avez pas réussi à la tuer. Vous resterez donc à 12 coeurs. ";
					}
				}
				
				
				desc.append(role_use);
				desc.append(target);
				j.getPlayer().sendMessage(desc.toString());
				return;
			}
			
			if (j.ange == LGAnge.Gardien) {
				
				String target = "\n" + gameTag + "§dLe joueur que vous devez protéger est §l" + name;
				
				if (name == "null") {
					target = "§eVous n'avez pas de protégé !";
				}
				
				String role_use;
				
				if (!j.angeTarget.isDead()) {
					 role_use = "§9Pour ce faire vous disposez de 12 coeurs. Vous avez un protégé et vous pouvez gagner avec lui. "
							+ "Vous pouvez lui donner une seule fois dans la partie Regeneration I pendant 1 minute grâce à la commande /lg regen. "
							+ "Si il meurt, vous repasserez à 10 coeurs et vous obtiendrez Weakness I pour le reste de la partie. ";
				} else {
					 role_use = "§9Votre protégé est mort ! Vous êtes donc passé à 10 coeurs et vous obtenez l'effet Weakness I pour le reste de la partie !";	
				}

				
				desc.append(role_use);
				desc.append(target);
				j.getPlayer().sendMessage(desc.toString());
				return;
			}
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
			String kill = "§9A chaque kill vous gagnez l'effet speed I pendant et deux coeurs d'absorption "
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
				
				String power = "";
				
				if (j.getPower() == 0) {
					 power = "§9Mais les autres loups ne vous reconnaissent pas ! Tuer un membre du village pour prouver votre valeur et pour vous souvenir de vos confrères !";
				}
				
				if (j.getPower() == 1) {
					 power = "§9Toutes les 5 minutes, vous vous souviendrez d'un de vos confères";
				}
				
				desc.append(effet);
				desc.append(kill);
				desc.append(power);
			}
			
			if (role == LGRoles.VPL) {
				
				String role_use = "§9Mais contrairement aux autres Loups-Garous vous disposez aussi de Speed I durant la nuit !" +
				" Attention à ne pas vous faire repèrer ! ";
				
				desc.append(effet);
				desc.append(role_use);
			}
			
			if (role == LGRoles.IPL) {
				
				
				String power = "\n§9De plus, ne fois dans la partie, vous pouvez ressusciter un joueur mort de la main d'un Loup, et le faire passer dans votre camp durant un lapse de temps. "
						+ "Pour se faire, vous pourrez utiliser la commande /lg Infect <Pseudo> ou cliquer sur un message message spécifique ! "
						+ "L'infecté conserve son pouvoir.";
				
				desc.append(effet);
				desc.append(kill);
				desc.append(power);
			}
			
			if (role == LGRoles.LGP) {
				
				String power = "\n§9De plus, vous disposez de l'effet d'invisibilité lorsque votre armure est retiré la nuit mais vous serez aussi plus faible (Weakness I). "
						+ "Lorque que vous êtes invisible, la petite fille peut voir des particules sous vos pied et quand celle ci est invisible vous voyez aussi les même particules sous ses pied. ";
				
				desc.append(kill);
				desc.append(power);
			}
			
			if (role == LGRoles.LGFeutre) {
				
				String power = "De plus, à chaque épisode, un rôle de façade vous sera attribué aléatoirement parmis les rôles des joueurs encore en vie. " 
				+  "Ce rôle de façade sera perçu par la voyante, par le renard, par le montreur d'ours ainsi que par le détective comme étant votre vrai rôle.";
				
				desc.append(effet);
				desc.append(kill);
				desc.append(power);
			}
			
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
	}

}
