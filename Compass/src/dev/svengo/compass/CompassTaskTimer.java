package dev.svengo.compass;

import static dev.svengo.compass.CompassCommand.HUNTING_MAP;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

// continually updates
public class CompassTaskTimer extends BukkitRunnable {
	
	public static void start(JavaPlugin plugin) {
		// updates every 10ms (100Hz)
		new CompassTaskTimer().runTaskTimer(plugin, 0, 10);
	}

	@Override
	public void run() {
		synchronized (HUNTING_MAP) {
			HUNTING_MAP.entrySet().stream().forEach(entry -> {
				Player hunter = entry.getKey();
				Player runner = entry.getValue();
				hunter.setCompassTarget(runner.getLocation());
			});
		}
	}

}