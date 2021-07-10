package dev.svengo.compass;

import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class CompassPlugin extends JavaPlugin {
	
	@Override
	public void onEnable() {
		new CompassTaskTimer().runTaskTimer(this, 0, 10);
		this.getCommand("track").setExecutor(new CompassCommand());
		setTextComponents();
		this.getServer().getPluginManager().registerEvents(new CompassListeners(), this);
		//Register click event or something to tell player what they are tracking (and where)
	}
	
	public void setTextComponents() {
		CompassCommand.errorMessage.setColor(ChatColor.RED);
		CompassCommand.validArgs.setColor(ChatColor.GRAY);
		CompassCommand.invalidArgs.setColor(ChatColor.RED);
		CompassCommand.invalidArgs.setUnderlined(true);
		CompassCommand.HERE.setColor(ChatColor.RED);
		CompassCommand.HERE.setText("<--[HERE]");
	}
}