package dev.datadecay.ddwhitelist;

import dev.datadecay.ddwhitelist.commands.DDWhitelistCommand;
import dev.datadecay.ddwhitelist.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class DDWhitelist extends JavaPlugin {

    private static DDWhitelist instance;
    private boolean whitelistEnabled;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        whitelistEnabled = getConfig().getBoolean("enabled", false);

        // Register command + events
        getCommand("ddwhitelist").setExecutor(new DDWhitelistCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        getLogger().info("DDWhitelist enabled. Whitelist is " + (whitelistEnabled ? "ON" : "OFF"));
    }

    @Override
    public void onDisable() {
        getLogger().info("DDWhitelist disabled.");
    }

    public static DDWhitelist getInstance() {
        return instance;
    }

    public boolean isWhitelistEnabled() {
        return whitelistEnabled;
    }

    public void setWhitelistEnabled(boolean enabled) {
        this.whitelistEnabled = enabled;
        getConfig().set("enabled", enabled);
        saveConfig();
    }
}
