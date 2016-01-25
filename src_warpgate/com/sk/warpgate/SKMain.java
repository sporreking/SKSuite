package com.sk.warpgate;

import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import com.sk.warpgate.command.SKCommandCreate;

public class SKMain extends JavaPlugin {
	
	private HashMap<String, SKWarpgate> warpgates;
	
	@Override
	public void onEnable() {
		
		warpgates = SKSave.loadWarpgates(getConfig());
		
		registerCommands();
		registerPositionChecker();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private void registerCommands() {
		getCommand("skwarpgate create").setExecutor(new SKCommandCreate(this));
	}
	
	private void registerPositionChecker() {
		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new SKPositionChecker(), 20L, 20L);
	}
}