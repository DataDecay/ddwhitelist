package dev.datadecay.ddwhitelist.commands;

import dev.datadecay.ddwhitelist.DDWhitelist;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DDWhitelistCommand implements CommandExecutor {

    private final DDWhitelist plugin;

    public DDWhitelistCommand(DDWhitelist plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Component.text("Usage: /" + label + "<on|off|status|reload|help>", NamedTextColor.YELLOW));
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