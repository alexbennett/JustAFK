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

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class JConfig
{
    // Define variables
    private FileConfiguration config = null;
    private static JustAFK plugin = null;

    public static void initialize(JustAFK instance)
    {
        plugin = instance;

        Configuration mainConfig = plugin.getConfig().getRoot();
        mainConfig.options().copyDefaults(true);
        plugin.saveConfig();
    }

    public static int getSettingInt(String id)
    {
        if(plugin.getConfig().isInt(id))
        {
            return plugin.getConfig().getInt(id);
        }
        else return -1;
    }

    public static String getSettingString(String id)
    {
        if(plugin.getConfig().isString(id))
        {
            return plugin.getConfig().getString(id);
        }
        else return null;
    }

    public static boolean getSettingBoolean(String id)
    {
        return !plugin.getConfig().isBoolean(id) || plugin.getConfig().getBoolean(id);
    }

    public static double getSettingDouble(String id)
    {
        if(plugin.getConfig().isDouble(id))
        {
            return plugin.getConfig().getDouble(id);
        }
        else return -1;
    }

    public static ArrayList<String> getSettingArrayListString(String id)
    {
        ArrayList<String> strings = new ArrayList<String>();
        if(plugin.getConfig().isList(id))
        {
            for(String s : plugin.getConfig().getStringList(id))
                strings.add(s);
            return strings;
        }
        else return null;
    }
}
