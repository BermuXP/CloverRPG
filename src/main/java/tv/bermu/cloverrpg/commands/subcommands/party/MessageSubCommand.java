package tv.bermu.cloverrpg.commands.subcommands.party;

import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.db.handlers.PartyHandler;

public class MessageSubCommand implements SubCommand {

    private final PartyHandler partyHandler;
    private final MessageFormatter messageFormatter;
    private final String permission;

    public MessageSubCommand(PartyHandler partyHandler, MessageFormatter messageFormatter, String permission) {
        this.partyHandler = partyHandler;
        this.messageFormatter = messageFormatter;
        this.permission = permission;
    }
    @Override
    public void execute(Player player, String[] args) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }
}
