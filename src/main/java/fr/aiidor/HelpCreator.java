package fr.aiidor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class HelpCreator {
	
	private LGUHC main;
	
	public HelpCreator(LGUHC main) {
		this.main = main;
	}
	
	
	public void create() {
		if (!main.getDataFolder().exists()) {
			main.getDataFolder().mkdir();
			System.out.println("[LOUP-GAROUS] "+ main.ANSI_GREEN + "Creation du fichier \"LOUP-GAROU\" ! " + main.ANSI_RESET);
		}
		
		File file = new File(main.getDataFolder() + File.separator + "ConfigHelp.txt");
		
		if (!file.exists()) {
			try {
				file.createNewFile();
				System.out.println("[LOUP-GAROUS] "+ main.ANSI_GREEN + "Creation du fichier \"ConfigHelp.txt\" ! " + main.ANSI_RESET);
				
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		
		try {
			FileWriter writter = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writter);
			
			bw.write("   ---- Aide pour la configuration ----");
			bw.newLine();
			
			bw.write("\n°Si le texte que vous écrivez sera affiché dans le jeu (Exemple : les informations du tab, ...) : \n"
					+ " - Le signe & permet de remplacer le signe § et ainsi de utiliser les codes couleurs (https://minecraft.tools/fr/color-code.php)\n"
					+ " - \\n permet de passer à la ligne\n\n"
					
					
					+ "°Les listes de joueurs, nom, ... s'écrivent dans la config :\n"
					+ "[objet1,objet2,...]\n");
			
			//FONCTIONS
			bw.newLine();
			bw.write("°Vous pouvez utiliser des fonctions grâce au signe % :\r\n"
					+ " - %date pour afficher la date.\n");
			
			bw.write("\nLes fonctions peuvent être utilisé dans les sections où il il faut mettre du texte (String)");
			
			bw.close();
			writter.close();

		} catch (Exception e) {
			return;
		}

	}
}
