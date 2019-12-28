package fr.aiidor.scoreboard;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NameManager {
	
	private Player player;
	
	public NameManager(Player player) {
		this.player = player;
	}
	
	public void changeName(String name) {
		try {
			Method getHandle = player.getClass().getMethod("getHandle",(Class<?>[]) null);
		       	try {
		       		Class.forName("com.mojang.authlib.GameProfile");
		         
		       	} catch (ClassNotFoundException e) {
		    	   
		         return;
		       	}
		       	
		         Object profile = getHandle.invoke(player).getClass()
		             .getMethod("getProfile")
		             .invoke(getHandle.invoke(player));
		         
		         Field ff = profile.getClass().getDeclaredField("name");
		         ff.setAccessible(true);
		         
		         ff.set(profile, name);
		         
		         for (Player players : Bukkit.getOnlinePlayers()) {
		        	 
		        	 players.hidePlayer(player);
		        	 players.showPlayer(player);
		       }
		         	
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchFieldException e) {
			
					e.printStackTrace();
		}
	}
	
	public void resetName() {
		try {
			Method getHandle = player.getClass().getMethod("getHandle",(Class<?>[]) null);
		       	try {
		       		Class.forName("com.mojang.authlib.GameProfile");
		         
		       	} catch (ClassNotFoundException e) {
		    	   
		         return;
		       	}
		       	
		         Object profile = getHandle.invoke(player).getClass()
		             .getMethod("getProfile")
		             .invoke(getHandle.invoke(player));
		         
		         Field ff = profile.getClass().getDeclaredField("name");
		         ff.setAccessible(true);
		         
		         
		         ff.set(profile, player.getName());
		         
		         for (Player players : Bukkit.getOnlinePlayers()) {
		        	 
		        	 players.hidePlayer(player);
		        	 players.showPlayer(player);
		       }
		         	
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchFieldException e) {
			
					e.printStackTrace();
		}
	}
}
