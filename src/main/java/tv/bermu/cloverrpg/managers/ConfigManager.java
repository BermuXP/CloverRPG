package tv.bermu.cloverrpg.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Config manager class
 * 
 * Manages the config files
 */
public class ConfigManager {

    private final JavaPlugin plugin;
    // TODO move configs to a map, so we can access them by name. this might be
    // lighter.
    private final Map<String, FileConfiguration> configs = new HashMap<>();
    private static ConfigManager instance;

    /**
     * Constructor
     * 
     * @param plugin The plugin
     */
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the instance of the ConfigManager
     * 
     * @param plugin The plugin
     * @return The instance of the ConfigManager
     */
    public ConfigManager getInstance(JavaPlugin plugin) {
        if (instance == null) {
            instance = new ConfigManager(plugin);
        }
        return instance;
    }

    /**
     * Load a config file
     * 
     * @param fileName The name of the file
     * @return The loaded config
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
     * Check if a config file exists
     * 
     * @param fileName The name of the file
     * @return True if the file exists, false otherwise
     */
    public boolean configExists(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName + ".yml");
        return configFile.exists();
    }

    /**
     * Reload a config file
     * 
     * @param fileName The name of the file
     * @return The reloaded config
     */
    public FileConfiguration reloadConfig(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName + ".yml");
        return YamlConfiguration.loadConfiguration(configFile);
    }
}
