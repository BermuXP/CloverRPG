package tv.bermu.cloverrpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CRPGCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute commands!");
            return true;
        }

        Player player = (Player) sender;
        player.sendMessage("Hello, " + player.getName() + "!");
        return true;
    }
}