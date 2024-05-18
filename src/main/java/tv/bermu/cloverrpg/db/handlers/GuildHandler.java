package tv.bermu.cloverrpg.db.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.db.Database;

public class GuildHandler {
    private final Database database;
    private final MessageFormatter messageFormatter;
    private final String tableName = "guilds";

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

    /**
     * Check if a user is in a guild
     * 
     * @param playerUUID The player UUID
     * @return True if the user is in a guild, false otherwise
     */
    public boolean userInGuild(String playerUUID) {
        try {
            ResultSet partyExists = database.selectData("SELECT id FROM guild_members WHERE player_uuid = ?",
                    new Object[] { playerUUID });
            return partyExists.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if a guild name is already taken
     * 
     * @param guildName The guild name
     * @return True if the guild name is taken, false otherwise
     */
    public boolean guildNameTaken(String guildName) {
        try {
            ResultSet guildNameTaken = database.selectData("SELECT id FROM guilds WHERE LOWER(name) LIKE LOWER(?)",
                    new Object[] { guildName });
            return guildNameTaken.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if the user has a specific guild permission
     * 
     * @param playerUUID The player UUID
     * @param permission The permission
     * @return True if the user has the permission, false otherwise
     */
    public boolean userHasGuildPermission(String playerUUID, String permission) {
        try {
            ResultSet guildUser = database.selectData("SELECT id FROM guilds WHERE LOWER(name) LIKE LOWER(?)",
                    new Object[] { permission });
            return guildUser.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
            if (guildNameTaken(guildName)) {
                return messageFormatter.formatMessageDefaultSlugs("guild_name_taken", language);
            }
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
