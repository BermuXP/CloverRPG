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
    //TODO move configs to a map, so we can access them by name. this might be lighter.
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
     * Get the language file configuration
     * TODO move this to config manager
     * 
     * @param language The language to get the configuration for
     * @return The language file configuration
     */
    public FileConfiguration getLanguagFileConfiguration(String language) {
        FileConfiguration langConfig = languageConfigs.get(language);

        if (langConfig == null) {
            if (!configManager.configExists("messages/" + language)) {
                language = "en_gb"; // Fallback to English if the language file doesn't exist
                langConfig = languageConfigs.get(language);
                if (langConfig == null) {
                    langConfig = configManager.loadConfig("messages/" + language);
                }
            }
            languageConfigs.put(language, langConfig);
        }
        return langConfig;
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
