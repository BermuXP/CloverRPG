package tv.bermu.cloverrpg.db.handlers;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.db.Database;

public class CharacterHandler {
    private static CharacterHandler instance = null;

    public static CharacterHandler getInstance(Database database, MessageFormatter messageFormatter) {
        if (instance == null) {
            instance = new CharacterHandler(database, messageFormatter);
        }
        return instance;
    }
}
