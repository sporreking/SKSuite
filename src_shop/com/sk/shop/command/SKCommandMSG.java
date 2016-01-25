package com.sk.shop.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.sk.shop.SKShopMain;

public class SKCommandMSG implements CommandExecutor {
	
	private SKShopMain main;
	
	public SKCommandMSG(SKShopMain main) {
		this.main = main;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		
		return false;
	}
	
}