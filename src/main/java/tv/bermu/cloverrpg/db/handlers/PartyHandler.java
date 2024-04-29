package tv.bermu.cloverrpg.db.handlers;

import tv.bermu.cloverrpg.db.Database;

public class PartyHandler  {

    private final Database database;
    private final String tableName = "party";

    public PartyHandler(Database database) {
        this.database = database;
    }
    
    public void createParty() {
        this.database.insertData(tableName, null, null);
    }
}
