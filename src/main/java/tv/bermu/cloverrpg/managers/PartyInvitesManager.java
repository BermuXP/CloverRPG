package tv.bermu.cloverrpg.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PartyInvitesManager {

    private HashMap<UUID, List<Integer>> partyInvites = new HashMap<>();
    private static PartyManager instance = null;

    /**
     * Private constructor
     */
    private PartyInvitesManager() {
    }

    /**
     * Get the instance of the PartyManager
     * 
     * @return The instance of the PartyManager
     */
    public static PartyManager getInstance() {
        if (instance == null) {
            instance = new PartyInvitesManager();
        }
        return instance;
    }

    public void addPartyInvite(UUID playerId, Integer partyId) {
        List<Integer> playerInvites = partyInvites.getOrDefault(playerId, new ArrayList<>());
        playerInvites.add(partyId);
        partyInvites.put(playerId, playerInvites);
    }

    public void removePartyInvite(UUID playerId, Integer partyId) {
        List<Integer> playerInvites = partyInvites.get(playerId);
        if (playerInvites != null) {
            playerInvites.remove(partyId);
            if (playerInvites.isEmpty()) {
                partyInvites.remove(playerId);
            } else {
                partyInvites.put(playerId, playerInvites);
            }
        }
    }

    public List<Integer> getPartyInvites(UUID playerId) {
        return partyInvites.get(playerId);
    }
}
