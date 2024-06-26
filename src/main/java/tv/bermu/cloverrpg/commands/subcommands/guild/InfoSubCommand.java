package tv.bermu.cloverrpg.commands.subcommands.guild;



import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.db.handlers.GuildHandler;

public class InfoSubCommand implements SubCommand{

    private final String permission;
    private final MessageFormatter messageFormatter;
    private final GuildHandler guildHandler;

    public InfoSubCommand(GuildHandler guildHandler, MessageFormatter messageFormatter, String permission) {
        this.messageFormatter = messageFormatter;
        this.permission = permission;
        this.guildHandler = guildHandler;
    }

    @Override
    public void execute(Player player, String[] args) {
        String playerLanguage = player.getLocale().toLowerCase();
        // player is not in a guild
        if (!guildHandler.userInGuild(player.getUniqueId().toString())) {
            player.sendMessage(messageFormatter.formatMessageDefaultSlugs("player_not_in_guild", playerLanguage));
            return;
        }
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }

}
