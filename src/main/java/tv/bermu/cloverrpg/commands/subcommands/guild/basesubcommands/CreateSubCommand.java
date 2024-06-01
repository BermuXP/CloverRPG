package tv.bermu.cloverrpg.commands.subcommands.guild.basesubcommands;

import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.Main;
import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.db.handlers.GuildHandler;
import tv.bermu.cloverrpg.listeners.CombatListener;

public class CreateSubCommand implements SubCommand {

    private final String permission;
    private final MessageFormatter messageFormatter;
    private final GuildHandler guildHandler;
    private CombatListener combatListener;

    public CreateSubCommand(GuildHandler guildHandler, MessageFormatter messageFormatter, String permission,
            CombatListener combatListener) {
        this.messageFormatter = messageFormatter;
        this.permission = permission;
        this.guildHandler = guildHandler;
        this.combatListener = combatListener;
    }

    @Override
    public void execute(Player player, String[] args) {
        String playerLanguage = player.getLocale().toLowerCase();
        if (combatListener.isInCombat(player)) {
            player.sendMessage(
                    messageFormatter.formatMessageDefaultSlugs("combat_cannot_perform_action", playerLanguage));
            return;
        }
        player.sendMessage("Not in combat");
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }
}
