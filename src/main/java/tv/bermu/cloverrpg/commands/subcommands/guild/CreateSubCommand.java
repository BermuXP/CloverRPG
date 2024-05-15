package tv.bermu.cloverrpg.commands.subcommands.guild;

import java.util.HashMap;

import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.db.handlers.GuildHandler;

public class CreateSubCommand implements SubCommand {

    private final String permission;
    private final MessageFormatter messageFormatter;
    private final GuildHandler guildHandler;

    public CreateSubCommand(GuildHandler guildHandler, MessageFormatter messageFormatter, String permission) {
        this.messageFormatter = messageFormatter;
        this.permission = permission;
        this.guildHandler = guildHandler;
    }

    @Override
    public void execute(Player player, String[] args) {
        String playerLanguage = player.getLocale().toLowerCase();
        if (args[1] == null) {
            HashMap<String, Object> slugs = new HashMap<>();
            slugs.put("command_name", "guild");
            slugs.put("subcommand", "create");
            slugs.put("usage", "<guild_name>");
            player.sendMessage(
                    messageFormatter.formatMessage("subcommand_usage", playerLanguage, slugs));
            return;
        }

        // already have a guild
        
        // already in a guild

        // guild name already exists
        

        player.sendMessage(guildHandler.createGuild(args[1], player,
                player.getLocale().toLowerCase()));
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }
}