package com.sk.warpgate.command;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk.warpgate.SKMain;
import com.sk.warpgate.SKWarpgate;
import com.sk.warpgate.SKWarpgateUtil;

public class SKCommand implements CommandExecutor {
	
	private SKMain main;
	
	public SKCommand(SKMain main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is for players only!");
			return true;
		}
		
		Player player = (Player) sender;
		
		if(args.length == 0)
			return info(player);
		
		switch(args[0]) {
		case "list":
			return list(player);
		}
		
		return true;
	}
	
	private boolean list(Player player) {
		String str = ChatColor.GOLD + "Your warpgates (" + ChatColor.DARK_GREEN
				+ "bound" + ChatColor.GOLD + "/" + ChatColor.DARK_RED
				+ "unbound" + ChatColor.GOLD + "): ";
		
		String wgs = "";
		
		for(SKWarpgate wg : main.getWarpgates().values()) {
			if(player.getUniqueId().equals(wg.getOwnerUUID())) {
				if(wg.isBound())
					wgs += ChatColor.DARK_GREEN;
				else
					wgs += ChatColor.DARK_RED;
				wgs += wg.getName();
				wgs += ChatColor.GOLD + ", ";
			}
		}
		
		if(wgs.length() == 0) {
			player.sendMessage(ChatColor.RED + "You have no warpgates!");
			return true;
		}
		
		player.sendMessage(str + wgs.substring(0, wgs.length() - 2));
		
		return true;
	}
	
	private boolean info(Player player) {
		player.sendMessage(ChatColor.GREEN + "SKWarpgate lets you build neat multistructural"
				+ " warpgates and link them together.");
		Bukkit.dispatchCommand(player, "help skwarpgate");
		return true;
	}
	
	private boolean usage(Player player, Command cmd) {
		player.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
		return true;
	}
}