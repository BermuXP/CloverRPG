package tv.bermu.cloverrpg;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PartyManager {

    private static final Map<Player, Set<Player>> parties = new HashMap<>();

    public static void createParty(Player leader) {
        Set<Player> partyMembers = new HashSet<>();
        partyMembers.add(leader);
        parties.put(leader, partyMembers);
    }

    public static boolean isInParty(Player player) {
        return parties.containsKey(player);
    }

    public static boolean isPartyLeader(Player player) {
        Set<Player> partyMembers = parties.get(player);
        return partyMembers != null && partyMembers.size() > 0 && partyMembers.iterator().next().equals(player);
    }

    public static void invitePlayer(Player inviter, Player invitedPlayer) {
        parties.get(inviter).add(invitedPlayer);
        invitedPlayer.sendMessage(inviter.getName() + " has invited you to their party. Type '/party accept " + inviter.getName() + "' to join.");
    }

    public static void acceptInvite(Player invitedPlayer, Player inviter) {
        if (parties.containsKey(inviter) && parties.get(inviter).contains(invitedPlayer)) {
            parties.get(inviter).add(invitedPlayer);
            invitedPlayer.sendMessage("You have joined " + inviter.getName() + "'s party!");
        } else {
            invitedPlayer.sendMessage("You have not been invited to join this party.");
        }
    }

    public static void leaveParty(Player player) {
        parties.remove(player);
    }

    public static void kickPlayer(Player leader, Player playerToKick) {
        if (parties.containsKey(leader) && parties.get(leader).contains(playerToKick)) {
            parties.get(leader).remove(playerToKick);
            playerToKick.sendMessage("You have been kicked from the party by " + leader.getName() + ".");
        }
    }

    public static void listPartyMembers(Player player) {
        Set<Player> partyMembers = parties.get(player);
        if (partyMembers != null && !partyMembers.isEmpty()) {
            StringBuilder membersList = new StringBuilder("Party members: ");
            for (Player member : partyMembers) {
                membersList.append(member.getName()).append(", ");
            }
            player.sendMessage(membersList.substring(0, membersList.length() - 2));
        } else {
            player.sendMessage("You are not in a party!");
        }
    }
}
