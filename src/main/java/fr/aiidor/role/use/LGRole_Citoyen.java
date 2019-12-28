package fr.aiidor.role.use;

import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGRoles;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class LGRole_Citoyen {
	
	private LGUHC main;
	public LGRole_Citoyen(LGUHC main) {
		this.main = main;
	}
	
	public void canSee(Joueur j) {
		
		Player p = j.getPlayer();
		
		if (j.getRole() != LGRoles.Citoyen) {
			p.sendMessage(main.gameTag + "§cErreur, vous devez être §oCitoyen §cpour effectuer cette commande !");
			return;
		}
		
		if (j.noPower) {
			p.sendMessage(main.gameTag + "§cErreur, vous avez perdu vos pouvoirs !");
		}
		
		if (j.getPower() < 1 ) {
			p.sendMessage(main.gameTag + "§cVous avez déjà utilisé 2 fois votre pouvoir !");
			return;
		}
		
		if (!main.canSeeVote) {
			p.sendMessage(main.gameTag + "§cVous ne pouvez pas encore utiliser votre pouvoir !");
			return;
		}
		
		j.setPower(j.getPower() - 1);
		
		p.sendMessage(main.gameTag + "§bLes résultats du vote : ");
		for (Joueur all : main.Players) {
			
			if (!all.isDead()) {
				String cible = "---";
				
				if (all.whoVote != null) cible = all.whoVote.getName();
				
				p.sendMessage("§7" + all.getName() + " §c✉➔§7 " + cible);
				
				if (!all.voteCorbeau.isEmpty()) {
					if (all.voteCorbeau.size() == 2) {
						p.sendMessage("§7" + all.getName() + " §c✉➔§7 " + all.voteCorbeau.get(0).getName());
						p.sendMessage("§7" + all.getName() + " §c✉➔§7 " + all.voteCorbeau.get(1).getName());
					}
				}
			}
		}
		
		if (j.canCancelVote) {
			TextComponent msg = new TextComponent("§c[ANNULER LA SENTENCE]");
			
			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cAnnuler ?").create()));
			msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg cancelVote"));
			j.getPlayer().spigot().sendMessage(msg);
			j.getPlayer().sendMessage(" ");
		}

		return;
	}
	
	public void cancelVote(Joueur j) {
		Player p = j.getPlayer();
		
		if (j.getRole() != LGRoles.Citoyen) {
			p.sendMessage(main.gameTag + "§cErreur, vous devez être §oCitoyen §cpour effectuer cette commande !");
			return;
		}
		
		if (j.canCancelVote) {
			p.sendMessage(main.gameTag + "§cVous avez déjà utilisé ce pouvoir !");
			return;
		}
		
		if (!main.canSeeVote) {
			p.sendMessage(main.gameTag + "§cVous ne pouvez pas ou ne pouvez plus utiliser votre pouvoir !");
			return;
		}
		
		if (main.cancelVote) {
			p.sendMessage(main.gameTag + "§cLe vote à déjà été annulé !");
			return;
		}
		
		p.sendMessage(main.gameTag + "§aLe vote à bien été annulé !");
		main.cancelVote = true;
		j.canCancelVote = false;
		return;
	}
}
