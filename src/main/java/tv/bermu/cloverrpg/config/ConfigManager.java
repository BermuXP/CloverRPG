package tv.bermu.cloverrpg.config;

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

    /**
     * Constructor
     * 
     * @param plugin
     */
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        // create locale (messages) folder.
        createFolder("messages");
    }

    /**
     * Load all messages
     */
    public void loadAllMessages() {
        File messagesFolder = new File(plugin.getDataFolder(), "messages");
        if (messagesFolder.exists()) {
            for (File file : messagesFolder.listFiles()) {
                if (file.getName().endsWith(".yml")) {
                    createConfig(file);
                }
            }
        }
    }

    /**
     * Create a config file
     * 
     * @param configFile The file to create
     */
    public void createConfig(File configFile) {
        if (!configFile.exists()) {
            plugin.getLogger().info("Creating " + configFile.getName());
            configFile.getParentFile().mkdirs();
            plugin.saveResource(configFile.getName(), false);
        } else {
            plugin.getLogger().info("Loading " + configFile.getName());
        }
    }

    /**
     * Load a config file
     * 
     * @param fileName The name of the file
     * @return The loaded config
     */
    public FileConfiguration loadConfig(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName + ".yml");
        createConfig(configFile);
        return YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Create a folder
     * 
     * @param folderName The name of the folder
     */
    public void createFolder(String folderName) {
        File newFolder = new File(plugin.getDataFolder() + folderName);
        if (newFolder.exists()) {
            newFolder.mkdirs();
        }
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
