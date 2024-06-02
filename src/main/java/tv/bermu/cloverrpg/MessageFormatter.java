package tv.bermu.cloverrpg;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import tv.bermu.cloverrpg.managers.ConfigManager;

public class MessageFormatter {

    private ConfigManager configManager;
    private FileConfiguration defaultConfig;

    /**
     * Constructor
     * 
     * @param configManager The config manager
     * @param defaultConfig The default config
     */
    public MessageFormatter(ConfigManager configManager, FileConfiguration defaultConfig) {
        this.configManager = configManager;
        this.defaultConfig = defaultConfig;
    }

    /**
     * Format a message with click events
     * 
     * @param configTag     The tag of the message in the language file
     * @param language      The language to use
     * @param commandString The command to run when the message is clicked
     * @return The formatted message
     */
    public TextComponent formatClickEventCommand(String configTag, String language, String commandString) {
        FileConfiguration langConfig = this.configManager.loadConfig("messages/" + language);
        String clickText = langConfig.getString(configTag + "_text");
        clickText = ChatColor.translateAlternateColorCodes('&', clickText);
        TextComponent message = new TextComponent(clickText);
        String hoverText = langConfig.getString(configTag + "_hover");
        hoverText = ChatColor.translateAlternateColorCodes('&', hoverText);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandString));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverText)));
        return message;
    }

    /**
     * Format a message with default slugs
     * 
     * @param configTag The tag of the message in the language file
     * @param language  The language to use
     * @return The formatted message
     */
    public String formatMessageDefaultSlugs(String configTag, String language) {
        return formatMessage(configTag, language, new HashMap<String, Object>() {
            {
            }
        });
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
        FileConfiguration langConfig = this.configManager.loadConfig("messages/" + language);

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

    /**
     * Format a message with slugs and return a TextComponent
     * 
     * @param configTag The tag of the message in the language file
     * @param language  The language to use
     * @param slugs     The slugs to replace in the message
     * @return The formatted message
     */
    public TextComponent formatMessageTextComponent(String configTag, String language, HashMap<String, Object> slugs) {
        FileConfiguration langConfig = this.configManager.loadConfig("messages/" + language);

        String rawMessage = langConfig.getString(configTag);
        TextComponent message = new TextComponent();

        if (slugs != null) {
            slugs.put("message_prefix", defaultConfig.getString("messageprefix"));
            for (Map.Entry<String, Object> entry : slugs.entrySet()) {
                String slug = entry.getKey();
                Object replacement = entry.getValue();

                if (replacement instanceof String) {
                    rawMessage = rawMessage.replace("{" + slug + "}", replacement.toString());
                } else if (replacement instanceof TextComponent) {
                    TextComponent slugComponent = (TextComponent) replacement;

                    String[] parts = rawMessage.split("\\{" + slug + "\\}", 2);
                    message.addExtra(ChatColor.translateAlternateColorCodes('&', parts[0]));
                    message.addExtra(slugComponent);

                    if (parts.length > 1) {
                        rawMessage = parts[1];
                    } else {
                        rawMessage = "";
                    }
                }
            }
        }
        return message;
    }
}
