package tv.bermu.cloverrpg;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.config.ConfigManager;

public class MessageFormatter {

    private ConfigManager configManager;
    private Map<String, FileConfiguration> languageConfigs = new HashMap<>();
    private Logger logger;

    /**
     * Constructor
     * @param configManager
     */
    public MessageFormatter(Logger logger, ConfigManager configManager) {
        this.configManager = configManager;
        this.logger = logger;
    }

    /**
     * Format a message
     * 
     * @param player  The player
     * @param slugs   The message slugs
     * @return The formatted message
     */
    public String formatMessage(String configTag, Player player, HashMap<String, Object> slugs) {
        String language = player.getLocale().toLowerCase();
        FileConfiguration langConfig = languageConfigs.get(language);
        if (langConfig == null) {
            if (!configManager.configExists("messages/" + language)) {
                language = "en_gb"; // Fallback to English if the language file doesn't exist
            }
            langConfig = configManager.loadConfig("messages/" + language);
            logger.info("Loading new " + language + " language file.");
            languageConfigs.put(language, langConfig);
        }
    
        String rawMessage = langConfig.getString(configTag);
        if (slugs != null) {
            for (Map.Entry<String, Object> entry : slugs.entrySet()) {
                String slug = entry.getKey();
                Object replacement = entry.getValue();
                rawMessage = rawMessage.replace("{" + slug + "}", replacement.toString());
            }
        }
        return ChatColor.translateAlternateColorCodes('&', rawMessage);
    }

}
