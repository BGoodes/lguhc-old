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

public class LGRole_IPL {
	
	private LGUHC main;
	
	public LGRole_IPL(LGUHC main) {
		this.main = main;
	}
	
	
	public void canRea(String targetname, Joueur Infect) {
		
	Player p = Infect.getPlayer();
	
	if (Infect.getRole() != LGRoles.IPL) {
		p.sendMessage(main.gameTag + "�cErreur, vous devez �tre �oInfect P�re des Loups �cpour effectuer cette commande !");
		return;
	}
	
	if (Infect.getPower() < 1 ) {
		p.sendMessage(main.gameTag + "�cVous avez d�j� utilis� votre pouvoir !");
		return;
	}
	
	if (Bukkit.getPlayer(targetname) == null) {
		p.sendMessage(main.gameTag + "�cErreur, le joueur "+ targetname + " n'est pas connect� ou n'existe pas !");
		return;
	}
	
	Player Target = Bukkit.getPlayer(targetname);
	
	if (main.getPlayer(Target.getUniqueId()) == null) {
		p.sendMessage(main.gameTag + "�cErreur, le joueur vis� doit �tre dans la partie !");
		return;
	}
	
	Joueur TargetJ = main.getPlayer(Target.getUniqueId());
	
	if (TargetJ.isDead()) {
		p.sendMessage(main.gameTag + "�cErreur, le joueur vis� doit �tre en vie !");
		return;
	}
	
	if (TargetJ.isRea()) {
		p.sendMessage(main.gameTag + "�cLe joueur �e" + targetname + " �cest d�j� r�anim� !");
		return;
	}
	
	if (TargetJ.isDying() && TargetJ.isDyingState(LGCamps.LoupGarou)) {
		TargetJ.setRea(true);
		
		p.sendMessage(main.gameTag + "�aVous avez bien sauv� " + targetname + " !");
		Infect.setPower(0);
		return;
	} else  {
		p.sendMessage(main.gameTag + "�cLe joueur �e" + targetname + "�c ne peut pas ou ne peut plus �tre r�anim� !");
		return;
	}
	
	}
	
	public void ReaMsg(Player damaged) {
		
		for (Joueur j : main.getPlayerRoles(LGRoles.IPL)) {
			if (j.getPower() > 0 ) {
				
				TextComponent msg = new TextComponent(main.gameTag + "�6Le joueur �f" + damaged.getName() + " �6 est Mort ! Vous avez 6 secondes pour l'infecter en cliquant "
						+ "sur le message ou en faisant /lg infect <Pseudo>");
				
				msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("�cInfecter ?").create()));
				msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg infect " + damaged.getName()));
				j.getPlayer().spigot().sendMessage(msg);
				j.getPlayer().sendMessage("");
			}
		}

	}
	

}