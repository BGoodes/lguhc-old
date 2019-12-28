package fr.aiidor.files;

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
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.LGUHC;
import fr.aiidor.Options.ChatOption;
import fr.aiidor.Options.DecoOption;
import fr.aiidor.Options.LGEnchants;
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
		main.gameName = getConfigString(config, "Game_Name").replace("&", "§");
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
		main.voteProtect = config.getBoolean(("LGRules.VoteP"));
		
		main.reduced = config.getBoolean(("LGRules.ReducedDebug"));
		main.announceAdv = config.getBoolean(("LGRules.announceAdv"));
		
		main.msg = config.getBoolean(("LGRules.Message_Prive"));
		main.chat = getChatOpByName(config.getString(("LGRules.Chat")));
		
		//BORDER -------------------------------------------------------------
		main.Map = config.getInt("WorldBorder.Size");
		main.wbMax = config.getInt("WorldBorder.FinalSize");
		main.wbTime = config.getInt("WorldBorder.Time");
		main.wbSpeed = config.getInt("WorldBorder.BlockParCycle");
		main.wbSecond = config.getInt("WorldBorder.Intervalle");
		
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
						if (item.getType() == Material.ENCHANTED_BOOK) {
							EnchantmentStorageMeta eb = (EnchantmentStorageMeta) itemM;
							eb.addStoredEnchant(Enchantment.getByName(enchant), Enchants.getInt(enchant), true);
							itemM = eb;
							
						} else {
							itemM.addEnchant(Enchantment.getByName(enchant), Enchants.getInt(enchant), true);
						}
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
						if (item.getType() == Material.ENCHANTED_BOOK) {
							EnchantmentStorageMeta eb = (EnchantmentStorageMeta) itemM;
							eb.addStoredEnchant(Enchantment.getByName(enchant), Enchants.getInt(enchant), true);
							itemM = eb;
							
						} else {
							itemM.addEnchant(Enchantment.getByName(enchant), Enchants.getInt(enchant), true);
						}
					}
				}
				
				item.setItemMeta(itemM);
				items.set(Integer.valueOf(slot), item);
			}
			
			main.deathItem = items.toArray(new ItemStack[items.size()]);
		} else {
			main.deathItem = null;
		}
		
		//SCENARIOS //////////////////////////////////////////////////////////////////////////
		
		//RUN --------------------------------------------------------------
		main.run = config.getBoolean("Scenarios.Run.run");
		main.cutclean = config.getBoolean("Scenarios.Run.cutClean");
		main.fastSmelting = config.getBoolean("Scenarios.Run.fastSmeling");
		main.timber = config.getBoolean("Scenarios.Run.timber");
		main.veinMiner = config.getBoolean("Scenarios.Run.veinMiner");
		main.HasteyBoys = config.getBoolean("Scenarios.Run.hasteyBoys");
		main.bleedingSweet = config.getBoolean("Scenarios.Run.bleedingSweet");
		main.SpeedyMiner = config.getInt("Scenarios.Run.speedyMiner");
		main.noWodenTool = Material.getMaterial(config.getString("Scenarios.Run.noWodenTool"));
		
		//LIMITE STUFF ------------------------------------------------------
		main.LimiteDePiece = config.getInt("Scenarios.Limit.limitPiece");
		main.BloodDiamond = config.getBoolean("Scenarios.Limit.bloodDiamond");
		main.diamondlimit = config.getInt("Scenarios.Limit.diamondLimit");
		main.RodLess = config.getBoolean("Scenarios.Limit.rodLess");
		main.enchantLess = config.getBoolean("Scenarios.Limit.enchantLess");
		
		main.oreLess = config.getBoolean("Scenarios.Limit.oreLess.state");
		main.diamondLess = config.getBoolean("Scenarios.Limit.oreLess.diamondLess");
		main.goldLess = config.getBoolean("Scenarios.Limit.oreLess.goldLess");
		main.ironLess = config.getBoolean("Scenarios.Limit.oreLess.ironLess");
		main.lapisLess = config.getBoolean("Scenarios.Limit.oreLess.lapisLess");
		main.coalLess = config.getBoolean("Scenarios.Limit.oreLess.coalLess");
		main.redstoneLess = config.getBoolean("Scenarios.Limit.oreLess.redstoneLess");
		main.quartzLess = config.getBoolean("Scenarios.Limit.oreLess.quartzLess");
		
		config.getBoolean("Scenarios.Limit.LimiteEnchant.state", main.LimiteEnchant);
		
		for (LGEnchants ench : main.enchantLimit) {
			ench.setLimit(config.getInt("Scenarios.Limit.LimiteEnchant." + ench.getEnchant().getName()));
		}
		
		//HEAL -------------------------------------------------------------
		main.Abso = config.getInt("Scenarios.Heal.abso");
		main.Notch = config.getBoolean("Scenarios.Heal.notch");
		main.GoldenHead = config.getBoolean("Scenarios.Heal.goldenHead");
		main.HeadHeal = config.getInt("Scenarios.Heal.headHeal");
		main.FinalHeal = config.getBoolean("Scenarios.Heal.finalHeal");
		main.Cupid = config.getBoolean("Scenarios.Heal.cupid");
		
		//VANILLA B ---------------------------------------------------------
		main.vanilla = config.getBoolean("Scenarios.VanillaB.vanillaB");
		main.apples = config.getInt("Scenarios.VanillaB.apples");
		main.flint = config.getInt("Scenarios.VanillaB.flint");
		main.infiniteEnc = config.getBoolean("Scenarios.VanillaB.infiniteEnc");
		main.noHunger = config.getBoolean("Scenarios.VanillaB.noHunger");
		main.CatEyes = config.getBoolean("Scenarios.VanillaB.catEyes");
		
		//VANILLA N ---------------------------------------------------------
		main.vanillaN = config.getBoolean("Scenarios.VanillaN.vanillaN");
		main.xpNerf = config.getBoolean("Scenarios.VanillaN.xpNerf");
		main.xpNerfVar = config.getInt("Scenarios.VanillaN.xpVar");
		main.noNether = config.getBoolean("Scenarios.VanillaN.noNether");
		main.noEnd = config.getBoolean("Scenarios.VanillaN.noEnd");
		main.noMine = config.getBoolean("Scenarios.VanillaN.noMine.State");
		main.noMineCouche = config.getInt("Scenarios.VanillaN.noMine.Couche");
		main.noMineTime = config.getInt("Scenarios.VanillaN.noMine.Ep");
		main.horseLess = config.getBoolean("Scenarios.VanillaN.horseLess");
		main.noNametag = config.getBoolean("Scenarios.VanillaN.noNametag");
		
		//PVE ----------------------------------------------------------------
		main.NightMare = config.getBoolean("Scenarios.Pve.nightMare");
		main.PoisonLess = config.getBoolean("Scenarios.Pve.poisonLess");
		
		System.out.println("[LOUP-GAROUS] " + main.ANSI_GREEN + "la Configuration est charge !" + main.ANSI_RESET);
	}
	
	//SAVE ========================================
	public void saveConfig(File fichier) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(fichier);
		
		//NAME --------------------------------------
		config.set("Game_Name", main.gameName.replace("§", "&"));
		
		if (!fichier.getName().equalsIgnoreCase("classic.yml")) {
			config.set("Icone.Material", Material.PAPER.name());
			config.set("Icone.Byte", (byte) 0);
		}
		
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
		config.set("LGRules.VoteP", main.voteProtect);
		
		config.set("LGRules.ReducedDebug", main.reduced);
		config.set("LGRules.announceAdv", main.announceAdv);
		
		config.set("LGRules.Message_Prive", main.msg);
		config.set("LGRules.Chat", main.chat.name());
				
		//BORDER --------------------------------------
		config.set("WorldBorder.Size", main.Map);
		config.set("WorldBorder.FinalSize", main.wbMax);
		config.set("WorldBorder.Time", main.wbTime);
		config.set("WorldBorder.BlockParCycle", main.wbSpeed);
		config.set("WorldBorder.Intervalle", main.wbSecond);
		
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
		
		//SCENARIOS //////////////////////////////////////////////////////////////////////////
		
		//RUN --------------------------------------------------------------
		config.set("Scenarios.Run.run", main.run);
		config.set("Scenarios.Run.cutClean", main.cutclean);
		config.set("Scenarios.Run.fastSmeling", main.fastSmelting);
		config.set("Scenarios.Run.timber", main.timber);
		config.set("Scenarios.Run.veinMiner", main.veinMiner);
		config.set("Scenarios.Run.hasteyBoys", main.HasteyBoys);
		config.set("Scenarios.Run.bleedingSweet", main.bleedingSweet);
		config.set("Scenarios.Run.speedyMiner", main.SpeedyMiner);
		config.set("Scenarios.Run.noWodenTool", main.noWodenTool.toString());
		
		//LIMITE STUFF ------------------------------------------------------
		config.set("Scenarios.Limit.limitPiece", main.LimiteDePiece);
		config.set("Scenarios.Limit.bloodDiamond", main.BloodDiamond);
		config.set("Scenarios.Limit.diamondLimit", main.diamondlimit);
		config.set("Scenarios.Limit.rodLess", main.RodLess);
		config.set("Scenarios.Limit.enchantLess", main.enchantLess);
		
		config.set("Scenarios.Limit.oreLess.state", main.oreLess);
		config.set("Scenarios.Limit.oreLess.diamondLess", main.diamondLess);
		config.set("Scenarios.Limit.oreLess.goldLess", main.goldLess);
		config.set("Scenarios.Limit.oreLess.ironLess", main.ironLess);
		config.set("Scenarios.Limit.oreLess.lapisLess", main.lapisLess);
		config.set("Scenarios.Limit.oreLess.coalLess", main.coalLess);
		config.set("Scenarios.Limit.oreLess.redstoneLess", main.redstoneLess);
		config.set("Scenarios.Limit.oreLess.quartzLess", main.quartzLess);
		
		
		
		//HEAL -------------------------------------------------------------
		config.set("Scenarios.Heal.abso", main.Abso);
		config.set("Scenarios.Heal.notch", main.Notch);
		config.set("Scenarios.Heal.goldenHead", main.GoldenHead);
		config.set("Scenarios.Heal.headHeal", main.HeadHeal);
		config.set("Scenarios.Heal.finalHeal", main.FinalHeal);
		config.set("Scenarios.Heal.cupid", main.Cupid);
		
		config.set("Scenarios.Limit.LimiteEnchant.state", main.LimiteEnchant);
		
		for (LGEnchants ench : main.enchantLimit) {
			config.set("Scenarios.Limit.LimiteEnchant." + ench.getEnchant().getName(), ench.getLimit());
		}
		
		//VANILLA B ---------------------------------------------------------
		config.set("Scenarios.VanillaB.vanillaB", main.vanilla);
		config.set("Scenarios.VanillaB.apples", main.apples);
		config.set("Scenarios.VanillaB.flint", main.flint);
		config.set("Scenarios.VanillaB.infiniteEnc", main.infiniteEnc);
		config.set("Scenarios.VanillaB.noHunger", main.noHunger);
		config.set("Scenarios.VanillaB.catEyes", main.CatEyes);
		
		//VANILLA N ---------------------------------------------------------
		config.set("Scenarios.VanillaN.vanillaN", main.vanillaN);
		config.set("Scenarios.VanillaN.xpNerf", main.xpNerf);
		config.set("Scenarios.VanillaN.xpVar", main.xpNerfVar);
		config.set("Scenarios.VanillaN.noNether", main.noNether);
		config.set("Scenarios.VanillaN.noEnd", main.noEnd);
		config.set("Scenarios.VanillaN.noMine.State", main.noMine);
		config.set("Scenarios.VanillaN.noMine.Couche", main.noMineCouche);
		config.set("Scenarios.VanillaN.noMine.Ep", main.noMineTime);
		config.set("Scenarios.VanillaN.horseLess", main.horseLess);
		config.set("Scenarios.VanillaN.noNametag", main.noNametag);
		
		//PVE ----------------------------------------------------------------
		config.set("Scenarios.Pve.nightMare", main.NightMare);
		config.set("Scenarios.Pve.poisonLess", main.PoisonLess);
		
		
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
			
			if (item.getType() == Material.ENCHANTED_BOOK) {
				EnchantmentStorageMeta bm = (EnchantmentStorageMeta) item.getItemMeta();
				
				if (bm.getStoredEnchants().size() > 0) {
					for (Enchantment ench : bm.getStoredEnchants().keySet()) {
						config.set(dir + ".enchants." + ench.getName(), bm.getStoredEnchantLevel(ench));
					}
				}
			}
		}
	}
	
	public void addConfig(String name) {
		name = name
				.replace("\\", "")
				.replace("/", "")
				.replace(":", "")
				.replace("?", "")
				.replace("<", "")
				.replace(">", "")
				.replace("\"", "");
		
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
	
	public void removeConfig(File fichier) {
		
		if (fichier.exists()) {
			try {
				fichier.delete();
				System.out.println("[LOUP-GAROUS] " + main.ANSI_GREEN + "La configuration " + fichier.getName() + " viens d'être suprimé !" + main.ANSI_RESET);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public ChatOption getChatOpByName(String name) {
		
		for (ChatOption op : ChatOption.values()) {
			if (op.name().equals(name)) {
				return op;
			}
		}
		
		return null;
	}
	
	public DecoOption getDecoOpByName(String name) {
		
		for (DecoOption op : DecoOption.values()) {
			if (op.name().equals(name)) {
				return op;
			}
		}
		
		return null;
	}
	
	private String getConfigString(FileConfiguration config, String path) {
		String string = config.getString(path)
				.replace("%date", main.date());
		
		return string;
	}
}
