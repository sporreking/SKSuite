package com.sk.shop;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.milkbowl.vault.item.Items;

public class SKShopEntry {
	
	public static final int ENTRY_NONE = 0;
	public static final int ENTRY_BUY = 1;
	public static final int ENTRY_SELL = 2;
	
	private SKShop shop;
	
	public final int ENTRY;
	
	public final String ITEM;
	public final int AMOUNT;
	public final double PRICE;
	public final int OFFER_LIMIT;
	
	private int processedOffers;
	
	public SKShopEntry(SKShop shop, int entry, String item, int amount, double price, int offerLimit) {
		this.shop = shop;
		this.ENTRY = entry;
		this.ITEM = item;
		this.AMOUNT = amount;
		this.PRICE = price;
		this.OFFER_LIMIT = offerLimit;
		
		processedOffers = 0;
	}
	
	public void process(Player player) {
		switch(ENTRY) {
		case ENTRY_BUY:
			
			if(SKShopUtil.contains(player.getInventory(), Material.matchMaterial(ITEM), AMOUNT)) {
				
				if(processedOffers >= OFFER_LIMIT && OFFER_LIMIT > 0) {
					player.sendMessage(ChatColor.RED + "This offer has expired!");
					return;
				}
				
				if(shop.getEconomy().getBalance(shop.getOfflineOwner()) < PRICE) {
					player.sendMessage(ChatColor.RED + "Shop owner does not have enough money to sell this");
					return;
				}
				
				Inventory chestInv = shop.getMainChest().getInventory();
				
				if(!SKShopUtil.fitsInInventory(chestInv, Material.matchMaterial(ITEM), AMOUNT)) {
					player.sendMessage(ChatColor.RED + "The shop is full at the moment!");
					return;
				} else {
					chestInv.addItem(new ItemStack(Material.matchMaterial(ITEM), AMOUNT));
				}
				
				player.getInventory().removeItem(new ItemStack(Material.matchMaterial(ITEM), AMOUNT));
				
				shop.getEconomy().withdrawPlayer(shop.getOfflineOwner(), PRICE);
				
				shop.getEconomy().depositPlayer(player, PRICE);
				
				player.sendMessage(ChatColor.GREEN + "Sold " + ChatColor.BLUE + AMOUNT + " "
						+ Items.itemByType(Material.matchMaterial(ITEM)).name + ChatColor.GREEN + " for " + ChatColor.BLUE + "$" + PRICE);
				
				if(OFFER_LIMIT > 0)
					processedOffers++;
				
				shop.openGUI(player, SKShop.GUI_SELL);
			} else {
				player.sendMessage(ChatColor.RED + "You are not carrying the item you wish to sell!");
			}
			
			break;
		case ENTRY_SELL:
			if(SKShopUtil.contains(shop.getMainChest().getInventory(), Material.matchMaterial(ITEM), AMOUNT)) {
				
				if(processedOffers >= OFFER_LIMIT && OFFER_LIMIT > 0) {
					player.sendMessage(ChatColor.RED + "This offer has expired!");
					return;
				}
				
				if(shop.getEconomy().getBalance(player) < PRICE) {
					player.sendMessage(ChatColor.RED + "You don't have enough money to purchase this!");
					return;
				}
				
				if(SKShopUtil.fitsInInventory(player.getInventory(), Material.matchMaterial(ITEM), AMOUNT)) {
					player.getInventory().addItem(new ItemStack(Material.matchMaterial(ITEM), AMOUNT));
				} else {
					player.sendMessage(ChatColor.RED + "You don't have enough room purchase this!");
					return;
				}
				
				shop.getMainChest().getInventory().removeItem(new ItemStack(Material.matchMaterial(ITEM), AMOUNT));
				shop.getEconomy().withdrawPlayer(player, PRICE);
				shop.getEconomy().depositPlayer(shop.getOfflineOwner(), PRICE);
				
				player.sendMessage(ChatColor.GREEN + "Bought " + ChatColor.BLUE + AMOUNT + " "
						+ Items.itemByType(Material.matchMaterial(ITEM)).name + ChatColor.GREEN + " for " + ChatColor.BLUE + "$" + PRICE);
				
				if(OFFER_LIMIT > 0)
					processedOffers++;
				
				shop.openGUI(player, SKShop.GUI_BUY);
			} else {
				player.sendMessage(ChatColor.RED + "This offer is out of stock!");
			}
			break;
		}
	}
	
	public SKShopEntry setProcessedOffers(int processedOffers) {
		this.processedOffers = processedOffers;
		return this;
	}
	
	public int processedOffers() {
		return processedOffers;
	}
}