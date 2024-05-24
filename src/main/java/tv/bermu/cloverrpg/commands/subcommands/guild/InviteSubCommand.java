package tv.bermu.cloverrpg.commands.subcommands.guild;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.db.handlers.GuildHandler;

public class InviteSubCommand implements SubCommand {

    private final String permission;
    private final MessageFormatter messageFormatter;
    private final GuildHandler guildHandler;
    private final JavaPlugin plugin;

    public InviteSubCommand(JavaPlugin plugin, GuildHandler guildHandler, MessageFormatter messageFormatter,
            String permission) {
        this.messageFormatter = messageFormatter;
        this.permission = permission;
        this.guildHandler = guildHandler;
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        String playerLanguage = player.getLocale().toLowerCase();
        HashMap<String, Object> slugs = new HashMap<>();
        if (args[1] == null) {
            slugs.put("command_name", "guild");
            slugs.put("subcommand", "invite");
            slugs.put("usage", "<guild_name>");
            player.sendMessage(
                    messageFormatter.formatMessage("subcommand_usage", playerLanguage, slugs));
            return;
        }

        String invitedPlayerName = args[1];

        String playerUUID = player.getUniqueId().toString();

        // youre not in a guild
        if (!guildHandler.userInGuild(playerUUID)) {
            player.sendMessage(messageFormatter.formatMessageDefaultSlugs("player_not_in_guild", playerLanguage));
            return;
        }

        // you dont have the guild permissions to invite
        if (!guildHandler.userHasGuildPermission(playerUUID, "invite")) {
            player.sendMessage(messageFormatter.formatMessage("no_permission", playerLanguage, slugs));
            return;
        }

        // guild is full

        Player invitedPlayer = plugin.getServer().getPlayer(invitedPlayerName);

        // player not found
        if (invitedPlayer == null) {
            slugs.put("player_name", invitedPlayerName);
            player.sendMessage(
                    messageFormatter.formatMessage("player_not_found", playerLanguage, slugs));
        }

        // player is not online
        if (!invitedPlayer.isOnline()) {
            slugs.put("player_name", invitedPlayer.getDisplayName());
            player.sendMessage(
                    messageFormatter.formatMessage("player_not_online", playerLanguage, slugs));
        }

        // cant invite yourself
        if (player.equals(invitedPlayer)) {
            player.sendMessage(messageFormatter.formatMessage("cant_invite_self", playerLanguage, slugs));
            return;
        }

        // player already in a guild
        if (guildHandler.userInGuild(invitedPlayer.getUniqueId().toString())) {
            player.sendMessage(messageFormatter.formatMessageDefaultSlugs("player_not_in_guild", playerLanguage));
            return;
        }

        // invite success
        player.sendMessage(messageFormatter.formatMessage("guild_invite", playerLanguage, slugs));
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }

}
