package com.sk.warpgate;

import org.bukkit.Location;
import org.bukkit.Material;

public class SKWarpgateUtil {
	
	public static final boolean validStructure(Location loc) {
		
		if(!checkDiamond(loc))
			return false;
		
		if(!checkGold(loc))
			return false;
		
		return true;
	}
	
	private static final boolean checkGold(Location loc) {
		
		return !isBlockAt(loc, -1, -1, 0, Material.GOLD_BLOCK)
				&& !isBlockAt(loc, 1, -1, 0, Material.GOLD_BLOCK)
				&& !isBlockAt(loc, 0, -1, -1, Material.GOLD_BLOCK)
				&& !isBlockAt(loc, 0, -1, 1, Material.GOLD_BLOCK);
	}
	
	private static final boolean checkDiamond(Location loc) {
		return isBlockAt(loc, 0, -1, 0, Material.DIAMOND_BLOCK);
	}
	
	public static final boolean isBlockAt(Location loc, int offsetX,
			int offsetY, int offsetZ, Material mat) {
		
		return loc.getWorld().getBlockAt(loc.getBlockX() + offsetX,
				loc.getBlockY() + offsetY, loc.getBlockZ() + offsetZ).getType() == mat;
	}
}