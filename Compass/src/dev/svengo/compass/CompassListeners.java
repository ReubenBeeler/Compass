package dev.svengo.compass;

import static dev.svengo.compass.CompassCommand.HUNTING_MAP;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class CompassListeners implements Listener {

	@EventHandler
	public void useCompass(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR && event.getItem().getType() == Material.COMPASS) {
			Bukkit.getServer().dispatchCommand(event.getPlayer(), "track");
		}
	}

	// Make sure the player can't put the compass in a TileEntity

	@EventHandler
	public void dropCompass(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (HUNTING_MAP.containsKey(player) && event.getItemDrop().getItemStack().getType() == Material.COMPASS) {
			event.getItemDrop().remove();
			player.getInventory().remove(Material.COMPASS); // do I need this line?
			new BukkitRunnable() {
				@Override
				public void run() {
					player.getInventory().addItem(new ItemStack(Material.COMPASS)); // replaces dropped compass
				}
			}.runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("Compass"), 1);
		}
	}

	@EventHandler
	public void playerRespawn(PlayerRespawnEvent event) {
		if (HUNTING_MAP.containsKey(event.getPlayer())) {
			event.getPlayer().getInventory().addItem(new ItemStack(Material.COMPASS, 1));
		}
	}

	@EventHandler
	public void entityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (HUNTING_MAP.containsKey(player) && player.getHealth() - event.getFinalDamage() <= 0) {
				player.getInventory().remove(Material.COMPASS);
			}
		}
	}

	@EventHandler
	public void playerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		synchronized (HUNTING_MAP) {
			HUNTING_MAP.remove(player);
			HUNTING_MAP.entrySet().stream().filter(entry -> entry.getValue() == player).map(Entry::getKey).forEach(hunter -> {
				Bukkit.getServer().dispatchCommand(hunter, "track spawn");
			});
		}
	}
}
