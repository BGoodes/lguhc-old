package fr.aiidor.role.use;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGRoles;

public class LGRole_LGA {
	
	private LGUHC main;
	public LGRole_LGA(LGUHC main) {
		this.main = main;
	}
	
	public void addLglist() {
		
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.LGA) {
				if (!j.isDead() && !j.noPower) {
					
					if (j.getPower() == 2) {
						if (!j.LgList.containsAll(main.getLgOff())) {
							
							if (!List(j).isEmpty()) {
								
								j.LgList.add(List(j).get(new Random().nextInt(List(j).size())));
								
								if (j.isConnected()) {
									j.getPlayer().sendMessage(main.gameTag + "§cVotre mémoire vous revient ! Vous vous souvenez d'un nouveau loup !");
								}
							}

						}
					}
				}
			}
		}
	}
	
	private List<Joueur> List(Joueur j) {
		
		List<Joueur> list = new ArrayList<>();
		
		if (main.getLgOff().size() > 0) {
			for (Joueur lg : main.getLgOff()) {
				if (!j.LgList.contains(lg) && !lg.equals(j)) {
					list.add(lg);
				}
			}
		}

		return list;
	}
}
