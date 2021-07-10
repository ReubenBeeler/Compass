package dev.svengo.compass;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class CompassCommand implements CommandExecutor {
	
	public static boolean forceCommand = false;

	public static HashMap<Player, Player> huntingMap = new HashMap<>();

	public static ArrayList<TextComponent> hunterComponents = new ArrayList<>();
	public static ArrayList<TextComponent> runnerComponents = new ArrayList<>();
	
	public static TextComponent errorMessage = new ChatComponent(null, ChatColor.RED).getTextComponent();
	public static TextComponent validArgs = new ChatComponent(null, ChatColor.GRAY).getTextComponent();
	public static TextComponent invalidArgs = new ChatComponent(null, ChatColor.RED, true).getTextComponent();
	public static final TextComponent HERE = new ChatComponent("<--[HERE]", ChatColor.RED).getTextComponent();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		hunterComponents.clear();
		runnerComponents.clear();
		boolean validCommand = true;
		if (sender instanceof Player) {
			Player hunter = (Player) sender;
			if (args.length == 0) {
				if (huntingMap.containsKey(hunter)) {
					Player runner = huntingMap.get(hunter);
					hunterComponents.add(new ChatComponent("Tracking", ChatColor.GREEN).getTextComponent());
					hunterComponents.add(new TextComponent(": "));
					hunterComponents.add(new ChatComponent(runner.getName(), ChatColor.YELLOW).getTextComponent());
					hunterComponents.add(new TextComponent(", "));
					hunterComponents.add(new ChatComponent("* " + runner.getLocation().getBlockY() + " *", ChatColor.AQUA).getTextComponent());
					
				} else {
					Location worldSpawn = hunter.getWorld().getSpawnLocation();
					hunterComponents.add(new TextComponent("Tracking world spawn: "));
					hunterComponents.add(new ChatComponent(worldSpawn.getBlockX() + " " + worldSpawn.getBlockY() + " " + worldSpawn.getBlockZ(), ChatColor.AQUA).getTextComponent());
				}
			} else if (args.length == 1) {
				if (args[0].equals("spawn")) {
					huntingMap.remove(hunter);
					if (!hunter.getInventory().contains(Material.COMPASS)) hunter.getInventory().setItemInMainHand(new ItemStack(Material.COMPASS, 1));
					Location worldSpawn = hunter.getWorld().getSpawnLocation();
					hunter.setCompassTarget(worldSpawn);
					hunterComponents.add(new TextComponent("Tracking world spawn: "));
					hunterComponents.add(new ChatComponent(worldSpawn.getBlockX() + " " + worldSpawn.getBlockY() + " " + worldSpawn.getBlockZ(), ChatColor.AQUA).getTextComponent());
				} else if (Bukkit.getServer().getPlayer(args[0]) != null) {
					if (!hunter.getInventory().contains(Material.COMPASS)) hunter.getInventory().setItemInMainHand(new ItemStack(Material.COMPASS, 1));
					Player runner = Bukkit.getServer().getPlayer(args[0]);
					huntingMap.put(hunter, runner);
					hunterComponents.add(new ChatComponent("Tracking", ChatColor.GREEN).getTextComponent());
					hunterComponents.add(new TextComponent(": "));
					hunterComponents.add(new ChatComponent(runner.getName(), ChatColor.YELLOW).getTextComponent());
					hunterComponents.add(new TextComponent(", "));
					hunterComponents.add(new ChatComponent("* " + runner.getLocation().getBlockY() + " *", ChatColor.AQUA).getTextComponent());
					
					runnerComponents.add(new ChatComponent("Tracked by", ChatColor.RED).getTextComponent());
					runnerComponents.add(new TextComponent(": "));
					runnerComponents.add(new ChatComponent(hunter.getName(), ChatColor.YELLOW).getTextComponent());
				} else {
					errorMessage.setText("Invalid argument for command");
					validArgs.setText("track ");
					invalidArgs.setText(args[0]);
					validCommand = false;
				}
			} else {
				if (args[0].equals("spawn") || Bukkit.getServer().getPlayer(args[0]) != null) {
					errorMessage.setText("Unnecessary argument(s) for command");
					validArgs.setText("track " + args[0] + " ");
					String unnecessaryArgs = args[1];
					for (int i = 2; i < args.length; i++) {
						unnecessaryArgs += " " + args[i];
					}
					invalidArgs.setText(unnecessaryArgs);
					
				} else {
					errorMessage.setText("Invalid arguments for command");
					validArgs.setText("track ");
					String allArgs = args[0];
					for (int i = 1; i < args.length; i++) {
						allArgs += " " + args[i];
					}
					invalidArgs.setText(allArgs);
				}
				validCommand = false;
			}
			if (validCommand) {
				hunter.spigot().sendMessage(hunterComponents.toArray(new TextComponent[hunterComponents.size()]));
				if (runnerComponents.size() != 0) {huntingMap.get(hunter).spigot().sendMessage(runnerComponents.toArray(new TextComponent[runnerComponents.size()]));}
			} else {
				hunter.spigot().sendMessage(errorMessage);
				hunter.spigot().sendMessage(new TextComponent[] {validArgs, invalidArgs, HERE});
			}
		} else {
			System.out.println("\"track\" is a player-based command");
			return false;
		}
		return validCommand;
	}

}
