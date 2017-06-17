package plugin.dev.wristz.listeners;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import plugin.dev.wristz.Freeze;
import plugin.dev.wristz.Freeze.CONFIGURATION;

public class FrozenPlayerListener implements Listener {

	public FrozenPlayerListener() {
		Bukkit.getPluginManager().registerEvents(this, Freeze.getInstance());
		runMessageTask();
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public void onPlayerQuit(PlayerQuitEvent ev) {
		/*
		 * You can add a auto ban system here. I just felt to lazy to add it to
		 * config.
		 */
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public void onPlayerMove(PlayerMoveEvent ev) {
		Player player = ev.getPlayer();

		if (player.hasPermission("GenericFreeze.Bypass"))
			return;

		if (!Freeze.getInstance().getFrozenPlayers().contains(player.getUniqueId())
				&& !Freeze.getInstance().isServerFrozen())
			return;

		if (ev.getTo().getBlockX() != ev.getFrom().getBlockX() || ev.getTo().getBlockZ() != ev.getFrom().getBlockZ())
			ev.setTo(ev.getFrom());

	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public void onBlockBreak(BlockBreakEvent ev) {
		Player player = ev.getPlayer();

		if (player.hasPermission("GenericFreeze.Bypass"))
			return;

		if (Freeze.getInstance().getFrozenPlayers().contains(player.getUniqueId())
				|| Freeze.getInstance().isServerFrozen())
			ev.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public void onBlockPlace(BlockPlaceEvent ev) {
		Player player = ev.getPlayer();

		if (player.hasPermission("GenericFreeze.Bypass"))
			return;

		if (Freeze.getInstance().getFrozenPlayers().contains(player.getUniqueId())
				|| Freeze.getInstance().isServerFrozen())
			ev.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public void onItemDrop(PlayerDropItemEvent ev) {
		Player player = ev.getPlayer();

		if (player.hasPermission("GenericFreeze.Bypass"))
			return;

		if (Freeze.getInstance().getFrozenPlayers().contains(player.getUniqueId())
				|| Freeze.getInstance().isServerFrozen())
			ev.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public void onItemPickup(PlayerPickupItemEvent ev) {
		Player player = ev.getPlayer();

		if (player.hasPermission("GenericFreeze.Bypass"))
			return;

		if (Freeze.getInstance().getFrozenPlayers().contains(player.getUniqueId())
				|| Freeze.getInstance().isServerFrozen())
			ev.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public void onPlayerInteract(PlayerInteractEvent ev) {
		Player player = ev.getPlayer();

		if (player.hasPermission("GenericFreeze.Bypass"))
			return;

		if (Freeze.getInstance().getFrozenPlayers().contains(player.getUniqueId())
				|| Freeze.getInstance().isServerFrozen())
			ev.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public void onPlayerInteractEntity(EntityDamageByEntityEvent ev) {

		if (!(ev.getEntity() instanceof Player))
			return;

		Player player = (Player) ev.getEntity();

		if (Freeze.getInstance().getFrozenPlayers().contains(player.getUniqueId())
				|| Freeze.getInstance().isServerFrozen())
			ev.setCancelled(true);

		if (ev.getDamager() instanceof Player)
			if (Freeze.getInstance().getFrozenPlayers().contains((player = ((Player) ev.getDamager())).getUniqueId())
					|| Freeze.getInstance().isServerFrozen())
				ev.setCancelled(true);

	}

	@SuppressWarnings("deprecation")
	private void runMessageTask() {
		new BukkitRunnable() {
			@Override
			public void run() {

				Collection<UUID> uuids = Freeze.getInstance().getFrozenPlayers();

				for (UUID uuid : uuids)
					if (Bukkit.getPlayer(uuid) != null) {
						Player player = Bukkit.getPlayer(uuid);
						player.sendMessage(Freeze.format(CONFIGURATION.FROZEN_MESSAGE_ONE.getString()));
						player.sendMessage(Freeze.format(CONFIGURATION.FROZEN_MESSAGE_TWO.getString()));
						player.sendMessage(Freeze.format(CONFIGURATION.FROZEN_MESSAGE_THREE.getString()));
						player.sendMessage(Freeze.format(CONFIGURATION.FROZEN_MESSAGE_FOUR.getString()));
						player.sendMessage(Freeze.format(CONFIGURATION.FROZEN_MESSAGE_FIVE.getString()));
						player.sendMessage(Freeze.format(CONFIGURATION.FROZEN_MESSAGE_SIX.getString()));
					}

				if (Freeze.getInstance().isServerFrozen())
					for (Player player : Bukkit.getOnlinePlayers())
						if (!uuids.contains(player.getUniqueId()) && !player.hasPermission("GenericFreeze.Bypass")) {
							player.sendMessage(Freeze.format(CONFIGURATION.SERVER_FROZEN_MESSAGE_ONE.getString()));
							player.sendMessage(Freeze.format(CONFIGURATION.SERVER_FROZEN_MESSAGE_TWO.getString()));
							player.sendMessage(Freeze.format(CONFIGURATION.SERVER_FROZEN_MESSAGE_THREE.getString()));
							player.sendMessage(Freeze.format(CONFIGURATION.SERVER_FROZEN_MESSAGE_FOUR.getString()));
							player.sendMessage(Freeze.format(CONFIGURATION.SERVER_FROZEN_MESSAGE_FIVE.getString()));
							player.sendMessage(Freeze.format(CONFIGURATION.SERVER_FROZEN_MESSAGE_SIX.getString()));
						}

			}
		}.runTaskTimerAsynchronously(Freeze.getInstance(), 20L, 60L);
	}

}
