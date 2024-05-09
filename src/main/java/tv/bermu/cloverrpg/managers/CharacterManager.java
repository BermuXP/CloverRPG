package tv.bermu.cloverrpg.managers;

import java.util.HashMap;
import java.util.UUID;

import tv.bermu.cloverrpg.models.CharacterModel;

public class CharacterManager {

    private static CharacterManager instance = null;
    private HashMap<UUID, CharacterModel> playerCharacters = new HashMap<>();

    /**
     * Private constructor
     */
    private CharacterManager() {
    }

    /**
     * Get the instance of the PartyManager
     * 
     * @return The instance of the PartyManager
     */
    public static CharacterManager getInstance() {
        if (instance == null) {
            instance = new CharacterManager();
        }
        return instance;
    }
 
    public void addPlayerCharacter(UUID playerId, CharacterModel character) {
        playerCharacters.put(playerId, character);
    }

    public void removePlayerCharacter(UUID playerId) {
        playerCharacters.remove(playerId);
    }

    public CharacterModel getCharacterOfPlayer(UUID playerId) {
        return playerCharacters.get(playerId);
    }
}
