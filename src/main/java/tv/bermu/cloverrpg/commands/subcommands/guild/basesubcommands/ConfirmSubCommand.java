package tv.bermu.cloverrpg.commands.subcommands.guild.basesubcommands;

import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;

public class ConfirmSubCommand implements SubCommand{
  
    private final String permission;
    private final MessageFormatter messageFormatter;

    public ConfirmSubCommand(MessageFormatter messageFormatter, String permission) {
        this.permission = permission;
        this.messageFormatter = messageFormatter;
    }

    @Override
    public void execute(Player player, String[] args) {
        String playerLanguage = player.getLocale();
        // todo configm current player is in the process of creating a guild base
        player.sendMessage(messageFormatter.formatMessageDefaultSlugs("guild_base_creation_confirmed", playerLanguage));
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }
}
