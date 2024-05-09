package tv.bermu.cloverrpg.db.handlers;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.db.Database;

public class CharacterHandler {

    private final Database database;
    private final MessageFormatter messageFormatter;
    private final String tableName = "characters";

    /**
     * Constructor
     * 
     * @param database         The database
     * @param messageFormatter The message formatter
     */
    public CharacterHandler(Database database, MessageFormatter messageFormatter) {
        this.database = database;
        this.messageFormatter = messageFormatter;
    }

    /**
     * Create a character
     * 
     * @param characterName The name of the character
     * @param playerUUID    The UUID of the player creating the character
     * @return Message indicating the result of the operation
     */
    public String createCharacter(String characterName, String playerUUID, String language) {
        try {
            database.insertData(tableName, new String[] { "name", "player_uuid" },
                    new Object[] { characterName, playerUUID });
            return messageFormatter.formatMessageDefaultSlugs("character_created_successfully", language);
        } catch (Exception e) {
            e.printStackTrace();
            return messageFormatter.formatMessageDefaultSlugs("error_creating_character", language);
        }
    }

    public Integer characterAmount() {
        return 0;
    }

}
