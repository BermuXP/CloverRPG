package tv.bermu.cloverrpg.commands.subcommands.party;

import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;

public class InviteDeclineSubCommand implements SubCommand {

    private final String permission;
    private final MessageFormatter messageFormatter;

    public InviteDeclineSubCommand(MessageFormatter messageFormatter, String permission) {
        this.messageFormatter = messageFormatter;
        this.permission = permission;
    }

    @Override
    public void execute(Player player, String[] args) {

    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }

}
