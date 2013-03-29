/*
 * Copyright (c) 2013 Alex Bennett
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.alexben.JustAFK;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class JUtility
{
	// Define variables
	private static JustAFK plugin = null;
	private static final Logger log = Logger.getLogger("Minecraft");
	private static final String pluginName = ChatColor.GREEN + "JustAFK" + ChatColor.RESET;
	private static final String pluginNameNoColor = "JustAFK";
	private static final HashMap<String, HashMap<String, Object>> save = new HashMap<String, HashMap<String, Object>>();

	public static void initialize(JustAFK instance)
	{
		plugin = instance;
	}

	/**
	 * Returns the logger for the current plugin instance.
	 * 
	 * @return the logger instance.
	 */
	public static Logger getLog()
	{
		return log;
	}

	/**
	 * Sends <code>msg</code> to the console with type <code>type</code>.
	 * 
	 * @param type the type of message.
	 * @param msg the message to send.
	 */
	public static void log(String type, String msg)
	{
		if(type.equalsIgnoreCase("info")) log.info("[" + pluginNameNoColor + "] " + msg);
		else if(type.equalsIgnoreCase("warning")) log.warning("[" + pluginNameNoColor + "] " + msg);
		else if(type.equalsIgnoreCase("severe")) log.severe("[" + pluginNameNoColor + "] " + msg);
	}

	/**
	 * Sends a server-wide message.
	 * 
	 * @param msg the message to send.
	 */
	public static void serverMsg(String msg)
	{
		if(JConfig.getSettingBoolean("tagmessages"))
		{
			Bukkit.getServer().broadcastMessage("[" + pluginName + "] " + msg);
		}
		else Bukkit.getServer().broadcastMessage(msg);

	}

	/**
	 * Sends a message to a player prepended with the plugin name.
	 * 
	 * @param player the player to message.
	 * @param msg the message to send.
	 */
	public static void sendMessage(Player player, String msg)
	{
		if(JConfig.getSettingBoolean("tagmessages"))
		{
			player.sendMessage("[" + pluginName + "] " + msg);
		}
		else
		{
			player.sendMessage(msg);
		}
	}

	/**
	 * Sets the <code>player</code>'s away status to <code>boolean</code>.
	 * 
	 * @param player the player to update.
	 * @param away the away status to set.
	 */
	public static void setAway(final Player player, boolean away)
	{
		// Hide or display the player based on their away status.
		if(away)
		{
			for(Player onlinePlayer : Bukkit.getOnlinePlayers())
			{
				onlinePlayer.hidePlayer(player);
			}
		}
		else
		{
			removeAllData(player);

			for(Player onlinePlayer : Bukkit.getOnlinePlayers())
			{
				onlinePlayer.showPlayer(player);
			}
		}

		// Save their availability
		saveData(player, "isafk", away);

		// Send the server-wide message
		if(away)
		{
			if(getData(player, "message") != null)
			{
				serverMsg(ChatColor.RED + StringEscapeUtils.unescapeJava(JustAFK.language.getConfig().getString("public_away_reason").replace("{name}", player.getDisplayName()).replace("{message}", getData(player, "message").toString())));
			}
			else
			{
				serverMsg(ChatColor.RED + StringEscapeUtils.unescapeJava(JustAFK.language.getConfig().getString("public_away_generic").replace("{name}", player.getDisplayName())));
			}
		}
		else
		{
			serverMsg(ChatColor.RED + StringEscapeUtils.unescapeJava(JustAFK.language.getConfig().getString("public_return").replace("{name}", player.getDisplayName())));
		}

		// If auto-kick is enabled then start the delayed task
		if(away && JConfig.getSettingBoolean("autokick") && !hasPermission(player, "justafk.immune"))
		{
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
			{
				@Override
				public void run()
				{
					if(!isAway(player)) return;

					// Remove their data, show them, and then finally kick them
					removeAllData(player);
					for(Player onlinePlayer : Bukkit.getOnlinePlayers())
						onlinePlayer.showPlayer(player);
					player.kickPlayer(ChatColor.translateAlternateColorCodes('&', JConfig.getSettingString("kickreason")));

					// Log it to the console
					log("info", StringEscapeUtils.unescapeJava(JustAFK.language.getConfig().getString("auto_kick").replace("{name}", player.getDisplayName())));
				}
			}, JConfig.getSettingInt("kicktime") * 20);
		}
	}

	/**
	 * Sets the <code>player</code>'s away message to <code>msg</code>.
	 * 
	 * @param player the player to update.
	 * @param msg the message to
	 */
	public static void setAwayMessage(Player player, String msg)
	{
		saveData(player, "message", msg);
	}

	/**
	 * Returns true if the <code>player</code> is currently AFK.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean isAway(Player player)
	{
		return getAwayPlayers().contains(player);
	}

	/**
	 * Returns an ArrayList of all currently away players.
	 * 
	 * @return ArrayList
	 */
	public static ArrayList<Player> getAwayPlayers()
	{
		ArrayList<Player> players = new ArrayList<Player>();

		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(getData(player, "isafk") != null && getData(player, "isafk").equals(true)) players.add(player);
		}

		return players;
	}

	/**
	 * Returns true if <code>player</code> has the permission called <code>permission</code>.
	 * 
	 * @param player the player to check.
	 * @param permission the permission to check for.
	 * @return boolean
	 */
	public static boolean hasPermission(OfflinePlayer player, String permission)
	{
		return player == null || player.getPlayer().hasPermission(permission);
	}

	/**
	 * Returns true if <code>player</code> has the permission called <code>permission</code> or is an OP.
	 * 
	 * @param player the player to check.
	 * @param permission the permission to check for.
	 * @return boolean
	 */
	public static boolean hasPermissionOrOP(OfflinePlayer player, String permission)
	{
		return player == null || player.isOp() || player.getPlayer().hasPermission(permission);
	}

	/**
	 * Checks movement for all online players and marks them as AFK if need-be.
	 */
	public static void checkMovement()
	{
		// Get all online players
		for(Player player : Bukkit.getOnlinePlayers())
		{
			// Make sure they aren't already away
			if(!isAway(player) && !hasPermissionOrOP(player, "justafk.immune"))
			{
				// Define variables
				boolean moved = false;

				// Check their movement
				if(getData(player, "position") != null)
				{
					if(((Location) getData(player, "position")).getYaw() != player.getLocation().getYaw() || ((Location) getData(player, "position")).getPitch() != player.getLocation().getPitch()) moved = true;
				}

				if(!moved)
				{
					// They player is AFK, set their status
					setAway(player, true);

					// Message them
					player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + StringEscapeUtils.unescapeJava(JustAFK.language.getConfig().getString("auto_away")));
				}

				saveData(player, "position", player.getLocation());
			}
		}
	}

	/**
	 * Saves <code>data</code> under the key <code>name</code> to <code>player</code>.
	 * 
	 * @param player the player to save data to.
	 * @param name the name of the data.
	 * @param data the data to save.
	 */
	public static void saveData(OfflinePlayer player, String name, Object data)
	{
		// Create new save for the player if one doesn't already exist
		if(!save.containsKey(player.getName()))
		{
			save.put(player.getName(), new HashMap<String, Object>());
		}

		// Prepend the data with "jafk" to avoid plugin collisions and save the data
		save.get(player.getName()).put(name.toLowerCase(), data);
	}

	/**
	 * Returns the data with the key <code>name</code> from <code>player</code>'s HashMap.
	 * 
	 * @param player the player to check.
	 * @param name the key to grab.
	 */
	public static Object getData(OfflinePlayer player, String name)
	{
		if(save.containsKey(player.getName()) && save.get(player.getName()).containsKey(name))
		{
			return save.get(player.getName()).get(name);
		}
		return null;
	}

	/**
	 * Removes the data with the key <code>name</code> from <code>player</code>.
	 * 
	 * @param player the player to remove data from.
	 * @param name the key of the data to remove.
	 */
	public static void removeData(OfflinePlayer player, String name)
	{
		if(save.containsKey(player.getName())) save.get(player.getName()).remove(name.toLowerCase());
	}

	/**
	 * Removes all data for the <code>player</code>.
	 * 
	 * @param player the player whose data to remove.
	 */
	public static void removeAllData(OfflinePlayer player)
	{
		save.remove(player.getName());
	}
}
