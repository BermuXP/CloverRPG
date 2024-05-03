package tv.bermu.cloverrpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import tv.bermu.cloverrpg.db.handlers.PartyHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.TabCompleter;

public class PartyCommand implements CommandExecutor, TabCompleter {

    private final Map<String, String> subcommands = new HashMap<>();
    private final PartyHandler partyHandler;
    private final Plugin plugin;
    private final MessageFormatter messageFormatter;

    /**
     * Constructor for the PartyCommand class
     */
    public PartyCommand(Plugin plugin, PartyHandler partyHandler, MessageFormatter messageFormatter) {
        this.plugin = plugin;
        this.partyHandler = partyHandler;
        this.messageFormatter = messageFormatter;
        
        // Initialize subcommands with their corresponding permissions
        subcommands.put("create", "cloverrpg.commands.party.create");
        subcommands.put("invite", "cloverrpg.commands.party.invite");
        subcommands.put("accept", "cloverrpg.commands.party.accept");
        subcommands.put("kick", "cloverrpg.commands.party.kick");
        subcommands.put("leave", "cloverrpg.commands.party.leave");
        subcommands.put("list", "cloverrpg.commands.party.list");
        subcommands.put("help", "cloverrpg.commands.party.help");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            // Display help or usage guide for the party command
            player.sendMessage("Usage: /party <subcommand>");
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
                            player.sendMessage("Usage: /party create <party_name>");
                            return true;
                        }
                        player.sendMessage(partyHandler.createParty(args[1], player.getUniqueId(),
                                player.getLocale().toLowerCase()));
                        break;
                    case "disband":
                        player.sendMessage(
                                partyHandler.deleteParty(player.getUniqueId(), player.getLocale().toLowerCase()));
                        break;
                    case "invite":
                        if (args.length < 2) {
                            player.sendMessage("Usage: /party invite <player_name>");
                            return true;
                        }
                        String invitedPlayerName = args[1];
                        Player invitedPlayer = plugin.getServer().getPlayer(invitedPlayerName);
                        if (invitedPlayer == null) {
                            player.sendMessage("Player not found: " + invitedPlayerName);
                            return true;
                        }
                        if (!invitedPlayer.isOnline()) {
                            player.sendMessage("Player is not online: " + invitedPlayerName);
                            return true;
                        }
                        player.getDisplayName();


                        HashMap<String, Object> slugs = new HashMap<>();
                        slugs.put("invited_player", monster.getName());
                        messageFormatter.formatMessage("party_invite", player.getLocale().toLowerCase(), slugs);
                        invitedPlayer.sendMessage();
                        break;
                    case "accept":
                        // Logic for accepting a party invitation
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
                player.sendMessage("You don't have permission to use this command.");
            }
        } else {
            player.sendMessage("Unknown subcommand. Use /party help for a list of commands.");
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
