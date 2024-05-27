package tv.bermu.cloverrpg.commands.subcommands.guild;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.db.handlers.GuildHandler;
import tv.bermu.cloverrpg.commands.subcommands.guild.basesubcommands.CreateSubCommand;
import tv.bermu.cloverrpg.commands.subcommands.guild.basesubcommands.InfoSubCommand;

public class BaseSubCommand implements SubCommand {

    private final String permission;
    private final MessageFormatter messageFormatter;
    private final GuildHandler guildHandler;
    private final Map<String, SubCommand> subcommands = new HashMap<>();

    public BaseSubCommand(GuildHandler guildHandler, MessageFormatter messageFormatter, String permission) {
        this.messageFormatter = messageFormatter;
        this.permission = permission;
        this.guildHandler = guildHandler;

        subcommands.put("create",
                new CreateSubCommand(guildHandler, messageFormatter, permission + ".create"));

        subcommands.put("cancel",
                new InfoSubCommand(guildHandler, messageFormatter, permission + ".cancel"));
    }

    @Override
    public void execute(Player player, String[] args) {
        String playerLanguage = player.getLocale().toLowerCase();

    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }

}