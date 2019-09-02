package fr.aiidor.role;

import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;

public class LGDesc {

	private LGUHC main;

	public LGDesc(LGUHC main) {
		this.main = main;
	}

	public void sendLGList(Player p) {

		if (main.getPlayer(p.getUniqueId()) != null) {
			if (main.getPlayer(p.getUniqueId()).getRole() == LGRoles.LGA) {
				Joueur lga = main.getPlayer(p.getUniqueId());

				if (lga.getPower() == 1) {
					p.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§4 Les pseudos des Loups-Garous sont : ");

					StringBuilder compo = new StringBuilder();

					for (Joueur lg : main.getLg()) {
						if (lga.lglist.contains(lg)) compo.append(lg.getPlayer().getName() + "   ");
						else compo.append("?????   ");
					}
				}
			}
		}

		StringBuilder compo = new StringBuilder();
		compo.append(" §c");
		p.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§4 Les pseudos des Loups-Garous sont : ");

		for (Joueur lg : main.getLg()) {
			compo.append(lg.getPlayer().getName() + "   ");
		}

		p.sendMessage(compo.toString());
		p.sendMessage(" ");
	}
}
