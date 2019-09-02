package fr.aiidor.role.use;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import fr.aiidor.LGUHC;

public class LGRole_Ancien {
	private LGUHC main;
	private UUID Ancien;

	public LGRole_Ancien(LGUHC main, UUID Ancien) {
		this.main = main;
		this.Ancien = Ancien;
	}

	public void Revive() {

		Player player = Bukkit.getPlayer(Ancien);

		player.sendMessage(main.gameTag + "§bVotre pouvoir vous à sauvé !");

		main.TpPower(Bukkit.getPlayer(Ancien));

		for(PotionEffect effect:player.getActivePotionEffects()){player.removePotionEffect(effect.getType());}
		player.setGameMode(GameMode.SURVIVAL);

		player.setHealth(player.getMaxHealth());
	}
}
