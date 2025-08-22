package dev.datadecay.ddwhitelist.update;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Self-contained update checker.
 * Usage: new UpdateChecker(this).checkForUpdate();
 */
public class UpdateChecker {

    private final JavaPlugin plugin;

    // Replace with your GitHub raw file or SpigotMC resource URL
    private final String versionUrl = "https://raw.githubusercontent.com/DataDecay/ddwhitelist/main/version.txt";

    public UpdateChecker(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Check for updates asynchronously and log the result.
     */
    public void checkForUpdate() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            String latest = getLatestVersion();
            String current = plugin.getDescription().getVersion();

            if (latest != null && isUpdateAvailable(current, latest)) {
                plugin.getLogger().info("A new version of " + plugin.getName() + " is available: " + latest);
                plugin.getLogger().info("Download it here: https://github.com/username/repo"); // Optional
            } else {
                plugin.getLogger().info(plugin.getName() + " is up to date.");
            }
        });
    }

    /**
     * Fetch the latest version from the remote URL.
     */
    private String getLatestVersion() {
        try {
            URL url = new URL(versionUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String latest = reader.readLine(); // assumes version is on the first line
            reader.close();

            return latest;
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to check for updates: " + e.getMessage());
            return null;
        }
    }

    /**
     * Compare two semantic version strings.
     */
    private boolean isUpdateAvailable(String current, String latest) {
        if (current == null || latest == null) return false;

        String[] curParts = current.split("\\.");
        String[] latParts = latest.split("\\.");

        int length = Math.max(curParts.length, latParts.length);
        for (int i = 0; i < length; i++) {
            int c = i < curParts.length ? Integer.parseInt(curParts[i]) : 0;
            int l = i < latParts.length ? Integer.parseInt(latParts[i]) : 0;
            if (l > c) return true;
            if (l < c) return false;
        }
        return false;
    }
}
