package tv.bermu.cloverrpg.commands.subcommands.guild;


import java.util.HashMap;

import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.db.handlers.GuildHandler;

public class InviteSubCommand implements SubCommand{

    private final String permission;
    private final MessageFormatter messageFormatter;
    private final GuildHandler guildHandler;

    public InviteSubCommand(GuildHandler guildHandler, MessageFormatter messageFormatter, String permission) {
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
            slugs.put("subcommand", "invite");
            slugs.put("usage", "<guild_name>");
            player.sendMessage(
                    messageFormatter.formatMessage("subcommand_usage", playerLanguage, slugs));
            return;
        }

        // youre not in a guiild
        // you dont have the guild permissions to invite
        // player not found
        // player already in a guild
        // guild is full
        // player is already in a guild
        // player is not online
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }

}
