package tv.bermu.cloverrpg.commands.subcommands.guild.basesubcommands;

import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.db.handlers.GuildHandler;

public class InfoSubCommand implements SubCommand {
    
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
        
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }
}
