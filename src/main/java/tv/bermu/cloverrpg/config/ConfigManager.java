package tv.bermu.cloverrpg.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * Config manager class
 * 
 * Manages the config files
 */
public class ConfigManager {
    private final JavaPlugin plugin;

    /**
     * Constructor
     * 
     * @param plugin
     */
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Load a config file
     * 
     * @param fileName The name of the file
     * @return
     */
    public FileConfiguration loadConfig(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName + ".yml");
        if (!configFile.exists()) {
            plugin.getLogger().info("Creating " + fileName + ".yml");
            configFile.getParentFile().mkdirs();
            plugin.saveResource(fileName + ".yml", false);
        } else {
            plugin.getLogger().info("Loading " + fileName + ".yml");
        }
        return YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Save a config file changes
     * 
     * @param config   The config file
     * @param fileName The name of the file
     */
    public void saveConfig(FileConfiguration config, String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName + ".yml");
        try {
            config.save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().severe("Could not save config to " + configFile.getName());
        }
    }

    /**
     * Reload a config file
     * 
     * @param fileName The name of the file
     * @return
     */
    public FileConfiguration reloadConfig(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName + ".yml");
        return YamlConfiguration.loadConfiguration(configFile);
    }
}
