package com.sk.warpgate;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk.warpgate.command.SKCommand;
import com.sk.warpgate.event.SKInteractListener;

public class SKMain extends JavaPlugin {
	
	private HashMap<String, SKWarpgate> warpgates;
	
	@Override
	public void onEnable() {
		
		warpgates = SKSave.loadWarpgates(getConfig());
		
		registerCommands();
		registerEvents();
		registerPositionChecker();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private void registerCommands() {
		getCommand("skwarpgate").setExecutor(new SKCommand(this));
	}
	
	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new SKInteractListener(this), this);
	}
	
	private void registerPositionChecker() {
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new SKPositionChecker(this), 20L, 20L);
	}
	
	public void addWarpgate(Location loc, UUID ownerUUID, String name) {
		warpgates.put(name, new SKWarpgate(loc, ownerUUID, name));
	}
	
	public HashMap<String, SKWarpgate> getWarpgates() {
		return warpgates;
	}
}