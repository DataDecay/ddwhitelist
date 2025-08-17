package dev.datadecay.ddwhitelist.commands;

import dev.datadecay.ddwhitelist.DDWhitelist;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DDWhitelistCommand implements CommandExecutor, TabCompleter {

    private final DDWhitelist plugin;

    public DDWhitelistCommand(DDWhitelist plugin) {
        this.plugin = plugin;
    }
    
    private void printVersion(CommandSender sender) {
        String version = plugin.getVersion(); 
        Component versionComponent;

        if (version.contains("SNAPSHOT")) {
            int index = version.indexOf("SNAPSHOT");
            String main = version.substring(0, index - 1);
            String snapshot = version.substring(index - 1);
            versionComponent = Component.text("v" + main, NamedTextColor.AQUA)
                                        .append(Component.text(snapshot, NamedTextColor.RED));
        } else {
            versionComponent = Component.text("v" + version, NamedTextColor.AQUA);
        }

        sender.sendMessage(Component.text("DDWhitelist ", NamedTextColor.DARK_AQUA)
                .append(versionComponent));
    }

    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(plugin.getTogglePerm())) {
            printVersion(sender);
            sender.sendMessage(Component.text("By DataDecay :D", NamedTextColor.YELLOW));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("Usage: /" + label + " <on|off|status|reload|help>", NamedTextColor.YELLOW));
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
                Component status = Component.text("Whitelist is currently ", NamedTextColor.YELLOW)
                        .append(Component.text(plugin.isWhitelistEnabled() ? "enabled" : "disabled",
                                plugin.isWhitelistEnabled() ? NamedTextColor.GREEN : NamedTextColor.RED));
                sender.sendMessage(status);
                sender.sendMessage(Component.text("Per-Server permission: ", NamedTextColor.YELLOW)
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
                printVersion(sender);
                break;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String[] args) {
        List<List<String>> tabFormat = Arrays.asList(
                Arrays.asList("on"),
                Arrays.asList("off"),
                Arrays.asList("status"),
                Arrays.asList("reload"),
                Arrays.asList("help")
        );
        Map<String, Object> completionTree = new HashMap<>();
        for (List<String> group : tabFormat) insertGroup(group, completionTree);
        Map<String, Object> currentNode = completionTree;
        for (int i = 0; i < args.length - 1; i++) {
            Object next = currentNode.get(args[i].toLowerCase());
            if (next instanceof Map) currentNode = (Map<String, Object>) next;
            else return Collections.emptyList();
        }
        List<String> completions = new ArrayList<>();
        String lastArg = args[args.length - 1].toLowerCase();
        for (String key : currentNode.keySet())
            if (key.toLowerCase().startsWith(lastArg)) completions.add(key);
        return completions;
    }

    private void insertGroup(List<String> group, Map<String, Object> node) {
        if (group.isEmpty()) return;
        String head = group.get(0).toLowerCase();
        Map<String, Object> child = node.containsKey(head) && node.get(head) instanceof Map ? (Map<String, Object>) node.get(head) : new HashMap<>();
        node.put(head, child);
        insertGroup(group.subList(1, group.size()), child);
    }
}
