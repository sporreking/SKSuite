package com.sk.warpgate;

import java.util.UUID;

import org.bukkit.Location;

public class SKWarpgate {
	
	private Location location;
	private String name;
	private UUID uuid;
	private SKWarpgate boundWarpgate;
	
	public SKWarpgate(Location location, UUID ownerUUID, String name) {
		this.location = location;
		this.name = name;
		this.uuid = ownerUUID;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public String getName() {
		return name;
	}
	
	public UUID getOwnerUUID() {
		return uuid;
	}
	
	public SKWarpgate getBoundWarpgate() {
		return boundWarpgate;
	}
	
	public boolean isBound() {
		return boundWarpgate != null;
	}
}