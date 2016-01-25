package com.sk.shop.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sk.shop.SKSave;
import com.sk.shop.SKShop;
import com.sk.shop.SKShopEntry;
import com.sk.shop.SKShopMain;

import net.milkbowl.vault.item.Items;

public class SKCommandRemove implements CommandExecutor {
	
	private SKShopMain main;
	
	public SKCommandRemove(SKShopMain main) {
		this.main = main;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is for players only!");
			return true;
		}
		
		Player player = (Player) sender;
		
		boolean sell = cmd.getLabel().equalsIgnoreCase("sksremovesell");
		
		if((!player.hasPermission("sk.shop.entry.remove.sell.self") && sell) ||
				(!player.hasPermission("sk.shop.entry.remove.buy.self") && !sell)) {
			player.sendMessage(ChatColor.RED + "You do not have permissions to use this command.");
			return true;
		}
		
		if(args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
			return true;
		}
		
		try {
			SKShop shop = main.getShop(Integer.parseInt(args[0]));
			
			if(shop == null) {
				sender.sendMessage(ChatColor.RED + "There is no SKShop with id " + ChatColor.BLUE + args[0]);
				return true;
			}
			
			if(!player.getName().equals(shop.getOwnerName())
					&& ((!player.hasPermission("sk.shop.entry.remove.sell.others") && sell)
					|| (!player.hasPermission("sk.shop.entry.remove.buy.others") && !sell))) {
				player.sendMessage(ChatColor.RED + "SKShop " + ChatColor.BLUE + args[0]
						+ ChatColor.RED + " belongs to " + ChatColor.BLUE + shop.getOwnerName());
				return true;
			}
			
			if(!sell) {
				if(!shop.removeBuyEntry(Integer.parseInt(args[1]) - 1)) {
					sender.sendMessage(ChatColor.RED + "There is no buy entry with index " + ChatColor.BLUE + args[1]);
					return true;
				}
				
			} else if(sell) {
				if(!shop.removeSellEntry(Integer.parseInt(args[1]) - 1)) {
					sender.sendMessage(ChatColor.RED + "There is no sell entry with index " + ChatColor.BLUE + args[1]);
					return true;
				}
			} else {
				return true;
			}
			
			sender.sendMessage(ChatColor.GREEN + "Removed " + (cmd.getLabel().equalsIgnoreCase("sksremovebuy") ? "buy" : "sell")
					+ " entry " + ChatColor.BLUE + args[1] + ChatColor.GREEN + " from " + ChatColor.BLUE + shop.getName());
			
			SKSave.save(main, main.getShopConfig(), shop);
			
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
			return true;
		}
		
		return true;
	}
}