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
import org.bukkit.World;
import org.bukkit.WorldBorder;
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
import fr.aiidor.event.EventsManager;
import fr.aiidor.game.Joueur;
import fr.aiidor.game.UHCState;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;
import fr.aiidor.scoreboard.ScoreboardSign;
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

	public String gameTag = "§b§l[§6§lLOUP-GAROUS§b§l] §r";
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

	public Boolean diamondLess = false;
	public Boolean oreLess = false;

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

	public Boolean xpNerf = false;

	public Boolean fun = false;

	public Boolean goneFishin = false;
	public Boolean skyHigh = false;
	public Boolean eggs = false;

	//VOTE
	public Boolean canVote = false;
	public Boolean canSeeVote = false;


	@Override
	public void onEnable() {

		saveDefaultConfig();

		state = UHCState.WAITING;

		EventsManager.registerEvents(this);

		world = Bukkit.getWorld(getConfig().getString("Spawn.worldName"));
		Spawn = getLocation(getConfig().getString("Spawn.location"));
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

		System.out.println("[LOUP-GAROU] Plugin foncionnel !");
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
		for (Joueur lg : getLg()) {
			lg.getPlayer().sendMessage(gameTag + "§cUn nouveau joueur à rejoint votre camp ! Faites /lg role pour plus de détails !");

			if (lg.getRole() == LGRoles.LGA && lg.getPower() == 1) {
				lg.lglist.add(j);
			}
		}

		j.setCamp(LGCamps.LoupGarou);
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

		world.getChunkAt(x, y).load();

		player.teleport(teleportLocation);
	}

	//RANDOMTP
	public void TpPower(Player player) {

		Random random = new Random();

		Location teleportLocation = null;

		int rangeMax = wbMax;
		int rangeMin = -wbMax;


		int x = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
		int z = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
		int y = 150;

		teleportLocation = new Location(player.getWorld(), x + Spawn.getX(), y, z + Spawn.getZ());

		player.teleport(teleportLocation);

		if (isInGame(player.getUniqueId())) {
			new UHCNoFall(getPlayer(player.getUniqueId())).runTaskTimer(this, 0, 20);
		}

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
       
