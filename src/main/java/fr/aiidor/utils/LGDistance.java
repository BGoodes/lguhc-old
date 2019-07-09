package fr.aiidor.utils;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class LGDistance {
	
	public static void getDistance(Player player) {
		
		
		double distance = Math.sqrt(Math.pow((0-player.getLocation().getBlockX()) , 2) + Math.pow((0-player.getLocation().getBlockZ()), 2));
		
		if (distance <= 300) {
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§5Distance au centre : §6✈ Entre 0 et 300 blocs "));
			return;
		}
		
		if (distance > 300 && distance <= 600) {
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§5Distance au centre : §e✈ Entre 300 et 600 blocs "));
			return;
		}
		
		if (distance > 600 && distance <= 900) {
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§5Distance au centre : §a✈ Entre 600 et 900 blocs "));
			return;
		}
		
		if (distance > 900 && distance <= 1200) {
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§5Distance au centre : §3✈ Entre 900 et 1200 blocs "));
			return;
		}
		
		if (distance > 1200 && distance <= 1500) {
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§5Distance au centre : §9✈ Entre 1200 et 1500 blocs "));
			return;
		}
		
		if (distance > 1500 && distance <= 1800) {
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§5Distance au centre : §c✈ Entre 1500 et 1800 blocs "));
			return;
		}
		if (distance > 1800) {
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§5Distance au centre : §4✈ + de 1800 blocs "));
			return;
		}
	}
}
