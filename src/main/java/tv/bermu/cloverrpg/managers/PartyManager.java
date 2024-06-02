package tv.bermu.cloverrpg.managers;

import java.util.HashMap;
import java.util.UUID;

import tv.bermu.cloverrpg.models.PartyModel;

public class PartyManager {

    private static PartyManager instance = null;
    private HashMap<UUID, PartyModel> playerParties = new HashMap<>();

    /**
     * Private constructor
     */
    private PartyManager() {
    }

    /**
     * Get the instance of the PartyManager
     * 
     * @return The instance of the PartyManager
     */
    public static PartyManager getInstance() {
        if (instance == null) {
            instance = new PartyManager();
        }
        return instance;
    }

    public void addPlayerToParty(UUID playerId, PartyModel party) {
        playerParties.put(playerId, party);
    }

    public void removePlayerFromParty(UUID playerId) {
        playerParties.remove(playerId);
    }

    public PartyModel getPartyOfPlayer(UUID playerId) {
        return playerParties.get(playerId);
    }

}