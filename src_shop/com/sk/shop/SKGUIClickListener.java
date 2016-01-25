package com.sk.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class SKGUIClickListener implements Listener {
	
	private SKShopMain main;
	
	public SKGUIClickListener(SKShopMain main) {
		this.main = main;
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		Inventory inv = e.getInventory();
		
		SKShop shop = main.getShop(inv.getName().split("[ ][-][ ]")[0]);
		
		if(shop == null) {
			return;
		} else {
			shop.getInteractingPlayers().remove(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		
		if(!(e.getWhoClicked() instanceof Player))
			return;
		
		Inventory inv = e.getInventory();
		
		SKShop shop = main.getShop(inv.getName().split("[ ][-][ ]")[0]);
		
		if(shop != null && shop.getInteractingPlayers().contains(e.getWhoClicked())) {
			if(e.getCurrentItem() == null)
				return;
			
			if(inv.getName().endsWith(SKShop.GUI_HOME_SUFFIX)) {
				onClickHome(e, shop, inv);
			} else if(inv.getName().endsWith(SKShop.GUI_BUY_SUFFIX) && e.getWhoClicked().hasPermission("sk.shop.buy")) {
				shop.buy((Player) e.getWhoClicked(), e.getSlot() - Math.floorDiv(e.getSlot(), 9) * 9 - 1);
				if(e.getCurrentItem().getType() == Material.BARRIER) {
					shop.openGUI((Player) e.getWhoClicked(), SKShop.GUI_HOME);
				}
			} else if(inv.getName().endsWith(SKShop.GUI_SELL_SUFFIX) && e.getWhoClicked().hasPermission("sk.shop.sell")) {
				shop.sell((Player) e.getWhoClicked(), e.getSlot() - Math.floorDiv(e.getSlot(), 9) * 9 - 1);
				if(e.getCurrentItem().getType() == Material.BARRIER) {
					shop.openGUI((Player) e.getWhoClicked(), SKShop.GUI_HOME);
				}
			}
			
			e.setCancelled(true);
		}
	}
	
	
	private void onClickHome(InventoryClickEvent e, SKShop shop, Inventory inv) {
		if(e.getCurrentItem().getType() == Material.GOLD_INGOT) {
			//Buy
			
			shop.openGUI((Player) e.getWhoClicked(), SKShop.GUI_BUY);
			
		} else if(e.getCurrentItem().getType() == Material.IRON_INGOT) {
			//Sell
			
			shop.openGUI((Player) e.getWhoClicked(), SKShop.GUI_SELL);
			
		}
	}
}