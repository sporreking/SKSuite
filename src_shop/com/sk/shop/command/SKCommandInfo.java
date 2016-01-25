package com.sk.shop.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk.shop.SKShopMain;

public class SKCommandInfo implements CommandExecutor {
	
	private SKShopMain main;
	
	public SKCommandInfo(SKShopMain main) {
		this.main = main;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			if(!((Player) sender).hasPermission("sk.shop.info")) {
				sender.sendMessage(ChatColor.RED + "You do not have permissions to use this command.");
				return true;
			}
		}
		
		sender.sendMessage(ChatColor.GOLD + "To create a shop with SKShop, right click on a sign that says"
				+ ChatColor.BLUE + "'[SKShop]'" + ChatColor.GOLD + " on the first row. A name has to be provided"
						+ " on the second row as well.");
		
		Bukkit.dispatchCommand(sender, "help skshop");
		
		return true;
	}
	
}