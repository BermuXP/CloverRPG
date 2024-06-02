package tv.bermu.cloverrpg.commands.subcommands.classes;

import org.bukkit.entity.Player;

import tv.bermu.cloverrpg.Main;
import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;


public class SelectSubCommand implements SubCommand {

    private final String permission;
    private final MessageFormatter messageFormatter;

    public SelectSubCommand(MessageFormatter messageFormatter, String permission) {
        this.messageFormatter = messageFormatter;
        this.permission = permission;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length > 0 && args[args.length - 1].equals(Main.uniqueInventoryIdentifier)) {
            player.sendMessage("wow, it worked... thats actually crazy");
        } else {
            // The command did not come from the inventory click
            player.sendMessage("command executed without unque identifier");
        }
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }
}
