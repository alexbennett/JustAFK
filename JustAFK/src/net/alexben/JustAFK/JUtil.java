package net.alexben.JustAFK;

import java.util.logging.Logger;

public class JUtil
{
    // Define variables
    private static final Logger log = Logger.getLogger("Minecraft");
    private static final String pluginName = "JustAFK";

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
}
