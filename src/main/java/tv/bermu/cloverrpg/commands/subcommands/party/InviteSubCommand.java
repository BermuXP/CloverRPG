package tv.bermu.cloverrpg.commands.subcommands.party;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.chat.TextComponent;
import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.managers.PartyInvitesManager;
import tv.bermu.cloverrpg.managers.PartyManager;
import tv.bermu.cloverrpg.models.PartyModel;

public class InviteSubCommand implements SubCommand {

    private final MessageFormatter messageFormatter;
    private final JavaPlugin plugin;
    private final String permission;

    public InviteSubCommand(MessageFormatter messageFormatter, String permission, JavaPlugin plugin) {
        this.messageFormatter = messageFormatter;
        this.permission = permission;
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        String playerLanguage = player.getLocale().toLowerCase();
        HashMap<String, Object> slugs = new HashMap<>();
        if (args.length < 2) {
            slugs.put("command_name", "party");
            slugs.put("subcommand", "invite");
            slugs.put("usage", "<player_name>");
            player.sendMessage(
                    messageFormatter.formatMessage("subcommand_usage", playerLanguage, slugs));
        }

        PartyManager partyManager = PartyManager.getInstance();
        PartyModel party = partyManager.getPartyOfPlayer(player.getUniqueId());
        if (party == null) {
            player.sendMessage(
                    messageFormatter.formatMessageDefaultSlugs("not_in_party", playerLanguage));
        }

        // TODO cant invite anymore players

        String invitedPlayerName = args[1];
        Player invitedPlayer = plugin.getServer().getPlayer(invitedPlayerName);

        // player not found
        if (invitedPlayer == null) {
            slugs.put("player_name", invitedPlayerName);
            player.sendMessage(
                    messageFormatter.formatMessage("player_not_found", playerLanguage, slugs));
        }
        // player is not online
        if (!invitedPlayer.isOnline()) {
            slugs.put("player_name", invitedPlayer.getDisplayName());
            player.sendMessage(
                    messageFormatter.formatMessage("player_not_online", playerLanguage, slugs));
        }

        // cant invite yourself
        if (player.getUniqueId() == invitedPlayer.getUniqueId()) {
            player.sendMessage(
                    messageFormatter.formatMessageDefaultSlugs("party_invite_self", playerLanguage));
        }

        // cant invite players that are already in the party
        if (party.getMembers().contains(player.getUniqueId())) {
            player.sendMessage(
                    messageFormatter.formatMessageDefaultSlugs("player_already_in_the_party",
                            playerLanguage));
        }

        // cant invite players that are already in ANOTHER the party
        if (partyManager.getPartyOfPlayer(invitedPlayer.getUniqueId()) != null) {
            player.sendMessage(
                    messageFormatter.formatMessageDefaultSlugs("player_already_in_another_the_party",
                            playerLanguage));
        }

        // cant invite players that are already invited
        PartyInvitesManager partyInvitesManager = PartyInvitesManager.getInstance();
        if (partyInvitesManager.hasPartyInvite(invitedPlayer.getUniqueId(), party.getPartyId())) {
            player.sendMessage(
                    messageFormatter.formatMessageDefaultSlugs("player_already_invited",
                            playerLanguage));
        }

        partyInvitesManager.addPartyInvite(invitedPlayer.getUniqueId(), party.getPartyId());

        String invitedPlayerLang = invitedPlayer.getLocale().toLowerCase();

        slugs.put("invited_player", invitedPlayer.getDisplayName());
        slugs.put("inviting_player", player.getDisplayName());

        player.sendMessage(messageFormatter.formatMessage("party_invite_sent", playerLanguage, slugs));

        slugs.put("click_here_party_accept", messageFormatter.formatClickEventCommand("click_accept",
                invitedPlayerLang, "/party accept"));
        slugs.put("click_here_party_decline", messageFormatter.formatClickEventCommand("click_decline",
                invitedPlayerLang, "/party decline"));

        TextComponent invitedMessage = messageFormatter.formatMessageTextComponent(
                "party_invite_received",
                invitedPlayer.getLocale().toLowerCase(), slugs);

        invitedPlayer.spigot().sendMessage(invitedMessage);
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }
}