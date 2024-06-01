package tv.bermu.cloverrpg.listeners;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatListener implements Listener {
    private HashMap<Player, Long> combatTime = new HashMap<>();

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

    public boolean isInCombat(Player player) {
        if (!combatTime.containsKey(player)) {
            return false;
        }

        long combatEndTime = combatTime.get(player) + 60000; // 60 seconds
        if (System.currentTimeMillis() > combatEndTime) {
            // Remove player from combat if 60 seconds have passed
            combatTime.remove(player);
            return false;
        }

        return true;
    }
}