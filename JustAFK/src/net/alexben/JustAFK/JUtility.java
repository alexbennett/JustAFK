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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    private static final String pluginName = ChatColor.RED + "JustAFK" + ChatColor.RESET;
    private static final HashMap<OfflinePlayer, HashMap<String, Object>> save = new HashMap<OfflinePlayer, HashMap<String, Object>>();

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
        if(type.equalsIgnoreCase("info")) log.info("[" + pluginName + "] " + msg);
        else if(type.equalsIgnoreCase("warning")) log.warning("[" + pluginName + "] " + msg);
        else if(type.equalsIgnoreCase("severe")) log.severe("[" + pluginName + "] " + msg);
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
     * Sets the <code>player</code>'s away status to <code>boolean</code>.
     *
     * @param player the player to update.
     * @param away the away status to set.
     */
    public static void setAway(final Player player, boolean away)
    {
        // Define variables
        String status = null;
        if(away) status = "has gone";
        else status = "is no longer";

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
        if(away && save.get(player).containsKey("afkmessage"))
        {
            serverMsg(ChatColor.RED + player.getDisplayName() + " " + status + " AFK." + ChatColor.GRAY + " (" + save.get(player).get("afkmessage") + ChatColor.RESET + ")");
        }
        else
        {
            serverMsg(ChatColor.RED + player.getDisplayName() + " " + status + " AFK.");
        }

        // If auto-kick is enabled then start the delayed task
        if(away && JConfig.getSettingBoolean("autokick"))
        {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    if(!isAway(player)) return;
                    removeAllData(player);
                    for(Player onlinePlayer : Bukkit.getOnlinePlayers()) onlinePlayer.showPlayer(player);
                    player.kickPlayer(JConfig.getSettingString("kickreason"));
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
        saveData(player, "afkmessage", msg);
    }

    /**
     * Returns true if the <code>player</code> is currently AFK.
     *
     * @param player the player to check.
     * @return boolean
     */
    public static boolean isAway(Player player)
    {
        for(Player awayPlayer : getAwayPlayers())
        {
            if(player.equals(awayPlayer)) return true;
        }

        return false;
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
            if(save.containsKey(player) && save.get(player).containsKey("isafk") && save.get(player).get("isafk").equals(true)) players.add(player);
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
     * Returns true if <code>player</code> has the permission called <code>permission</code>
     * or is an OP.
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
     * Saves <code>data</code> under the key <code>name</code> to <code>player</code>.
     *
     * @param player the player to save data to.
     * @param name the name of the data.
     * @param data the data to save.
     */
    public static void saveData(OfflinePlayer player, String name, Object data)
    {
        // Create new save for the player if one doesn't already exist
        if(!save.containsKey(player))
        {
            save.put(player, new HashMap<String, Object>());
        }

        // Save the data
        save.get(player).put(name.toLowerCase(), data);
    }

    /**
     * Removes the data with the key <code>name</code> from <code>player</code>.
     *
     * @param player the player to remove data from.
     * @param name the key of the data to remove.
     */
    public static void removeData(OfflinePlayer player, String name)
    {
        if(save.containsKey(player)) save.get(player).remove(name.toLowerCase());
    }

    /**
     * Removes all data for the <code>player</code>.
     *
     * @param player the player whose data to remove.
     */
    public static void removeAllData(OfflinePlayer player)
    {
        save.remove(player);
    }
}
