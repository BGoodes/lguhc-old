package fr.aiidor.roles;

import java.util.UUID;

public enum LGCamps {
	
	VILLAGEOIS(true), LOUPGAROUS(true), COUPLE(true), ASSASSIN(true), VOLEUR(true);
	
	private boolean isActivated;
	private static LGCamps camp;
	
	LGCamps(boolean isActivated) {
		this.isActivated = isActivated;
	}
	
	
	//CAMP ACTIVE ?
	public static boolean isOn(LGCamps role) {
		
		return LGCamps.camp.isActivated;
	}
	
	
	//GET ET SET CAMPS
	public static void SetCamp(UUID uuid, LGCamps camp) {
		
		if (LGRoleManager.PlayerCamp.containsKey(uuid)) {
			LGRoleManager.PlayerCamp.remove(uuid);
		}
		
		LGRoleManager.PlayerCamp.put(uuid, camp);
	}
	
	public static LGCamps getCamp(UUID uuid) {

		return LGRoleManager.PlayerCamp.get(uuid);
	}
}
