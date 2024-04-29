package tv.bermu.cloverrpg.models;

import java.util.UUID;

public class PlayerModel {

    private UUID uuid;
    private int unsavedExp;

    public PlayerModel(UUID uuid) {
        this.uuid = uuid;
        this.unsavedExp = 0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getUnsavedExp() {
        return unsavedExp;
    }

    public void addUnsavedExp(int exp) {
        this.unsavedExp += exp;
    }

    public void clearUnsavedExp() {
        this.unsavedExp = 0;
    }
}
