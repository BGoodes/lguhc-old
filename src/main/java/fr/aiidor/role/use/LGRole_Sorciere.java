package fr.aiidor.role.use;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class LGRole_Sorciere {

	private LGUHC main;

	public LGRole_Sorciere(LGUHC main) {
		this.main = main;
	}


	public void canRea(String targetname, Joueur sorcière) {

		Player p = sorcière.getPlayer();

		if (sorcière.getRole() != LGRoles.Sorciere) {
			p.sendMessage(main.gameTag + "§cErreur, vous devez être §oSorcière §cpour effectuer cette commande !");
			return;
		}

		if (sorcière.getPower() < 1 ) {
			p.sendMessage(main.gameTag + "§cVous avez déjà utilisé votre pouvoir !");
			return;
		}

		if (Bukkit.getPlayer(targetname) == null) {
			p.sendMessage(main.gameTag + "§cErreur, le joueur "+ targetname + " n'est pas connecté ou n'existe pas !");
			return;
		}

		Player Target = Bukkit.getPlayer(targetname);

		if (main.getPlayer(Target.getUniqueId()) == null) {
			p.sendMessage(main.gameTag + "§cErreur, le joueur visé doit être dans la partie !");
			return;
		}

		Joueur TargetJ = main.getPlayer(Target.getUniqueId());

		if (TargetJ.isDead()) {
			p.sendMessage(main.gameTag + "§cErreur, le joueur visé doit être en vie !");
			return;
		}

		if (TargetJ.isRea()) {
			p.sendMessage(main.gameTag + "§cLe joueur §e" + targetname + " §cest déjà réanimé !");
			return;
		}
		if (TargetJ.isDying() && TargetJ.isDyingState(LGCamps.Village)) {
			TargetJ.setRea(true);

			p.sendMessage(main.gameTag + "§aVous avez bien sauvé §f" + targetname + " !");
			sorcière.setPower(0);
			return;
		} else  {
			p.sendMessage(main.gameTag + "§cLe joueur §e" + targetname + "§c ne peut pas être réanimé !");
			return;
		}

	}

	public void ReaMsg(Player damaged) {

		for (Joueur j : main.getPlayerRoles(LGRoles.Sorciere)) {
			if (j.getPower() > 0 ) {
				TextComponent msg = new TextComponent(main.gameTag + "§6Le joueur §f" + damaged.getName() + " §6 est Mort ! Vous avez 6 secondes pour le réanimer en cliquant "
						+ "sur le message ou en faisant /lg revive <Pseudo>");

				msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cRéanimer ?").create()));
				msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg revive " + damaged.getName()));
				j.getPlayer().spigot().sendMessage(msg);
				j.getPlayer().sendMessage("");
			}
		}
	}

}
