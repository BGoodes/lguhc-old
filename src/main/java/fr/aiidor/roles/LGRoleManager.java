package fr.aiidor.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

public class LGRoleManager extends JavaPlugin {
	
	//ROLE ET CAMP
	public static HashMap<UUID, LGRoles> PlayerRole = new HashMap<>();
	public static HashMap<UUID, LGCamps> PlayerCamp = new HashMap<>();
	
	//POWER
	public static HashMap<UUID, Integer> Power = new HashMap<>();
	
	//COUPLE
	public static ArrayList<UUID> couple = new ArrayList<>();
	
	//LG
	public static ArrayList<String> lg = new ArrayList<>();
	public static HashMap<UUID, LGCamps> mortD = new HashMap<>(); //DELAIS DE MORT
	
	//MORT
	public static ArrayList<UUID> Infect = new ArrayList<>(); //INFECTION
	public static ArrayList<UUID> Rea = new ArrayList<>(); //REA
	
	//VOLEUR
	public static ArrayList<UUID> Voleur = new ArrayList<>();
	
	//ENFANT SAUVAGE
	public static HashMap<UUID, UUID> model = new HashMap<>();
	
	public static void setPower(UUID p) {
		
		if (LGRoles.getRole(p) == LGRoles.Renard) {
			Power.put(p, 3);
		}
		
		if (LGRoles.getRole(p) == LGRoles.Ancien || LGRoles.getRole(p) == LGRoles.LgInfect || LGRoles.getRole(p) == LGRoles.Sorcière || LGRoles.getRole(p) == LGRoles.Cupidon 
			|| LGRoles.getRole(p) == LGRoles.Voleur || LGRoles.getRole(p) == LGRoles.EnfantS) {
			Power.put(p, 1);
		}
	}
}
