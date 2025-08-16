package dev.datadecay.ddwhitelist.listeners;

import dev.datadecay.ddwhitelist.DDWhitelist;
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
    public void onJoin(PlayerJoinEvent event) {
        if (plugin.isWhitelistEnabled() &&
            !event.getPlayer().hasPermission("ddwhitelist.allowjoin")) {

            event.setJoinMessage(null);
            event.getPlayer().kickPlayer(plugin.getConfig().getString("messages.not_whitelisted"));
            plugin.getServer().broadcastMessage("§c" + event.getPlayer().getName() + " tried to join but is not whitelisted!");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (plugin.isWhitelistEnabled() &&
            !event.getPlayer().hasPermission("ddwhitelist.allowjoin")) {
            event.setQuitMessage(null);
        }
    }
}
