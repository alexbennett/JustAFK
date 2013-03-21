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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class JListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        for(Player awayPlayer : JUtility.getAwayPlayers())
        {
            player.hidePlayer(awayPlayer);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();

        if(event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ())
        {
            if(JUtility.isAway(player))
            {
                JUtility.setAway(player, false);
                JUtility.sendMessage(player, ChatColor.AQUA + "You are no longer away.");
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();

        if(JUtility.isAway(player))
        {
            JUtility.setAway(player, false);
            JUtility.sendMessage(player, ChatColor.AQUA + "You are no longer away.");
        }
    }
}
