package com.sk.warpgate.event;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.sk.warpgate.SKMain;
import com.sk.warpgate.SKWarpgate;
import com.sk.warpgate.SKWarpgateUtil;

public class SKInteractListener implements Listener {
	
	private SKMain main;
	
	public SKInteractListener(SKMain main) {
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getClickedBlock().getType() == Material.DIAMOND_BLOCK
					&& e.getPlayer().getItemInHand().getType() == Material.WATCH) {
				
				Player p = e.getPlayer();
				
				ItemStack clock = e.getPlayer().getItemInHand();
				
				if(!SKWarpgateUtil.validStructure(e.getClickedBlock().getLocation())) {
					p.sendMessage(ChatColor.RED + "Invalid warpgate structure!");
					return;
				}
				
				for(Map.Entry<String, SKWarpgate> wg : main.getWarpgates().entrySet()) {
					if(wg.getValue().getLocation().equals(e.getClickedBlock().getLocation())) {
						p.sendMessage(ChatColor.RED + "This warpgate location is already occupied!");
						return;
					}
				}
				
				if(!clock.getItemMeta().hasDisplayName()) {
					p.sendMessage(ChatColor.RED + "You must provide a name for this warpgate!"
							+ ChatColor.GOLD + " (Rename the clock in an anvil)");
					return;
				}
				
				String name = clock.getItemMeta().getDisplayName();
				
				if(main.getWarpgates().containsKey(name)) {
					p.sendMessage(ChatColor.RED + "The name " + ChatColor.BLUE
							+ name + ChatColor.RED + " is already taken!");
					return;
				}
				
				p.sendMessage(ChatColor.GREEN + "Created new warpgate " + ChatColor.BLUE + name);
				main.addWarpgate(e.getClickedBlock().getLocation(),
						p.getUniqueId(), name);
			}
		}
	}
}