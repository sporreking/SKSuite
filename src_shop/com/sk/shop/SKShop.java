package com.sk.shop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.item.Items;

public class SKShop {
	
	public static final int GUI_NONE = 0;
	
	public static final int GUI_HOME = 1;
	public static final String GUI_HOME_SUFFIX = " - Buy/Sell";
	
	public static final int GUI_BUY = 2;
	public static final String GUI_BUY_SUFFIX = " - Buy";
	
	public static final int GUI_SELL = 3;
	public static final String GUI_SELL_SUFFIX = " - Sell";
	
	private SKShopMain main;
	
	private int id;
	
	private Sign sign;
	private Chest chest;
	private Chest chest2;
	
	private String name;
	private String owner;
	private UUID ownerUUID;
	
	private HashSet<Player> interactingPlayers;
	private ArrayList<SKShopEntry> buyEntries;
	private ArrayList<SKShopEntry> sellEntries;
	
	
	public SKShop(SKShopMain main, int id, String name, UUID uuid, Sign sign) {
		this.main = main;
		this.id = id;
		this.sign = sign;
		this.name = name;
		this.ownerUUID = uuid;
		this.owner = main.getServer().getOfflinePlayer(uuid).getName();
		
		interactingPlayers = new HashSet<>();
		buyEntries = new ArrayList<>();
		sellEntries = new ArrayList<>();
		
		findHostChest();
		
		assignMetadata();
		formatSign();
	}
	
	private void findHostChest() {
		BlockFace face = ((org.bukkit.material.Sign) sign.getData()).getFacing();
		
		BlockState block = null;
		
		switch(face) {
		case NORTH:
			block = sign.getWorld().getBlockAt(sign.getX(), sign.getY(), sign.getZ() + 1).getState();
			break;
		case SOUTH:
			block = sign.getWorld().getBlockAt(sign.getX(), sign.getY(), sign.getZ() - 1).getState();
			break;
		case WEST:
			block = sign.getWorld().getBlockAt(sign.getX() + 1, sign.getY(), sign.getZ()).getState();
			break;
		case EAST:
			block = sign.getWorld().getBlockAt(sign.getX() - 1, sign.getY(), sign.getZ()).getState();
			break;
		}
		
		chest = (Chest) block;
		if(chest.getInventory().getSize() == 54)
			chest2 = getAdjacentChest(chest.getLocation());
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
	
	private void assignMetadata() {
		sign.setMetadata("skshop-id", new FixedMetadataValue(main, id));
		sign.setMetadata("skshop-name", new FixedMetadataValue(main, name));
		sign.setMetadata("skshop-owner", new FixedMetadataValue(main, owner));
		
		chest.setMetadata("skshop-id", new FixedMetadataValue(main, id));
		chest.setMetadata("skshop-name", new FixedMetadataValue(main, name));
		chest.setMetadata("skshop-owner", new FixedMetadataValue(main, owner));
		chest.setMetadata("skshop-chestmain", new FixedMetadataValue(main, true));
		
		assignSecondChestMetadata();
	}
	
	private void assignSecondChestMetadata() {
		if(chest2 != null) {
			chest2.setMetadata("skshop-id", new FixedMetadataValue(main, id));
			chest2.setMetadata("skshop-name", new FixedMetadataValue(main, name));
			chest2.setMetadata("skshop-owner", new FixedMetadataValue(main, owner));
			chest2.setMetadata("skshop-chestmain", new FixedMetadataValue(main, false));
		}
	}
	
	private void formatSign() {
		sign.setLine(0, ChatColor.DARK_GREEN + "[SKShop " + ChatColor.BLUE + "ID: " + id + ChatColor.DARK_GREEN + "]");
		sign.setLine(1, ChatColor.BLUE + name);
		sign.setLine(2, ChatColor.DARK_GREEN + "Owner:");
		sign.setLine(3, ChatColor.BLUE + owner);
		
		sign.update();
	}
	
	public boolean addEntry(SKShopEntry entry) {
		
		switch(entry.ENTRY) {
		case SKShopEntry.ENTRY_BUY:
			if(buyEntries.size() >= 7 * 2)
				return false;
			buyEntries.add(entry);
			return true;
		case SKShopEntry.ENTRY_SELL:
			if(sellEntries.size() >= 7 * 2)
				return false;
			sellEntries.add(entry);
			return true;
		}
		
		return false;
	}
	
	public boolean removeBuyEntry(int index) {
		if(index < 0 || index >= buyEntries.size())
			return false;
		
		buyEntries.remove(index);
		
		return true;
	}
	
	public boolean removeSellEntry(int index) {
		if(index < 0 || index >= sellEntries.size())
			return false;
		
		sellEntries.remove(index);
		
		return true;
	}
	
	public ArrayList<SKShopEntry> getBuyEntries() {
		return buyEntries;
	}
	
	public ArrayList<SKShopEntry> getSellEntries() {
		return sellEntries;
	}
	
	public void buy(Player player, int offset) {
		
		if(offset < 0 || offset >= sellEntries.size())
			return;
		
		sellEntries.get(offset).process(player);
	}
	
	public void sell(Player player, int offset) {
		if(offset < 0 || offset >= buyEntries.size())
			return;
		
		buyEntries.get(offset).process(player);
	}
	
	private Inventory createHomeGUI() {
		Inventory inv = Bukkit.createInventory(null, 9 * 3, name + GUI_HOME_SUFFIX);
		
		
		ItemStack buy = new ItemStack(Material.GOLD_INGOT);
		ItemMeta buyMeta = buy.getItemMeta();
		buyMeta.setDisplayName(ChatColor.GREEN + "Buy");
		buyMeta.setLore(getLore(ChatColor.BLUE + "Buy from " + name));
		buy.setItemMeta(buyMeta);
		
		ItemStack sell = new ItemStack(Material.IRON_INGOT);
		ItemMeta sellMeta = sell.getItemMeta();
		sellMeta.setDisplayName(ChatColor.GREEN + "Sell");
		sellMeta.setLore(getLore(ChatColor.BLUE + "Sell to " + name));
		sell.setItemMeta(sellMeta);
		
		
		inv.setItem(11, buy);
		inv.setItem(15, sell);
		
		return inv;
	}
	
	private Inventory createBuyGUI(Player player) {
		
		Inventory inv = Bukkit.createInventory(null, (int) Math.ceil(sellEntries.size() / 7f + 2) * 9, name + GUI_BUY_SUFFIX);
		
		for(int i = 0; i < sellEntries.size(); i++) {
			ItemStack is = new ItemStack(Material.matchMaterial(sellEntries.get(i).ITEM), sellEntries.get(i).AMOUNT);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_PURPLE + "Buy "
					+ sellEntries.get(i).AMOUNT + " " + Items.itemByType(Material.matchMaterial(sellEntries.get(i).ITEM)).name);
			if(SKShopUtil.contains(chest.getInventory(), Material.matchMaterial(sellEntries.get(i).ITEM), sellEntries.get(i).AMOUNT)) {
				if(sellEntries.get(i).OFFER_LIMIT == 0)
					meta.setLore(getLore(ChatColor.BLUE + "for $" + sellEntries.get(i).PRICE));
				else
					meta.setLore(getLore(ChatColor.BLUE + "for $" + sellEntries.get(i).PRICE
							+ getGradientChatColor(sellEntries.get(i).processedOffers(), sellEntries.get(i).OFFER_LIMIT)
							+ " (" + sellEntries.get(i).processedOffers() + "/" + sellEntries.get(i).OFFER_LIMIT + ")"));
			} else {
				if(sellEntries.get(i).OFFER_LIMIT == 0)
					meta.setLore(getLore(ChatColor.BLUE + "for $" + sellEntries.get(i).PRICE, ChatColor.RED + "OUT OF STOCK"));
				else
					meta.setLore(getLore(ChatColor.BLUE + "for $" + sellEntries.get(i).PRICE
							+ getGradientChatColor(sellEntries.get(i).processedOffers(), sellEntries.get(i).OFFER_LIMIT)
							+ " (" + sellEntries.get(i).processedOffers() + "/" + sellEntries.get(i).OFFER_LIMIT + ")",
							ChatColor.RED + "OUT OF STOCK"));
			}
			is.setItemMeta(meta);
			
			inv.setItem((i % 7) + Math.floorDiv(i, 7) * 9 + 10, is);
		}
		
		ItemStack is = new ItemStack(Material.BARRIER);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Back");
		meta.setLore(getLore(ChatColor.BLUE + "Return to Buy/Sell"));
		is.setItemMeta(meta);
		
		inv.setItem((int) Math.ceil(sellEntries.size() / 7f + 1) * 9 + 4, is);
		
		return inv;
	}
	
	private Inventory createSellGUI(Player player) {

		Inventory inv = Bukkit.createInventory(null, (int) Math.ceil(buyEntries.size() / 7f + 2) * 9, name + GUI_SELL_SUFFIX);
		
		for(int i = 0; i < buyEntries.size(); i++) {
			ItemStack is = new ItemStack(Material.matchMaterial(buyEntries.get(i).ITEM), buyEntries.get(i).AMOUNT);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_PURPLE + "Sell "
					+ buyEntries.get(i).AMOUNT + " " + Items.itemByType(Material.matchMaterial(buyEntries.get(i).ITEM)).name);
			
			if(SKShopUtil.fitsInInventory(chest.getInventory(), Material.matchMaterial(buyEntries.get(i).ITEM), buyEntries.get(i).AMOUNT)) {
				if(buyEntries.get(i).OFFER_LIMIT == 0)
					meta.setLore(getLore(ChatColor.BLUE + "for $" + buyEntries.get(i).PRICE));
				else
					meta.setLore(getLore(ChatColor.BLUE + "for $" + buyEntries.get(i).PRICE
							+ getGradientChatColor(buyEntries.get(i).processedOffers(), buyEntries.get(i).OFFER_LIMIT)
							+ " (" + buyEntries.get(i).processedOffers() + "/" + buyEntries.get(i).OFFER_LIMIT + ")"));
			} else {
				if(buyEntries.get(i).OFFER_LIMIT == 0)
					meta.setLore(getLore(ChatColor.BLUE + "for $" + buyEntries.get(i).PRICE, ChatColor.RED + "SHOP FULL"));
				else
					meta.setLore(getLore(ChatColor.BLUE + "for $" + buyEntries.get(i).PRICE
							+ getGradientChatColor(buyEntries.get(i).processedOffers(), buyEntries.get(i).OFFER_LIMIT)
							+ " (" + buyEntries.get(i).processedOffers() + "/" + buyEntries.get(i).OFFER_LIMIT + ")",
							ChatColor.RED + "SHOP FULL"));
			}
			is.setItemMeta(meta);
			
			inv.setItem((i % 7) + Math.floorDiv(i, 7) * 9 + 10, is);
		}
		
		ItemStack is = new ItemStack(Material.BARRIER);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Back");
		meta.setLore(getLore(ChatColor.BLUE + "Return to Buy/Sell"));
		is.setItemMeta(meta);
		
		inv.setItem((int) Math.ceil(buyEntries.size() / 7f + 1) * 9 + 4, is);
		
		return inv;
	}
	
	private ChatColor getGradientChatColor(int amount, int max) {
		int color = (int) Math.floor(((float) amount) / max * 5);
		
		switch(color) {
		default:
			return ChatColor.BLACK;
		case 0:
			return ChatColor.DARK_GREEN;
		case 1:
			return ChatColor.GREEN;
		case 2:
			return ChatColor.YELLOW;
		case 3:
			return ChatColor.GOLD;
		case 4:
			return ChatColor.RED;
		case 5:
			return ChatColor.DARK_RED;
		}
	}
	
	private List<String> getLore(String... lore) {
		ArrayList<String> data = new ArrayList<>();
		for(String s : lore)
			data.add(s);
		
		return data;
	}
	
	public void openGUI(Player player, int gui) {
		
		Inventory inv;
		switch(gui) {
		default:
		case GUI_HOME:
			inv = createHomeGUI();
			break;
		case GUI_BUY:
			inv = createBuyGUI(player);
			break;
		case GUI_SELL:
			inv = createSellGUI(player);
			break;
		}
		
		player.openInventory(inv);
		interactingPlayers.add(player);
	}
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getOwnerName() {
		return owner;
	}
	
	public Sign getSign() {
		return sign;
	}
	
	public Chest getMainChest() {
		return chest;
	}
	
	public void setSecondChest(Chest chest) {
		chest2 = chest;
		assignSecondChestMetadata();
	}
	
	public Chest getSecondChest() {
		return chest2;
	}
	
	public Economy getEconomy() {
		return main.getEconomy();
	}
	
	public UUID getOwnerUUID() {
		return ownerUUID;
	}
	
	public OfflinePlayer getOfflineOwner() {
		return main.getServer().getOfflinePlayer(ownerUUID);
	}
	
	public HashSet<Player> getInteractingPlayers() {
		return interactingPlayers;
	}
	
	protected void removeSecondChest() {
		if(chest2 != null) {
			chest2.removeMetadata("skshop-id", main);
			chest2.removeMetadata("skshop-name", main);
			chest2.removeMetadata("skshop-owner", main);
			chest2.removeMetadata("skshop-chestmain", main);
			chest2 = null;
		}
	}
	
	protected void destroy() {
		sign.removeMetadata("skshop-id", main);
		sign.removeMetadata("skshop-name", main);
		sign.removeMetadata("skshop-owner", main);
		
		chest.removeMetadata("skshop-id", main);
		chest.removeMetadata("skshop-name", main);
		chest.removeMetadata("skshop-owner", main);
		chest.removeMetadata("skshop-chestmain", main);
		
		removeSecondChest();
	}
}