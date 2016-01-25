package com.sk.shop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class SKSave {
	
	public static final String SAVE_PATH = "plugins/SKShop/shops.yml";
	
	public static final HashMap<Integer, SKShop> load(SKShopMain main, FileConfiguration config) {
		
		try {
			config.load(new File(SAVE_PATH));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Set<String> keys = config.getKeys(false);
		Iterator<String> i = keys.iterator();
		
		while(i.hasNext()) {
			String key = i.next();
			
			
			World world = main.getServer().getWorld(config.getString(key + ".world"));
			
			if(world == null)
				world = main.getServer().createWorld(new WorldCreator(config.getString(key + ".world")));
			
			Block block = world.getBlockAt(config.getInt(key + ".x"),
					config.getInt(key + ".y"),
					config.getInt(key + ".z"));
			
			if(!(block.getState() instanceof Sign)) {
				remove(main, config, Integer.parseInt(key));
				continue;
			}
			
			Sign sign = (Sign) block.getState();
			
			SKShop shop = new SKShop(main, Integer.parseInt(key), config.getString(key + ".name"), 
					UUID.fromString(config.getString(key + ".owner")), sign);
			
			List<String> sellEntries = config.getStringList(shop.getID() + ".entries.sell");
			
			for(int j = 0; j < sellEntries.size(); j++) {
				String[] data = sellEntries.get(j).substring(1, sellEntries.get(j).length() - 1).split(",");
				shop.addEntry(new SKShopEntry(shop, SKShopEntry.ENTRY_SELL, data[0], Integer.parseInt(data[1]),
						Double.parseDouble(data[2]), Integer.parseInt(data[3])).setProcessedOffers(Integer.parseInt(data[4])));
			}
			
			List<String> buyEntries = config.getStringList(shop.getID() + ".entries.buy");
			
			for(int j = 0; j < buyEntries.size(); j++) {
				String[] data = buyEntries.get(j).substring(1, buyEntries.get(j).length() - 1).split(",");
				shop.addEntry(new SKShopEntry(shop, SKShopEntry.ENTRY_BUY, data[0], Integer.parseInt(data[1]),
						Double.parseDouble(data[2]), Integer.parseInt(data[3])).setProcessedOffers(Integer.parseInt(data[4])));
			}
			
			main.shops.put(Integer.parseInt(key), shop);
			
			i.remove();
		}
		
		return null;
	}
	
	public static final void save(SKShopMain main, FileConfiguration config, SKShop shop) {
		
		//Save name
		config.set(shop.getID() + ".name", shop.getName());
		
		//Save owner
		config.set(shop.getID() + ".owner", shop.getOwnerUUID().toString());
		
		//Save pos
		config.set(shop.getID() + ".x", shop.getSign().getX());
		config.set(shop.getID() + ".y", shop.getSign().getY());
		config.set(shop.getID() + ".z", shop.getSign().getZ());
		config.set(shop.getID() + ".world", shop.getSign().getWorld().getName());
		
		//Save entries (SKShop shop, int entry, String item, int amount, double price, int offerLimit)
		
		ArrayList<String> sellEntries = new ArrayList<>();
		
		for(int i = 0; i < shop.getSellEntries().size(); i++) {
			SKShopEntry e = shop.getSellEntries().get(i);
			
			sellEntries.add("[" + e.ITEM + "," + e.AMOUNT + ","
					+ e.PRICE + "," + e.OFFER_LIMIT + "," + e.processedOffers() + "]");
		}
		
		ArrayList<String> buyEntries = new ArrayList<>();
		
		for(int i = 0; i < shop.getBuyEntries().size(); i++) {
			SKShopEntry e = shop.getBuyEntries().get(i);
			
			buyEntries.add("[" + e.ITEM + "," + e.AMOUNT + ","
					+ e.PRICE + "," + e.OFFER_LIMIT + "," + e.processedOffers() + "]");
		}
		
		config.set(shop.getID() + ".entries.sell", sellEntries);
		config.set(shop.getID() + ".entries.buy", buyEntries);
		
		try {
			config.save(new File(SAVE_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static final FileConfiguration getShopConfig() {
		File file = new File(SAVE_PATH);
		
		try {
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
		} catch (IOException e) {e.printStackTrace();}
		
		return YamlConfiguration.loadConfiguration(file);
	}
	
	public static final void remove(SKShopMain main, FileConfiguration config, int id) {
		config.set(Integer.toString(id), null);
		
		try {
			config.save(new File(SAVE_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}