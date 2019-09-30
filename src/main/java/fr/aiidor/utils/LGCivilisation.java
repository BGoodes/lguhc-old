package fr.aiidor.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;

public class LGCivilisation {
	
	private List<UUID> Players = new ArrayList<>(); 
	private String name;
	private Location spawn;
	
	public LGCivilisation(String name, Location loc) {
		this.name = name;
		this.spawn = loc;
		
	}
	
	public String getName() {
		return name;
	}
	
	public Location getSpawn() {
		return spawn;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addPlayer(UUID uuid) {
		Players.add(uuid);
	}
	
	public List<UUID> getPlayers() {
		return Players;
	}
	
	public int getSize() {
		return Players.size();
	}
	
	public boolean hasPlayer(UUID uuid) {
		return Players.contains(uuid);
	}
}
