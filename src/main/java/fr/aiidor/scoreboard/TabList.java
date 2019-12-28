package fr.aiidor.scoreboard;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;

public class TabList {
	
	private LGUHC main;
	
	public TabList(LGUHC main) {
		this.main = main;
	}
	
	//TABLIST
	public void set(Player player) {
		String gameInfo = "";
		
		if (main.PlayerHasRole) {
			if (main.hasRole(player)) {
				Joueur j = main.getPlayer(player.getUniqueId());
				
				gameInfo = "\n§b" + j.getRole().name + " §f--- §7" + j.getKills() + " kills";
			}
		}
		
		String command = "§6/lg pour voir les commandes";
		
		if (main.Spectator.contains(player.getUniqueId())) {
			command = "§6/spec pour voir les commandes";
		}
		
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		Object header = new ChatComponentText(main.TabName);
		Object footer = new ChatComponentText("\n§l§fLGUHC" + gameInfo + "\n" + command + "\n\n§bPlugin original par Lapin\n§dDéveloppé par B_Goodes");
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
		
	}
}
