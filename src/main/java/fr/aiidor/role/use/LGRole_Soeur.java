package fr.aiidor.role.use;

import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGRoles;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class LGRole_Soeur {
	
	private LGUHC main;
	public LGRole_Soeur(LGUHC main) {
		this.main = main;
	}
	
	public void canSee(Joueur j, boolean name) {
		
		Player p = j.getPlayer();
		
		if (j.getRole() != LGRoles.Soeur) {
			p.sendMessage(main.gameTag + "§cErreur, vous devez être §oSoeur §cpour effectuer cette commande !");
			return;
		}
		
		if (j.Soeur == null) {
			p.sendMessage(main.gameTag + "§cErreur, vous n'avez pas de soeur !");
			return;
		}
		
		if (!j.Soeur.isDead()) {
			p.sendMessage(main.gameTag + "§cErreur, votre Soeur n'est pas morte !");
			return;
		}
		
		if (j.Soeur.Killer == null) {
			p.sendMessage(main.gameTag + "§cVotre Soeur n'est pas morte de la main d'un joueur !");
			return;
		}
		
		if (j.getPower() == 0) {
			p.sendMessage(main.gameTag + "§cVous avez déjà utilisé votre pouvoir ! Ou ne pouvez pas encore l'utiliser !");
			return;
		}
		
		j.setPower(0);
		
		if (name) {
			p.sendMessage(main.gameTag + "§4Celui qui à tué votre Soeur se nomme §l" + j.Soeur.Killer.getName());
			p.sendMessage(" ");
			
		} else {
			p.sendMessage(main.gameTag + "§4Celui qui à tué votre Soeur est §l" + j.Soeur.Killer.getRole().name);
			p.sendMessage(" ");
		}
		
	}
	
	public void canSeeMsg(Joueur j) {
		j.setPower(1);
		
		j.getPlayer().sendMessage(main.gameTag + "§cVotre Soeur est morte ! Mais vous avez la possiilité de récupérer une information sur le tueur !");
		
		TextComponent msg = new TextComponent("§6§l[VOIR NOM]");
		
		msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cRegarder nom ?").create()));
		msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg SeeKiller name"));
		j.getPlayer().spigot().sendMessage(msg);
		
		TextComponent msg2 = new TextComponent("§6§l[VOIR ROLE]");
		
		msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cRegarder rôle ?").create()));
		msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg SeeKiller role"));
		j.getPlayer().spigot().sendMessage(msg2);
		j.getPlayer().sendMessage("");
	}
}
