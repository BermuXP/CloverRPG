package tv.bermu.cloverrpg.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import tv.bermu.cloverrpg.Main;
import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.commands.subcommands.guild.CreateSubCommand;
import tv.bermu.cloverrpg.commands.subcommands.guild.InfoSubCommand;
import tv.bermu.cloverrpg.commands.subcommands.guild.InviteSubCommand;
import tv.bermu.cloverrpg.commands.subcommands.guild.ListSubCommand;
import tv.bermu.cloverrpg.db.handlers.GuildHandler;

public class GuildCommand implements CommandExecutor {

    private final Map<String, SubCommand> subcommands = new HashMap<>();

    public GuildCommand(JavaPlugin javaPlugin, GuildHandler guildHandler, MessageFormatter messageFormatter) {
        subcommands.put("create",
                new CreateSubCommand(guildHandler, messageFormatter, Main.baseCommandPermission + "guild.create"));
        subcommands.put("info",
                new InfoSubCommand(guildHandler, messageFormatter, Main.baseCommandPermission + "guild.info"));
        subcommands.put("list",
                new ListSubCommand(guildHandler, messageFormatter, Main.baseCommandPermission + "guild.list"));
        subcommands.put("invite",
                new InviteSubCommand(javaPlugin, guildHandler, messageFormatter, Main.baseCommandPermission + "guild.invite"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

}
