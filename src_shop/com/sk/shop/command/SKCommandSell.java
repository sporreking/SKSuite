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
import com.sk.shop.SKShopUtil;

import net.milkbowl.vault.item.Items;

public class SKCommandSell implements CommandExecutor {
	
	private SKShopMain main;
	
	public SKCommandSell(SKShopMain main) {
		this.main = main;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is for players only!");
			return true;
		}
		
		Player player = (Player) sender;
		
		if(!player.hasPermission("sk.shop.entry.add.sell.self")) {
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
			
			if(!player.getName().equals(shop.getOwnerName()) && !player.hasPermission("sk.shop.entry.add.sell.others")) {
				player.sendMessage(ChatColor.RED + "SKShop " + ChatColor.BLUE + args[0]
						+ ChatColor.RED + " belongs to " + ChatColor.BLUE + shop.getOwnerName());
				return true;
			}
			
			ItemStack is = player.getItemInHand();
			
			if(is.getType() == Material.AIR) {
				player.sendMessage(ChatColor.RED + "You are not holding any items!");
				return true;
			}
			
			if(args[1].contains(".")) {
				if(args[1].split("\\.")[1].length() > 2) {
					player.sendMessage(ChatColor.RED + "Price may only contian two decimals!");
					return true;
				}
			}
			
			shop.addEntry(new SKShopEntry(shop, SKShopEntry.ENTRY_SELL, is.getType().name(), is.getAmount(),
					Double.parseDouble(args[1]), args.length > 2 ? Integer.parseInt(args[2]) : 0));
			
			sender.sendMessage(ChatColor.BLUE + shop.getName() + ChatColor.GREEN + " is now selling "
					+ ChatColor.BLUE + is.getAmount() + " " + Items.itemByType(is.getType()).name + ChatColor.GREEN + " for "
					+ ChatColor.BLUE + "$" + args[1] + (args.length > 2 ? ChatColor.RED + " (" + args[2]  + " offers)": ""));
			
			SKSave.save(main, main.getShopConfig(), shop);
			
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
			return true;
		}
		
		return true;
	}
	
}