package dev.svengo.compass;

import static dev.svengo.compass.TextComponentUtil.getTextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

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

	public static final HashMap<Player, Player> HUNTING_MAP = new HashMap<>();
	
	// null means hunter is not tracking anyone (technically not a hunter but whatever)
	private static final Player getRunner(Player hunter) {
		synchronized (HUNTING_MAP) {
			return HUNTING_MAP.get(hunter);
		}
	}
	
	public static TextComponent errorMessage = getTextComponent(null, ChatColor.RED);
	public static TextComponent validArgs = getTextComponent(null, ChatColor.GRAY);
	public static TextComponent invalidArgs = getTextComponent(null, ChatColor.RED, true);
	public static final TextComponent HERE = getTextComponent("<--[HERE]", ChatColor.RED);

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		final ArrayList<TextComponent> hunterComponents = new ArrayList<>();
		final ArrayList<TextComponent> runnerComponents = new ArrayList<>();
		
		final Consumer<Player> addTrackingComponents = (runner) -> {
			hunterComponents.add(getTextComponent("Tracking", ChatColor.GREEN));
			hunterComponents.add(new TextComponent(": "));
			hunterComponents.add(getTextComponent(runner.getName(), ChatColor.YELLOW));
			hunterComponents.add(new TextComponent(", "));
			hunterComponents.add(getTextComponent("* " + runner.getLocation().getBlockY() + " *", ChatColor.AQUA));
		};
		
		boolean validCommand = true;
		if (sender instanceof Player) {
			Player hunter = (Player) sender;
			if (args.length == 0) {
				final Player runner = getRunner(hunter);
				if (runner != null) {
					addTrackingComponents.accept(runner);
				} else {
					Location worldSpawn = hunter.getWorld().getSpawnLocation();
					hunterComponents.add(new TextComponent("Tracking world spawn: "));
					hunterComponents.add(getTextComponent(worldSpawn.getBlockX() + " " + worldSpawn.getBlockY() + " " + worldSpawn.getBlockZ(), ChatColor.AQUA));
				}
			} else if (args.length == 1) {
				if (args[0].equals("spawn")) {
					synchronized (HUNTING_MAP) {
						HUNTING_MAP.remove(hunter);
					}
					if (!hunter.getInventory().contains(Material.COMPASS)) hunter.getInventory().setItemInMainHand(new ItemStack(Material.COMPASS, 1));
					Location worldSpawn = hunter.getWorld().getSpawnLocation();
					hunter.setCompassTarget(worldSpawn);
					hunterComponents.add(new TextComponent("Tracking world spawn: "));
					hunterComponents.add(getTextComponent(worldSpawn.getBlockX() + " " + worldSpawn.getBlockY() + " " + worldSpawn.getBlockZ(), ChatColor.AQUA));
				} else if (Bukkit.getServer().getPlayer(args[0]) != null) {
					if (!hunter.getInventory().contains(Material.COMPASS)) hunter.getInventory().setItemInMainHand(new ItemStack(Material.COMPASS, 1));
					Player runner = Bukkit.getServer().getPlayer(args[0]);
					synchronized (HUNTING_MAP) {
						HUNTING_MAP.put(hunter, runner);
					}
					addTrackingComponents.accept(runner);
					
					runnerComponents.add(getTextComponent("Tracked by", ChatColor.RED));
					runnerComponents.add(new TextComponent(": "));
					runnerComponents.add(getTextComponent(hunter.getName(), ChatColor.YELLOW));
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
					StringBuilder allArgs = new StringBuilder(args[0]);
					for (int i = 1; i < args.length; ++i) {
						allArgs.append(' ').append(args[i]);
					}
					invalidArgs.setText(allArgs.toString());
				}
				validCommand = false;
			}
			if (validCommand) {
				hunter.spigot().sendMessage(hunterComponents.toArray(new TextComponent[0]));
				if (runnerComponents.size() != 0) {
					final Player runner = getRunner(hunter);
					runner.spigot().sendMessage(runnerComponents.toArray(new TextComponent[0]));
				}
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
