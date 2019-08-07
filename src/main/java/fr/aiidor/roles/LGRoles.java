package fr.aiidor.roles;

import java.util.UUID;

public enum LGRoles {
	
    Voyante(0, "Voyante", 1), Renard(0, "Renard", 1),  VilainPL(0, "Vilain Petit Loup", 2), Montreur(0, "Montreur D'ours", 1), 
    Petite(0, "Petite fille", 1), Voleur(0, "Voleur", 4),
    
    LGB(0, "Loup-Garou Blanc", 2), Assassin(0, "Assassin", 3),  LG(1, "Loup-Garou", 2),
	
	Ancien(0, "Ancien", 1), Sorciere(0, "Sorciere", 1), LgInfect(0, "Infect père des loups", 2),
	Cupidon(0, "Cupidon", 1), Salvateur(0, "Salvateur", 1),
	Ange(0, "Ange", 1), Trublion(0, "Trublion", 1),
	
	EnfantS(0, "Enfant Sauvage", 1),
	
	Mineur(0, "Mineur", 1),
	
	LGA(0, "Loup-Garou Amnésique", 2),
	
	//ROLE TAMPON
	 SV(1, "Simple Villageois", 1), Thanos(0, "Thanos", 1);
	
	public int number;
	private String Name;
	private int camp;
	
	LGRoles(int number, String Name, int camp) {
		this.number = number;
		this.Name = Name;
		this.camp = camp;
	}
	
	//NOM DU ROLE
	public static String getName(LGRoles role) {
		
		return role.Name;
	}
	
	
	//ROLE Number ?
	public static int isAvailable(LGRoles role) {
		
		return role.number;
	}
	
	public static LGCamps getCamp(LGRoles role) {
		
		if (role.camp == 1) {
			return LGCamps.VILLAGEOIS;
		}
		
		if (role.camp == 2) {
			return LGCamps.LOUPGAROUS;
		}
		
		if (role.camp == 3) {
			return LGCamps.ASSASSIN;
		}
		
		if (role.camp == 4) {
			return LGCamps.VOLEUR;
		}
		
		return null;
		
	}
	
	
	
	//SET ET GET ROLE
	public static void SetRole(UUID uuid, LGRoles role) {
		
		if (LGRoleManager.PlayerCamp.containsKey(uuid)) {
			LGRoleManager.PlayerCamp.remove(uuid);
		}
		
		LGRoleManager.PlayerRole.put(uuid, role);
	}
	
	public static LGRoles getRole(UUID uuid) {
		
		
		return LGRoleManager.PlayerRole.get(uuid);
	}
	
	public static String getRoleName(UUID uuid) {
		
		LGRoles Role = getRole(uuid);
		return getName(Role);
		
	}
	
	
	//IS LG ?
	public static Boolean isLG(UUID uuid) {
		
		if (LGRoles.getRole(uuid) == LGRoles.LG ||
			LGRoles.getRole(uuid) == LGRoles.LgInfect ||
			LGRoles.getRole(uuid) == LGRoles.VilainPL ||
			LGRoles.getRole(uuid) == LGRoles.LGB ||
			LGRoles.getRole(uuid) == LGRoles.LGA) {
			
			return true;
		}
		
		return false;
	}
	
}
