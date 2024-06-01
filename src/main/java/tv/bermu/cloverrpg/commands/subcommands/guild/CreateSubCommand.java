package tv.bermu.cloverrpg.commands.subcommands.guild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.db.handlers.GuildHandler;
import tv.bermu.cloverrpg.listeners.CombatListener;

public class CreateSubCommand implements SubCommand {

    private final String permission;
    private final MessageFormatter messageFormatter;
    private final GuildHandler guildHandler;
    private final CombatListener combatListener;

    public CreateSubCommand(GuildHandler guildHandler, MessageFormatter messageFormatter, String permission, CombatListener combatListener) {
        this.messageFormatter = messageFormatter;
        this.permission = permission;
        this.guildHandler = guildHandler;
        this.combatListener = combatListener;
    }

    @Override
    public void execute(Player player, String[] args) {
        String playerLanguage = player.getLocale().toLowerCase();
        HashMap<String, Object> slugs = new HashMap<>();
        if (args[1] == null) {
            slugs.put("command_name", "guild");
            slugs.put("subcommand", "create");
            slugs.put("usage", "<guild_name>");
            player.sendMessage(
                    messageFormatter.formatMessage("subcommand_usage", playerLanguage, slugs));
            return;
        }

        String playerUUID = player.getUniqueId().toString();
        
        // already in a guild
        if (guildHandler.userInGuild(playerUUID)) {
            player.sendMessage(messageFormatter.formatMessageDefaultSlugs("player_already_in_a_guild", playerLanguage));
            return;
        }
        
        // guild name already exists
        String chosenGuildName = args[1];
        if (guildHandler.guildNameTaken(chosenGuildName)) {
            slugs.put("guild_name", chosenGuildName);   
            player.sendMessage(messageFormatter.formatMessage("guild_name_taken", playerLanguage, slugs));
            return;
        }

        player.sendMessage(guildHandler.createGuild(chosenGuildName, playerUUID,
                playerLanguage));
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }
}