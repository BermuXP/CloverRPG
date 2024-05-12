package tv.bermu.cloverrpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import tv.bermu.cloverrpg.Main;
import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.commands.subcommands.party.CreateSubCommand;
import tv.bermu.cloverrpg.commands.subcommands.party.DisbandSubCommand;
import tv.bermu.cloverrpg.commands.subcommands.party.InviteAcceptSubCommand;
import tv.bermu.cloverrpg.commands.subcommands.party.InviteDeclineSubCommand;
import tv.bermu.cloverrpg.commands.subcommands.party.InviteSubCommand;
import tv.bermu.cloverrpg.commands.subcommands.party.MessageSubCommand;
import tv.bermu.cloverrpg.db.handlers.PartyHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.TabCompleter;

public class PartyCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subcommands = new HashMap<>();
    private final MessageFormatter messageFormatter;

    /**
     * Constructor for the PartyCommand class
     */
    public PartyCommand(JavaPlugin plugin, PartyHandler partyHandler, MessageFormatter messageFormatter) {
        this.messageFormatter = messageFormatter;

        subcommands.put("create",
                new CreateSubCommand(partyHandler, messageFormatter, Main.baseCommandPermission + "party.invite"));
        subcommands.put("disband",
                new DisbandSubCommand(partyHandler, messageFormatter, Main.baseCommandPermission + "party.disband"));

        InviteSubCommand inviteSubCommand = new InviteSubCommand(messageFormatter,
                Main.baseCommandPermission + "party.invite", plugin);
        subcommands.put("invite", inviteSubCommand);
        subcommands.put("inv", inviteSubCommand);

        InviteAcceptSubCommand acceptInviteSubCommand = new InviteAcceptSubCommand(messageFormatter,
                Main.baseCommandPermission + "party.invite.accept");
        subcommands.put("accept", acceptInviteSubCommand);
        subcommands.put("a", acceptInviteSubCommand);

        InviteDeclineSubCommand declineInviteSubCommand = new InviteDeclineSubCommand(messageFormatter,
                Main.baseCommandPermission + "party.invite.decline");
        subcommands.put("decline", declineInviteSubCommand);
        subcommands.put("d", declineInviteSubCommand);

        MessageSubCommand partyMessage = new MessageSubCommand(partyHandler, messageFormatter,
                Main.baseCommandPermission + "party.message");
        subcommands.put("message", partyMessage);
        subcommands.put("msg", partyMessage);
        subcommands.put("m", partyMessage);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
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

        String subCommandText = args[0].toLowerCase();

        if (subcommands.containsKey(subCommandText)) {
            SubCommand subCommand = subcommands.get(subCommandText);
            if (subCommand.hasPermission(player)) {
                subCommand.execute(player, args);
            } else {
                player.sendMessage(
                        messageFormatter.formatMessageDefaultSlugs("no_permissions_command", player.getLocale()));
            }
        } else {
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
