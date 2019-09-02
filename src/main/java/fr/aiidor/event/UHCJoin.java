package fr.aiidor.event;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.LGUHC;
import fr.aiidor.role.LGRoles;
import fr.aiidor.scoreboard.ScoreboardSign;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;

public class UHCJoin implements Listener{

	private LGUHC main;

	public UHCJoin(LGUHC pl) {
		this.main = pl;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();

		for(PotionEffect effect:player.getActivePotionEffects()){player.removePotionEffect(effect.getType());}

		player.setCompassTarget(main.Spawn);

		if (main.host == null) {

			main.setHost(player.getUniqueId());
			player.sendMessage(main.gameTag + "§dVous êtes le Host de la partie !");
			player.setPlayerListName("§7[§cHOST§7] §f" + player.getName());

			for (int x = main.Spawn.getBlockX()-30; x <= main.Spawn.getBlockX() + 30; x++) {
				for (int z = main.Spawn.getBlockZ()-30; z <= main.Spawn.getBlockX() + 30; z++) {

					main.world.getBlockAt(x, main.Spawn.getBlockY() -1 , z).setType(Material.BARRIER);
				}
			}

			Location l1 = new Location(main.world, main.Spawn.getX() - 30, main.Spawn.getY() + 3, main.Spawn.getZ() + 30);
			Location l2 = new Location(main.world, main.Spawn.getX() + 30, main.Spawn.getY(), main.Spawn.getZ() - 30);

			wallArrounndRegion(l1, l2);

		} else if (player.getUniqueId().equals(main.host)) {

			player.sendMessage(main.gameTag + "§dVous êtes le Host de la partie !");
			player.setPlayerListName("§7[§cHOST§7] §f" + player.getName());
		}


		if (main.orgas.contains(player.getUniqueId())) {

			player.sendMessage(main.gameTag + "§bVous êtes un organisateur de la partie !");
			player.setPlayerListName("§7[§9Orga§7] §f" + player.getName());
		}



		//TABLIST
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		Object header = new ChatComponentText("§5§k|||§6 Aiidor§bGAMES §5§k|||\n§9Serveur de la §6team Aiidor §9!");
		Object footer = new ChatComponentText("\n§l§fLGUHC\n\n§dPlugin Par B_Goodes\n§bPlugin Original Par Lapin");
		try {
			Field a = packet.getClass().getDeclaredField("a");
			Field b = packet.getClass().getDeclaredField("b");
			a.setAccessible(true);
			b.setAccessible(true);

			a.set(packet, header);
			b.set(packet, footer);

			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
		} catch (NoSuchFieldException | IllegalAccessException exception) {
			exception.printStackTrace();
		}

		//SCOREBOARD

		ScoreboardSign scoreboard = new ScoreboardSign(player, "§fLG UHC");
		scoreboard.create();
		scoreboard.setLine(0, "§c");
		scoreboard.setLine(1, "§ePartie en Attente !");

		main.boards.put(player, scoreboard);


		if (!main.canJoin()) {


			if (main.Spectator.contains(player.getUniqueId())) {
				spec(player);
				e.setJoinMessage("§8[§a+§8] " + player.getName() + "§7(§e"+ Bukkit.getOnlinePlayers().size() + " §7/§e" + Bukkit.getMaxPlayers() +"§7) §b(Spectateur)");
				return;
			}

			if (!main.PlayersHasRole()) {
				if (!main.PlayerInGame.contains(player.getUniqueId())) {
					spec(player);
					e.setJoinMessage("§8[§a+§8] " + player.getName() + "§7(§e"+ Bukkit.getOnlinePlayers().size() + " §7/§e" + Bukkit.getMaxPlayers() +"§7) §b(Spectateur)");
					return;
				}
			} else {

				if (!main.isInGame(player.getUniqueId())) {
					spec(player);
					e.setJoinMessage("§8[§a+§8] " + player.getName() + "§7(§e"+ Bukkit.getOnlinePlayers().size() + " §7/§e" + Bukkit.getMaxPlayers() +"§7) §b(Spectateur)");
					return;
				}

			}
			e.setJoinMessage("§8[§a+§8] " + player.getName() + "§7(§e"+ Bukkit.getOnlinePlayers().size() + " §7/§e" + Bukkit.getMaxPlayers() +"§7)");
			return;
		}

		player.teleport(main.Spawn.add(0, 1, 0));
		player.setOp(false);

		e.setJoinMessage("§8[§a+§8] " + player.getName() + "§7(§e"+ Bukkit.getOnlinePlayers().size() + " §7/§e" + Bukkit.getMaxPlayers() +"§7)");
		main.reset(player);

		if (player.getName().equalsIgnoreCase("xorware")) {
			player.setPlayerListName("Dimitri Koutovski");
		}

		player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, false, false));

		if (player.getUniqueId().equals(main.host) || main.orgas.contains(player.getUniqueId())) {
			player.getInventory().setItem(4, getItem(Material.ENDER_CHEST, "§d§lConfiguration"));
			player.teleport(main.Spawn.add(0, 4, 0));
		}
	}

	private void wallArrounndRegion(Location l1, Location l2) {
		int minX = Math.min(l1.getBlockX(), l2.getBlockX());
		int maxX = Math.max(l1.getBlockX(), l2.getBlockX());
		int minY = Math.min(l1.getBlockY(), l2.getBlockY());
		int maxY = Math.max(l1.getBlockY(), l2.getBlockY());
		int minZ = Math.min(l1.getBlockZ(), l2.getBlockZ());
		int maxZ = Math.max(l1.getBlockZ(), l2.getBlockZ());

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				Block b = new Location(l1.getWorld(), x, y, minZ).getBlock();
				b.setType(Material.BARRIER);
			}
		}

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				Block b = new Location(l1.getWorld(), x, y, maxZ).getBlock();
				b.setType(Material.BARRIER);
			}
		}

		for (int z = minZ; z <= maxZ; z++) {
			for (int y = minY; y <= maxY; y++) {
				Block b = new Location(l1.getWorld(), minX, y, z).getBlock();
				b.setType(Material.BARRIER);
			}
		}

		for (int z = minZ; z <= maxZ; z++) {
			for (int y = minY; y <= maxY; y++) {
				Block b = new Location(l1.getWorld(), maxX, y, z).getBlock();
				b.setType(Material.BARRIER);
			}
		}
	}

	private void spec (Player player) {
		player.teleport(main.Spawn);

		main.reset(player);

		player.setPlayerListName("§8[Spectateur] §7" + player.getName());

		if (main.PlayerHasRole) {
			if (main.isInGame(player.getUniqueId())) {
				if (main.getPlayer(player.getUniqueId()).Rob) player.setPlayerListName("§8[MORT] §7" + player.getName() + " §8(" + LGRoles.Voleur.name + ")");
				else player.setPlayerListName("§8[MORT] §7" + player.getName() + " §8(" + main.getPlayer(player.getUniqueId()).getRole().name + ")");

				player.sendMessage(main.gameTag + "§cVous êtes mort ! Vous serez donc un spectateur !");
			}
		}

		if (!main.PlayerHasRole || !main.isInGame(player.getUniqueId())) {
			player.sendMessage(main.gameTag + "§cLa partie à déjà commencé ! Vous serez donc un spectateur !");
		}


		player.sendMessage("§6===============§4§k0§6===============");
		player.sendMessage("§bVous êtes spectateur : §9faites /spec §bpour accéder aux commandes des spectateurs !");
		player.sendMessage("§6===============§4§k0§6===============");
		player.sendMessage(" ");
		player.sendMessage("§cVous n'avez pas le droit de donner d'informations aux joueurs dans la partie !");

		if (!main.Spectator.contains(player.getUniqueId())) main.Spectator.add(player.getUniqueId());

		player.setOp(false);

		Bukkit.getScheduler().runTaskLater(main, new Runnable() {

			@Override
			public void run() {
				player.setGameMode(GameMode.SPECTATOR);
			}
		}, 5);
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {

		int size = Bukkit.getOnlinePlayers().size() - 1;
		e.setQuitMessage("§8[§c-§8] " + e.getPlayer().getName() + "§7(§e"+ size + " §7/§e" + Bukkit.getMaxPlayers() +"§7)");
	}



	private ItemStack getItem(Material mat, String Name) {

		ItemStack it = new ItemStack(mat, 1);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName(Name);

		it.setItemMeta(itM);
		return it;
	}
}
