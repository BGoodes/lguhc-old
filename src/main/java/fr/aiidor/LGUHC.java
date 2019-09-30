package fr.aiidor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import fr.aiidor.commands.CommandLg;
import fr.aiidor.commands.CommandOrga;
import fr.aiidor.commands.CommandSpec;
import fr.aiidor.effect.Sounds;
import fr.aiidor.event.EventsManager;
import fr.aiidor.game.Joueur;
import fr.aiidor.game.UHCState;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;
import fr.aiidor.scoreboard.ScoreboardSign;
import fr.aiidor.utils.LGCivilisation;
import fr.aiidor.utils.UHCLootCrate;
import fr.aiidor.utils.UHCNoFall;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;

public class LGUHC extends JavaPlugin{

	public List<UUID> Spectator = new ArrayList<>();
	public List<Joueur> Players = new ArrayList<>();
	public List<UUID> PlayerInGame = new ArrayList<>();

	public ArrayList<LGRoles> compo = new ArrayList<>();
	public boolean hidecompo = false;

	public HashMap<Player, ScoreboardSign> boards = new HashMap<>();

	public ArrayList<Joueur> death = new ArrayList<>();

	public Boolean coupleAlea = false;

	public Boolean cage = true;

	//ORGAS
	private UHCState state;
	public UUID host;
	public ArrayList<UUID> orgas = new ArrayList<>();

	//PERMS ORGA
	public boolean chat = false;

	public int PlayerMin = 1;
	public boolean cancelStart = false;
	public boolean PlayerHasRole = false;

	public int GroupLimit = 9;
	public int ep = 1;

	public final String gameTag = "§b§l[§6§lLOUP-GAROUS§b§l] §r";
	public String gameName = "§f§lLG UHC";


	public Location Spawn;
	public World world;
	public WorldBorder wb;

	//OPTION
	public Integer Map = 1000;
	public Integer wbEP = 5;
	public Integer wbMax = 300;

	public Integer epTime = 20;
	public Boolean DailyCycle = true;
	public Integer pvp = 25;
	public Integer announceRole = 20;
	public Integer voteEp = 3;

	public ItemStack[] startItem;
	public ItemStack[] deathItem;

	public boolean deathChat = false;

	//SCENARIOS
	public boolean run = false;

	public Boolean cutclean = true;
	public Boolean timber = true;
	public Boolean bleedingSweet = false;
	public Boolean HasteyBoys = true;
	public Boolean fastSmelting = false;

	public Integer diamondlimit = 17;
	public Boolean BloodDiamond = false;

	public Boolean FireEnchantLess = true;

	public Integer LimiteDePiece = 2;


	public Boolean oreLess = false;
	public Boolean diamondLess = true;
	public Boolean goldLess = true;
	public Boolean ironLess = true;
	public Boolean coalLess = true;
	public Boolean redstoneLess = true;
	public Boolean lapisLess = true;
	public Boolean quartzLess = true;

	public Boolean enchantLess = false;

	public Boolean FinalHeal = false;

	public Integer Abso = 2;
	public Boolean Notch = false;

	public Boolean GoldenHead = false;
	public Integer HeadHeal = 4;
	public Boolean Cupid = false;

	public Boolean RodLess  = true;

	public Integer SpeedyMiner = 1;

	public Boolean vanilla = false;
	public Integer flint = 15;
	public Integer apples = 1;
	public Boolean infiniteEnc = false;
	public Boolean noHunger = false;
	public Boolean CatEyes = false;

	public Boolean vanillaN = true;
	public Boolean xpNerf = true;
	public Boolean noNether = true;
	public Boolean noMine = false;
	public Integer noMineEp = 1;
	public Integer noMineCouche = 32;

	public Boolean NightMare = false;
	public Boolean PoisonLess = false;

	public Boolean fun = false;

	public Boolean goneFishin = false;
	public Boolean skyHigh = false;
	public Boolean eggs = false;

	public Boolean LootCrate = false;
	public ArrayList<ItemStack> Loots = new ArrayList<>();

	public Boolean Civilisation = false;
	public Integer vilRange = 400;
	public Integer vilSize = 50;
	public List<String> civNames = new ArrayList<>();

	public Boolean meetup = false;

	public void addNames() {
		civNames.add("Thiercelieux");
		civNames.add("Bedburg");
		civNames.add("Besançon");
		civNames.add("Dole");
		civNames.add("Edimbourg");
	}

	public int CivSize = 6;

	public List<LGCivilisation> civilisations = new ArrayList<>();

	//VOTE
	public Boolean canVote = false;
	public Boolean canSeeVote = false;

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


		if (Bukkit.getWorld(getConfig().getString("Spawn.worldName")) == null) {
			if (getConfig().getBoolean("MultiWorld.state")) {

				WorldCreator Wc = new WorldCreator(getConfig().getString("Spawn.worldName"));
				if (getConfig().getLong("Multiword.seed") != 0) {
					Wc.seed(getConfig().getLong("Multiword.seed"));
				}

				Wc.type(WorldType.NORMAL);
				Wc.generateStructures(getConfig().getBoolean("Multiworld.generateStructure"));

				System.out.println(ANSI_GREEN + "[LOUP-GAROUS] Création du monde : " + ANSI_WHITE + getConfig().getString("Spawn.worldName") + ANSI_RESET);

				Wc.createWorld();

			} else {
				Bukkit.getPluginManager().disablePlugin(this);
				System.out.println(ANSI_RED  + "[LOUP-GAROUS] Monde invalide ! (Plugin Off)" + ANSI_RESET);
				return;
			}

		}

		state = UHCState.WAITING;

		EventsManager.registerEvents(this);

		world = Bukkit.getWorld(getConfig().getString("Spawn.worldName"));
		Spawn = getLocation(getConfig().getString("Spawn.location"));
		cage = getConfig().getBoolean("Spawn.cage");

		wb = world.getWorldBorder();

		//BORDER
		wb.setCenter(Spawn.getX(), Spawn.getZ());
		wb.setSize(Map * 2);

		getCommand("config").setExecutor(new CommandOrga(this));
		getCommand("lg").setExecutor(new CommandLg(this));
		getCommand("spec").setExecutor(new CommandSpec(this));
		world.setPVP(false);

		world.setGameRuleValue("reducedDebugInfo", "true");
		world.setGameRuleValue("naturalRegeneration", "false");
		world.setGameRuleValue("announceAdvancements", "false");
		world.setGameRuleValue("spawnRadius", "0");
		world.setGameRuleValue("keepInventory", "true");
		world.setGameRuleValue("showDeathMessages", "false");

		ShapedRecipe recipe = new ShapedRecipe(getItem(Material.GOLDEN_APPLE, "§6Golden Head"));
		recipe.shape(new String[] { "GGG", "GSG", "GGG" });
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('S', new ItemStack(Material.SKULL_ITEM, 1, (short) 3).getData());

		getServer().addRecipe(recipe);

		System.out.println(ANSI_RED +  "========================================" + ANSI_RESET);
		System.out.println(ANSI_GREEN + "Plugin LOUP-GAROU UHC - Version 0.4" + ANSI_RESET);
		System.out.println(ANSI_YELLOW + "      Auteur : B_Goodes" + ANSI_RESET);
		System.out.println(ANSI_RED +  "========================================" + ANSI_RESET);

		addNames();
		new UHCLootCrate(this).setLoots();
	}


	private Location getLocation(String loc) {
		String[] args = loc.split(",");

		double x = Double.valueOf(args[0]);
		double y = Double.valueOf(args[1]);
		double z = Double.valueOf(args[2]);

		return new Location(world, x, y, z);
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
		if (state == UHCState.WAITING || state == UHCState.STARTING) return true;
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

	public int getLgGroup() {
		int lg = 0;
		for (Joueur j : getJoueursOff()) {
			if (j.getRole() == LGRoles.LG || j.getRole() == LGRoles.VPL || j.getRole() == LGRoles.IPL) {
				if (!j.isDead()) {
					lg ++;
				}
			}
		}
		return lg;
	}

	public int getVillageGroup() {
		int village = 0;
		for (Joueur j : getJoueursOff()) {
			if (!j.isLg() || j.isInfect() || j.getRole() == LGRoles.EnfantS) {
				if (!j.isDead()) {
					village ++;
				}
			}
		}
		return village;
	}

	public void setLimitGroup() {
		//LIMITE DE GROUPE
		if (getLgGroup() <= getVillageGroup()) GroupLimit = getLgGroup();
		else GroupLimit =  getVillageGroup();
		if (GroupLimit < 3) GroupLimit = 3;
	}

	public boolean isInGame(UUID uuid) {

		if (getPlayer(uuid) == null) return false;
		else return true;
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

	public void addLg(Joueur j) {

		if (!j.isLg()) {

			for (Joueur lg : getLg()) {

				if (lg.isConnected()) {
					lg.getPlayer().sendMessage(gameTag + "§cUn nouveau joueur à rejoint votre camp ! Faites /lg role pour plus de détails !");
					new Sounds(lg.getPlayer()).PlaySound(Sound.WOLF_GROWL);
				}

				if (lg.getRole() == LGRoles.LGA && lg.getPower() == 1) {
					lg.lglist.add(j);
				}

				j.setCamp(LGCamps.LoupGarou);
				j.getPlayer().sendMessage(gameTag + "§cVous pouvez voir la liste des loups-garous en vie grâce a la commande /lg rôle !");
			}
		}
	}

	public boolean canSeeLgList() {
		if (compo.contains(LGRoles.Trublion)) {
			if (ep >= 3) {
				for (Joueur j : Players) {
					if (j.getRole() == LGRoles.Trublion) {
						if (j.getPower() == 1) return false;
					}
				}
			} else return false;
		}
		return true;
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

	//RANDOMTP
	public void randomTp(Player player, Integer range) {

		Random random = new Random();

		int rangeMax = range;
		int rangeMin = -range;


		int x = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
		int z = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
		int y = 150;

		Location teleportLocation = new Location(player.getWorld(), x + Spawn.getX(), y, z + Spawn.getZ());

		player.teleport(teleportLocation);
	}

	//RANDOMTP
	public void TpPower(Player player) {

		Random random = new Random();

		Location teleportLocation = null;

		int rangeMax = (int) wb.getSize() /2 - 10;
		int rangeMin = - (int) wb.getSize() /2 + 10;


		int x = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
		int z = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
		int y = 150;

		teleportLocation = new Location(player.getWorld(), x + Spawn.getX(), y, z + Spawn.getZ());

		player.teleport(teleportLocation);

		if (isInGame(player.getUniqueId())) {
			new UHCNoFall(getPlayer(player.getUniqueId())).runTaskTimer(this, 0, 20);
		}

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
				((CraftPlayer) player).getHandle ().playerConnection.a(paquet);
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

		inv.setItem(15, getItem(Material.WATCH, "§6Gestion Temps"));
		inv.setItem(16, getItem(Material.CHEST, "§6Gestion Stuff"));

		inv.setItem(22, getItem(Material.SLIME_BALL, "§aLancer la partie ?"));

		if (isState(UHCState.STARTING)) inv.setItem(23, getItem(Material.REDSTONE_BLOCK, "§cAnnuler lancement ?"));

		inv.setItem(26, getItem(Material.BARRIER, "§cSortir"));

		p.openInventory(inv);

	}

	private ItemStack getItem(Material material, String Name) {

		ItemStack Item = new ItemStack(material);
		ItemMeta ItemM = Item.getItemMeta();

		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);

		return Item;
	}

	private ItemStack vitre(int color) {

		ItemStack vitre = new ItemStack(Material.STAINED_GLASS_PANE, 1 , (byte) color);
		ItemMeta vitreM = vitre.getItemMeta();

		vitreM.setDisplayName(" ");
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
}
       
