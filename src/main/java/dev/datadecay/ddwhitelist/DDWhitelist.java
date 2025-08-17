package dev.datadecay.ddwhitelist;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import dev.datadecay.ddwhitelist.commands.DDWhitelistCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import me.clip.placeholderapi.PlaceholderAPI;


public final class DDWhitelist extends JavaPlugin implements Listener {

    private boolean whitelistEnabled;
    private String whitelistMessage;
    private String kickMessage;
    private String serverName;
    private String globalPerm = "ddwhitelist.allowjoin";
    private String serverPerm = "ddwhitelist.allowjoin"; // Set later

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigValues();
        this.getCommand("ddwhitelist").setExecutor(new DDWhitelistCommand(this));
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("DDWhitelist enabled. Whitelist is " + (whitelistEnabled ? "enabled" : "disabled"));
    }

    @Override
    public void onDisable() {
        getLogger().info("DDWhitelist disabled.");
    }

    public void loadConfigValues() {
        FileConfiguration config = getConfig();
        if (!config.contains("whitelist-enabled")) {
            config.set("whitelist-enabled", false);
        }
        whitelistEnabled = config.getBoolean("whitelist-enabled");
        if (!config.contains("whitelist-message")) {
            config.set("whitelist-message", "&c%player_name% tried to join but is not whitelisted!");
        }
        whitelistMessage = config.getString("whitelist-message");
        if (!config.contains("kick-message")) {
            config.set("kick-message", "This server is being worked on.");
        }
        kickMessage = config.getString("kick-message");
        if (!config.contains("server-name")) {
            config.set("server-name", "survival");
        }
        serverName = config.getString("server-name");
        //end config portion
        
        serverPerm = "ddwhitelist.allowjoin." + serverName;
    }


    public boolean isWhitelistEnabled() {
        return whitelistEnabled;
    }
    
    public String getWhitelistMessage() {
        return whitelistMessage;
    }

    public String getKickMessage() {
        return kickMessage;
    }
    
    public String getServerName() {
        return serverName;
    }

    public void setWhitelistEnabled(boolean enabled) {
        whitelistEnabled = enabled;
        getConfig().set("whitelist-enabled", enabled);
        saveConfig();
    }
    
    public String getGlobalPerm() {
    	return globalPerm;
    }
    
    public String getServerPerm() {
    	return serverPerm;
    }
    
}

