package com.sk.shop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import net.milkbowl.vault.item.Items;

public final class SKShopUtil {
	
	public static final boolean fitsInInventory(Inventory inventory, Material material, int amount) {
		int availableSlots = 0;
		for(int i = 0; i < inventory.getContents().length; i++) {
			
			if(inventory.getItem(i) == null) {
				availableSlots += material.getMaxStackSize();
			} 
			
			if(inventory.getItem(i) != null && inventory.getItem(i).getType() == material) {
				availableSlots += material.getMaxStackSize() - inventory.getItem(i).getAmount();
			}
			
			if(availableSlots >= amount) {
				return true;
			}
		}
		
		return false;
	}
	
	public static final boolean contains(Inventory inventory, Material material, int amount) {
		int items = 0;
		
		for(int i = 0; i < inventory.getSize(); i++) {
			if(inventory.getItem(i) == null)
				continue;
			
			if(inventory.getItem(i).getType() == material
					&& !inventory.getItem(i).hasItemMeta()
					&& inventory.getItem(i).getDurability() == 0
					&& inventory.getItem(i).getEnchantments().size() == 0)
				items += inventory.getItem(i).getAmount();
			
			if(items >= amount)
				return true;
		}
		
		return false;
	}
}