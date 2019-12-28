package fr.aiidor.role.use;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.LGUHC;
import fr.aiidor.effect.Sounds;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGCamps;
import fr.aiidor.role.LGRoles;

public class LGRole_Ange {
	
	private LGUHC main;
	
	public LGRole_Ange(LGUHC main) {
		this.main = main;
	}
	
	public void canChoose() {
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				cannotChoose();
			}
		}, 12000);
	}
	
	
	private void cannotChoose() {
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.Ange) {
				if (!j.isDead() && j.ange == null) {
						
					if (j.isConnected()) {
						j.getPlayer().sendMessage(main.gameTag + "§cVous avez attendu plus de 5 min, vous n'aurez donc pas le choix.");
						
					}
					
					if (new Random().nextBoolean()) choose(j, LGAnge.Gardien);
					else choose(j, LGAnge.Déchu);

				}
			}
		}
	}
	
	public void choose(Joueur j, String angeType) {
		
		Player p = j.getPlayer();
		
		if (j.getRole() != LGRoles.Ange) {
			p.sendMessage(main.gameTag + "§cErreur, vous devez être §oAnge §cpour effectuer cette commande !");
			return;
		}
		
		if (j.ange != null) {
			p.sendMessage(main.gameTag + "§cErreur, vous avez déjà choisit quelle type d'ange vous êtes.");
			return;
		}
		
		if (angeType.equalsIgnoreCase("dechu") || angeType.equalsIgnoreCase("déchu") || angeType.equalsIgnoreCase("gardien")) {
			
			if (angeType.equalsIgnoreCase("dechu") || angeType.equalsIgnoreCase("déchu")) {
				choose(j, LGAnge.Déchu);
				return;
			}
			
			if (angeType.equalsIgnoreCase("gardien")) {
				choose(j, LGAnge.Gardien);
				return;
			}
		} 
		else {
			p.sendMessage(main.gameTag + "§cErreur, ce type d'ange n'existe pas.");
			return;
		}
	}
	
	private void choose(Joueur j, LGAnge angeType) {
		
		j.setCamp(LGCamps.Ange);
		
		if (main.getJoueursOff().size() > 1) {
			Joueur angeT = main.getJoueursOff().get(new Random().nextInt(main.getJoueursOff().size()));
			
			while (j.equals(angeT)) {
				angeT = main.getJoueursOff().get(new Random().nextInt(main.getJoueursOff().size()));
			}
			
			j.angeTarget = angeT;
		}
		else {
			j.angeTarget = null;
		}
		
		j.ange = angeType;
		
		if (angeType == LGAnge.Gardien) {
			j.setPower(1);
		}
		
		j.getPlayer().sendMessage(main.gameTag + "§6Vous êtes un ange " + angeType + ", faites /lg role pour plus de détail !");
		new Sounds(j.getPlayer()).PlaySound(Sound.LEVEL_UP);
	}
	
	public void heal(Joueur j) {
		Player p = j.getPlayer();
		
		if (j.getRole() != LGRoles.Ange) {
			p.sendMessage(main.gameTag + "§cErreur, vous devez être §oAnge §cpour effectuer cette commande !");
			return;
		}
		
		if (j.ange != LGAnge.Gardien) {
			p.sendMessage(main.gameTag + "§cErreur, vous devez être §oAnge Gardien §cpour effectuer cette commande !");
			return;
		}
		
		if (j.getPower() < 1) {
			p.sendMessage(main.gameTag + "§cVous avez déjà utilisé votre pouvoir !");
			return;
		}
		
		if (j.angeTarget.isDead()) {
			p.sendMessage(main.gameTag + "§cVotre protégé est mort !");
			return;
		}
		
		if (!j.angeTarget.isConnected()) {
			p.sendMessage(main.gameTag + "§cVotre protégé n'est pas connecté !");
			return;
		}
		
		j.setPower(j.getPower() - 1);
		
		j.angeTarget.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
		j.angeTarget.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1200, 0, false, false));
		
		new Sounds(j.angeTarget.getPlayer()).PlaySound(Sound.DRINK);
		j.angeTarget.getPlayer().sendMessage(main.gameTag + "§aUn Ange gardien vous à offert un effet de régénération I pendant 1 minute !");
		
		p.sendMessage(main.gameTag + "§aVotre protégé à bien reçu votre cadeau !");
		return;
	}
}
