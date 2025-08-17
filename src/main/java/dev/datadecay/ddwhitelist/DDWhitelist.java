package dev.datadecay.ddwhitelist;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

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
    private String serverPerm = "ddwhitelist.allowjoin"; // Set later, don't worry!

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigValues();

        // Register command
        this.getCommand("ddwhitelist").setExecutor(new DDWhitelistCommand(this));

        // Register events
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("DDWhitelist enabled. Whitelist is " + (whitelistEnabled ? "enabled" : "disabled"));
    }

    @Override
    public void onDisable() {
        getLogger().info("DDWhitelist disabled.");
    }

    public void loadConfigValues() {
        FileConfiguration config = getConfig();
        whitelistEnabled = config.getBoolean("whitelist-enabled", false);
        whitelistMessage = config.getString("whitelist-message", "%player_name% tried to join but is not whitelisted!");
        kickMessage = config.getString("kick-message", "This server is being worked on.");
        serverName = config.getString("server-name", "survival");
        serverPerm = "ddwhitelist.allowjoin." + serverName;
    }


    public boolean isWhitelistEnabled() {
        return whitelistEnabled;
    }

    public void setWhitelistEnabled(boolean enabled) {
        whitelistEnabled = enabled;
        getConfig().set("whitelist-enabled", enabled);
        saveConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (whitelistEnabled && !(player.hasPermission(globalPerm) || player.hasPermission(serverPerm))) {
            e.setJoinMessage(null);

            // Replace placeholder
            String msg = whitelistMessage;
            msg = PlaceholderAPI.setPlaceholders(player, msg);
            getServer().broadcastMessage(msg);
            Bukkit.getScheduler().runTaskLater(this, () -> player.kick(LegacyComponentSerializer.legacyAmpersand().deserialize(kickMessage)), 2L);

            getLogger().info(player.getName() + " is not whitelisted and tried to join!");
        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (whitelistEnabled && !(player.hasPermission(globalPerm) || player.hasPermission(serverPerm))) {
            e.setQuitMessage(null);
        }
    }
    
    public String getGlobalPerm() {
    	return globalPerm;
    }
    
    public String getServerPerm() {
    	return serverPerm;
    }
}

// Command handler
class DDWhitelistCommand implements CommandExecutor {

    private final DDWhitelist plugin;

    public DDWhitelistCommand(DDWhitelist plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Component.text("Usage: /ddwhitelist <on|off|status|reload|help>", NamedTextColor.YELLOW));
            return true;
        }

        String arg = args[0].toLowerCase();
        switch (arg) {
            case "on":
                plugin.setWhitelistEnabled(true);
                Bukkit.getServer().broadcast(Component.text("Whitelist is now enabled", NamedTextColor.GREEN));
                break;
            case "off":
                plugin.setWhitelistEnabled(false);
                Bukkit.getServer().broadcast(Component.text("Whitelist is now disabled", NamedTextColor.RED));
                break;
            case "status":
                Component status = Component.text("Whitelist is currently ", NamedTextColor.WHITE)
                        .append(Component.text(plugin.isWhitelistEnabled() ? "enabled" : "disabled", plugin.isWhitelistEnabled() ? NamedTextColor.GREEN : NamedTextColor.RED));
                sender.sendMessage(status);
                sender.sendMessage(Component.text("Per-Server permission: ", NamedTextColor.GRAY)
                        .append(Component.text(plugin.getServerPerm(), NamedTextColor.YELLOW)));
                break;
            case "reload":
                plugin.reloadConfig();
                plugin.loadConfigValues();
                sender.sendMessage(Component.text("DDWhitelist config reloaded.", NamedTextColor.AQUA));
                break;
            case "help":
            default:
                sender.sendMessage(Component.text("Commands:", NamedTextColor.YELLOW));
                sender.sendMessage(Component.text("/ddwhitelist on - Enable the whitelist", NamedTextColor.GREEN));
                sender.sendMessage(Component.text("/ddwhitelist off - Disable the whitelist", NamedTextColor.GREEN));
                sender.sendMessage(Component.text("/ddwhitelist status - Check whitelist status", NamedTextColor.GREEN));
                sender.sendMessage(Component.text("/ddwhitelist reload - Reload the config", NamedTextColor.GREEN));
                sender.sendMessage(Component.text("/ddwhitelist help - Show this help message", NamedTextColor.GREEN));
                break;
        }
        return true;
    }
}
