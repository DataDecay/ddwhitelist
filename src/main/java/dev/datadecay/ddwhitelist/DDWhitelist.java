package dev.datadecay.ddwhitelist;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

public final class DDWhitelist extends JavaPlugin implements Listener {

    private boolean whitelistEnabled;

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
        if (whitelistEnabled && !player.hasPermission("ddwhitelist.allowjoin")) {
            e.setJoinMessage(null);
            Bukkit.getScheduler().runTaskLater(this, () -> player.kick("This server is being worked on!"), 2L);
            Bukkit.getLogger().info(player.getName() + " is not whitelisted and tried to join!");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (whitelistEnabled && !player.hasPermission("ddwhitelist.allowjoin")) {
            e.setQuitMessage(null);
        }
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
            sender.sendMessage(ChatColor.YELLOW + "Usage: /ddwhitelist <on|off|status|reload|help>");
            return true;
        }

        String arg = args[0].toLowerCase();
        switch (arg) {
            case "on":
                plugin.setWhitelistEnabled(true);
                Bukkit.broadcastMessage(ChatColor.GREEN + "Whitelist is now enabled");
                break;
            case "off":
                plugin.setWhitelistEnabled(false);
                Bukkit.broadcastMessage(ChatColor.RED + "Whitelist is now disabled");
                break;
            case "status":
                sender.sendMessage("Whitelist is currently " + (plugin.isWhitelistEnabled() ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled"));
                break;
            case "reload":
                plugin.reloadConfig();
                plugin.loadConfigValues();
                sender.sendMessage(ChatColor.AQUA + "DDWhitelist config reloaded.");
                break;
            case "help":
            default:
                sender.sendMessage(ChatColor.YELLOW + "DDWhitelist Commands:");
                sender.sendMessage(ChatColor.GREEN + "/ddwhitelist on " + ChatColor.WHITE + "- Enable the whitelist");
                sender.sendMessage(ChatColor.GREEN + "/ddwhitelist off " + ChatColor.WHITE + "- Disable the whitelist");
                sender.sendMessage(ChatColor.GREEN + "/ddwhitelist status " + ChatColor.WHITE + "- Check whitelist status");
                sender.sendMessage(ChatColor.GREEN + "/ddwhitelist reload " + ChatColor.WHITE + "- Reload the config");
                sender.sendMessage(ChatColor.GREEN + "/ddwhitelist help " + ChatColor.WHITE + "- Show this help message");
                break;
        }
        return true;
    }
}
