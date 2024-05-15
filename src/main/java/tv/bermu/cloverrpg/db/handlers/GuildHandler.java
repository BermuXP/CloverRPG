package tv.bermu.cloverrpg.db.handlers;

import java.sql.SQLException;
import java.util.UUID;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.db.Database;

public class GuildHandler {
    private final Database database;
    private final MessageFormatter messageFormatter;
    private final String tableName = "guild";

    /**
     * Constructor
     * 
     * @param database         The database
     * @param messageFormatter The message formatter
     */
    public GuildHandler(Database database, MessageFormatter messageFormatter) {
        this.database = database;
        this.messageFormatter = messageFormatter;
    }

    public boolean userInGuild(String playerUUID) {
        return false;
    }

    public boolean guildNameTaken(String guildName) {
        return false;

    }

    /**
     * Check if the user has a specific guild permission
     * 
     * @param playerUUID The player UUID
     * @param permission The permission
     * @return True if the user has the permission, false otherwise
     */
    public boolean userHasGuildPermission(String playerUUID, String permission) {
        return false;
    }

    /**
     * Create a guild
     * 
     * @param guildName  The guild name
     * @param playerUUID The player UUID
     * @param language   The language
     * @return The message
     */
    public String createGuild(String guildName, String playerUUID, String language) {
        try {
            database.insertData(tableName, new String[] { "name", "player_uuid" },
                    new Object[] { guildName, playerUUID });

            return messageFormatter.formatMessageDefaultSlugs("guild_created_successfully", language);
        } catch (SQLException e) {
            e.printStackTrace();
            return messageFormatter.formatMessageDefaultSlugs("error_creating_character", language);
        }
    }

    /**
     * Add a guild member
     * 
     * @param playerUUID The player UUID
     * @param guildId    The guild ID
     * @param language   The language
     * @param rank       The rank
     * @param level      The level
     * @return The message
     */
    public String addGuildMember(UUID playerUUID, Integer guildId, String language, String rank, String level) {
        try {
            database.insertData("guild_members", new String[] { "guild_id", "player_uuid", "rank", "level" },
                    new Object[] { guildId, playerUUID, rank, level });
            return "";
        } catch (SQLException e) {
            e.printStackTrace();
            return messageFormatter.formatMessageDefaultSlugs("error_creating_character", language);
        }
    }

}
