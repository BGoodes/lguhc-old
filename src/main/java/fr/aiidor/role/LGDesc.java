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
		
		if (main.hasRole(p)) {
			Joueur j = main.getPlayer(p.getUniqueId());
			
			if (j.getRole() == LGRoles.LGA && !j.isInfect()) {
				if (j.getPower() < 2) {
					return;
				}
				
				if (j.getPower() == 2 && !j.LgList.containsAll(main.getLgOff())) {
					p.sendMessage(main.gameTag + "§4 Les pseudos des Loups-Garous sont : ");
						
					StringBuilder compo = new StringBuilder();
						
					for (Joueur lg : main.getLg()) {
						
						if (j.LgList.contains(lg)) compo.append("§c" + lg.getPlayer().getName() + "   ");
						else compo.append("§c?????   ");
					}
						
					p.sendMessage(compo.toString());
					p.sendMessage(" ");
					return;
				}
			}
		}
		
		StringBuilder compo = new StringBuilder();
		compo.append(" §c");
		p.sendMessage(main.gameTag + "§4 Les pseudos des Loups-Garous sont : ");
		
		for (Joueur lg : main.getLg()) {
			if (lg.getRole() == LGRoles.LGA && lg.getPower() < 2 && !lg.isInfect());
			else compo.append(lg.getPlayer().getName() + "   ");
		}
		
		p.sendMessage(compo.toString());
		p.sendMessage(" ");
	}
}
