package dev.datadecay.ddwhitelist.listeners;

import dev.datadecay.ddwhitelist.DDWhitelist;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final DDWhitelist plugin;

    public PlayerListener(DDWhitelist plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        //plugin.getLogger().info(Boolean.toString(player.hasPermission(plugin.getGlobalPerm())));
        //plugin.getLogger().info(Boolean.toString(player.hasPermission(plugin.getServerPerm())));
        //plugin.getLogger().info(Boolean.toString(player.hasPermission(plugin.getTogglePerm())));

        if (plugin.isWhitelistEnabled() && !(player.hasPermission(plugin.getGlobalPerm()) || player.hasPermission(plugin.getServerPerm()))) {
            e.setJoinMessage(null);
            String msg = plugin.getWhitelistMessage();
            msg = PlaceholderAPI.setPlaceholders(player, msg);
            Component componentMsg = LegacyComponentSerializer.legacyAmpersand().deserialize(msg);
            plugin.getServer().broadcast(componentMsg);
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> player.kick(LegacyComponentSerializer.legacyAmpersand().deserialize(plugin.getKickMessage())), 2L);

            plugin.getLogger().info(player.getName() + " is not whitelisted and tried to join!");
        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (plugin.isWhitelistEnabled() && !(player.hasPermission(plugin.getGlobalPerm()) || player.hasPermission(plugin.getServerPerm()))) {
            e.setQuitMessage(null);
        }
    }
}
