package fr.aiidor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.scoreboard.ScoreboardManager;

public class ConfigManager {

	private LGUHC main;
	public ConfigManager(LGUHC main) {
		this.main = main;
	}

	public void checkConfigFiles() {
		if (!main.getDataFolder().exists()) {
			main.getDataFolder().mkdir();
			System.out.println("[LOUP-GAROUS] "+ main.ANSI_GREEN + "Creation du fichier \"LOUP-GAROU\" ! " + main.ANSI_RESET);
		}

		File fichier = new File(main.getDataFolder() + File.separator + "Configurations");

		if (!fichier.exists()) {
			try {
				fichier.mkdir();
				System.out.println("[LOUP-GAROUS] "+ main.ANSI_GREEN + "Creation du fichier \"configuration\" ! " + main.ANSI_RESET);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		File baseConfig = new File(main.getDataFolder() + File.separator + "Configurations" + File.separator + "classic.yml");

		if (!baseConfig.exists()) {
			try {
				baseConfig.createNewFile();
				System.out.println("[LOUP-GAROUS] "+ main.ANSI_GREEN + "Creation du fichier \"classic.yml\" ! " + main.ANSI_RESET);
				saveConfig(baseConfig);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public List<String> getAllConfig() {
		List<String> configs = new ArrayList<>();
		File fichier = new File(main.getDataFolder() + File.separator + "Configurations");

		if (fichier.exists()) {
			for (File file : fichier.listFiles()) {
				if (getExtension(file.getName()).equalsIgnoreCase("yml")) {
					configs.add(file.getName());
				}
			}
		}

		return configs;
	}

	private String getExtension(String fileName) {
		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i+1);
		}

		return extension;
	}



	//LOAD ========================================
	public void loadConfig(File fichier) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(fichier);

		//NAME --------------------------------------------------------------
		main.gameName = config.getString("Game_Name").replace("&", "§");
		new ScoreboardManager(main).changeName(main.gameName);

		//TIME --------------------------------------------------------------

		main.epTime = config.getInt(("Time.EpTime"));
		main.pvp = config.getInt(("Time.Pvp"));
		main.announceRole = config.getInt("Time.Role");
		main.voteEp = config.getInt("Time.EpVote");
		main.DailyCycle = config.getBoolean("Time.DailyCycle");

		//LG RULES -----------------------------------------------------------
		main.meetup = config.getBoolean(("LGRules.Meetup"));
		main.hidecompo = config.getBoolean(("LGRules.Compo_Cache"));
		main.coupleAlea = config.getBoolean(("LGRules.Couple_Aleatoire"));

		//BORDER -------------------------------------------------------------

		main.Map = config.getInt("WorldBorder.Size");
		main.wbMax = config.getInt("WorldBorder.FinalSize");
		main.wbEP = config.getInt("WorldBorder.Episode");

		//ITEMS --------------------------------------------------------------
		if (config.isConfigurationSection("Items.startItem")) {
			List<ItemStack> items = new ArrayList<>();
			for (int i = 0; i < 40; i ++) {
				items.add(new ItemStack(Material.AIR));
			}

			ConfigurationSection startItem = config.getConfigurationSection("Items.startItem");
			for (String slot : startItem.getKeys(false)) {

				Material mat = Material.getMaterial(startItem.getString(slot + ".material"));
				int number = startItem.getInt(slot + ".amount");

				ItemStack item = new ItemStack(mat, number);

				ItemMeta itemM = item.getItemMeta();

				if (startItem.getString(slot + ".byte") != null) {
					item.setDurability((short) startItem.getInt(slot + ".byte"));
				}

				if (startItem.getString(slot + ".name") != null) {
					itemM.setDisplayName(startItem.getString(slot + ".name").replace("&", "§"));
				}

				if (startItem.isConfigurationSection(slot + ".enchants")) {
					ConfigurationSection Enchants = config.getConfigurationSection("Items.startItem." + slot + ".enchants");

					for (String enchant : Enchants.getKeys(false)) {
						itemM.addEnchant(Enchantment.getByName(enchant), Enchants.getInt(enchant), true);
					}
				}

				item.setItemMeta(itemM);
				items.set(Integer.valueOf(slot), item);
			}

			main.startItem = items.toArray(new ItemStack[items.size()]);
		} else {
			main.startItem = null;
		}

		//DEATH ITEM -------------------------------------------------
		if (config.isConfigurationSection("Items.deathItem")) {
			List<ItemStack> items = new ArrayList<>();
			for (int i = 0; i < 18; i ++) {
				items.add(new ItemStack(Material.AIR));
			}

			ConfigurationSection deathItem = config.getConfigurationSection("Items.deathItem");
			for (String slot : deathItem.getKeys(false)) {

				Material mat = Material.getMaterial(deathItem.getString(slot + ".material"));
				int number = deathItem.getInt(slot + ".amount");

				ItemStack item = new ItemStack(mat, number);

				ItemMeta itemM = item.getItemMeta();

				if (deathItem.getString(slot + ".byte") != null) {
					item.setDurability((short) deathItem.getInt(slot + ".byte"));
				}

				if (deathItem.getString(slot + ".name") != null) {
					itemM.setDisplayName(deathItem.getString(slot + ".name").replace("&", "§"));
				}

				if (deathItem.isConfigurationSection(slot + ".enchants")) {
					ConfigurationSection Enchants = config.getConfigurationSection("Items.deathItem." + slot + ".enchants");

					for (String enchant : Enchants.getKeys(false)) {
						itemM.addEnchant(Enchantment.getByName(enchant), Enchants.getInt(enchant), true);
					}
				}

				item.setItemMeta(itemM);
				items.set(Integer.valueOf(slot), item);
			}

			main.deathItem = items.toArray(new ItemStack[items.size()]);
		} else {
			main.deathItem = null;
		}

		System.out.println("[LOUP-GAROUS] " + main.ANSI_GREEN + "la Configuration est charge !" + main.ANSI_RESET);
	}







	//SAVE ========================================
	public void saveConfig(File fichier) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(fichier);

		//NAME --------------------------------------
		config.set("Game_Name", main.gameName.replace("§", "&"));

		//TIME --------------------------------------
		config.set("Time.EpTime", main.epTime);
		config.set("Time.Pvp", main.pvp);
		config.set("Time.Role", main.announceRole);
		config.set("Time.EpVote", main.voteEp);
		config.set("Time.DailyCycle", main.DailyCycle);

		//LG RULES -----------------------------------
		config.set("LGRules.Meetup", main.meetup);
		config.set("LGRules.Compo_Cache", main.hidecompo);
		config.set("LGRules.Couple_Aleatoire", main.coupleAlea);

		//BORDER --------------------------------------
		config.set("WorldBorder.Size", main.Map);
		config.set("WorldBorder.FinalSize", main.wbMax);
		config.set("WorldBorder.Episode", main.wbEP);

		//ITEMS -------------------------------------

		int number = 0;

		if (main.startItem != null) {
			for (ItemStack item : main.startItem) {
				SaveItemStack(config, "Items.startItem." + number, item);
				if (number == 39) break;
				number ++;
			}
		}

		number = 0;

		if (main.deathItem != null) {
			for (ItemStack item : main.deathItem) {
				SaveItemStack(config, "Items.deathItem." + number, item);
				if (number == 17) break;
				number ++;
			}
		}

		try {
			config.save(fichier);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[LOUP-GAROUS] " + main.ANSI_RED + "Erreur d'enregistrement du fichier !" + main.ANSI_RESET);
		}
	}

	private void SaveItemStack(FileConfiguration config, String dir, ItemStack item) {
		if (item != null && item.getType() != Material.AIR) {
			config.set(dir + ".material", item.getType().name());
			config.set(dir + ".amount", item.getAmount());
			if (item.getDurability() != 0) config.set(dir + ".byte", item.getDurability());

			if (item.hasItemMeta()) {
				if (item.getItemMeta().hasDisplayName()) {
					config.set(dir + ".name", item.getItemMeta().getDisplayName());
				}
			}

			if (item.getEnchantments().size() > 0) {
				for (Enchantment ench : item.getEnchantments().keySet()) {
					config.set(dir + ".enchants." + ench.getName(), item.getEnchantmentLevel(ench));
				}
			}
		}
	}

	public void addConfig(String name) {
		File config = new File(main.getDataFolder() + File.separator + "Configurations" + File.separator + name + ".yml");

		if (!config.exists()) {
			try {
				config.createNewFile();
				saveConfig(config);
				System.out.println("[LOUP-GAROUS] "+ main.ANSI_GREEN +"La configuration \"" + name + "\" viens d'etre cree !" + main.ANSI_RESET);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			config.delete();
			try {
				config.createNewFile();
				saveConfig(config);
				System.out.println("[LOUP-GAROUS] " + main.ANSI_GREEN + "La configuration " + name + " viens d'être modifie !" + main.ANSI_RESET);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
