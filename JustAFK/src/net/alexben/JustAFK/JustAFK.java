package net.alexben.JustAFK;

import org.bukkit.plugin.java.JavaPlugin;

public class JustAFK extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        JUtil.log("info", "Enabled!");
    }

    @Override
    public void onDisable()
    {
        JUtil.log("info", "Disabled!");
    }
}
