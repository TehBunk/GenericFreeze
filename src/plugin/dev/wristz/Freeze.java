package plugin.dev.wristz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.Setter;
import plugin.dev.wristz.commands.FreezeCommand;
import plugin.dev.wristz.listeners.FrozenPlayerListener;

/**
 * This is a Generic Freezing plugin created by WristzPvP or Wristz. MCM: (
 * http://www.mc-market.org/members/48315/ )
 * 
 * This project was created on the 06/17/2017
 */
public class Freeze extends JavaPlugin {

	@Getter @Setter
	private static Freeze instance;
	@Getter @Setter
	private Collection<UUID> frozenPlayers;
	@Getter @Setter
	private boolean serverFrozen = false;

	@Override // Called on server reload / start.
	public void onEnable() {
		setInstance(this);
		load();
	}

	@Override // Called on server reload / stop.
	public void onDisable() {
		unload();
	}

	/**
	 * This is the method called when the plugin first enables. Therefore, it
	 * contains all of the nessicary code to start the plugin.
	 */
	public void load() {
		Bukkit.getConsoleSender().sendMessage(format("&aGenericFreeze by WristzPvP has been enabled!"));
		setFrozenPlayers(new ArrayList<UUID>());
		new BukkitRunnable() {
			public void run() {
				new Config();
			}
		}.runTaskLaterAsynchronously(instance, 5L);
		new FrozenPlayerListener();
		new FreezeCommand();
	}

	/**
	 * This is the method called when the plugin disables or reloads. Therefore,
	 * it contains all of the nessicary code to stop the plugin.
	 */
	public void unload() {
		Bukkit.getConsoleSender().sendMessage(format("&aGenericFreeze by WristzPvP has been enabled!"));
		setFrozenPlayers(null);
		setInstance(null);
	}

	/**
	 * This method takes unformatted text and translates the color-codes to
	 * actual colors.
	 */
	public static String format(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	/**
	 * 
	 * @author TehBunk (Wristz)
	 * Backend configuration stuff. (Some spigot's don't like the saveDefaultConfig() method)
	 *
	 */
	public class Config {
		
		@Getter @Setter
		private File dir = getDataFolder(), config = null;
		
		public Config() {
			if (!dir.exists()) 
				dir.mkdir();
				
			setConfig(new File(dir, "config.yml"));
			
			boolean created = false;
			
			if (!getConfig().exists()) {
				try {
					
					getConfig().createNewFile();
			
					YamlConfiguration cfg = YamlConfiguration.loadConfiguration(getConfig());
					
					for (CONFIGURATION selection : CONFIGURATION.values())
						cfg.set(selection.toString(), selection.getString());
					
					cfg.save(getConfig());
					created = true;
				
				} catch (IOException e) {
					Bukkit.getConsoleSender().sendMessage("&a----------------------[IOException Error]----------------------");
					e.printStackTrace();
					Bukkit.getConsoleSender().sendMessage("&a----------------------[IOException Error]----------------------");
				}
				
			}
			
			if (!created) {
				
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(getConfig());
				
				for (CONFIGURATION selection : CONFIGURATION.values())
					if (cfg.getString(selection.toString()) != selection.getString())
						selection.setString(cfg.getString(selection.toString()));
				
				
			}
			
			
			
		}
		
	}

	/**
	 * 
	 * @author TehBunk (Wristz)
	 *
	 */
	public enum CONFIGURATION {
		CONSOLE_SENDER("&cThat feature is for players only!"), NO_PERMISSION("&cYou do not have permission to do that!"), TARGET_NULL("&cThat player is not online, or does not exist!"), 
		TARGET_FROZEN("&eYou have &6successfully &efrozen &6!target&e!"), TARGET_UNFROZEN("&eYou have &6successfully &eunfrozen &6!target&e!"), 
		SERVER_FROZEN("&eThe server has been &6frozen &eby &6!player&e!"), SERVER_UNFROZEN("&eThe server has been &6unfrozen &eby &6!player&e!"),
		FROZEN_MESSAGE_ONE("&4&lYou have been frozen!"),FROZEN_MESSAGE_TWO(""),FROZEN_MESSAGE_THREE("&cDo not log out or you will be &lBANNED&c!"),FROZEN_MESSAGE_FOUR("&7Join ts.mycoolserver.org (you have 3 mins)."),FROZEN_MESSAGE_FIVE(""),FROZEN_MESSAGE_SIX(""),
		SERVER_FROZEN_MESSAGE_ONE("&4&lThe server has been frozen!"),SERVER_FROZEN_MESSAGE_TWO(""),SERVER_FROZEN_MESSAGE_THREE("&cYou may logout, you will &lNOT&c be banned!"),SERVER_FROZEN_MESSAGE_FOUR(""),SERVER_FROZEN_MESSAGE_FIVE(""),SERVER_FROZEN_MESSAGE_SIX("");
	
		@Getter @Setter
		private String string;

		private CONFIGURATION(String string) {
			setString(string);
		}

	}

}
