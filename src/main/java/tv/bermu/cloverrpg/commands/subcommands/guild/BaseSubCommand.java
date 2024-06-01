package tv.bermu.cloverrpg.commands.subcommands.guild;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.db.handlers.GuildHandler;
import tv.bermu.cloverrpg.listeners.CombatListener;
import tv.bermu.cloverrpg.commands.subcommands.guild.basesubcommands.CreateSubCommand;
import tv.bermu.cloverrpg.commands.subcommands.guild.basesubcommands.InfoSubCommand;

public class BaseSubCommand implements SubCommand {

    private final String permission;
    private final MessageFormatter messageFormatter;
    private final GuildHandler guildHandler;
    private final Map<String, SubCommand> subcommands = new HashMap<>();
    private final CombatListener combatListener;

    public BaseSubCommand(GuildHandler guildHandler, MessageFormatter messageFormatter, String permission,
            CombatListener combatListener) {
        this.messageFormatter = messageFormatter;
        this.permission = permission;
        this.guildHandler = guildHandler;
        this.combatListener = combatListener;
        subcommands.put("create",
                new CreateSubCommand(guildHandler, messageFormatter, permission + ".create", combatListener));

        subcommands.put("cancel",
                new InfoSubCommand(guildHandler, messageFormatter, permission + ".cancel"));

    }

    @Override
    public void execute(Player player, String[] args) {
        String playerLanguage = player.getLocale().toLowerCase();
        if (args.length <= 1) {
            HashMap<String, Object> slugs = new HashMap<>();
            slugs.put("command_name", "guild base");
            slugs.put("subcommand", "<subcommand>");
            slugs.put("usage", "");
            player.sendMessage(messageFormatter.formatMessage("subcommand_usage", playerLanguage, slugs));
            return;
        }

        String subCommandText = args[1].toLowerCase();

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
            slugs.put("command_name", "guild base");
            player.sendMessage(messageFormatter.formatMessage("unknown_command", playerLanguage, slugs));
        }
        return;
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }

    @Override
    public boolean hasSubCommands() {
        return !subcommands.isEmpty();
    }

    @Override
    public Map<String, SubCommand> getSubCommands() {
        return subcommands;
    }
}