package fr.aiidor.files;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;

public class StatManager {
	
	private LGUHC main;
	
	public StatManager(LGUHC main) {
		this.main = main;
	}
	
	public void checkStatFiles() {
		
		if (!main.getDataFolder().exists()) {
			main.getDataFolder().mkdir();
			System.out.println("[LOUP-GAROUS] "+ main.ANSI_GREEN + "Creation du fichier \"LOUP-GAROU\" ! " + main.ANSI_RESET);
		}
		
		File fichier = new File(main.getDataFolder() + File.separator + "Game");
		
		if (!fichier.exists()) {
			try {
				fichier.mkdir();
				System.out.println("[LOUP-GAROUS] "+ main.ANSI_GREEN + "Creation du fichier \"Game\" ! " + main.ANSI_RESET);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		File file = new File(main.getDataFolder() + File.separator + "Game" + File.separator + "Statistiques.yml");
		
		if (!file.exists()) {
			try {
				file.createNewFile();
				System.out.println("[LOUP-GAROUS] "+ main.ANSI_GREEN + "Creation du fichier \"Statistiques.yml\" ! " + main.ANSI_RESET);
				
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	public void addPlayer(Player p) {
		File file = new File(main.getDataFolder() + File.separator + "Game" + File.separator + "Statistiques.yml");
		UUID uuid = p.getUniqueId();
		
		if (!file.exists()) return;
		if (!main.stats) return;
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		if (!config.contains(uuid.toString())) {
			
			config.set(uuid.toString() + ".name", p.getName());
			config.set(uuid.toString() + ".stat.nombre_partie", 0);
			config.set(uuid.toString() + ".stat.nombre_victoire", 0);
			config.set(uuid.toString() + ".stat.mort", 0);
			config.set(uuid.toString() + ".stat.kill", 0);
			
			System.out.println("Ajout de " +  p.getName() + " dans les stats.");
			
		} else {
			config.set(uuid.toString() + ".name", p.getName());
		}	
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[LOUP-GAROUS] " + main.ANSI_RED + "Erreur d'enregistrement du fichier \"Statistiques.yml\" !" + main.ANSI_RESET);
		}
	}
	
	public void changeState(UUID uuid, String path, Integer stat, StatAction action) {
		File file = new File(main.getDataFolder() + File.separator + "Game" + File.separator + "Statistiques.yml");
		
		if (!file.exists()) return;
		if (!main.stats) return;
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		String target = uuid.toString() + ".stat." + path;
		
		
		if (action == StatAction.Set) {
			config.set(target, stat);
		}
		
		if (action == StatAction.Increase) {
			int current = config.getInt(target);
			int value = current + stat;
			
			config.set(target, value);
		}
		
		
		
		
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[LOUP-GAROUS] " + main.ANSI_RED + "Erreur d'enregistrement du fichier \"Statistiques.yml\" !" + main.ANSI_RESET);
		}
	}
	
	public void changeState(UUID uuid, String path, StatAction action) {
		changeState(uuid, path, 1, action);
	}
	
	/*
	 	STATISTIQUES :
	 	
	 	-nombre_partie
	 	-nombre_victoire
	 	-mort
	 	-kill
	 	-vote
	 	-couple 
	 	
	 	-role
	 
	 */
}
