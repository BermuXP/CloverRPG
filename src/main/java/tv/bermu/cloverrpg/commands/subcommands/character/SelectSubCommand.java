package tv.bermu.cloverrpg.commands.subcommands.character;

import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;

public class SelectSubCommand implements SubCommand {

    private final String permission;
    private final MessageFormatter messageFormatter;

    public SelectSubCommand(MessageFormatter messageFormatter, String permission) {
        this.messageFormatter = messageFormatter;
        this.permission = permission;
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