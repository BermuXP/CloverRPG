package tv.bermu.cloverrpg.commands.subcommands.character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;

public class CancelSubCommand implements SubCommand {

    private final String permission;
    private final MessageFormatter messageFormatter;
    private final Set<Player> creatingCharacter;

    public CancelSubCommand(MessageFormatter messageFormatter, String permission, Set<Player> creatingCharacter) {
        this.permission = permission;
        this.messageFormatter = messageFormatter;
        this.creatingCharacter = creatingCharacter;
    }

    @Override
    public void execute(Player player, String[] args) {
        String playerLanguage = player.getLocale();
        if (creatingCharacter.contains(player)) {
            creatingCharacter.remove(player);
            player.sendMessage(
                    messageFormatter.formatMessageDefaultSlugs("character_creation_cancelled",
                            playerLanguage));
        } else {
            player.sendMessage(
                    messageFormatter.formatMessageDefaultSlugs("no_character_creation_in_progress",
                            playerLanguage));
        }
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }
}