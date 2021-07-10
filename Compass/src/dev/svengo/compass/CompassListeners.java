package dev.svengo.compass;

import java.util.Map;

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

public class CompassListeners implements Listener {
	
	public static Player prey = null;
	
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
		if (CompassCommand.huntingMap.containsKey(player) && event.getItemDrop().getItemStack().getType() == Material.COMPASS) {
			event.getItemDrop().remove();
			player.getInventory().remove(Material.COMPASS);
			new CompassReturnTask(event).runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("Compass"), 1);
		}
	}
	
	@EventHandler
	public void playerRespawn(PlayerRespawnEvent event) {
		if (CompassCommand.huntingMap.containsKey(event.getPlayer())) {
			event.getPlayer().getInventory().addItem(new ItemStack(Material.COMPASS, 1));
		}
	}
	
	@EventHandler
	public void entityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (CompassCommand.huntingMap.containsKey(player) && player.getHealth() - event.getFinalDamage() <= 0) {
				player.getInventory().remove(Material.COMPASS);
			}
		}
	}
	
	@EventHandler 
	public void playerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		CompassCommand.huntingMap.remove(player);
		for (Map.Entry<Player, Player> entry : CompassCommand.huntingMap.entrySet()) {
			if (entry.getValue() == player) {
				CompassCommand.forceCommand = true;
				Bukkit.getServer().dispatchCommand(entry.getKey(), "track spawn");
				CompassCommand.forceCommand = false;
			}
		}
	}
}
