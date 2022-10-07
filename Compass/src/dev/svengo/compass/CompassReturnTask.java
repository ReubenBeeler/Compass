package dev.svengo.compass;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class CompassReturnTask extends BukkitRunnable {
	
	private PlayerDropItemEvent event;
	
	public CompassReturnTask(PlayerDropItemEvent event) {
		this.event = event;
	}

	@Override
	public void run() {
		event.getPlayer().getInventory().addItem(new ItemStack(Material.COMPASS));
	}
}
