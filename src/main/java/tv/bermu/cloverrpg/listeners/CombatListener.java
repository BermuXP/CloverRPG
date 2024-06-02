package tv.bermu.cloverrpg.listeners;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatListener implements Listener {
    private HashMap<Player, Long> combatTime = new HashMap<>();
    private int combatTimeSeconds = 60;

    /**
     * Constructor for the CombatListener class
     * 
     * @param config The configuration file
     */
    public CombatListener(FileConfiguration config) {
        if (config.contains("combatTime")) {
            combatTimeSeconds = config.getInt("combatTime");
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            // Set both players as being in combat
            combatTime.put(player, System.currentTimeMillis());
            combatTime.put(damager, System.currentTimeMillis());
        }
    }

    /**
     * Check if a player is in combat
     * 
     * @param player The player to check
     * @return True if the player is in combat, false otherwise
     */
    public boolean isInCombat(Player player) {
        if (!combatTime.containsKey(player)) {
            return false;
        }

        long combatEndTime = combatTime.get(player) + combatTimeSeconds * 1000; // seconds
        if (System.currentTimeMillis() > combatEndTime) {
            combatTime.remove(player);
            return false;
        }

        return true;
    }

    /**
     * Get the time left in combat for a player
     * 
     * @param player The player to get the time left in combat for
     * @return The time left in combat for the player
     */
    public int timeLeftInCombat(Player player) {
        return (int) (combatTime.get(player) + combatTimeSeconds * 1000); // seconds
    }
}