package tv.bermu.cloverrpg;

import java.util.Collections;
import java.util.Map;

import org.bukkit.entity.Player;

public interface SubCommand {
    void execute(Player player, String[] args);

    boolean hasPermission(Player player);

    default boolean hasSubCommands() {
        return !getSubCommands().isEmpty();
    }

    default Map<String, SubCommand> getSubCommands() {
        return Collections.emptyMap();
    }
}