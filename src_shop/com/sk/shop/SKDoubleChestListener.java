package com.sk.shop;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class SKDoubleChestListener implements Listener {
	
	private SKShopMain main;
	
	public SKDoubleChestListener(SKShopMain main) {
		this.main = main;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		
		if(!(e.getBlock().getState() instanceof Chest))
			return;
		
		Chest chest = getAdjacentChest(e.getBlock().getLocation());
		
		if(chest == null)
			return;
		
		if(!chest.hasMetadata("skshop-id"))
			return;
		
		main.getShop(chest.getMetadata("skshop-id").get(0).asInt()).setSecondChest((Chest) e.getBlock().getState());
	}
	
	private Chest getAdjacentChest(Location loc) {
		Chest chest = null;
		
		if(loc.getWorld().getBlockAt(loc.getBlockX() + 1, loc.getBlockY(), loc.getBlockZ()).getState() instanceof Chest)
			chest = (Chest) loc.getWorld().getBlockAt(loc.getBlockX() + 1, loc.getBlockY(), loc.getBlockZ()).getState();
		if(loc.getWorld().getBlockAt(loc.getBlockX() - 1, loc.getBlockY(), loc.getBlockZ()).getState() instanceof Chest)
			chest = (Chest) loc.getWorld().getBlockAt(loc.getBlockX() - 1, loc.getBlockY(), loc.getBlockZ()).getState();
		if(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() + 1).getState() instanceof Chest)
			chest = (Chest) loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() + 1).getState();
		if(loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() - 1).getState() instanceof Chest)
			chest = (Chest) loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() - 1).getState();
		
		return chest;
	}
}