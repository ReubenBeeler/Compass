package dev.svengo.compass;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CompassTaskTimer extends BukkitRunnable {

	@Override
	public void run() {
		for (Map.Entry<Player, Player> entry : CompassCommand.huntingMap.entrySet()) {
			Player hunter = entry.getKey();
			Player runner = entry.getValue();
			hunter.setCompassTarget(runner.getLocation());
		}

	}

}