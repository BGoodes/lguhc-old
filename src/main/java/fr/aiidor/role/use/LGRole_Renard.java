package fr.aiidor.role.use;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;

public class LGRole_Renard {

	private LGUHC main;
	private Joueur renard;

	public LGRole_Renard(LGUHC main, Joueur renard) {
		this.main = main;
		this.renard = renard;
	}

	public void canFlaire(String targetname) {

		Player p = renard.getPlayer();

		if (renard.getRole() != LGRoles.Renard) {
			p.sendMessage(main.gameTag + "§cErreur, vous devez être §oRenard §cpour effectuer cette commande !");
			return;
		}

		if (renard.getPower() < 1 ) {
			p.sendMessage(main.gameTag + "§cVous avez déjà utilisé vos 3 flaires");
			return;
		}

		if (Bukkit.getPlayer(targetname) == null) {
			p.sendMessage(main.gameTag + "§cErreur, le joueur "+ targetname + " n'est pas connecté ou n'existe pas !");
			return;
		}

		Player Target = Bukkit.getPlayer(targetname);

		if (main.getPlayer(Target.getUniqueId()) == null) {
			p.sendMessage(main.gameTag + "§cErreur, le joueur visé doit être dans la partie !");
			return;
		}

		Joueur TargetJ = main.getPlayer(Target.getUniqueId());

		if (TargetJ.isDead()) {
			p.sendMessage(main.gameTag + "§cErreur, le joueur visé doit être en vie !");
			return;
		}

		if (p.getLocation().distance(Target.getLocation()) > 10) {
			p.sendMessage(main.gameTag + "§cLe Joueur ne se trouve pas dans un rayon de 10 blocs.");
			return;

		} else {
			renard.setPower(renard.getPower() - 1);
			flaire(TargetJ);
		}
	}

	public void flaire(Joueur target) {

		Player p = renard.getPlayer();

		if (target.getCamp() == LGCamps.LoupGarou || target.getCamp() == LGCamps.LGB || target.getRole() == LGRoles.LGA) {

			p.sendMessage(main.gameTag + "§e Le joueur §f" + target.getPlayer().getName() + "§e appartient au camp des Loups-Garous !");
			return;
		} else {

			p.sendMessage(main.gameTag + "§e Le joueur §f" + target.getPlayer().getName() + "§e appartient au camp des Innocents !");
		}
	}
}
