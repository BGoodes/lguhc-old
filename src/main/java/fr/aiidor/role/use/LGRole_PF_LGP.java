package fr.aiidor.role.use;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.LGUHC;
import fr.aiidor.game.Joueur;
import fr.aiidor.role.LGRoles;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class LGRole_PF_LGP extends BukkitRunnable {

	private LGUHC main;
	
	public LGRole_PF_LGP(LGUHC main) {
		this.main = main;
	}
	
	@Override
	public void run() {
		if (main.getPlayerRolesOff(LGRoles.PetiteFille).size() == 0 && main.getPlayerRolesOff(LGRoles.LGP).size() == 0) {
			cancel();
			return;
		}
		
		for (Joueur j : produceParticle()) {
			if (j.isConnected()) {
				if (j.getPlayer().hasPotionEffect(PotionEffectType.INVISIBILITY)) {
					
					for (Joueur watch : produceParticle()) {
						if (watch.isConnected()) {
							
							if (watch.getPlayer().hasPotionEffect(PotionEffectType.INVISIBILITY)) {
								
								float x = (float) j.getPlayer().getLocation().getX();
								float y = (float) j.getPlayer().getLocation().getY() + 0.15F;
								float z = (float) j.getPlayer().getLocation().getZ();
								
								int red = new Random().nextInt(255);
								int green = new Random().nextInt(255);
								int blue = new Random().nextInt(255);
								
								PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, x, y, z, red, green, blue, (float)10, 0, 40);;
		                        ((CraftPlayer) watch.getPlayer()).getHandle().playerConnection.sendPacket(particles);
							}
						}
					}
				}
			}
		}
	}
	
	private List<Joueur> produceParticle() {
		List<Joueur> list = new ArrayList<>();
		
		if (main.getPlayerRolesOff(LGRoles.PetiteFille).size() != 0) {
			list.addAll(main.getPlayerRolesOff(LGRoles.PetiteFille));
		}
		
		if (main.getPlayerRolesOff(LGRoles.LGP).size() != 0) {
			list.addAll(main.getPlayerRolesOff(LGRoles.LGP));
		}
		
		return list;
	}
}
