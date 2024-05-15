package tv.bermu.cloverrpg.commands.subcommands.party;

import java.util.HashMap;

import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.db.handlers.PartyHandler;

public class CreateSubCommand implements SubCommand {

    private final PartyHandler partyHandler;
    private final MessageFormatter messageFormatter;
    private final String permission;

    public CreateSubCommand(PartyHandler partyHandler, MessageFormatter messageFormatter, String permission) {
        this.partyHandler = partyHandler;
        this.messageFormatter = messageFormatter;
        this.permission = permission;
    }

    @Override
    public void execute(Player player, String[] args) {
        String playerLanguage = player.getLocale().toLowerCase();
        if (args[1] == null) {
            HashMap<String, Object> slugs = new HashMap<>();
            slugs.put("command_name", "party");
            slugs.put("subcommand", "create");
            slugs.put("usage", "<party_name>");
            player.sendMessage(
                    messageFormatter.formatMessage("subcommand_usage", playerLanguage, slugs));
            return;
        }
        
        player.sendMessage(partyHandler.createParty(args[1], player,
                player.getLocale().toLowerCase()));
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }
}