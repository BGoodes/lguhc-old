package fr.aiidor.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.role.LGRoles;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class LGVote {
	
	private LGUHC main;
	private List<Joueur> voteMax = new ArrayList<>();
	
	public LGVote(LGUHC main) {
		this.main = main;
	}
	
	public void canVote() {
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				cannotVote();
			}
		}, 1200);
		main.canVote = true;
		
		for (Joueur j : main.getJoueurs()) {
			j.canVote = true;
			
			j.getPlayer().sendMessage(main.gameTag + "§6Vous avez 1 minute pour voter en faisant /lg vote <Pseudo> !");
			j.getPlayer().sendMessage(main.gameTag + "§6Le joueur ayant la majorité des votes perdra la moitié de sa vie !");
			j.getPlayer().sendMessage(" ");
		}
	}
	
	private void cannotVote() {
		
		main.canVote = false;
		
		for (Joueur j : main.Players) {
			j.canVote = false;
		}
		
		//CITOYEN + SPEC
		main.canSeeVote = true;
		if (main.compo.contains(LGRoles.Citoyen)) {
			for (Joueur j : main.getPlayerRoles(LGRoles.Citoyen)) {
				if (j.getPower() > 0) {
					j.getPlayer().sendMessage(main.gameTag + "§3Vous avez la possiblité de consulter encore " + j.getPower() + " fois les votes des joueurs. "
							+ "Attention, vous n'avez que 15 secondes pour vous décider ! ");
					
					TextComponent msg = new TextComponent("§b§l[CONSULTER LES VOTES]");
					
					msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§bConsulter ?").create()));
					msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg seevote"));
					j.getPlayer().spigot().sendMessage(msg);
					j.getPlayer().sendMessage("");
				}
			}
		}
		
		if (main.getSpectator().size() > 0) {
			for (Player p : main.getSpectator()) {
				p.sendMessage(main.gameTag + "§bVous pouvez consulter les votes (15 secondes) "); 
				TextComponent msg = new TextComponent("§9§l[CONSULTER LES VOTES]");
				
				msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§bConsulter ?").create()));
				msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spec seevote"));
				p.spigot().sendMessage(msg);
				p.sendMessage("");
			}
		}

		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				main.canSeeVote = false;
				checkvote();
			}
		}, 300);
		
	}
	
	public void vote(Joueur j, String targetname) {
		
		Player p = j.getPlayer();
		
		if (!j.canVote || !main.canVote) {
			p.sendMessage(main.gameTag + "§cVous ne pouvez pas voter pour le moment !");
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
		
		//VOTE
		j.canVote = false;
		j.whoVote = TargetJ;
		
		if (j.getRole() == LGRoles.Corbeau)  {
			TargetJ.vote = TargetJ.vote + 3;
			p.sendMessage(main.gameTag + "§aVotre malédiction atteint §f" + targetname + "§a et il reçoit par conséquent 3 votes !");
		}
		else {
			TargetJ.vote ++;
			p.sendMessage(main.gameTag + "§aVotre vote à bien été comptabilisé !");
		}
		
		
	
	}
	
	private void checkvote() {
		int max = 0;
		
		for (Joueur j : main.getJoueurs()) {
			if (j.vote > max) {
				max = j.vote;
			}
		}
		
		for (Joueur j : main.getJoueurs()) {
			if (j.vote == max) {
				voteMax.add(j);
			}
		}
		
		//0
		if (max == 0) {
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(main.gameTag + "§bPersonne n'a voté donc personne ne perd de vie !");
			reset();
			return;
		}
		
		//!EGALITE
		if (voteMax.size() == 1) {
			
			VoteEffect(voteMax.get(0), false);
			reset();
			return;
		}
		
		//EGALITE
		
		if (voteMax.size() > 1) {
			if (main.compo.contains(LGRoles.Bouc)) {
				if (main.getPlayerRoles(LGRoles.Bouc).size() != 0) {
					for (Joueur j : main.getPlayerRoles(LGRoles.Bouc)) {
						VoteEffect(j, true);
					}
					Bukkit.broadcastMessage(main.gameTag + "§bLes villageois sont indécis, c'est donc le Bouc Emissaire qui prend les votes !");
					return;
				}
			}
			
			
			Random ran = new Random();
			int choose = ran.nextInt(voteMax.size());
			
			VoteEffect(voteMax.get(choose), false);
			
			reset();
			return;
		}
	}
	
	private void reset() {
		for (Joueur j : main.Players) {
			j.vote = 0;
			j.whoVote = null;
		}
	}
	
	private void VoteEffect(Joueur j, boolean bouc) {
		j.getPlayer().setMaxHealth(j.getPlayer().getMaxHealth() / 2);
		j.getPlayer().damage(0);
		
		j.setVoteCible(true);
		
		if (!bouc) {
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(main.gameTag + "§6Le joueur " + j.getName() + " §6à obtenue la majorité des vote : §b" + j.vote + " §6vote(s) !");
			
			if (main.getPlayerRoles(LGRoles.Ange).size() > 0) {
				for (Joueur ange : main.getPlayerRoles(LGRoles.Ange)) {
					if (ange.vote > 1) {
						
						int heart = ange.vote /2;
						ange.getPlayer().setMaxHealth(ange.getPlayer().getMaxHealth() + heart * 2);
						ange.getPlayer().sendMessage(main.gameTag + "§eVous avez été voté §6" + ange.vote + " §e fois, vous recevez donc §c" + heart + " §ecoeur(s) potentiel(s) supplémentaire(s) !");
					} else {
						
						ange.getPlayer().sendMessage(main.gameTag + "§eVous avez été voté §6" + ange.vote + " §e fois.");
					}
				}
			}
		}
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				for (Joueur j : main.getJoueurs()) {
					
					if (j.isVoteCible()) {
						j.setVoteCible(false);
						
						double angeHeart = 0;
						if (j.getRole() == LGRoles.Ange) angeHeart = j.getPlayer().getHealth() - 10;
						
						j.getPlayer().setMaxHealth(j.getPlayer().getMaxHealth() * 2 + angeHeart);
					}
				}
			}
		}, 6000);


	}
}
