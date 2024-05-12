package tv.bermu.cloverrpg;

import org.bukkit.entity.Player;

public interface SubCommand {
    void execute(Player player, String[] args);

    boolean hasPermission(Player player);
}