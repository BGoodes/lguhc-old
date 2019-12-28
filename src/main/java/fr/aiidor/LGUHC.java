package fr.aiidor;

import fr.aiidor.Options.ChatOption;
import fr.aiidor.Options.DecoOption;
import fr.aiidor.Options.LGEnchants;
import fr.aiidor.Options.LGOption;
import fr.aiidor.commands.CommandLg;
import fr.aiidor.commands.CommandOrga;
import fr.aiidor.commands.CommandPing;
import fr.aiidor.commands.CommandSpec;
import fr.aiidor.effect.Sounds;
import fr.aiidor.event.EventsManager;
import fr.aiidor.files.ConfigManager;
import fr.aiidor.files.HelpCreator;
import fr.aiidor.files.StatAction;
import fr.aiidor.files.StatManager;
import fr.aiidor.game.Joueur;
import fr.aiidor.game.UHCState;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;
import fr.aiidor.scoreboard.ScoreboardSign;
import fr.aiidor.task.UHCStart;
import fr.aiidor.utils.LGCivilisation;
import fr.aiidor.utils.UHCLootCrate;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class LGUHC extends JavaPlugin{
	
	public Boolean stats = true;
	
	public List<UUID> Spectator = new ArrayList<>();
	public List<Joueur> Players = new ArrayList<>();
	public List<UUID> PlayerInGame = new ArrayList<>();
	
	public ArrayList<LGRoles> compo = new ArrayList<>();
	public boolean hidecompo = false;
	
	public HashMap<Player, ScoreboardSign> boards = new HashMap<>();
	
	public ArrayList<Joueur> death = new ArrayList<>();
	
	public Boolean coupleAlea = false;
	
	public Boolean cage = true;
	public Integer cageSize = 30;
	public Integer cageHeight = 4;
	
	//ORGAS
	private UHCState state;
	public UUID host;
	public ArrayList<UUID> orgas = new ArrayList<>();
	
	public ArrayList<String> canHost = null;
	
	public ArrayList<UUID> noFall = new ArrayList<>();
	
	private HashMap<UUID, LGOption> OptionChat = new HashMap<>();
	
	//PERMS ORGA
	public ChatOption chat = ChatOption.Off_IG;
	public Boolean msg = false;
	public Integer chatRegion = 40;
	
	public int PlayerMin = 1;
	public UHCStart start;
	public boolean PlayerHasRole = false;
	
	public int GroupLimit = 5;
	public int ep = 1;
	
	public String gameTag = "§b§l[§6§lLOUP-GAROUS§b§l] §r";
	public String gameName = "§f§lLG UHC";
	
	public String TabName = "§5§k|||§6 Aiidor§bGAMES §5§k|||\n§9Serveur de la §6team Aiidor §9!";
	
	public Location Spawn;
	public World world;
	public WorldBorder wb;
	
	public Integer MaxY = 150;
	public Integer MaxRange = 0;
	
	public Boolean cancelVote = false;
	
	//OPTION
	public Integer Map = 1000;
	public Integer wbTime = 100;
	public Integer wbMax = 300;
	public Integer wbSpeed = 1;
	public Integer wbSecond = 5;
	
	public Integer epTime = 20;
	public Boolean DailyCycle = true;
	public Integer pvp = 30;
	public Integer announceRole = 20;
	public Integer voteEp = 3;
	
	public Boolean canSeeList = true;
	
	public HashMap<UUID, List<ItemStack>> DecoInv = new HashMap<>();

	public DecoOption decoOption = DecoOption.Amis;
	public Integer decoTime = 60;
	
	public ItemStack[] startItem = {
			new ItemStack(Material.COOKED_BEEF, 20)
	};
	
	public ItemStack[] deathItem= {
			new ItemStack(Material.GOLDEN_APPLE, 1)
	};
	
	public boolean deathChat = false;
	
	public Team nhide;
	
	//GAMERULE
	public Boolean reduced = true;
	public Boolean announceAdv = false;
	
	//SCENARIOS
	public boolean run = false;
	
	public Boolean cutclean = false;
	public Boolean timber = false;
	public Boolean veinMiner = false;
	public Boolean bleedingSweet = false;
	public Boolean HasteyBoys = false;
	public Boolean fastSmelting = false;
	public Integer SpeedyMiner = 0;
	public Material noWodenTool = Material.WOOD;
	
	
	//LIMI
	public Integer diamondlimit = 17;
	public Boolean BloodDiamond = false;
	
	
	public Integer LimiteDePiece = 2;
	
	//LIMITE ENCHANT
	public Boolean LimiteEnchant = true;
	
	//ORELESS
	public Boolean oreLess = false;
	public Boolean diamondLess = true;
	public Boolean goldLess = true;
	public Boolean ironLess = true;
	public Boolean coalLess = true;
	public Boolean redstoneLess = true;
	public Boolean lapisLess = true;
	public Boolean quartzLess = true;
	
	public HashMap<UUID, Integer> DiamondPl = new HashMap<>();
	
	public Boolean enchantLess = false;
	
	public Boolean FinalHeal = false;
	
	public Integer Abso = 1;
	public Boolean Notch = false;
	
	public Boolean GoldenHead = false;
	public Integer HeadHeal = 4;
	public Boolean Cupid = false;
	
	public Boolean RodLess  = true;
	
	public Boolean vanilla = true;
	public Integer flint = 20;
	public Integer apples = 1;
	public Boolean infiniteEnc = false;
	public Boolean noHunger = false;
	public Boolean CatEyes = false;
	public Boolean Utils = true;
	
	public Boolean vanillaN = true;
	public Boolean xpNerf = false;
	public double xpNerfVar = 2.00;
	public Boolean noNether = true;
	public Boolean noEnd = true;
	public Boolean noMine = false;
	public Integer noMineTime = 80;
	public Integer noMineCouche = 32;
	public Boolean noNametag = true;
	public Boolean horseLess = true;
	
	public Boolean NightMare = false;
	public Boolean PoisonLess = false;
	
	public Boolean fun = false;
	
	public Boolean goneFishin = false;
	public Boolean skyHigh = false;
	public Boolean eggs = false;
	
	public Boolean LootCrate = false;
	public ArrayList<ItemStack> Loots = new ArrayList<>();
	
	//SCENARIO CIV
	public Boolean Civilisation = false;
	public Integer vilRange = 400;
	public Integer vilSize = 50;
	public List<String> civNames = new ArrayList<>();
	public int CivSize = 6;
	public List<LGCivilisation> civilisations = new ArrayList<>();
	
	public Boolean meetup = false;
	
	public List<LGEnchants> enchantLimit = new ArrayList<>();
	
	public void addNames() {
		 civNames.add("Thiercelieux");
		 civNames.add("Bedburg");
		 civNames.add("Besançon");
		 civNames.add("Dole");
		 civNames.add("Edimbourg");
	}
	
	//VOTE
	public Boolean canVote = false;
	public Boolean canSeeVote = false;
	public Boolean voteProtect = true;
	public ArrayList<Joueur> cannotVote = new ArrayList<>();
	
	public final String ANSI_RESET = "\u001B[0m";
	public final String ANSI_BLACK = "\u001B[30m";
	public final String ANSI_RED = "\u001B[31m";
	public final String ANSI_GREEN = "\u001B[32m";
	public final String ANSI_YELLOW = "\u001B[33m";
	public final String ANSI_BLUE = "\u001B[34m";
	public final String ANSI_PURPLE = "\u001B[35m";
	public final String ANSI_CYAN = "\u001B[36m";
	public final String ANSI_WHITE = "\u001B[37m";
	
	@Override
	public void onEnable() {
		
		System.out.println(ANSI_GREEN + "[LOUP-GAROUS] Le plugin est en cours de demarage !" + ANSI_RESET); 
		
		saveDefaultConfig();
		new ConfigManager(this).checkConfigFiles();
		
		if (new ConfigManager(this).getAllConfig().size() > 0) {
			System.out.println("[LOUP-GAROUS]" + ANSI_PURPLE + " fichier(s) de configuration trouve(s) : " + ANSI_RESET);
			for (String name : new ConfigManager(this).getAllConfig()) {
				System.out.println(ANSI_CYAN + "- " + name + ANSI_RESET);
			}
		} else {
			System.out.println("[LOUP-GAROUS]" + ANSI_RED + " Aucun fichiers de configuration trouve" + ANSI_RESET);
		}
		
		new HelpCreator(this).create();
		
		stats = getConfig().getBoolean("Stats.state");
		
		if (stats) {
			new StatManager(this).checkStatFiles();
		}
		
		
		TabName = getConfigString("Informations.tabHeader")
				.replace("&", "§");
		
		StringBuilder gameTagB = new StringBuilder();
		gameTagB.append(getConfigString("Informations.gameTag")
				.replace("&", "§"));
		
		gameTagB.append(" §r");
		
		gameTag = gameTagB.toString();
		
		if (Bukkit.getWorld(getConfigString("Spawn.worldName")) == null) {
			if (getConfig().getBoolean("MultiWorld.state")) {
				
				WorldCreator Wc = new WorldCreator(getConfigString("Spawn.worldName"));
				
				if (getConfig().getLong("MultiWorld.seed") != 0) {
					Wc.seed(getConfig().getLong("MultiWorld.seed"));
				}
				
				Wc.generateStructures(getConfig().getBoolean("MultiWorld.generateStructure"));
				Wc.environment(getEnvironnement(getConfig().getString("MultiWorld.dimension")));
				
				System.out.println(ANSI_GREEN + "[LOUP-GAROUS] Création du monde : " + ANSI_WHITE + getConfig().getString("Spawn.worldName") + ANSI_RESET);
				
				try {
					Wc.createWorld();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(ANSI_RED  + "[LOUP-GAROUS] Le monde n'a pas reussi a etre genere ! (Plugin Off)" + ANSI_RESET);
					Bukkit.getPluginManager().disablePlugin(this);
					return;
				}
				
				
			} else {
				Bukkit.getPluginManager().disablePlugin(this);
				System.out.println(ANSI_RED  + "[LOUP-GAROUS] Monde invalide ! (Plugin Off)" + ANSI_RESET);
				return;
			}

		}
		
		state = UHCState.WAITING;
		
		EventsManager.registerEvents(this);
		
		world = Bukkit.getWorld(getConfigString("Spawn.worldName"));
		Spawn = getLocation(getConfigString("Spawn.location"));
		
		cage = getConfig().getBoolean("Cage.state");
		cageSize = getConfig().getInt("Cage.cageSize") / 2;
		cageHeight = getConfig().getInt("Cage.cageHeight");
		
		MaxY = getConfig().getInt("Spawn.maxY");
		MaxRange = getConfig().getInt("Spawn.maxRange");
		
		wb = world.getWorldBorder();
				
		///CAN HOST
		if (getConfig().getString("HOST.hostList") != null) {
			String configNames = getConfigString("HOST.hostList")
					.replace("[", "")
					.replace("]", "")
					.replace(" ", "");
			
			String[] Names = configNames.split(",");
			
			canHost = new ArrayList<String>();
			
			for (String name : Names) {
				canHost.add(name);
			}
		}
		
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		Team t = board.getTeam("nhide");
		
		if (t == null) {
			t = board.registerNewTeam("nhide");
			t.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OWN_TEAM);
			t.setCanSeeFriendlyInvisibles(false);
			t.setAllowFriendlyFire(true);
		}	
		
		nhide = t;
		
		//BORDER
		wb.setCenter(Spawn.getX(), Spawn.getZ());
		wb.setSize(Map * 2);

		getCommand("config").setExecutor(new CommandOrga(this));
		getCommand("lg").setExecutor(new CommandLg(this));
		getCommand("spec").setExecutor(new CommandSpec(this));
		getCommand("ping").setExecutor(new CommandPing(this));
		
		world.setPVP(true);
		
		world.setGameRuleValue("reducedDebugInfo", "true");
		world.setGameRuleValue("naturalRegeneration", "false");
		world.setGameRuleValue("announceAdvancements", "false");
		world.setGameRuleValue("spawnRadius", "0");
		world.setGameRuleValue("keepInventory", "true");
		world.setGameRuleValue("showDeathMessages", "false");
		
		world.setDifficulty(Difficulty.HARD);
		
		ShapedRecipe recipe = new ShapedRecipe(getItem(Material.GOLDEN_APPLE, "§6Golden Head"));
		recipe.shape(new String[] { "GGG", "GSG", "GGG" });
	    recipe.setIngredient('G', Material.GOLD_INGOT);
	    recipe.setIngredient('S', new ItemStack(Material.SKULL_ITEM, 1, (short) 3).getData());
		 
		getServer().addRecipe(recipe);
		
		System.out.println(ANSI_RED +  "========================================" + ANSI_RESET);
		System.out.println(ANSI_GREEN + "Plugin LOUP-GAROU UHC - Version 0.6" + ANSI_RESET);
		System.out.println(ANSI_YELLOW + "       Auteur : B_Goodes" + ANSI_RESET);
		System.out.println(ANSI_RED +  "========================================" + ANSI_RESET);
		
		addNames();
		new UHCLootCrate(this).setLoots();
		
		setEnchantsLimit();
	}
	
	@Override
	public void onDisable() {
		System.out.println("[LOUP-GAROUS] "+ ANSI_RED +"Le plugin est off." + ANSI_RESET);
		nhide.unregister();
	}
	
	private Location getLocation(String loc) {
		String[] args = loc.split(",");
		
		double x = Double.valueOf(args[0]);
		double y = Double.valueOf(args[1]);
		double z = Double.valueOf(args[2]);
		
		return new Location(world, x, y, z);
	}
	
	public Environment getEnvironnement(String name) {
		if (name.equalsIgnoreCase("normal")) {
			return Environment.NORMAL;
		}
		if (name.equalsIgnoreCase("nether")) {
			return Environment.NETHER;
		}
		if (name.equalsIgnoreCase("end")) {
			return Environment.THE_END;
		}
		return null;
	}
	
	public void setEnchantsLimit() {
		for (Enchantment ench : Enchantment.values()) {
			
			if (ench.equals(Enchantment.FIRE_ASPECT) || ench.equals(Enchantment.ARROW_FIRE)) {	
				enchantLimit.add(new LGEnchants(ench, 0));
					
			} else {	
				enchantLimit.add(new LGEnchants(ench));
			}
		}
	}
	
	public LGEnchants getEnchant(String name) {
		
		for (LGEnchants ench : enchantLimit) {
			if (ench.getEnchantName().equals(name)) {
				
				return ench;
			}
		}
		
		return null;
	}
	
	public LGEnchants getEnchant(Enchantment enchant) {
		
		for (LGEnchants ench : enchantLimit) {
			if (ench.getEnchant().equals(enchant)) {
				
				return ench;
			}
		}
		
		return null;
	}
	
	public String getConfigString(String path) {
		String string = getConfig().getString(path)
				.replace("%date", date());
		
		return string;
	}
	
	public String date() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		
		return sdf.format(date);
	}
	
	//TEAM NHIDE
	public void nhideReset() {
		
		if (!nhide.getEntries().isEmpty()) {
			for (String entry : nhide.getEntries()) {
				nhide.removeEntry(entry);
			}
		}
	}
	
	//CHAT OPTION
	public Boolean hasOptionChat(UUID uuid) {
		return OptionChat.containsKey(uuid);
	}
	
	public void setOptionChat(UUID uuid, LGOption op) {
		if (hasOptionChat(uuid)) {
			OptionChat.remove(uuid);
			if (Bukkit.getPlayer(uuid) != null) Bukkit.getPlayer(uuid).sendMessage(gameTag + "§aVotre précédente action dans le Chat vient d'être remplacé !");
		}
		
		OptionChat.put(uuid, op);
	}
	
	public void removeOptionChat(UUID uuid, Boolean msg) {
		if (OptionChat.containsKey(uuid)) {
			OptionChat.remove(uuid);
			if (msg) {
				if (Bukkit.getPlayer(uuid) != null) Bukkit.getPlayer(uuid).sendMessage(gameTag + "§aVotre précédente action dans le Chat vient d'être retiré !");
			}
		}
	}
	
	public void removeOptionChat(UUID uuid) {
		removeOptionChat(uuid, true);
	}
	
	public LGOption getOptionChat(UUID uuid) {
		if (OptionChat.containsKey(uuid)) {
			return OptionChat.get(uuid);
		}
		return null;
	}
	
	//PERMISSIONS
	public void setHost(UUID uuid) {
		this.host = uuid;
	}
	public Player gethost() {
		return Bukkit.getPlayer(host);
	}
	
	//STATE
	public void setState(UHCState state) {
		this.state = state;
	}
	
	public Boolean isState(UHCState state) {
		return this.state == state;
	}
	
	public Boolean canJoin() {
		if (state == UHCState.WAITING || state == UHCState.WAITING_GAME || state == UHCState.STARTING) return true;
		else return false;
	}
	
	//SPECTATORS
	public List<Player> getSpectator() {
		List<Player> players = new ArrayList<>();
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (Spectator.contains(pl.getUniqueId())) {
				players.add(Bukkit.getPlayer(pl.getUniqueId()));
			}
		}
		return players;
	}
	
	//PLAYERS
	public Joueur getPlayer(UUID uuid) {
		for (Joueur player : Players) {
			if (player.getUUID().equals(uuid)) {
				return player;
			}
		}
		return null;
	}
	
	//PLAYERS
	public Joueur getPlayer(String name) {
		for (Joueur player : Players) {
			if (player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}
	
	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<>();
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (!(Spectator.contains(pl.getUniqueId()))) {
				players.add(pl);
			}
		}
		return players;
	}
	
	public List<Joueur> getPlayerRoles(LGRoles role) {
		List<Joueur> joueurs = new ArrayList<>();
		for (Joueur j : getJoueurs()) {
			if (j.isConnected()) {
				if (j.getRole() == role) {
					if (!j.isDead()) {
						joueurs.add(j);
					}
				}
			}
		}
		return joueurs;
	}
	
	//LIMITE DE GROUPE
	public List<Joueur> getPlayerRolesOff(LGRoles role) {
		List<Joueur> joueurs = new ArrayList<>();
		for (Joueur j : this.Players) {
			if (j.getRole() == role) {
				if (!j.isDead()) {
					joueurs.add(j);
				}
			}
		}
		return joueurs;
	}
	
	public List<LGRoles> getFakeRoles() {
		
		List<LGRoles> compo = new ArrayList<>();
		
		for (Joueur j : getJoueursOff()) {
			if (j.getRole() != LGRoles.LGFeutre) {
				if (!compo.contains(j.getRole())) compo.add(j.getRole());
			}
		}
		return compo;
	}
	
	public void setLimitGroup() {
		int size = getJoueursOff().size();
		
		if (size >= 15) {
			GroupLimit = 5;
			return;
		}
		
		if (size >= 10) {
			GroupLimit = 4;
			return;
		}
		
		GroupLimit = 3;
	}
	
	public boolean hasRole(UUID uuid) {
		
		if (getPlayer(uuid) == null) return false;
		else return true;
	}
	
	public boolean hasRole(Player player) {
		return hasRole(player.getUniqueId());
	}
	
	public boolean isPlaying(UUID uuid) {
		
		if (Spectator.contains(uuid)) return false;
		
		if (PlayerHasRole) {
			if (hasRole(uuid)) {
				if (!getPlayer(uuid).isDead()) return true;
			}
			
			return false;
		} 
		else {
			if (PlayerInGame.contains(uuid)) return true;
		}
		
		return false;

	}
	
	
	//ROLE
	public List<LGRoles> allRoles() {
		List<LGRoles> roles = new ArrayList<>();
		
		for (LGRoles role : LGRoles.values()) {
			if (role != LGRoles.SV) {
				roles.add(role);
			}
		}
		
		return roles;
	}
	
	public boolean RoleIsEmpty() {
		
		for (LGRoles role : LGRoles.values()) {
			if (role.number > 0 && role != LGRoles.SV) return false;
		}
		return true;
	}
	
	public boolean PlayersHasRole() {
		return PlayerHasRole;
	}
	
	public List<Joueur> getJoueurs() {
		List<Joueur> joueurs = new ArrayList<>();
		for (Joueur j : Players) {
			if (j.isConnected()) {
				if (!j.isDead()) {
					joueurs.add(j);
				}
			}
		}
		
		return joueurs;
	}
	
	public List<Joueur> getJoueursOff() {
		List<Joueur> joueurs = new ArrayList<>();
		for (Joueur j : Players) {
			if (!j.isDead()) {
				joueurs.add(j);
			}
		}
		
		return joueurs;
	}
	
	public List<Joueur> getLg() {
		
		List<Joueur> joueurs = new ArrayList<>();
		for (Joueur j : Players) {
			if (j.isConnected()) {
				if (!j.isDead()) {
					if (j.getCamp() == LGCamps.LoupGarou || j.getCamp() == LGCamps.LGB)
					joueurs.add(j);
				}
			}
		}
		
		return joueurs;
	}
	
	public List<Joueur> getLgOff() {
		
		List<Joueur> joueurs = new ArrayList<>();
		for (Joueur j : Players) {
			if (!j.isDead()) {
				if (j.getCamp() == LGCamps.LoupGarou || j.getCamp() == LGCamps.LGB)
				joueurs.add(j);
			}
		}
		
		return joueurs;
	}
	
	public boolean isNight() {
		long time = world.getTime();
		return time >= 13000 && time < 23500;
	}
	
	public boolean isDay() {
		long time = world.getTime();
		return time >= 23500 || time < 13000;
	}
	
	public void addLg(Joueur j) {
		
		if (j.getCamp() != LGCamps.LoupGarou) {
			
			for (Joueur lg : getLg()) {
				
				if (lg.isConnected()) {
					lg.getPlayer().sendMessage(gameTag + "§cUn nouveau joueur à rejoint votre camp ! Faites /lg role pour plus de détails !");
					new Sounds(lg.getPlayer()).PlaySound(Sound.WOLF_GROWL);
				}
			}
			
			
			j.setCamp(LGCamps.LoupGarou);
			j.getPlayer().sendMessage(gameTag + "§cVous pouvez voir la liste des loups-garous en vie grâce a la commande /lg rôle !");
		}
	}
	
	public void revealLg(Joueur j) {
		for (Joueur lg : getLg()) {
			
			if (!lg.equals(j)) {
				if (lg.isConnected()) {
					lg.getPlayer().sendMessage(gameTag + "§cUn nouveau joueur à rejoint votre camp ! Faites /lg role pour plus de détails !");
					new Sounds(lg.getPlayer()).PlaySound(Sound.WOLF_GROWL);
				}
			}

		}
		
		j.getPlayer().sendMessage(gameTag + "§cVous pouvez voir la liste des loups-garous en vie grâce a la commande /lg rôle !");
	}
	
	//CIVILISATION ----------------------------
	public LGCivilisation getVillage(UUID uuid) {
		if (fun && Civilisation) {	
			for (LGCivilisation civ : civilisations) {
				if (civ.hasPlayer(uuid)) {
					return civ;
				}
			}
			
		}
		return null;
	}
	
	public Boolean PlayerHasVillage(UUID uuid) {
		if (fun && Civilisation) {	
			if (getVillage(uuid) != null) return true;
		}
		return false;

	}
	
	public void sendStaffMsg(String msg) {
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getUniqueId().equals(host) || orgas.contains(p.getUniqueId())) {
				p.sendMessage(msg);
			}
		}
	}
	
	public void reset(Player player) {
		
		player.setGameMode(GameMode.ADVENTURE);
		
		EntityLiving cp = ((CraftPlayer)player).getHandle();
		cp.setAbsorptionHearts(0);
		cp.removeAllEffects();
		
		player.setMaxHealth(20);
		
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(20);
		
		player.setLevel(0);
		player.setExp(0);
		
		clearArmor(player);
		
		for(ItemStack item:player.getInventory().getContents()){
			player.getInventory().remove(item);
			player.updateInventory();
		}
		
		for(PotionEffect effect:player.getActivePotionEffects()){player.removePotionEffect(effect.getType());}
	}
	
	public void clearInventory(Player player) {
		
		for(ItemStack item:player.getInventory().getContents()){
			player.getInventory().remove(item);
			player.updateInventory();
		}
		
		clearArmor(player);
	}
	
	//RANDOMTP
	public void randomTp(Player player) {
   	    
        Random random = new Random();
       
	    int rangeMax = (int) wb.getSize() /2 - 15;
	    int rangeMin = (int) -wb.getSize() /2 + 15;
	    
	    if (MaxRange != 0) {
		    rangeMax = (int) MaxRange - 15;
		    rangeMin = - (int) MaxRange + 15;
	    }
	    
	    int x = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
	    int z = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
        int y = MaxY;
        
        Location teleportLocation = new Location(player.getWorld(), x + Spawn.getX(), y, z + Spawn.getZ());
       
       player.teleport(teleportLocation);
	}
	
	//RANDOMTP
	public void TpPower(Player player) {
   	    
        Random random = new Random();
        
        Location teleportLocation = null;
        
	    int rangeMax = (int) wb.getSize() /2 - 15;
	    int rangeMin = - (int) wb.getSize() /2 + 15;
	    
	    if (MaxRange != 0) {
		    rangeMax = (int) MaxRange /2 - 15;
		    rangeMin = - (int) MaxRange /2 + 15;
	    }
	    
	    int x = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
	    int z = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
        int y = MaxY;
        
        teleportLocation = new Location(player.getWorld(), x + Spawn.getX(), y, z + Spawn.getZ());
       
        player.teleport(teleportLocation);
        
        noFall.add(player.getUniqueId());
        
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			
			@Override
			public void run() {
				 noFall.remove(player.getUniqueId());
			}
		}, 200);
      
	}
	
	public void respawn(Player player) {
		
		player.setGameMode(GameMode.SURVIVAL);
		respawnInstant(player);
		player.setHealth(player.getMaxHealth());
		
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			
			@Override
			public void run() {
				TpPower(player);
			}
			
		}, 5);
	}
	
    public void respawnInstant(final Player player) {
        Bukkit.getScheduler().runTaskLater(this, new Runnable () {
        	
        @Override
        public void run() {
        	PacketPlayInClientCommand paquet = new PacketPlayInClientCommand (EnumClientCommand.PERFORM_RESPAWN);
        	((CraftPlayer) player).getHandle().playerConnection.a(paquet);
        }
     }, 5L);
   }

    
    
	public void configInvBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§5Menu: §dConfiguration");
		
		for (int i = 18; i < 27; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		for (int i = 0; i < 9; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		inv.setItem(9, vitre(15));
		inv.setItem(17, vitre(15));
		
		inv.setItem(10, getItem(Material.DIAMOND_SWORD, "§6Scénarios"));
		inv.setItem(11, getItem(Material.BOOKSHELF, "§6Rôles"));
		inv.setItem(12, getItem(Material.BEACON, "§6WorldBorder"));
		inv.setItem(13, getItem(Material.ANVIL, "§6Règles LGUHC"));
		inv.setItem(14, getItem(Material.SKULL_ITEM, (byte) 2,"§6Gestion Joueurs"));
		inv.setItem(15, getItem(Material.WATCH, "§6Gestion Temps"));
		inv.setItem(16, getItem(Material.CHEST, "§6Gestion Stuff"));
		
		inv.setItem(8, getItem(Material.ENDER_PEARL, "§6Gestion Configuration"));
		
		inv.setItem(22, getItem(Material.SLIME_BALL, "§aLancer la partie ?"));
		
		if (isState(UHCState.WAITING)) inv.setItem(21, getItem(Material.INK_SACK, (byte) 4, "§9Lancer mini jeu ?"));
		if (isState(UHCState.WAITING_GAME)) inv.setItem(21, getItem(Material.INK_SACK, (byte) 4, "§cArrêter mini jeu ?"));
			
		if (isState(UHCState.STARTING)) inv.setItem(23, getItem(Material.REDSTONE_BLOCK, "§cAnnuler lancement ?"));
		
		inv.setItem(26, getItem(Material.BARRIER, "§cSortir"));
		
		p.openInventory(inv);
		
	}
	
	public void getSpectatorInv(Player player) {
		Inventory inv = Bukkit.createInventory(null, 36, "§6Spectateurs :");
		int slot = 0;
		
		for (Player p : getSpectator()) {
			inv.setItem(slot, getHead(p));
			slot++;
		}
		
		for (int i = 27; i < 36; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		inv.setItem(30, getItem(Material.WOOL, (byte) 13,"§aAjouter"));
		inv.setItem(31, getItem(Material.WOOL, (byte) 14, "§cRetirer"));
		inv.setItem(35, getItem(Material.BARRIER, "§cQuitter"));
		
		player.openInventory(inv);
	}
	
	public void InvConfigSaveBuilder(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 36, "§5Menu: §6Configurations");
		
		int slot = 0;
		
		List<String> lore = Arrays.asList("§7>> §fClique Gauche : §aLoad", "§7>> §fClique Droit : §cRemove", "§7>> §fClique Molette : §eEcraser");
		
		if (new ConfigManager(this).getAllConfig().size() > 0) {
			for (String yml : new ConfigManager(this).getAllConfig()) {
				String name = yml.substring(0, yml.length() - 4);
				
				FileConfiguration config = YamlConfiguration.loadConfiguration(new File(getDataFolder() + File.separator + "Configurations" + File.separator + yml));
				
				if (name.equalsIgnoreCase("classic")) {
					inv.setItem(slot, getItem(Material.BOOK, "§7" + name, 1, false, lore));
				} else {
					
					Material mat = Material.getMaterial(config.getString("Icone.Material"));
					ItemStack item = new ItemStack(mat, 1, (byte) config.getInt("Icone.Byte"));
					ItemMeta itemM = item.getItemMeta();
					
					itemM.setDisplayName("§6" + name);
					itemM.setLore(lore);
					item.setItemMeta(itemM);
					
					inv.setItem(slot, item);
				}
				
				slot++;
			}
		}
		
		for (int i = 27; i < 36; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		inv.setItem(31, getItem(Material.CHEST,"§aSauvegarder"));
		inv.setItem(35, getItem(Material.BARRIER, "§cRevenir en arrière"));
		p.openInventory(inv);
	}
	
	public void InvStaffBuilder(Player player) {
		
		Inventory inv = Bukkit.createInventory(null, 36, "§6Staff :");
		
		for (int i = 0; i < 9; i ++) {
			inv.setItem(i, vitre(15));
		}
		
		if (host != null) {
			inv.setItem(4, getHead(Bukkit.getPlayer(host), "§4"));
			if (player.getUniqueId().equals(host)) {
				inv.setItem(0, getItem(Material.WOOL, (byte) 13,"§aAjouter Orga"));
				inv.setItem(1, getItem(Material.WOOL, (byte) 14,"§cRetirer Orga"));
				inv.setItem(7, getItem(Material.ANVIL,"§cNe plus être Host"));
				inv.setItem(8, getItem(Material.ENDER_PEARL,"§6Changer Host"));
			}
		}
		
		int slot = 9;
		
		for (UUID uuid : orgas) {
			inv.setItem(slot, getHead(Bukkit.getPlayer(uuid), "§b"));
			slot++;
		}
		
		inv.setItem(35, getItem(Material.BARRIER, "§cQuitter"));
		
		player.openInventory(inv);
	}
	
	public void createCage() {
		
		for (int x = Spawn.getBlockX()-cageSize; x <= Spawn.getBlockX() + cageSize; x++) {
			for (int z = Spawn.getBlockZ()-cageSize; z <= Spawn.getBlockX() + cageSize; z++) {
					
				world.getBlockAt(x, Spawn.getBlockY() , z).setType(Material.BARRIER);
			}
		}
			
		Location l1 = new Location(world, Spawn.getX() - cageSize, Spawn.getY() + cageHeight, Spawn.getZ() + cageSize);
		Location l2 = new Location(world, Spawn.getX() + cageSize, Spawn.getY(), Spawn.getZ() - cageSize);
		
		wallArrounndRegion(l1, l2, Material.BARRIER);
			
		System.out.println("[LOUP-GAROUS] " + ANSI_GREEN + "La cage au spawn a ete genere !" + ANSI_RESET);
	}
	
    public void wallArrounndRegion(Location l1, Location l2, Material mat) {
        int minX = Math.min(l1.getBlockX(), l2.getBlockX());
        int maxX = Math.max(l1.getBlockX(), l2.getBlockX());
        int minY = Math.min(l1.getBlockY(), l2.getBlockY());
        int maxY = Math.max(l1.getBlockY(), l2.getBlockY());
        int minZ = Math.min(l1.getBlockZ(), l2.getBlockZ());
        int maxZ = Math.max(l1.getBlockZ(), l2.getBlockZ());
             
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                Block b = new Location(l1.getWorld(), x, y, minZ).getBlock();
                b.setType(mat);
            }
        }
     
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                Block b = new Location(l1.getWorld(), x, y, maxZ).getBlock();
                b.setType(mat);
            }
        }
     
        for (int z = minZ; z <= maxZ; z++) {
            for (int y = minY; y <= maxY; y++) {
                Block b = new Location(l1.getWorld(), minX, y, z).getBlock();
                b.setType(mat);
            }
        }
     
        for (int z = minZ; z <= maxZ; z++) {
            for (int y = minY; y <= maxY; y++) {
                Block b = new Location(l1.getWorld(), maxX, y, z).getBlock();
                b.setType(mat);
            }
        }
    }
	
	public List<ItemStack> getInventory(Player player) {
		
		List<ItemStack> inv = new ArrayList<>();
		
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null) inv.add(item);
		}
		
		for (ItemStack item : player.getInventory().getArmorContents()) {
			if (item != null) inv.add(item);
		}
		
		return inv;
	}
	
	public void kill(UUID dead, String name, Location grave, List<ItemStack> inv) {
		
		String village = "Le village";
		
		if (PlayerHasVillage(dead)) {
			village = getVillage(dead).getName();
		}
		
		Bukkit.broadcastMessage("§c§l==========§4§k0§c§l==========");
		Bukkit.broadcastMessage("§2" + village + " à perdu un de ses membres : §l" + name);
		Bukkit.broadcastMessage("§c§l=====================");	
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
		  	pl.playSound(pl.getLocation(), Sound.AMBIENCE_THUNDER, 8.0F, 0.8F);
		}
		
		dropInventory(dead, grave, inv);
		
		if (!Spectator.contains(dead)) Spectator.add(dead);
		
		if (Bukkit.getPlayer(dead) != null) {
			
			Player player = Bukkit.getPlayer(dead);
			
			player.setPlayerListName("§7[Spec] §f" + name);
			
			player.sendMessage("§6===============§4§k0§6===============");
			player.sendMessage("§bVous êtes spectateur : §9faites /spec §bpour accéder aux commandes des spectateurs !");
			player.sendMessage("§6===============§4§k0§6===============");
			player.sendMessage(" ");
				
			reset(player);
			player.setGameMode(GameMode.SPECTATOR);
		}
		
		if (stats) new StatManager(this).changeState(dead, "mort", StatAction.Increase);
	}
	
	public void dropInventory(UUID uuid, Location grave, List<ItemStack> inv) {
		
		if (GoldenHead) {
			
		    ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		    SkullMeta meta = (SkullMeta)head.getItemMeta();
		    
		    if (Bukkit.getPlayer(uuid) != null) {
			    meta.setOwner(Bukkit.getPlayer(uuid).getName());
			    meta.setDisplayName("§6Tête de §c" + Bukkit.getPlayer(uuid).getName());
		    }

		    meta.setLore(Arrays.asList(new String[] { "§bCette tête a été récupéré ", "§bsur un joueur" }));
		    head.setItemMeta(meta);
		}
		
		if (!inv.isEmpty()) {
			
			for (ItemStack it : inv) {
				if (it != null) {
					if (it.getType() != Material.AIR) {
						grave.getWorld().dropItemNaturally(grave, it);
					}
				}
			}
		}
		
	}
	
	private ItemStack getHead(Player player) {
		
	    ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
	    if (player == null) return head;
	    SkullMeta meta = (SkullMeta)head.getItemMeta();
	    
	    meta.setOwner(player.getName());
	    meta.setDisplayName("§6" + player.getName());
	    
	    head.setItemMeta(meta);
	    
	    return head;
	}
	
	private ItemStack getHead(Player player, String color) {
		
	    ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
	    if (player == null) return head;
	    SkullMeta meta = (SkullMeta) head.getItemMeta();
	    
	    meta.setOwner(player.getName());
	    meta.setDisplayName(color + player.getName());
	    
	    head.setItemMeta(meta);
	    
	    return head;
	}
	
	public ItemStack getItem(Material material, String Name) {
		
		ItemStack Item = new ItemStack(material);
		ItemMeta ItemM = Item.getItemMeta();
		
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}
	
	
	public ItemStack getItem(Material material, int number, String Name) {
		
		ItemStack Item = new ItemStack(material);
		ItemMeta ItemM = Item.getItemMeta();
		
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}
	
	
	private ItemStack getItem(Material material, byte tag, String Name) {
		
		ItemStack Item = new ItemStack(material, 1, tag);
		ItemMeta ItemM = Item.getItemMeta();
		
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}
	
	private ItemStack getItem(Material material, String Name, int number, boolean enchant, List<String> lore) {
		
		ItemStack Item = new ItemStack(material, number);
		ItemMeta ItemM = Item.getItemMeta();
		
		if (enchant) {
			ItemM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
		}
		ItemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ItemM.setDisplayName(Name);
		ItemM.setLore(lore);
		
		Item.setItemMeta(ItemM);
		
		return Item;
	}
	
	private ItemStack vitre(int color) {
		
		ItemStack vitre = new ItemStack(Material.STAINED_GLASS_PANE, 1 , (byte) color);
		ItemMeta vitreM = vitre.getItemMeta();
		
		vitreM.setDisplayName(" ");
		vitreM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		vitre.setItemMeta(vitreM);
		
		return vitre;
	}
	
    public void clearArmor(Player player) {
    	player.getInventory().setHelmet(null);
    	player.getInventory().setChestplate(null);
    	player.getInventory().setLeggings(null);
    	player.getInventory().setBoots(null);
    	
    	player.updateInventory();
    }
    
	public String getEnchantName(Enchantment enchant) {
		
		if (enchant.equals(Enchantment.ARROW_DAMAGE)) return "Power";
		if (enchant.equals(Enchantment.ARROW_FIRE)) return "Flame";
		if (enchant.equals(Enchantment.ARROW_INFINITE)) return "Infinity";
		if (enchant.equals(Enchantment.ARROW_KNOCKBACK)) return "Punch";
		if (enchant.equals(Enchantment.DAMAGE_ALL)) return "Sharpness";
		if (enchant.equals(Enchantment.DAMAGE_ARTHROPODS)) return "Bane of arthropods";
		if (enchant.equals(Enchantment.DAMAGE_UNDEAD)) return "Smite";
		if (enchant.equals(Enchantment.DEPTH_STRIDER)) return "Depth Strider";
		if (enchant.equals(Enchantment.DIG_SPEED)) return "Efficiency";
		if (enchant.equals(Enchantment.DURABILITY)) return "Unbreaking";
		if (enchant.equals(Enchantment.FIRE_ASPECT)) return "Fire Aspect";
		if (enchant.equals(Enchantment.KNOCKBACK)) return "Knockback";
		if (enchant.equals(Enchantment.LOOT_BONUS_BLOCKS)) return "Fortune";
		if (enchant.equals(Enchantment.LOOT_BONUS_MOBS)) return "Looting";
		if (enchant.equals(Enchantment.LUCK)) return "Luck of the sea";
		if (enchant.equals(Enchantment.LURE)) return "Lure";
		if (enchant.equals(Enchantment.OXYGEN)) return "Respiration";
		if (enchant.equals(Enchantment.PROTECTION_ENVIRONMENTAL)) return "Protection";
		if (enchant.equals(Enchantment.PROTECTION_EXPLOSIONS)) return "Blast Protection";
		if (enchant.equals(Enchantment.PROTECTION_FALL)) return "Feather Falling";
		if (enchant.equals(Enchantment.PROTECTION_FIRE)) return "Fire Protection";
		if (enchant.equals(Enchantment.PROTECTION_PROJECTILE)) return "Projectile Protection";
		if (enchant.equals(Enchantment.SILK_TOUCH)) return "Silk Touch";
		if (enchant.equals(Enchantment.THORNS)) return "Thorns";
		if (enchant.equals(Enchantment.WATER_WORKER)) return "Aqua Affinity";
		
		return null;
	}
	
	public String getPotionName(PotionEffectType pot) {
		
		if (pot.equals(PotionEffectType.FIRE_RESISTANCE)) return "Fire Resistance";
		if (pot.equals(PotionEffectType.HARM)) return "Instant Damage";
		if (pot.equals(PotionEffectType.HEAL)) return "Instant Heal";
		if (pot.equals(PotionEffectType.INCREASE_DAMAGE)) return "Strength";
		if (pot.equals(PotionEffectType.INVISIBILITY)) return "Invisibility";
		if (pot.equals(PotionEffectType.JUMP)) return "Jump";
		if (pot.equals(PotionEffectType.NIGHT_VISION)) return "Night Vision";
		if (pot.equals(PotionEffectType.POISON)) return "Poison";
		if (pot.equals(PotionEffectType.REGENERATION)) return "Regeneration";
		if (pot.equals(PotionEffectType.SLOW)) return "Slowness";
		if (pot.equals(PotionEffectType.SPEED)) return "Speed";
		if (pot.equals(PotionEffectType.WATER_BREATHING)) return "Water Breathing";
		if (pot.equals(PotionEffectType.WEAKNESS)) return "Weakness";
		
		return null;
	}
    
}
       
