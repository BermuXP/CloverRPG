package tv.bermu.cloverrpg.commands.subcommands.guild.basesubcommands;

import java.util.HashMap;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import tv.bermu.cloverrpg.MessageFormatter;
import tv.bermu.cloverrpg.SubCommand;
import tv.bermu.cloverrpg.db.handlers.GuildHandler;
import tv.bermu.cloverrpg.listeners.CombatListener;

public class CreateSubCommand implements SubCommand {

    private final String permission;
    private final MessageFormatter messageFormatter;
    private final GuildHandler guildHandler;
    private CombatListener combatListener;
    private JavaPlugin plugin;

    /**
     * Constructor for the CreateSubCommand
     * 
     * @param guildHandler
     * @param messageFormatter
     * @param permission
     * @param combatListener
     * @param plugin
     */
    public CreateSubCommand(GuildHandler guildHandler, MessageFormatter messageFormatter, String permission,
            CombatListener combatListener, JavaPlugin plugin) {
        this.messageFormatter = messageFormatter;
        this.permission = permission;
        this.guildHandler = guildHandler;
        this.combatListener = combatListener;
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        String playerLanguage = player.getLocale().toLowerCase();
        if (combatListener.isInCombat(player)) {
            HashMap<String, Object> slugs = new HashMap<>();
            slugs.put("combat_time", combatListener.timeLeftInCombat(player));
            player.sendMessage(
                    messageFormatter.formatMessage("combat_cannot_perform_action", playerLanguage, slugs));
            return;
        }

        // Define the half-length of the side of the square
        int halfSideLength = 5;

        // Create a new BukkitRunnable to spawn the particles
        new BukkitRunnable() {
            @Override
            public void run() {
                // Check if the player is in combat
                if (combatListener.isInCombat(player)) {
                    // If they are, cancel the task and return
                    this.cancel();
                    player.sendMessage("You entered combat, area marking cancelled.");
                    return;
                }

                // Get the player's current location
                Location center = player.getLocation();

                // Loop through the points on the perimeter of the square
                for (int dx = -halfSideLength; dx <= halfSideLength; dx++) {
                    for (int dz = -halfSideLength; dz <= halfSideLength; dz++) {
                        // Only spawn particles at the perimeter, not inside the square
                        if (Math.abs(dx) != halfSideLength && Math.abs(dz) != halfSideLength) {
                            continue;
                        }

                        double x = center.getX() + dx;
                        double z = center.getZ() + dz;

                        int y = center.getWorld().getHighestBlockYAt((int) x, (int) z) + 1;

                        Location point = new Location(center.getWorld(), x, y, z);

                        DustOptions dustOptions = new DustOptions(Color.BLUE, 1);
                        center.getWorld().spawnParticle(Particle.REDSTONE, point, 1, dustOptions);
                    }
                }
            }
        }.runTaskTimer(this.plugin, 0L, 20L); // Run the task every second (20 ticks = 1 second)

        player.sendMessage("Not in combat");
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }

}
