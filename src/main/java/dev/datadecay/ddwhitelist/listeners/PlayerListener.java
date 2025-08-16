package dev.datadecay.ddwhitelist.listeners;

import dev.datadecay.ddwhitelist.DDWhitelist;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

public class PlayerListener implements Listener {

    private final DDWhitelist plugin;

    public PlayerListener(DDWhitelist plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.getConfig().getBoolean("enabled") && !player.hasPermission("ddwhitelist.allowjoin")) {
            event.setJoinMessage(null);

            String msg = "%player_name% tried to join but is not whitelisted!";
            if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                msg = PlaceholderAPI.setPlaceholders(player, msg);
            }

            plugin.getServer().broadcastMessage(msg);
            player.kickPlayer("§cThis server is currently whitelisted!");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.getConfig().getBoolean("enabled") && !player.hasPermission("ddwhitelist.allowjoin")) {
            event.setQuitMessage(null);
        }
    }
}
