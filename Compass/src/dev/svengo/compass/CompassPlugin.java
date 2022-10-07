package dev.svengo.compass;

import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class CompassPlugin extends JavaPlugin {
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new CompassListeners(), this); // register compass events
		CompassTaskTimer.start(this); // start continually updating compasses' target locations
		this.getCommand("track").setExecutor(new CompassCommand()); // enables tracking command
	}
}