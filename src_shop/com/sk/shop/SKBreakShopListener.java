package com.sk.shop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SKBreakShopListener implements Listener {
	
	private SKShopMain main;
	
	public SKBreakShopListener(SKShopMain main) {
		this.main = main;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.getBlock().getType() == Material.WALL_SIGN || e.getBlock().getType() == Material.CHEST) {
			BlockState block = e.getBlock().getState();
			Player player = e.getPlayer();
			if(block.hasMetadata("skshop-id") && block.hasMetadata("skshop-owner") && block.hasMetadata("skshop-name")) {
				
				if(block.getMetadata("skshop-owner").get(0).asString().equals(player.getName())
						|| e.getPlayer().hasPermission("sk.shop.remove.others")) {
					if(!e.getPlayer().hasPermission("sk.shop.remove.self")
							&& block.getMetadata("skshop-owner").get(0).asString().equals(player.getName())) {
						e.getPlayer().sendMessage(ChatColor.RED + "You do not have permissions to remove your SKShops");
						e.setCancelled(true);
						return;
					}
					
					if(block.hasMetadata("skshop-chestmain")) {
						if(!block.getMetadata("skshop-chestmain").get(0).asBoolean()) {
							main.getShop(block.getMetadata("skshop-id").get(0).asInt()).removeSecondChest();
							return;
						}
					}
					
					player.sendMessage(ChatColor.GREEN + "Destroyed SKShop " + ChatColor.BLUE
							+ block.getMetadata("skshop-name").get(0).asString());
					main.removeShop(block.getMetadata("skshop-id").get(0).asInt());
				} else {
					e.setCancelled(true);
					player.sendMessage(ChatColor.RED + "This SKShop belongs to " + ChatColor.BLUE
							+ block.getMetadata("skshop-owner").get(0).asString());
				}
			}
		}
	}
}