package dev.datadecay.ddwhitelist.commands;

import dev.datadecay.ddwhitelist.DDWhitelist;
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
        if (args.length == 0) {
            sender.sendMessage("§eUsage: /ddwhitelist <on|off|status|help>");
            return true;
        }

        String arg = args[0].toLowerCase();

        switch (arg) {
            case "on":
                plugin.setWhitelistEnabled(true);
                sender.getServer().broadcastMessage(plugin.getConfig().getString("messages.whitelist_on"));
                break;

            case "off":
                plugin.setWhitelistEnabled(false);
                sender.getServer().broadcastMessage(plugin.getConfig().getString("messages.whitelist_off"));
                break;

            case "status":
                if (plugin.isWhitelistEnabled()) {
                    sender.sendMessage(plugin.getConfig().getString("messages.whitelist_enabled"));
                } else {
                    sender.sendMessage(plugin.getConfig().getString("messages.whitelist_disabled"));
                }
                break;

            case "help":
                sender.sendMessage("§e§lWhitelist Commands:");
                sender.sendMessage("§a/ddwhitelist on §7- Enable whitelist");
                sender.sendMessage("§a/ddwhitelist off §7- Disable whitelist");
                sender.sendMessage("§a/ddwhitelist status §7- Check whitelist status");
                sender.sendMessage("§a/ddwhitelist help §7- Show this help message");
                break;

            default:
                sender.sendMessage("§eUsage: /ddwhitelist <on|off|status|help>");
                break;
        }

        return true;
    }
}
