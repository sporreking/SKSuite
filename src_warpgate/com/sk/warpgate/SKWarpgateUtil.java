package com.sk.warpgate;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;

import net.minecraft.server.v1_8_R3.Blocks;

public class SKWarpgateUtil {
	
	public static final boolean validStructure(Location loc) {
		
		if(!checkDiamond(loc))
			return false;
		
		if(!checkGold(loc))
			return false;
		
		if(!checkStone(loc))
			return false;
		
		if(!checkLapis(loc))
			return false;
		
		if(!checkMaterial(loc))
			return false;
		
		if(!checkCrystal(loc))
			return false;
		
		return true;
	}
	
	private static final boolean checkCrystal(Location loc) {
		
		if(!(isBlockAt(loc, 0, 6, 0, Material.STAINED_GLASS)
				&& isBlockAt(loc, 0, 7, 0, Material.STAINED_GLASS)
				&& isBlockAt(loc, 1, 8, 0, Material.STAINED_GLASS)
				&& isBlockAt(loc, 1, 9, 0, Material.STAINED_GLASS)
				&& isBlockAt(loc, 1, 10, 0, Material.STAINED_GLASS)
				&& isBlockAt(loc, -1, 8, 0, Material.STAINED_GLASS)
				&& isBlockAt(loc, -1, 9, 0, Material.STAINED_GLASS)
				&& isBlockAt(loc, -1, 10, 0, Material.STAINED_GLASS)
				&& isBlockAt(loc, 0, 8, 1, Material.STAINED_GLASS)
				&& isBlockAt(loc, 0, 9, 1, Material.STAINED_GLASS)
				&& isBlockAt(loc, 0, 10, 1, Material.STAINED_GLASS)
				&& isBlockAt(loc, 0, 8, -1, Material.STAINED_GLASS)
				&& isBlockAt(loc, 0, 9, -1, Material.STAINED_GLASS)
				&& isBlockAt(loc, 0, 10, -1, Material.STAINED_GLASS)
				&& isBlockAt(loc, 0, 11, 0, Material.STAINED_GLASS)
				&& isBlockAt(loc, 0, 12, 0, Material.STAINED_GLASS)))
			return false;
		
		if(!(isBlockAt(loc, 0, 8, 0, Material.SEA_LANTERN)
				&& isBlockAt(loc, 0, 9, 0, Material.DIAMOND_BLOCK)
				&& isBlockAt(loc, 0, 10, 0, Material.SEA_LANTERN)))
			return false;
		return true;
	}
	
	private static final boolean checkMaterial(Location loc) {
		Material mat = loc.getWorld().getBlockAt(loc.getBlockX() + 1,
				loc.getBlockY(), loc.getBlockZ() + 3).getType();
		
		//Checking material
		if(mat != Material.NETHER_BRICK && mat != Material.QUARTZ_BLOCK
				&& mat != Material.SMOOTH_BRICK && mat != Material.LOG)
			return false;
		
		//Checking floor
		if(!(isBlockAt(loc, 1, 0, 3, mat, 0)
				&& isBlockAt(loc, 3, 0, 1, mat)
				&& isBlockAt(loc, -1, 0, 3, mat)
				&& isBlockAt(loc, -3, 0, 1, mat)
				&& isBlockAt(loc, 1, 0, -3, mat)
				&& isBlockAt(loc, 3, 0, -1, mat)
				&& isBlockAt(loc, -1, 0, -3, mat)
				&& isBlockAt(loc, -3, 0, -1, mat, 0)))
			return false;
		
		if(!(checkPillar(loc, mat, 2, 3, 5)
				&& checkPillar(loc, mat, 3, 2, 5)
				&& checkPillar(loc, mat, 2, -3, 5)
				&& checkPillar(loc, mat, 3, -2, 5)
				&& checkPillar(loc, mat, -2, 3, 5)
				&& checkPillar(loc, mat, -3, 2, 5)
				&& checkPillar(loc, mat, -2, -3, 5)
				&& checkPillar(loc, mat, -3, -2, 5)))
			return false;
		
		if(!(checkOrnamentalPillar(loc, 2, 2, mat)
				&& checkOrnamentalPillar(loc, 2, -2, mat)
				&& checkOrnamentalPillar(loc, -2, -2, mat)
				&& checkOrnamentalPillar(loc, -2, 2, mat)))
			return false;
		
		return true;
	}
	
	private static final boolean checkOrnamentalPillar(Location loc,
			int offsetX, int offsetZ, Material mat) {
		
		return isBlockAt(loc, offsetX, 1, offsetZ, mat)
				&& isBlockAt(loc, offsetX, 2, offsetZ, Material.EMERALD_BLOCK)
				&& isBlockAt(loc, offsetX, 3, offsetZ, Material.GLOWSTONE)
				&& isBlockAt(loc, offsetX, 4, offsetZ, Material.EMERALD_BLOCK)
				&& isBlockAt(loc, offsetX, 5, offsetZ, mat);
	}
	
	private static final boolean checkPillar(Location loc, Material mat,
			int offsetX, int offsetZ, int height) {
		
		for(int i = 0; i < height; i++) {
			if(!isBlockAt(loc, offsetX, i + 1, offsetZ, mat))
				return false;
		}
		
		return true;
	}
	
	private static final boolean checkLapis(Location loc) {
		return isBlockAt(loc, 1, 0, 2, Material.LAPIS_BLOCK, 0)
				&& isBlockAt(loc, 2, 0, 1, Material.LAPIS_BLOCK, 0)
				&& isBlockAt(loc, -1, 0, 2, Material.LAPIS_BLOCK, 0)
				&& isBlockAt(loc, -2, 0, 1, Material.LAPIS_BLOCK, 0)
				&& isBlockAt(loc, 1, 0, -2, Material.LAPIS_BLOCK, 0)
				&& isBlockAt(loc, 2, 0, -1, Material.LAPIS_BLOCK, 0)
				&& isBlockAt(loc, -1, 0, -2, Material.LAPIS_BLOCK, 0)
				&& isBlockAt(loc, -2, 0, -1, Material.LAPIS_BLOCK, 0)
				&& isBlockAt(loc, 3, 0, 0, Material.LAPIS_BLOCK, 0)
				&& isBlockAt(loc, -3, 0, 0, Material.LAPIS_BLOCK, 0)
				&& isBlockAt(loc, 0, 0, 3, Material.LAPIS_BLOCK, 0)
				&& isBlockAt(loc, 0, 0, -3, Material.LAPIS_BLOCK, 0);
	}
	
	private static final boolean checkStone(Location loc) {
		
		return isBlockAt(loc, 1, 0, 1, Material.SMOOTH_BRICK, 3)
				&& isBlockAt(loc, 1, 0, -1, Material.SMOOTH_BRICK, 3)
				&& isBlockAt(loc, -1, 0, 1, Material.SMOOTH_BRICK, 3)
				&& isBlockAt(loc, -1, 0, -1, Material.SMOOTH_BRICK, 3)
				&& isBlockAt(loc, 2, 0, 0, Material.SMOOTH_BRICK, 3)
				&& isBlockAt(loc, -2, 0, 0, Material.SMOOTH_BRICK, 3)
				&& isBlockAt(loc, 0, 0, 2, Material.SMOOTH_BRICK, 3)
				&& isBlockAt(loc, 0, 0, -2, Material.SMOOTH_BRICK, 3);
	}
	
	private static final boolean checkGold(Location loc) {
		
		return isBlockAt(loc, -1, 0, 0, Material.GOLD_BLOCK, 0)
				&& isBlockAt(loc, 1, 0, 0, Material.GOLD_BLOCK, 0)
				&& isBlockAt(loc, 0, 0, -1, Material.GOLD_BLOCK, 0)
				&& isBlockAt(loc, 0, 0, 1, Material.GOLD_BLOCK, 0);
	}
	
	private static final boolean checkDiamond(Location loc) {
		return isBlockAt(loc, 0, 0, 0, Material.DIAMOND_BLOCK, 0);
	}
	
	@SuppressWarnings("deprecation")
	public static final boolean isBlockAt(Location loc, int offsetX,
			int offsetY, int offsetZ, Material material) {
		
		Block block = loc.getWorld().getBlockAt(loc.getBlockX() + offsetX,
				loc.getBlockY() + offsetY, loc.getBlockZ() + offsetZ);
		
		return block.getType() == material;
	}
	
	@SuppressWarnings("deprecation")
	public static final boolean isBlockAt(Location loc, int offsetX,
			int offsetY, int offsetZ, Material material, int md) {
		
		Block block = loc.getWorld().getBlockAt(loc.getBlockX() + offsetX,
				loc.getBlockY() + offsetY, loc.getBlockZ() + offsetZ);
		
		return block.getType() == material && block.getData() == (byte) md;
	}
}