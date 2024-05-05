package tv.bermu.cloverrpg.models;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

public class PartyModel {

    private Player leader;
    private Integer partyId;
    private String name;
    private final Set<Player> members;

    public PartyModel(Integer partyId, String name, Player leader, HashSet<Player> members) {
        this.partyId = partyId;
        this.name = name;
        this.leader = leader;
        this.members = members;
    }

    public Integer getPartyId() {
        return partyId;
    }

    public String getName() {
        return name;
    }

    public Player getLeader() {
        return leader;
    }

    public Set<Player> getMembers() {
        return members;
    }

    public boolean isMember(Player player) {
        return members.contains(player);
    }

    public void addMember(Player player) {
        members.add(player);
    }

    public void removeMember(Player player) {
        members.remove(player);
    }

    public void transferLeader(Player newLeader) {
        if (members.contains(newLeader)) {
            this.leader = newLeader;
        }
    }
}
