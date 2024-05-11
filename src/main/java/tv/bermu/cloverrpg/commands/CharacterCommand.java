package tv.bermu.cloverrpg.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.utils.CustomInventory;

public class CharacterCommand implements CommandExecutor {

    private JavaPlugin plugin;
    private MessageFormatter messageFormatter;
    private final Map<String, String> subcommands = new HashMap<>();
    private CustomInventory customClassInventory;
    private Set<Player> creatingCharacter;

    public CharacterCommand(JavaPlugin plugin, MessageFormatter messageFormatter, CustomInventory classesInventory,
            Set<Player> creatingCharacter) {
        this.plugin = plugin;
        this.messageFormatter = messageFormatter;
        this.customClassInventory = classesInventory;
        this.creatingCharacter = creatingCharacter;
        initializeSubcommands();
    }

    /**
     * Initialize subcommands with their corresponding permissions
     */
    private void initializeSubcommands() {
        subcommands.put("select", "cloverrpg.commands.character.select");
        subcommands.put("create", "cloverrpg.commands.character.create");
        subcommands.put("help", "cloverrpg.commands.character.help");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            // No so we default to english, possible change this to server language if that
            // is a thing?
            sender.sendMessage(messageFormatter.formatMessageDefaultSlugs("only_players_can_execute_command", "en_GB"));
            return true;
        }

        Player player = (Player) sender;
        String playerLanguage = player.getLocale();
        String subCommand = args[0].toLowerCase();
        if (subcommands.containsKey(subCommand)) {
            String permission = subcommands.get(subCommand);
            if (player.hasPermission(permission)) {
                // Execute logic for the subcommand
                switch (subCommand) {
                    case "create":
                        player.sendMessage(
                                messageFormatter.formatMessageDefaultSlugs("character_creation_start", playerLanguage));
                        creatingCharacter.add(player);
                        // remove this :
                        // player.openInventory(customClassInventory.getInventory());
                        break;
                    case "cancel":
                        if (creatingCharacter.contains(player)) {
                            creatingCharacter.remove(player);
                            player.sendMessage(
                                    messageFormatter.formatMessageDefaultSlugs("character_creation_cancelled", playerLanguage));
                        } else {
                            player.sendMessage(
                                    messageFormatter.formatMessageDefaultSlugs("no_character_creation_in_progress", playerLanguage));
                        }
                    case "select":
                        break;
                }
            }
        }

        player.sendMessage("Hello, " + player.getName() + "!");
        return true;
    }

}
