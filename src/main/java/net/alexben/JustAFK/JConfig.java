package net.alexben.JustAFK;

import java.util.ArrayList;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

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
