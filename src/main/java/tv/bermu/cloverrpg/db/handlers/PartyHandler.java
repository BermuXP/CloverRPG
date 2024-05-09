package tv.bermu.cloverrpg.db.handlers;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.db.Database;
import tv.bermu.cloverrpg.managers.PartyManager;
import tv.bermu.cloverrpg.models.PartyModel;

/**
 * Handler for party operations
 */
public class PartyHandler {

    private final Database database;
    private final MessageFormatter messageFormatter;
    private final String tableName = "party";

    /**
     * Constructor
     * 
     * @param database         The database
     * @param messageFormatter The message formatter
     */
    public PartyHandler(Database database, MessageFormatter messageFormatter) {
        this.database = database;
        this.messageFormatter = messageFormatter;
    }

    /**
     * Create a party
     * 
     * @param partyName  The name of the party
     * @param playerUUID The UUID of the player creating the party
     * @return Message indicating the result of the operation
     */
    public String createParty(String partyName, Player player, String language) {
        try {
            UUID playerUUID = player.getUniqueId();
            if (!this.userInParty(playerUUID)) {
                Integer partyId = database.insertData(tableName, new String[] { "name" }, new Object[] { playerUUID });

                database.insertData("party_members", new String[] { "party_id", "player_uuid", "is_leader" },
                        new Object[] { partyId, playerUUID, 1 });

                HashSet<Player> members = new HashSet<>();
                members.add(player);
                PartyModel party = new PartyModel(partyId, partyName, player, members);

                PartyManager partyManager = PartyManager.getInstance();
                partyManager.addPlayerToParty(playerUUID, party);
                return messageFormatter.formatMessageDefaultSlugs("party_created_successfully", language);
            } else {
                return messageFormatter.formatMessageDefaultSlugs("already_in_party", language);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return messageFormatter.formatMessageDefaultSlugs("error_creating_party", language);
        }
    }

    /**
     * Check if a player is in a party
     * 
     * @param playerUUID The UUID of the player
     * @return True if the player is in a party, false otherwise
     */
    public boolean userInParty(UUID playerUUID) {
        try {
            ResultSet partyExists = database.selectData("SELECT id FROM party_members WHERE player_uuid = ?",
                    new Object[] { playerUUID });
            return partyExists.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a party and its members
     * 
     * @param playerUUID The UUID of the player
     * @param language   The language of the player
     * @return Message indicating the result of the operation
     */
    public String deleteParty(UUID playerUUID, String language) {
        try {
            ResultSet partyLead = this.isPartyLead(playerUUID);
            if (partyLead == null) {
                return messageFormatter.formatMessageDefaultSlugs("not_in_party", language);
            } else if (partyLead.getInt("is_leader") == 1) {
                return messageFormatter.formatMessageDefaultSlugs("not_the_party_leader", language);
            } else {
                int id = partyLead.getInt("id");
                database.deleteData("party_members", new String[] { "party_id" }, new Object[] { id });
                database.deleteData(tableName, new String[] { "id" }, new Object[] { id });
                return messageFormatter.formatMessageDefaultSlugs("party_disbanded_successfully", language);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return messageFormatter.formatMessageDefaultSlugs("error_disbanding_party", language);
        }
    }

    /**
     * Check if a player is the party lead
     * 
     * @param playerUUID The UUID of the player
     * @return ResultSet with the party lead information
     */
    public ResultSet isPartyLead(UUID playerUUID) {
        try {
            ResultSet party = database.selectData("SELECT is_leader, id FROM party_members WHERE player_uuid = ?",
                    new Object[] { playerUUID });
            return party;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String removeFromParty(UUID playerUUID, String language) {
        return "";
    }
}
