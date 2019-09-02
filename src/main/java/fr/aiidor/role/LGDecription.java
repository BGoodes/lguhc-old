package fr.aiidor.role;

import org.bukkit.entity.Player;

import fr.aiidor.game.Joueur;

public class LGDecription {
	
	private Joueur j;
	private Player p;
	
	private String gameTag = "�b�l[�6�lLOUP-GAROUS�b�l] �r";
	
	public LGDecription(Joueur j) {
		this.j = j;
		this.p = j.getPlayer();
	}
	
	public void sendDescription() {
		
		StringBuilder roleName = new StringBuilder();
		
		if (j.getRole() == LGRoles.LGA && !j.isLg() && j.getPower() == 0) {
			roleName.append(gameTag + "�9[Priv�] Vous �tes �o�9" + LGRoles.SV.name);
		} else {
			roleName.append(gameTag + "�9[Priv�] Vous �tes �o�9" + j.getRole().name);
		}
		
		if (j.isInfect()) roleName.append(" �4Infect�");
		
		else if (j.getRole() == LGRoles.EnfantS && j.getCamp() == LGCamps.LoupGarou) {
			roleName.append(" �4Transform�");
		}
		
		roleName.append(". ");
		p.sendMessage(roleName.toString());
		
		
		LGRoles role = j.getRole();
		LGCamps camp = j.getCamp();
		
		StringBuilder desc = new StringBuilder();
		
		if (camp == LGCamps.Village) desc.append("�9Votre objectif est d'�liminer les Loups-Garous. ");
		if (camp == LGCamps.LoupGarou) desc.append("�9Votre objectif est de tuer tous les Villageois ! ");
		if (camp == LGCamps.Voleur) desc.append("�9Vous n'appartenez � aucun camps ! Votre objectif est de r�cuperer un r�le un joueur en le tuant ! ");
		if (camp == LGCamps.LGB || camp == LGCamps.Assassin ) desc.append("�9Votre objectif est de gagner seul ! ");
		
		if (role == LGRoles.SV) {
			
			desc.append("�9Mais vous ne disposez d'aucun pouvoir particulier !");
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Bouc) {
			
			desc.append("�9Mais vous ne disposez d'aucun pouvoir particulier ! Si les villageois se retrouvent ind�cis, c'est vous qui prendrez le vote !");
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Voyante || role == LGRoles.VoyanteB) {
			
			String role_Use = "�9Pour cela, vous disposez de l'effet de Night vision ainsi que d'une capacit� : "
					+ "au d�but de chaque �pisode vous pourrez conna�tre le r�le d'un joueur choisit gr�ce a la commande"
					+ "/lg see <Pseudo> ! ";
			
			String stuff = "\n�6Items de d�part : �94 obsidienne, 4 biblio";
			
			desc.append(role_Use);
			desc.append(stuff);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Renard) {
			
			String role_Use = "�9Pour cela, vous disposez de l'effet Speed I ainsi qu'une capacit� : "
					+ "Vous pouvez effectuer 3 fois dans la partie la commande /lg flairer <Pseudo> "
					+ "si la personne cibl�e est situ�e � moins de 10 blocs. "
					+ "Cette commande permet de savoir si celle ci est Loup ou Innocent ! ";

			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.MontreurDours) {
			
			String role_Use = "�9Afin de r�ussir vous disposez d'une capacit� : "
					+ "Au d�but de chaque �pidode, pour chaque loups dans un rayon de 50 blocs, "
					+ "un grognement s'affichera dans le chat !";
			
			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Mineur) {
			
			String role_Use = "�9Pour cela vous pourrez vous stuff plus facilement gr�ce � un effet permanent de Haste II ! ";
			String stuff = "\n�6Item de d�part : �9Pioche en diamant efficacit� II";
			
			desc.append(role_Use);
			desc.append(stuff);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.PetiteFille) {
			
			String role_Use = "�9Pour cela, vous serez invisible la nuit mais aussi plus faible (Weakness I) ! ";
			String power = "�9Une fois par nuit, vous verrez les pseudos des joueurs situ�s � moins de 100 blocs ! ";
			String stuff = "\n�6Items de d�part : �95 tnt !";
			
			desc.append(role_Use);
			desc.append(power);
			desc.append(stuff);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Ancien) {
			
			String role_Use = "�9Pour cela, vous disposez d'un effet de Resistance I ainsi que d'un pouvoir : \n"
					+ "- Si vous mourrez par un LG, vous survivrez (une fois) \n" 
					+ "- Si vous mourrez par un non-LG (ou PVE), vous perdrez 1/2 de votre vie et votre effet de resistance ";
			
			
			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Sorci�re) {
			
			String role_Use = "�9Afin d'y arriver, vous avez le pouvoir de ressusciter un joueur mort dans la partie durant un lapse de temps d�finit."
					+ "Pour se faire, vous pourrez utiliser la commande /lg Revive <Pseudo> ou cliquer sur un message message sp�cifique !";
			String stuff = "\n�6Items de d�part : �94 Potion en spalsh : 3 Potions d'instant health, 3 Potions de d�gats, et 1 Potion de r�gen�ration";
				
			desc.append(role_Use);
			desc.append(stuff);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Chasseur) {
			
			String role_Use = "�9Pour ce faire, a votre mort vous pouvez choisir un joueur qui perdra la moiti� de sa vie gr�ce � la commande"
					+ " /lg pan <Pseudo>";
			String stuff = "\n�6Items de d�part : �9Arc Power IV, 128 fl�ches, 3 oeufs de loups, 15 os";
				
			desc.append(role_Use);
			desc.append(stuff);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Cupidon) {
			
			String role_Use = "�9Pour cela vous disposez d'un stuff de d�part ! "
					+ "De plus, vous pourrez mettre 2 personnes en couple et donc de choisir de gagnez avec le Village ou "
					+ "gagnez avec le couple !";
			
			String stuff = "\n�6Items de d�part : �9Arc Punch I, 64 fl�ches";
				
			desc.append(role_Use);
			desc.append(stuff);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Ange) {
			
			String role_Use = "�9Vous disposez d'un avantage lors du vote. En effet, tous les deux votes orient�s contre vous permetterons de gagner "
					+ "1 coeur potentiel ! Mais attention � ne pas recevoir trop de vote et par cons�quent de perdre la moiti� de votre vie !";
				
			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Trublion) {
			
			String role_Use = "�9Pour ce faire, vous pourrez �changer le r�le de 2 joueurs. De plus, si vous mourrez, "
					+ "tous les joueurs seront t�l�port�s individuellement � des endroits al�atoire de la map !";
			
			
			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Citoyen) {
			
			String role_Use = "�9Pour ce faire, vous avez la possiblit� de consulterles votes des joueurs 2 fois dans la partie. Pour cela,"
					+ " vous devrez cliquer sur le message qui vous y invitera, entre la clot�re des votes et la r�v�lation du r�sultat !";
			
			
			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Salvateur) {
			
			String role_Use = "�9Pour y arriver, � chaque d�but d'�pisode, vous pouvez choisir un joueur (y compris vous m�me) a qui vous conf�rez "
					+ "NoFall et r�sistance pendant 20min, � l'aide la commande /lg protect. Vous ne pouvez pas choisir 2 d'afil�e la m�me personne !";
			
			String stuff = "\n�6Items de d�part : �92 Potions de Heal";
				
			desc.append(role_Use);
			desc.append(stuff);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Soeur) {
			
			String role_Use = "�9Pour y parvenir, vous diposez d'un alli� qui d�tient le m�me r�le que vous : votre soeur ! Si vous �tes � moins de 10 blocs "
					+ "l'un de l'autre, vous disposerez de l'effet de resistance. De plus, si votre soeur meurt, vous pouvez choisir de voir le Pseudo OU le R�le du "
					+ "Joueur qui l'a tu� !";
				
			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			
			if (j.Soeur != null)  {
				
				String soeur = j.Soeur.getName();
				if (j.Soeur.isDead()) soeur = "�m" + j.Soeur.getName();
				j.getPlayer().sendMessage("�b�l[�6�lLOUP-GAROUS�b�l] �dLe pseudo de votre soeur est �l" + soeur);
				
			}
			else j.getPlayer().sendMessage("�b�l[�6�lLOUP-GAROUS�b�l] �dMalheuresement, vous n'avez pas de Seour !");
			return;
		}
		
		if (role == LGRoles.Chaman) {
			
			String role_Use = "�9Pour ce faire, vous pouvez \"�couter les morts\" pendant 30 secondes 1 nuit sur 2 ! Les messages des morts seront anonymes, a vous de "
					+ "d�m�ler le vrai du faux !";
				
			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Corbeau) {
			
			desc.append("�9Pour ce faire, tous les jours, vous pourrez jeter votre mal�diction sur un joueur : en effet, lorsque "
					+ "vous effectuez un vote, celui-ci compte triple !");
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.Pyromane) {
			
			String role_Use = "�9Pour y parvenir, vous disposez d'un �quippement de d�part et de l'effet de r�sistance au feu !";
			String stuff = "\n�6Items de d�part : �91 Livre Fire Aspect I, 1 Livre Flame, 2 Seaux de lave, 1 Briquet";
				
			desc.append(role_Use);
			desc.append(stuff);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		
		//SOLO
		
		if (camp == LGCamps.Voleur) {
			
			String role_use ="Pour cela, vous avez l'effet de r�sistance I jusqu'� votre premier meurtre !";
			desc.append(role_use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		
		if (camp == LGCamps.Assassin) {
			
			String role_use = "�9Votre Objectif est de gagner seul, "
					+ "vous ne faite partie d'aucun camp ! "
					+ "Pour y arriver vous disposez de l'effet Force I le jour ainsi que d'un stuff de d�part ! ";
			String stuff = "\n�6Items de d�part : �91 Livre Sharpness III, 1 Livre Protection III et 1 Livre Power III";
			
			desc.append(role_use);
			desc.append(stuff);
					
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.EnfantS) {
			
			String role_use = "�9Vous pourrez choisir un mod�le parmis les joueurs. "
				  	+"Ci celui-ci meurt, vous deviendrez un membre du camp des Loups-Garous !";
			
			desc.append(role_use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		
		if (role == LGRoles.Valentin) {
			
			String role_Use = "�9Buvez de l'eau ! Dans 20-30 ans il y en aura plus !";
				
			desc.append(role_Use);
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
		
		if (role == LGRoles.LGA && !j.isLg()) {
			
			if (j.getPower() == 1) {
				desc.append("�9Toutes les 5 minutes, le nom d'un des Loups-Garous vous sera r�v�l� !");
				j.getPlayer().sendMessage(desc.toString());
				j.getPlayer().sendMessage("�cCependant prenez garde car vos cong�n�res ne connaitrons votre identit� que dans 5 minutes !");
				j.getPlayer().sendMessage(" ");
			} else {
				desc.append("�9Mais vous ne disposez d'aucun pouvoir particulier !");
				j.getPlayer().sendMessage(desc.toString());
			}
			return;
		}
		
		
		//LG
		if (j.isLg()) {
			String kill = "�9A chaque kill vous gagnez l'effet speed I pendant 1 minute et deux coeurs d'absorption "
					+ "pendant 1 minute. ";
			
			String effet = "�9Afin de r�ussir vous disposez de l'effet de Force I (la nuit) et "
					+ "de Night vision. ";
			
			
			if (role == LGRoles.LGB) {
				
				String role_use = "�9Afin de r�ussir vous disposez de l'effet de Force I (la nuit) et "
						+ "de Night vision, ainsi que de 5 coeurs supl�mentaires ! ";
				
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
					desc.append("�9Toutes les 5 minutes, le nom d'un des Loups-Garous vous sera r�v�l� !");
				}
			}
			
			if (role == LGRoles.VPL) {
				
				String role_use = "�9Mais contrairement aux autres Loups-Garous vous disposez aussi de Speed I durant la nuit !" +
				" Attention � ne pas vous faire rep�rer ! ";
				
				desc.append(effet);
				desc.append(role_use);
			}
			
			if (role == LGRoles.IPL) {
				
				
				String power = "�9Une fois dans la partie, vous pouvez ressusciter un joueur mort de la main d'un Loup, et le faire passer dans votre camp durant un lapse de temps. "
						+ "Pour se faire, vous pourrez utiliser la commande /lg Infect <Pseudo> ou cliquer sur un message message sp�cifique ! "
						+ "L'infect� conserve son pouvoir.";
				
				desc.append(effet);
				desc.append(kill);
				desc.append(power);
			}
			
			j.getPlayer().sendMessage(desc.toString());
			return;
		}
	}

}
