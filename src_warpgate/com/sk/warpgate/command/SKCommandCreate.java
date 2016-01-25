package com.sk.warpgate.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk.warpgate.SKMain;
import com.sk.warpgate.SKWarpgateUtil;

public class SKCommandCreate implements CommandExecutor {
	
	private SKMain main;
	
	public SKCommandCreate(SKMain main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is for players only!");
			return true;
		}
		
		Player player = (Player) sender;
		
		main.getServer().broadcastMessage(
				Boolean.toString(SKWarpgateUtil.validStructure(player.getLocation())));
		return true;
	}
}