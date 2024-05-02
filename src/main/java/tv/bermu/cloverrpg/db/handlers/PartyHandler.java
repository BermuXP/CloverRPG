package tv.bermu.cloverrpg.db.handlers;

import java.sql.ResultSet;
import java.util.UUID;

import tv.bermu.cloverrpg.db.Database;

public class PartyHandler {

    private final Database database;
    private final String tableName = "party";

    /**
     * Constructor
     * 
     * @param database
     */
    public PartyHandler(Database database) {
        this.database = database;
    }

    /**
     * Create a party
     * 
     * @param partyName  The name of the party
     * @param playerUUID The UUID of the player creating the party
     */
    public boolean createParty(String partyName, UUID playerUUID) {
        try {
            if (!this.userInParty(playerUUID)) {
                int partyId = database.insertData(tableName, new String[] { "name" }, new Object[] { playerUUID });
                
                database.insertData("party_members", new String[] { "party_id", "player_uuid", "is_leader" },
                        new Object[] { partyId, playerUUID, 1 });
                return true;
            } else {
                // Player is already in a party
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if a player is in a party
     * 
     * @param playerUUID The UUID of the player
     * @return
     */
    public boolean userInParty(UUID playerUUID) {
        try {
            ResultSet partyExists = database.selectData("SELECT id FROM party_members WHERE player_uuid = ?", new Object[] { playerUUID });
            return partyExists.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
