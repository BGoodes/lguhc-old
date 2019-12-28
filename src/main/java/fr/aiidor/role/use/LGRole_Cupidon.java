package fr.aiidor.role.use;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.LGUHC;
import fr.aiidor.effect.Sounds;
import fr.aiidor.files.StatAction;
import fr.aiidor.files.StatManager;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGRoles;
import fr.aiidor.task.LGCompass;

public class LGRole_Cupidon {
	
	private LGUHC main;
	public LGRole_Cupidon(LGUHC main) {
		this.main = main;
	}
	
	public void canChoose() {
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				cannotChoose();
			}
		}, 6000);
		
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.Cupidon) {
				if (!j.isDead()) {
					j.setPower(1);
						
					if (j.isConnected()) {
						j.getPlayer().sendMessage(main.gameTag + "§bVous avez 5 minutes pour créer un couple avec la commande §l/lg love <pseudo1> <pseudo2> §b!");
					}
				}

			}
		}
	}
	
	
	private void cannotChoose() {
		for (Joueur j : main.Players) {
			if (j.getRole() == LGRoles.Cupidon) {
				if (!j.isDead()) {
					if (j.getPower() > 0) {
						
						j.setPower(0);
						
						if (j.isConnected()) {
							j.getPlayer().sendMessage(main.gameTag + "§cVous avez attendu plus de 5 min, vous pourrez donc plus choisir de couple !");
						}
					}
				}
			}
		}
	}
	
	public void canChoose(Joueur j, String targetname, String targetname2) {
		
		Player p = j.getPlayer();
		
		if (j.getRole() != LGRoles.Cupidon) {
			p.sendMessage(main.gameTag + "§cErreur, vous devez être §oCupidon §cpour effectuer cette commande !");
			return;
		}
		
		if (j.getPower() < 1 ) {
			p.sendMessage(main.gameTag + "§cVous avez déjà choisit un couple ou avez attendu trop longtemps (5min) !");
			return;
		}
		
		if (targetname.equalsIgnoreCase(targetname2)) {
			p.sendMessage(main.gameTag + "§cErreur, vous ne pouvez pas choisir deux fois la même personne !");
			return;
		}
		
		if (Bukkit.getPlayer(targetname) == null || Bukkit.getPlayer(targetname2) == null) {
			p.sendMessage(main.gameTag + "§cErreur, le joueur l'un des joueurs n'est pas connecté ou n'existe pas !");
			return;
		}
		
		Player Target = Bukkit.getPlayer(targetname);
		Player Target2 = Bukkit.getPlayer(targetname2);
		
		if (main.getPlayer(Target.getUniqueId()) == null || main.getPlayer(Target2.getUniqueId()) == null) {
			p.sendMessage(main.gameTag + "§cErreur, les joueurs visés doit être dans la partie !");
			return;
		}
		
		Joueur TargetJ = main.getPlayer(Target.getUniqueId());
		Joueur TargetJ2 = main.getPlayer(Target2.getUniqueId());
		
		
		if (TargetJ.isDead() || TargetJ2.isDead()) {
			p.sendMessage(main.gameTag + "§cErreur, les joueurs visés doit être en vie !");
			return;
		}
		
		if (TargetJ.hasCouple() || TargetJ2.hasCouple()) {
			p.sendMessage(main.gameTag + "§cErreur, un des joueurs est déjà en couple !");
		}
		
		choose(j, TargetJ, TargetJ2);
	}
	
	public void choose(Joueur j, Joueur t, Joueur t2) {
		
		if (j != null) {
			Player p = j.getPlayer();
			
			p.sendMessage(" ");
			j.setPower(0);
			
			p.sendMessage(main.gameTag + "§aVous avez bien mis §l" + t.getName() + "§a et §l" + t2.getName() + "§a en couple ! Si l'un meurt, l'autre ne pourrais supporter "
					+ "cette souffrance et se suiciderais immédiatement !");
			p.sendMessage(main.gameTag + "§2Vous pouvez gagnez avec eux ou avec le village !");
			p.sendMessage(" ");
		}

		t.setCouple(t2);
		t2.setCouple(t);
		
		new Sounds(t.getPlayer()).PlaySound(Sound.LEVEL_UP);
		t.getPlayer().sendMessage(" ");
		t.getPlayer().sendMessage(main.gameTag + "§c♥§3 Vous êtes amoureux de §l" + t2.getName() + "§3 si il vient à mourrir, vous mourrerez aussitôt ! "
				+ "Vous pouvez cependant lui offrir de votre vie grâve a la commande /lg don <% de vie>.");
		
		giveItemStack(t.getPlayer(), getItem(Material.COMPASS, "§dBoussole des amoureux"));
		
		new Sounds(t2.getPlayer()).PlaySound(Sound.LEVEL_UP);
		t2.getPlayer().sendMessage(" ");
		t2.getPlayer().sendMessage(main.gameTag +  "§c♥§3 Vous êtes amoureux de §l" + t.getName() + "§3 si il vient à mourrir, vous mourrerez aussitôt ! "
				+ "Vous pouvez cependant lui offrir de votre vie grâve a la commande /lg don <% de vie>.");
		
		giveItemStack(t2.getPlayer(), getItem(Material.COMPASS, "§dBoussole des amoureux"));
		
		new LGCompass(main, t, t2).runTaskTimer(main, 0, 20);
		
		if (main.stats) {
			new StatManager(main).changeState(t.getUUID(), "couple", StatAction.Increase);
			new StatManager(main).changeState(t2.getUUID(), "vote", StatAction.Increase);
		}
	}
	
	private void giveItemStack(Player p, ItemStack it) {
		if (isInventoryFull(p)) p.getWorld().dropItem(p.getLocation(), it);
		else p.getInventory().addItem(it);
	}
	
	public boolean isInventoryFull(Player p) {
	    return p.getInventory().firstEmpty() == -1;
	}
	
	private ItemStack getItem(Material material, String Name) {
		
		ItemStack Item = new ItemStack(material);
		ItemMeta ItemM = Item.getItemMeta();
		
		ItemM.setDisplayName(Name);
		Item.setItemMeta(ItemM);
		
		return Item;
	}
}
