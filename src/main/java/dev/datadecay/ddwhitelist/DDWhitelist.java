package dev.datadecay.ddwhitelist;

import dev.datadecay.ddwhitelist.commands.DDWhitelistCommand;
import dev.datadecay.ddwhitelist.listeners.PlayerListener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public final class DDWhitelist extends JavaPlugin implements Listener {

    private boolean whitelistEnabled;
    private String whitelistMessage;
    private String kickMessage;
    private String serverName;
    private String globalPerm = "ddwhitelist.allowjoin";
    private String togglePerm = "ddwhitelist.toggle";
    private String serverPerm = "";
    

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigValues();
        this.getCommand("ddwhitelist").setExecutor(new DDWhitelistCommand(this));
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
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
    
    public String getTogglePerm() {
    	return togglePerm;
    }
    
    public String getVersion() {
    	return getPluginMeta().getVersion();
    }
    
}

