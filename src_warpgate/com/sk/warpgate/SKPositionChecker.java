package com.sk.warpgate;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SKPositionChecker implements Runnable {
	
	private SKMain main;
	
	private HashSet<Player> teleportingPlayers;
	
	public SKPositionChecker(SKMain main) {
		this.main = main;
		
		teleportingPlayers = new HashSet<>();
	}
	
	@Override
	public void run() {
		for(Player p : main.getServer().getOnlinePlayers()) {
			for(SKWarpgate wg : main.getWarpgates().values()) {
				if(p.getLocation().getBlockX() == wg.getLocation().getBlockX()
						&& p.getLocation().getBlockY() == wg.getLocation().getBlockY() + 1
						&& p.getLocation().getBlockZ() == wg.getLocation().getBlockZ()
						&& !teleportingPlayers.contains(p)) {
					
					teleportingPlayers.add(p);
					
					p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
							1200, 1000, false, false));
					
					int id = main.getServer().getScheduler()
							.scheduleSyncDelayedTask(main, new SKTeleporter(p, wg), 60L);
					new Thread(new SKTeleportCanceler(p, wg, id)).start();
					
					break;
				}
			}
		}
	}
	
	private class SKTeleportCanceler implements Runnable {
		
		private Player player;
		private SKWarpgate warpgate;
		private int taskID;
		
		public SKTeleportCanceler(Player player, SKWarpgate warpgate, int taskID) {
			this.player = player;
			this.warpgate = warpgate;
			this.taskID = taskID;
		}
		
		@Override
		public void run() {
			
			boolean loop = true;
			while(loop) {
				if(!main.getServer().getScheduler().isQueued(taskID))
					break;
				
				if(!player.isOnline() || !(
						player.getLocation().getBlockX() == warpgate.getLocation().getBlockX()
						&& player.getLocation().getBlockY() == warpgate.getLocation().getBlockY() + 1
						&& player.getLocation().getBlockZ() == warpgate.getLocation().getBlockZ())) {
					
					cancel();
					loop = false;
				}
			}
		}
		
		private void cancel() {
			main.getServer().getScheduler().cancelTask(taskID);
			teleportingPlayers.remove(player);
			player.removePotionEffect(PotionEffectType.CONFUSION);
		}
	}
	
	private class SKTeleporter implements Runnable {
		
		private Player player;
		private SKWarpgate warpgate;
		
		public SKTeleporter(Player player, SKWarpgate warpgate) {
			this.player = player;
			this.warpgate = warpgate;
		}
		
		@Override
		public void run() {
			if(player.isOnline()) {
				player.teleport(warpgate.getLocation().clone().add(.5d, 4, .5d));
				teleportingPlayers.remove(player);
				player.removePotionEffect(PotionEffectType.CONFUSION);
			}
		}
	}
}