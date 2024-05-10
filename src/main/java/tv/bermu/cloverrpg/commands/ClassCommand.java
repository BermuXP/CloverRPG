package tv.bermu.cloverrpg.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import tv.bermu.cloverrpg.Main;
import tv.bermu.cloverrpg.MessageFormatter;

public class ClassCommand implements CommandExecutor {

    private final Map<String, String> subcommands = new HashMap<>();
    private Inventory fakeInventory = null;
    private MessageFormatter messageFormatter;
    private JavaPlugin plugin;

    /**
     * Constructor
     * 
     * @param plugin Plugin instance
     * @param config Configuration section of the classes
     */
    public ClassCommand(JavaPlugin plugin, ConfigurationSection classesSection, MessageFormatter messageFormatter) {
        this.messageFormatter = messageFormatter;
        this.plugin = plugin;
        initializeSubcommands();
    }

    /**
     * Initialize subcommands with their corresponding permissions
     */
    private void initializeSubcommands() {
        subcommands.put("select", "cloverrpg.commands.classes.select");
        subcommands.put("help", "cloverrpg.commands.classes.help");
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
        String subCommand = args[0].toLowerCase();
        if (subcommands.containsKey(subCommand)) {
            String permission = subcommands.get(subCommand);
            if (player.hasPermission(permission)) {
                // Execute logic for the subcommand
                switch (subCommand) {
                    case "select":
                        if (args.length > 0 && args[args.length - 1].equals(Main.uniqueInventoryIdentifier)) {
                            player.sendMessage("wow, it worked... thats actually crazy");
                        } else {
                            // The command did not come from the inventory click
                            player.sendMessage("command executed without unque identifier");
                        }
                        break;
                }
            }
        }

        if (fakeInventory != null) {
            player.openInventory(fakeInventory);
        }
        return true;
    }

}
