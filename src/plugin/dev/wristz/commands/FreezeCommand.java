package plugin.dev.wristz.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import plugin.dev.wristz.Freeze;
import plugin.dev.wristz.Freeze.CONFIGURATION;

public class FreezeCommand implements CommandExecutor {

	public FreezeCommand() {
		Freeze.getInstance().getCommand("Freeze").setExecutor(this);
		Freeze.getInstance().getCommand("Freeze").setAliases(Arrays.asList("ss", "halt"));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!sender.hasPermission("GenericFreeze.Freeze")) {
			sender.sendMessage(Freeze.format(CONFIGURATION.NO_PERMISSION.getString()));
			return false;
		}

		if (args.length == 0) {
			sender.sendMessage(Freeze.format("&c/Freeze <player, server>."));
			return false;
		}

		label = args[0];

		if (label.equalsIgnoreCase("server") || label.equalsIgnoreCase("all")) {
			boolean freeze = (Freeze.getInstance().isServerFrozen() ? false : true);
			Bukkit.broadcastMessage(Freeze.format(
					(freeze ? CONFIGURATION.SERVER_FROZEN.getString() : CONFIGURATION.SERVER_UNFROZEN.getString()))
					.replace("!player", sender.getName()));
			Freeze.getInstance().setServerFrozen(freeze);
			return true;
		}

		Player target = Bukkit.getPlayer(label);

		if (target == null) {
			sender.sendMessage(Freeze.format(CONFIGURATION.TARGET_NULL.getString()));
			return false;
		}

		if (target.hasPermission("GenericFreeze.Bypass")) {
			sender.sendMessage(Freeze.format("&cThat player has the bypass permission!"));
			return false;
		}

		if (Freeze.getInstance().getFrozenPlayers().contains(target.getUniqueId())) {
			Freeze.getInstance().getFrozenPlayers().remove(target.getUniqueId());
			sender.sendMessage(Freeze.format(CONFIGURATION.TARGET_UNFROZEN.getString()
					.replace("!player", sender.getName()).replace("!target", target.getName())));
		} else {
			Freeze.getInstance().getFrozenPlayers().add(target.getUniqueId());
			sender.sendMessage(Freeze.format(CONFIGURATION.TARGET_FROZEN.getString()
					.replace("!player", sender.getName()).replace("!target", target.getName())));
		}
		return true;
	}

}
