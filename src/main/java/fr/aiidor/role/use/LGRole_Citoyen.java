package fr.aiidor.role.use;

import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGRoles;

public class LGRole_Citoyen {
	
	private LGUHC main;
	public LGRole_Citoyen(LGUHC main) {
		this.main = main;
	}
	
	public void canSee(Joueur j) {
		
		Player p = j.getPlayer();
		
		if (j.getRole() != LGRoles.Citoyen) {
			p.sendMessage(main.gameTag + "§cErreur, vous devez être §oCitoyen §cpour effectuer cette commande !");
			return;
		}
		
		if (j.getPower() < 1 ) {
			p.sendMessage(main.gameTag + "§cVous avez déjà utilisé 2 fois votre pouvoir !");
			return;
		}
		
		if (!main.canSeeVote) {
			p.sendMessage(main.gameTag + "§cVous ne pouvez pas encore utiliser votre pouvoir !");
			return;
		}
		
		j.setPower(j.getPower() - 1);
		p.sendMessage(main.gameTag + "§bLes résultats du vote : ");
		for (Joueur all : main.Players) {
			
			if (!all.isDead()) {
				String cible;
				
				if (all.whoVote == null) cible = "---";
				else cible = all.whoVote.getName();
				
				p.sendMessage("§7" + all.getName() + " §c✉➔§7 " + cible);
			}

		}
	}
}
