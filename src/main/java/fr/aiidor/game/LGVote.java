package fr.aiidor.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.files.StatAction;
import fr.aiidor.files.StatManager;
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
			
			if (j.getRole() == LGRoles.Corbeau) {
				j.getPlayer().sendMessage(main.gameTag + "§bMais en tant que corbeau, vous pouvez utiliser votre malédiction en faisant la commande /lg vote <Pseudo1> <Pseudo2> <Pseudo3>.");
			}
			
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
				if (j.getPower() > 0 && !j.noPower) {
					j.getPlayer().sendMessage(main.gameTag + "§3Vous avez la possiblité de consulter encore " + j.getPower() + " fois les votes des joueurs. "
							+ "Attention, vous n'avez que 30 secondes pour regarder ! ");
					
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
				p.sendMessage(main.gameTag + "§bVous pouvez consulter les votes (30 secondes) "); 
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
		}, 600);
		
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
		
		TargetJ.vote ++;
		p.sendMessage(main.gameTag + "§aVotre vote à bien été comptabilisé !");
		
		if (main.stats) {
			new StatManager(main).changeState(TargetJ.getUUID(), "nombre_vote", StatAction.Increase);
		}
	}
	
	public void corbeauVote(Joueur j, String targetname1, String targetname2, String targetname3) {
		
		Player p = j.getPlayer();
		
		if (!j.canVote || !main.canVote) {
			p.sendMessage(main.gameTag + "§cVous ne pouvez pas voter pour le moment !");
			return;
		}
		
		if (j.getRole() != LGRoles.Corbeau) {
			p.sendMessage(main.gameTag + "§cErreur, vous devez être §oCorbeau §cpour voter 3 joueurs !");
			return;
		}
		
		if (Bukkit.getPlayer(targetname1) == null || Bukkit.getPlayer(targetname2) == null || Bukkit.getPlayer(targetname3) == null) {
			p.sendMessage(main.gameTag + "§cErreur, l'un des joueurs n'est pas connecté ou n'existe pas !");
			return;
		}
		
		Player Target1 = Bukkit.getPlayer(targetname1);
		Player Target2 = Bukkit.getPlayer(targetname2);
		Player Target3 = Bukkit.getPlayer(targetname3);
		
		if (!main.hasRole(Target1.getUniqueId()) || !main.hasRole(Target2.getUniqueId()) || !main.hasRole(Target3.getUniqueId())) {
			p.sendMessage(main.gameTag + "§cErreur, l'un des joueurs n'est pas dans la partie !");
			return;
		}
		
		Joueur TargetJ1 = main.getPlayer(Target1.getUniqueId());
		Joueur TargetJ2 = main.getPlayer(Target2.getUniqueId());
		Joueur TargetJ3 = main.getPlayer(Target3.getUniqueId());
		
		if (TargetJ1.isDead() || TargetJ2.isDead() || TargetJ3.isDead()) {
			p.sendMessage(main.gameTag + "§cErreur, l'un des joueurs est mort !");
			return;
		}
		
		j.canVote = false;
		
		j.whoVote = TargetJ1;
		j.voteCorbeau.add(TargetJ2);
		j.voteCorbeau.add(TargetJ3);
		
		TargetJ1.vote ++;
		TargetJ2.vote ++;
		TargetJ3.vote ++;
		
		p.sendMessage(main.gameTag + "§aVos votes ont bien été comptabilisés !");
		
		//STATS
		if (main.stats) {
			new StatManager(main).changeState(TargetJ1.getUUID(), "nombre_vote", StatAction.Increase);
			new StatManager(main).changeState(TargetJ2.getUUID(), "nombre_vote", StatAction.Increase);
			new StatManager(main).changeState(TargetJ3.getUUID(), "nombre_vote", StatAction.Increase);
		}
	}
	
	private void checkvote() {
		int max = 0;
		
		Bukkit.broadcastMessage(main.gameTag + "§6RESULTAT DU VOTE :");
		
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
					
					Bukkit.broadcastMessage(main.gameTag + "§bLes villageois sont indécis, c'est donc le Bouc Emissaire qui perd la moitié de sa vie !");
					reset();
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
			j.voteCorbeau.clear();
		}
	}
	
	private void VoteEffect(Joueur j, boolean bouc) {
		
		if (main.cancelVote) {
			Bukkit.broadcastMessage(main.gameTag + "§bLe citoyen a refusé la sanction du vote, personne ne perd de vie.");
			main.cancelVote = false;
			return;
		}
		
		if (j.isDead()) {
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(main.gameTag + "§bLe joueur §l" + j.getName() + " §bà obtenue la majorité des vote : §9" + j.vote + " §bvote(s). Mais puisqu'il est mort, les effets ne lui seront pas attribué !");
			return;
		}
		
		
		if (main.voteProtect && !bouc && main.cannotVote.contains(j)) {
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(main.gameTag + "§bLe joueur §l" + j.getName() + " §bà obtenue la majorité des vote : §9" + j.vote + " §bvote(s). Mais puisque celui ci à déjà pris les votes durant cette partie, les effets ne lui seront pas attribué ! ");
			return;
		}
		
		j.getPlayer().setMaxHealth(j.getPlayer().getMaxHealth() / 2);
		j.getPlayer().damage(0);
		
		j.setVoteCible(true);
		
		if (!bouc) {
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(main.gameTag + "§bLe joueur §l" + j.getName() + " §bà obtenue la majorité des vote : §9" + j.vote + " §bvote(s). Il perd la moitié de sa vie !");
			
			main.cannotVote.add(j);
		}
		
		int timing = 6000;
		
		if (main.epTime == 20) timing = 12000;
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				for (Joueur j : main.getJoueurs()) {
					
					if (j.isVoteCible()) {
						j.setVoteCible(false);
						
						j.getPlayer().setMaxHealth(j.getPlayer().getMaxHealth() * 2);
					}
				}
			}
		}, timing);


	}
}
