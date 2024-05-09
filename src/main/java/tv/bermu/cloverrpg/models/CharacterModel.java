package tv.bermu.cloverrpg.models;

import java.util.UUID;

public class CharacterModel {

    private UUID uuid;
    private Integer unsavedExp;
    private Integer exp;
    private Integer level;
    private String name;
    private Integer classId;
    private Integer raceId;

    public CharacterModel(UUID uuid, Integer exp, String name, Integer level, Integer raceId, Integer classId) {
        this.uuid = uuid;
        this.exp = exp;
        this.name = name;
        this.level = level;
        this.raceId = raceId;
        this.classId = classId;
        this.unsavedExp = 0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public Integer getClassId() {
        return classId;
    }

    public Integer getRaceId() {
        return raceId;
    }

    public Integer getUnsavedExp() {
        return unsavedExp;
    }

    public void addUnsavedExp(int exp) {
        this.unsavedExp += exp;
    }

    public void clearUnsavedExp() {
        this.unsavedExp = 0;
    }
}
