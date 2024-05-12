package tv.bermu.cloverrpg.commands.subcommands.party;

import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.db.handlers.PartyHandler;

public class DisbandSubCommand implements SubCommand {

    private final String permission;
    private final MessageFormatter messageFormatter;
    private final PartyHandler partyHandler;

    public DisbandSubCommand(PartyHandler partyHandler, MessageFormatter messageFormatter, String permission) {
        this.permission = permission;
        this.messageFormatter = messageFormatter;
        this.partyHandler = partyHandler;
    }

    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage(
            partyHandler.deleteParty(player.getUniqueId(), player.getLocale().toLowerCase()));
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }
}
