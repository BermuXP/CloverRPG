package tv.bermu.cloverrpg.models;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

public class Party {
    
    private Player leader;
    private final Set<Player> members;

    public Party(Player leader, HashSet<Player> members) {
        this.leader = leader;
        this.members = members;
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
