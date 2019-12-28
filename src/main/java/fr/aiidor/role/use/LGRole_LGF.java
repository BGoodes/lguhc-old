package fr.aiidor.role.use;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGRoles;

public class LGRole_LGF {
	
	private LGUHC main;
	public LGRole_LGF(LGUHC main) {
		this.main = main;
	}
	
	public void setFakeRole() {
		
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.LGFeutre) {
				if (!j.isDead() && !j.noPower) {
					
					j.setFakeRole();
					
				} else {
					j.fakeRole = LGRoles.LGFeutre;
				}
			}
		}
	}
}
