package com.github.teraprath.tinylib.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.annotation.Nonnull;

public class UpdateChecker {

    private static final String API_URL = "https://api.github.com/repos/%s/%s/releases/latest";
    private static final String DOWNLOAD_URL = "https://github.com/%s/%s/releases/latest";

    private final JavaPlugin plugin;

    private final String owner;
    private final String repo;
    private final String updateMessage = "A new version of %s is available! Download v%s here: %s";

    public UpdateChecker(@Nonnull JavaPlugin plugin, @Nonnull String owner, @Nonnull String repo) {
        this.plugin = plugin;
        this.owner = owner;
        this.repo = repo;
    }

    public String getLatestReleaseVersion() throws IOException, InterruptedException {
        String url = String.format(API_URL, owner, repo);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/vnd.github+json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            return jsonResponse.get("tag_name").getAsString();
        } else {
            throw new IOException("Failed to fetch release version. Status code: " + response.statusCode());
        }
    }


    public void checkForUpdate() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, task -> {
            try {
                final String currentVersion = plugin.getDescription().getVersion();
                final String latestVersion = getLatestReleaseVersion();
                if (currentVersion.equals(latestVersion)) {
                    plugin.getLogger().info("You're using the latest version.");
                } else {
                    plugin.getLogger().warning(String.format(updateMessage, plugin.getDescription().getName(), latestVersion, String.format(DOWNLOAD_URL, owner, repo)));
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void checkForUpdate(@Nonnull Player player) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, task -> {
            try {
                final String currentVersion = plugin.getDescription().getVersion();
                final String latestVersion = getLatestReleaseVersion();
                if (!currentVersion.equals(latestVersion)) {
                    player.sendMessage(String.format(updateMessage, plugin.getDescription().getName(), latestVersion, String.format(DOWNLOAD_URL, owner, repo)));
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
