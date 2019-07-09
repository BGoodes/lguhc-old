package fr.aiidor.roles.use;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.LGUHC;
import fr.aiidor.roles.LGCamps;
import fr.aiidor.roles.LGRoleManager;
import fr.aiidor.roles.LGRoles;
import fr.aiidor.task.LGA;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class LG {
	
	//EFFET DE KILL
	public static void LgKill(Player p) {
		
		if (LGRoles.getRole(p.getUniqueId()) == LGRoles.VilainPL) {
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0));
			return;
		}
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 0));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0, false, false), true);
	}
	
	
	//EFFET DE NUIT
	public static void NightEffect(Player p) {
		
		if (LGCamps.getCamp(p.getUniqueId()) != LGCamps.LOUPGAROUS) {return;}
		//if (LGRoleManager.Infect.contains(p.getUniqueId()) && !LGRoles.isLG(p.getUniqueId())) {return;} //INFECT
		if (LGRoles.getRole(p.getUniqueId()) == LGRoles.LGA && !LGRoleManager.Power.containsKey(p.getUniqueId())) { return;}
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), true);
		
		if (LGRoles.getRole(p.getUniqueId()) == LGRoles.VilainPL) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), true);
			return;
		}
		
	}
	
	//INFECT PERE DES LOUPS
	
	public static void InfectMsg(UUID uuid, Player damaged) {
		
		LGRoleManager.mortD.put(damaged.getUniqueId(), LGCamps.LOUPGAROUS);
		
		TextComponent msg = new TextComponent("§b§l[§6§lLOUP-GAROUS§b§l]§c Le joueur §f" + damaged.getName() + " §c est Mort ! Vous avez §66 secondes §cpour le réanimer en cliquant "
				+ "sur le message ou en faisant /lg Infect <Pseudo>");
		
		msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cRéanimer ?").create()));
		msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lg infect " + damaged.getName()));
		Bukkit.getPlayer(uuid).spigot().sendMessage(msg);
		Bukkit.getPlayer(uuid).sendMessage("");
	}
	
	public static void Infect(UUID uuid) {
		
		LGRoleManager.Infect.add(uuid);
		LGRoleManager.Rea.add(uuid);
		
		Bukkit.getPlayer(uuid).sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§a L'Infect Père des Loups à décidé de vous ressusciter !");
		Bukkit.getPlayer(uuid).sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§4 Vous faites désormais partie du camp des Loups-Garous mais gardez vos mêmes pouvoir !");
		
		for (String name : LGRoleManager.lg) {
			Player lg = Bukkit.getPlayer(name);
			lg.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§c Un nouveau Loup à rejoint votre camp ! Faite /lg role pour plus de détail !");  //NOUVEAU JOUEUR
		}
		
		for (UUID uuid2 : LGUHC.getInstance().PlayerInGame) {
			
			if (LGRoles.getRole(uuid2) == LGRoles.LGA) {
				if (LGRoleManager.Power.containsKey(uuid2)) {
					LGA.lgM.add(Bukkit.getPlayer(uuid).getName());
				}
			}
		}
	
		LGRoleManager.lg.add(Bukkit.getPlayer(uuid).getName());
		
		
		LGCamps.SetCamp(uuid, LGCamps.LOUPGAROUS);
		
	}
	
	
	
	//LG COMPO
	public static void LGCompo(Player p) {
		
		if (LGCamps.getCamp(p.getUniqueId()) != LGCamps.LOUPGAROUS) {return;}
		
		//LOUP-GAROUS AMNESIQUE ---------------------------
		if (LGRoles.getRole(p.getUniqueId()) == LGRoles.LGA && !LGRoleManager.Power.containsKey(p.getUniqueId())) {return;} //LGA
		if (LGRoles.getRole(p.getUniqueId()) == LGRoles.LGA && LGRoleManager.Power.containsKey(p.getUniqueId())) {
			
			StringBuilder compo = new StringBuilder();
			compo.append(" §c");
			
			for (String lg : LGRoleManager.lg) {
				
				if (LGA.lgM.contains(lg)) {
					compo.append(lg + "   ");
				}
				else {
					compo.append("?????" + "   ");
				}
			}
			
			p.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§4 Les pseudos des Loups-Garous sont : ");
			p.sendMessage(compo.toString());
			return;
		}
		
		//LG --------------------------------
		StringBuilder compo = new StringBuilder();
		compo.append(" §c");
		p.sendMessage("§b§l[§6§lLOUP-GAROUS§b§l]§4 Les pseudos des Loups-Garous sont : ");
		
		for (String lg : LGRoleManager.lg) {
			
			compo.append(lg + "   ");
		}
		
		p.sendMessage(compo.toString());
	}
}
