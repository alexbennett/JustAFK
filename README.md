I am no longer supporting this plugin. Feel free to check out forks that other developers have made or fork it yourself for updates.

---

**JustAFK** brings a simple - yet powerful - AFK (away from keyboard) plugin to Bukkit servers.

![JustAFK](http://alexben.net/projects/bukkit/JustAFK/justafk_logo.png)

Installation
============

To install **JustAFK**, simply drop the jar file into your server's plugin directory and reload! A configuration file will be automatically created which you can edit to fine-tune your own installation.


Features
========

* Optional, automatic kicking of AFK players
* Outwits methods used to circumvent automatic AFK
* Configurable AFK time limit
* Configurable kick message
* Players are automatically hidden from others when going AFK
* Automatic detection of players returning from AFK *(e.g. when moving or chatting)*
* Set a status when going AFK
* Fully customizable messages via the generated `localization.yml` file


Commands
========

* **/afk**: Sets yourself to away, making your player invisible and broadcasting a message to the server.
* **/afk [reason]**: Sets yourself to away as above, but also includes a reason.
* **/whosafk**: Shows you a list of who is currently set to away on the server.
* **/justafk**: Gives you a bit of information about JustAFK.
* **/setafk <player>**: Sets the specified player to away.


Permissions
=========

* **justafk.basic**: The basic permission node which allows for full use of the plugin.
* **justafk.immune**: Makes the player immune to auto-afk and auto-kicking.
* **justafk.admin**: Gives the player access to the admin commands (seen above) included in the plugin.


Configuration
=========

    tagmessages: true # If enabled, all AFK messages will be tagged with "[JustAFK]"
    autokick: true # If enabled, players will be kicked automatically if inactive
    kicktime: 300 # The amount of time in seconds that a player can be AFK before being kicked
    kickreason: Reason # The reason to give when kicking an AFK player.
    movementcheckfreq: 15 # The amount of time in seconds to check player movement for inactivity.


Localization
=========

As of **JustAFK v1.2**, you can completely customize the messages associated with **JustAFK** by editing the generated `localization.yml` file after loading the plugin. You can use it to simply change the way the messages look, or to translate to another language. This _should_ work with most languages, but you may run into some issues with certain character types. If you have any problems with the plugin after doing a translation please let me know in the comments on the plugin [project page](http://dev.bukkit.org/server-mods/justafk/).

If you'd like to contribute your translation for **JustAFK**, please let me know and I'll work with you to get it included as a download alongside the plugin. Thanks!


Miscellaneous
=============

To get support for **JustAFK** or to suggest new features, just leave a comment on the [project page](http://dev.bukkit.org/server-mods/justafk/) and I'll have a look and see what I can do!


![MCStats](http://mcstats.org/signature/JustAFK.png)

[MCStats](http://mcstats.org/plugin/JustAFK) | [Source](http://github.com/alexbennett/Minecraft-JustAFK/) | [Censored Software](http://www.censoredsoftware.com/)
