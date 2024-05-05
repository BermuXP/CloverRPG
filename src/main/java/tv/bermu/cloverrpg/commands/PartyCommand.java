package tv.bermu.cloverrpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.chat.TextComponent;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.db.handlers.PartyHandler;
import tv.bermu.cloverrpg.managers.PartyInvitesManager;
import tv.bermu.cloverrpg.managers.PartyManager;
import tv.bermu.cloverrpg.models.PartyModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.TabCompleter;

public class PartyCommand implements CommandExecutor, TabCompleter {

    private final Map<String, String> subcommands = new HashMap<>();
    private final PartyHandler partyHandler;
    private final JavaPlugin plugin;
    private final MessageFormatter messageFormatter;

    /**
     * Constructor for the PartyCommand class
     */
    public PartyCommand(JavaPlugin plugin, PartyHandler partyHandler, MessageFormatter messageFormatter) {
        this.plugin = plugin;
        this.partyHandler = partyHandler;
        this.messageFormatter = messageFormatter;

        // TODO Initialize subcommands with their corresponding permissions from another
        // file/location?
        initializeSubcommands();
    }

    /**
     * Initialize subcommands with their corresponding permissions
     */
    private void initializeSubcommands() {
        subcommands.put("create", "cloverrpg.commands.party.create");
        subcommands.put("invite", "cloverrpg.commands.party.invite");
        subcommands.put("accept", "cloverrpg.commands.party.accept");
        subcommands.put("decline", "cloverrpg.commands.party.decline");
        subcommands.put("kick", "cloverrpg.commands.party.kick");
        subcommands.put("leave", "cloverrpg.commands.party.leave");
        subcommands.put("list", "cloverrpg.commands.party.list");
        subcommands.put("help", "cloverrpg.commands.party.help");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            // No so we default to english, possible change this to server language if that
            // is a thing?
            sender.sendMessage(messageFormatter.formatMessageDefaultSlugs("only_players_can_execute_command", "en_GB"));
            return true;
        }

        Player player = (Player) sender;
        String playerLanguage = player.getLocale().toLowerCase();
        if (args.length == 0) {
            HashMap<String, Object> slugs = new HashMap<>();
            slugs.put("command_name", "party");
            slugs.put("subcommand", "<subcommand>");
            slugs.put("usage", "");
            player.sendMessage(messageFormatter.formatMessage("subcommand_usage", playerLanguage, slugs));
            return true;
        }

        String subCommand = args[0].toLowerCase();

        // Check if the subcommand exists and if the player has permission to use it
        if (subcommands.containsKey(subCommand)) {
            String permission = subcommands.get(subCommand);
            if (player.hasPermission(permission)) {
                // Execute logic for the subcommand
                switch (subCommand) {
                    case "create":
                        if (args[1] == null) {
                            HashMap<String, Object> slugs = new HashMap<>();
                            slugs.put("command_name", "party");
                            slugs.put("subcommand", "create");
                            slugs.put("usage", "<party_name>");
                            player.sendMessage(
                                    messageFormatter.formatMessage("subcommand_usage", playerLanguage, slugs));
                            return true;
                        }

                        player.sendMessage(partyHandler.createParty(args[1], player,
                                player.getLocale().toLowerCase()));
                        break;
                    case "disband":
                        player.sendMessage(
                                partyHandler.deleteParty(player.getUniqueId(), player.getLocale().toLowerCase()));
                        break;
                    case "invite":
                        if (args.length < 2) {
                            HashMap<String, Object> slugs = new HashMap<>();
                            slugs.put("command_name", "party");
                            slugs.put("subcommand", "invite");
                            slugs.put("usage", "<player_name>");
                            player.sendMessage(
                                    messageFormatter.formatMessage("subcommand_usage", playerLanguage, slugs));
                            return true;
                        }

                        PartyManager partyManager = PartyManager.getInstance();
                        PartyModel party = partyManager.getPartyOfPlayer(player.getUniqueId());
                        if (party == null) {
                            player.sendMessage(
                                    messageFormatter.formatMessageDefaultSlugs("not_in_party", playerLanguage));
                            return true;
                        }

                        // TODO cant invite anymore players

                        String invitedPlayerName = args[1];
                        Player invitedPlayer = plugin.getServer().getPlayer(invitedPlayerName);
                        HashMap<String, Object> slugs = new HashMap<>();

                        // player not found
                        if (invitedPlayer == null) {
                            slugs.put("player_name", invitedPlayerName);
                            player.sendMessage(
                                    messageFormatter.formatMessage("player_not_found", playerLanguage, slugs));
                            return true;
                        }
                        // player is not online
                        if (!invitedPlayer.isOnline()) {
                            slugs.put("player_name", invitedPlayer.getDisplayName());
                            player.sendMessage(
                                    messageFormatter.formatMessage("player_not_online", playerLanguage, slugs));
                            return true;
                        }

                        // cant invite yourself
                        if (player.getUniqueId() == invitedPlayer.getUniqueId()) {
                            player.sendMessage(
                                    messageFormatter.formatMessageDefaultSlugs("party_invite_self", playerLanguage));
                            return true;
                        }

                        // cant invite players that are already in the party
                        if (party.getMembers().contains(player.getUniqueId())) {
                            player.sendMessage(
                                    messageFormatter.formatMessageDefaultSlugs("player_already_in_the_party",
                                            playerLanguage));
                            return true;
                        }

                        // cant invite players that are already in ANOTHER the party
                        if (partyManager.getPartyOfPlayer(invitedPlayer.getUniqueId()) != null) {
                            player.sendMessage(
                                    messageFormatter.formatMessageDefaultSlugs("player_already_in_another_the_party",
                                            playerLanguage));
                            return true;
                        }

                        // cant invite players that are already invited
                        PartyInvitesManager partyInvitesManager = PartyInvitesManager.getInstance();
                        if (partyInvitesManager.hasPartyInvite(invitedPlayer.getUniqueId(), party.getPartyId())) {
                            player.sendMessage(
                                    messageFormatter.formatMessageDefaultSlugs("player_already_invited",
                                            playerLanguage));
                            return true;
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

                        // figure out how this influences possible paper api support
                        invitedPlayer.spigot().sendMessage(invitedMessage);
                        break;
                    case "accept":
                        player.sendMessage("You've joined");
                        break;
                    case "decline":
                        player.sendMessage("Declined.");
                        break;
                    case "kick":
                        // Logic for kicking a player from the party
                        break;
                    case "leave":
                        // Logic for leaving the party
                        break;
                    case "list":
                        // Logic for listing members of the party
                        break;
                    case "help":
                        // Display help or usage guide for the party command
                        break;
                }
            } else {
                player.sendMessage(
                        messageFormatter.formatMessageDefaultSlugs("no_permissions_command", playerLanguage));
            }
        } else

        {
            HashMap<String, Object> slugs = new HashMap<>();
            slugs.put("command_name", "party");
            player.sendMessage(messageFormatter.formatMessage("unknown_command", playerLanguage, slugs));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) { // Only suggest subcommands if no arguments are provided beyond the first one
            List<String> completions = new ArrayList<>();
            String partialSubcommand = args[0].toLowerCase();
            for (String subcommand : subcommands.keySet()) {
                if (subcommand.startsWith(partialSubcommand)) {
                    completions.add(subcommand);
                }
            }
            return completions;
        }
        return null;
    }
}
