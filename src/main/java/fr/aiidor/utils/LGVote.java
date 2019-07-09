package fr.aiidor.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import fr.aiidor.LGUHC;
import fr.aiidor.effects.Sounds;
import fr.aiidor.roles.LGRoles;

public class LGVote {
	
	public static ArrayList<UUID> aVote = new ArrayList<>();
	public static boolean canVote = false;
	
	private static int voteNumber = 0;
	private static HashMap<UUID, Integer> vote = new HashMap<>();
	private static ArrayList<UUID> egalite = new ArrayList<>();
	public static Player cible;
	
	public static void AnounceVote() {
		
		for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
			
			Bukkit.getPlayer(uuid).sendMessage(" ");
			Bukkit.getPlayer(uuid).sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§6 Vous avez 1 minute pour voter en faisaint /lg vote <Pseudo> !");
			Bukkit.getPlayer(uuid).sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§6 Le joueur ayant la majorité des votes perdra la moitié de sa vie !");
		}
		
		canVote = true;
		
	}
	
	public static void VoteEffect(UUID uuid) {
		cible = Bukkit.getPlayer(uuid);
		
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage("§b§l[§6§lLOUP-GAROUS§b§l]§6 Le joueur " + cible.getName() + " §6à obtenue la majorité des vote : §b" + vote.get(uuid) + " §6vote(s) !");
		
		if (LGRoles.getRole(uuid) == LGRoles.Ange) {
			
			if (vote.get(uuid) < voteNumber) {
				
				Bukkit.broadcastMessage("§b§l[§6§lLOUP-GAROUS§b§l]§a Votre pouvoir vous permet de ne pas perdre des ceours !");
				return;
			}
		}
		
		cible.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10);
		new Sounds(LGVote.cible).PlaySound(Sound.ENTITY_PLAYER_ATTACK_CRIT);
		if (LGRoles.getRole(uuid) == LGRoles.LGB) {
			cible.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(15);
		}
		
		reset();
	}
	
	public static void AddVote(UUID uuid) {
		
		voteNumber++;
		
		if (!vote.containsKey(uuid)) {
			vote.put(uuid, 1);
			return;
		}
		
		int voteN = vote.get(uuid);
		
		vote.remove(uuid);
		vote.put(uuid, voteN + 1);
		
		return;
	}
	
	@SuppressWarnings("deprecation")
	public static void Result() {
		
		//MAX
		int max = 0;
		
		for (int i : vote.values()) {
			
			if (i > max) {
				max = i;
			}
		}
		
		//ANGE
		for (UUID uuid : LGUHC.getInstance().PlayerInGame) {
			
			if (LGRoles.getRole(uuid) == LGRoles.Ange) {
				Player ange = Bukkit.getPlayer(uuid);
				if (vote.get(uuid) >= 2) {
					int hearth = 0;
					
					if (vote.get(uuid)%2 == 0) {
						hearth = vote.get(uuid)/2;
					}
					else {
						hearth = (int) (vote.get(uuid)/2 - 0.5);
					}
					
					ange.setMaxHealth(ange.getMaxHealth() + hearth);
				}
			}
		}
		
		//LES JOUEURS QUI ONT LE MAX
		for (UUID uuid : vote.keySet()) {
			
			if (vote.get(uuid) == max) {
				egalite.add(uuid);
			}
		}
		
		//0
		if (max == 0) {
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage("§b§l[§6§lLOUP-GAROUS§b§l]§b personne n'a voté donc personne ne perd de vie !");
			reset();
			return;
		}
		
		//!EGALITE
		if (egalite.size() == 1) {
			
			VoteEffect(egalite.get(0));
			return;
		}
		
		//EGALITE
		
		if (egalite.size() > 1) {
			
			Random ran = new Random();
			int choose = ran.nextInt(egalite.size());
			
			VoteEffect(egalite.get(choose));
			return;
		}
		
		return;
		
	}
	
	public static void reset() {
		//RESET
		canVote = false;
		voteNumber = 0;
		
		aVote.clear();
		vote.clear();
		egalite.clear();
	}
}
