package com.sk.shop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SKInteractListener implements Listener {
	
	private SKShopMain main;
	
	public SKInteractListener(SKShopMain main) {
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			if(e.getClickedBlock().getType() == Material.CHEST) {
				if(e.getClickedBlock().hasMetadata("skshop-id")) {
					if(!e.getClickedBlock().getMetadata("skshop-owner").get(0).asString().equals(e.getPlayer().getName())) {
						if(e.getPlayer().hasPermission("sk.shop.chest.open.others"))
							return;
						e.setCancelled(true);
						e.getPlayer().sendMessage(ChatColor.RED + "This SKShop belongs to " + ChatColor.BLUE
								+ e.getClickedBlock().getMetadata("skshop-owner").get(0).asString());
					} else if(!e.getPlayer().hasPermission("sk.shop.chest.open.self")) {
						e.getPlayer().sendMessage(ChatColor.RED + "You do not have permissions to open your SKShop chests");
						e.setCancelled(true);
					}
				}
			}
			
			if(e.getClickedBlock().getType() == Material.WALL_SIGN) {
				Sign sign = (Sign) e.getClickedBlock().getState();
				
				if(sign.hasMetadata("skshop-id")) {
					
					if(!e.getPlayer().hasPermission("sk.shop.open")) {
						e.getPlayer().sendMessage(ChatColor.RED + "You do not have permissions to open SKShops");
						return;
					}
					
					main.getShop(sign.getMetadata("skshop-id").get(0).asInt()).openGUI(e.getPlayer(), SKShop.GUI_HOME);
					
				} else if(sign.getLine(0).equals("[SKShop]") && e.getPlayer().hasPermission("sk.shop.create")) {
					if(hasAdjacentChest(sign)) {
						if(sign.getLine(1).length() > 0) {
							main.createShop(e, sign);
						} else {
							e.getPlayer().sendMessage(ChatColor.RED + "Unspecified SKShop name");
							sign.update();
						}
					} else {
						e.getPlayer().sendMessage(ChatColor.RED + "SKShops may only be created on chests");
					}
				}
			}
		}
	}
	
	private boolean hasAdjacentChest(Sign sign) {
		
		BlockFace face = ((org.bukkit.material.Sign) sign.getData()).getFacing();
		
		if(face == BlockFace.NORTH)
			return sign.getWorld().getBlockAt(sign.getX(), sign.getY(), sign.getZ() + 1).getType() == Material.CHEST;
		
		if(face == BlockFace.SOUTH)
			return sign.getWorld().getBlockAt(sign.getX(), sign.getY(), sign.getZ() - 1).getType() == Material.CHEST;
		
		if(face == BlockFace.WEST)
			return sign.getWorld().getBlockAt(sign.getX() + 1, sign.getY(), sign.getZ()).getType() == Material.CHEST;
		
		if(face == BlockFace.EAST)
			return sign.getWorld().getBlockAt(sign.getX() - 1, sign.getY(), sign.getZ()).getType() == Material.CHEST;
		
		return false;
	}
}