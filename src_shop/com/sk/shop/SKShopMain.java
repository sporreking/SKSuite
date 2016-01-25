package com.sk.shop;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk.shop.command.*;

import net.milkbowl.vault.economy.Economy;

public class SKShopMain extends JavaPlugin {
	
	protected HashMap<Integer, SKShop> shops;
	
	private FileConfiguration shopConfig;
	
	private Economy economy;
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = getDescription();
		
		shops = new HashMap<>();
		
		shopConfig = SKSave.getShopConfig();
		
		SKSave.load(this, shopConfig);
		
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
		
		if(economyProvider == null)
			throw new IllegalStateException("SKShop requires Vault to be installed");
		else
			economy = economyProvider.getProvider();
		
		registerCommands();
		registerEvents();
		
		Logger log = Logger.getLogger("logger");
	}
	
	@Override
	public void onDisable() {
		for(SKShop shop : shops.values()) {
			SKSave.save(this, shopConfig, shop);
		}
	}
	
	public FileConfiguration getShopConfig() {
		return shopConfig;
	}
	
	public Economy getEconomy() {
		return economy;
	}
	
	private void registerCommands() {
		getCommand("skshop").setExecutor(new SKCommandInfo(this));
		getCommand("sksbuy").setExecutor(new SKCommandBuy(this));
		getCommand("skssell").setExecutor(new SKCommandSell(this));
		getCommand("sksremovebuy").setExecutor(new SKCommandRemove(this));
		getCommand("sksremovesell").setExecutor(new SKCommandRemove(this));
	}
	
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new SKInteractListener(this), this);
		pm.registerEvents(new SKBreakShopListener(this), this);
		pm.registerEvents(new SKGUIClickListener(this), this);
		pm.registerEvents(new SKDoubleChestListener(this), this);
	}
	
	protected void removeShop(int id) {
		shops.get(id).destroy();
		shops.remove(id);
		SKSave.remove(this, shopConfig, id);
	}
	
	public SKShop getShop(int id) {
		return shops.get(id);
	}
	
	public SKShop getShop(String name) {
		for(Map.Entry<Integer, SKShop> se : shops.entrySet()) {
			if(getShop(se.getKey()) == null)
				continue;
			
			if(getShop(se.getKey()).getName().equals(name))
				return getShop(se.getKey());
		}
		
		return null;
	}
	
	protected void createShop(PlayerInteractEvent e, Sign sign) {
		
		int i = 0;
		while(true) {
			
			if(!shops.containsKey(i)) {
				SKShop shop = new SKShop(this, i, sign.getLine(1), e.getPlayer().getUniqueId(), sign);
				shops.put(i, shop);
				SKSave.save(this, shopConfig, shop);
				break;
			}
			
			i++;
		}
		
		e.getPlayer().sendMessage(ChatColor.GREEN + "Created new SKShop " + sign.getLine(1));
	}
}