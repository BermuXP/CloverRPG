package tv.bermu.cloverrpg.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import tv.bermu.cloverrpg.Main;
import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.commands.subcommands.guild.BaseSubCommand;
import tv.bermu.cloverrpg.commands.subcommands.guild.CreateSubCommand;
import tv.bermu.cloverrpg.commands.subcommands.guild.InfoSubCommand;
import tv.bermu.cloverrpg.commands.subcommands.guild.InviteSubCommand;
import tv.bermu.cloverrpg.commands.subcommands.guild.ListSubCommand;
import tv.bermu.cloverrpg.commands.subcommands.guild.TeleportSubCommand;
import tv.bermu.cloverrpg.db.handlers.GuildHandler;
import tv.bermu.cloverrpg.listeners.CombatListener;

public class GuildCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subcommands = new HashMap<>();
    private final MessageFormatter messageFormatter;

    public GuildCommand(JavaPlugin javaPlugin, GuildHandler guildHandler, MessageFormatter messageFormatter,
            CombatListener combatListener) {
        this.messageFormatter = messageFormatter;
        subcommands.put("create",
                new CreateSubCommand(guildHandler, messageFormatter, Main.baseCommandPermission + "guild.create",
                        combatListener));
        subcommands.put("base",
                new BaseSubCommand(guildHandler, messageFormatter, Main.baseCommandPermission + "guild.base",
                        combatListener, javaPlugin));
        subcommands.put("info",
                new InfoSubCommand(guildHandler, messageFormatter, Main.baseCommandPermission + "guild.info"));
        subcommands.put("list",
                new ListSubCommand(guildHandler, messageFormatter, Main.baseCommandPermission + "guild.list"));
        subcommands.put("invite",
                new InviteSubCommand(javaPlugin, guildHandler, messageFormatter,
                        Main.baseCommandPermission + "guild.invite"));
        
        TeleportSubCommand tpSubCommand = new TeleportSubCommand(guildHandler, messageFormatter,
                Main.baseCommandPermission + "guild.teleport");
        subcommands.put("teleport",tpSubCommand);
        subcommands.put("tp",tpSubCommand);
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
            slugs.put("command_name", "guild");
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
            slugs.put("command_name", "guild");
            player.sendMessage(messageFormatter.formatMessage("unknown_command", playerLanguage, slugs));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) { // Only suggest subcommands if no arguments are provided beyond the first one
            String partialSubcommand = args[0].toLowerCase();
            for (String subcommand : subcommands.keySet()) {
                if (subcommand.startsWith(partialSubcommand)) {
                    completions.add(subcommand);
                }
            }
        } else if (args.length >= 2) { // Suggest subcommands of the subcommand
            SubCommand subcommand = subcommands.get(args[0].toLowerCase());
            if (subcommand != null && subcommand.hasSubCommands()) {
                String partialSubSubcommand = args[1].toLowerCase();
                for (String subSubcommand : subcommand.getSubCommands().keySet()) {
                    if (subSubcommand.startsWith(partialSubSubcommand)) {
                        completions.add(subSubcommand);
                    }
                }
            }
        }
        return completions;
    }
}
