package fr.aiidor.utils;

import java.util.UUID;

import fr.aiidor.LGUHC;
import fr.aiidor.roles.LGCamps;

public class LGGroups {

	public static int setGroups() {
		
		int groups = 3;
		
		for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
			if (LGCamps.getCamp(uuid) == LGCamps.LOUPGAROUS) {
				
				groups ++;
			}
		}
		
		return groups;
	}
}
