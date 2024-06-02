package tv.bermu.cloverrpg.commands.subcommands.character;

import java.util.Set;

import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;

public class CreateSubCommand implements SubCommand {

    private final String permission;
    private final MessageFormatter messageFormatter;
    private final Set<Player> creatingCharacter;

    public CreateSubCommand(MessageFormatter messageFormatter, String permission, Set<Player> creatingCharacter) {
        this.permission = permission;
        this.messageFormatter = messageFormatter;
        this.creatingCharacter = creatingCharacter;
    }

    @Override
    public void execute(Player player, String[] args) {
        String playerLanguage = player.getLocale();

        if (creatingCharacter.contains(player)) {
            player.sendMessage(
                    messageFormatter.formatMessageDefaultSlugs("character_creation_already_in_progress",
                            playerLanguage));
            return;

        }
        player.sendMessage(
                messageFormatter.formatMessageDefaultSlugs("character_creation_start", playerLanguage));
        creatingCharacter.add(player);
        // remove this :
        // player.openInventory(customClassInventory.getInventory());
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }
}
