package tv.bermu.cloverrpg;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import tv.bermu.cloverrpg.config.ConfigManager;

public class MessageFormatter {

    private ConfigManager configManager;
    private Map<String, FileConfiguration> languageConfigs = new HashMap<>();
    private FileConfiguration defaultConfig;

    /**
     * Constructor
     * 
     * @param configManager The config manager
     */
    public MessageFormatter(ConfigManager configManager, FileConfiguration defaultConfig) {
        this.configManager = configManager;
        this.defaultConfig = defaultConfig;
    }

    /**
     * Format a message with default slugs
     * 
     * @param configTag The tag of the message in the language file
     * @param language  The language to use
     * @return The formatted message
     */
    public String formatMessageDefaultSlugs(String configTag, String language) {
        return formatMessage(configTag, language, new HashMap<String, Object>() {{
        }});
    }

    /**
     * Format a message with slugs
     * 
     * @param configTag The tag of the message in the language file
     * @param language  The language to use
     * @param slugs     The slugs to replace in the message
     * @return The formatted message
     */
    public String formatMessage(String configTag, String language, HashMap<String, Object> slugs) {
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

        String rawMessage = langConfig.getString(configTag);
        if (slugs != null) {
            slugs.put("message_prefix", defaultConfig.getString("messageprefix"));
            for (Map.Entry<String, Object> entry : slugs.entrySet()) {
                String slug = entry.getKey();
                Object replacement = entry.getValue();
                rawMessage = rawMessage.replace("{" + slug + "}", replacement.toString());
            }
        }
        return ChatColor.translateAlternateColorCodes('&', rawMessage);
    }

}
