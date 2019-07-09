package fr.aiidor.roles.use;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.roles.LGRoleManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class LGSorcière {
	
	public static void ReviveMsg(UUID uuid, Player damaged) {
		
		TextComponent msg = new TextComponent("§b§l[§6§lLOUP-GAROUS§b§l]§2 Le joueur §f" + damaged.getName() + " §2 est Mort ! Vous avez §66 secondes §2pour le réanimer en cliquant "
				+ "sur le message ou en faisant /lg Revive <Pseudo>");
		
		msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cRéanimer ?").create()));
		msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg revive " + damaged.getName()));
		Bukkit.getPlayer(uuid).spigot().sendMessage(msg);
		Bukkit.getPlayer(uuid).sendMessage("");
	}
	
	public static void Revive(UUID uuid) {
		
		LGRoleManager.Rea.add(uuid);
		Bukkit.getPlayer(uuid).sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§a La sorcière à décidé de vous ressusciter !");
		return;
	}
}
