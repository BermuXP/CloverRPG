package tv.bermu.cloverrpg.listeners;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class EntityDeath implements Listener {

    private final Logger logger;

    public EntityDeath(Logger logger) {
        // Get the logger instance for your plugin
        this.logger = logger;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        //TODO make config file to change these item names, and specifically for mobs so it can be disabled.

        // if (event.getEntityType() == EntityType.SKELETON) {
        //     List<ItemStack> drops = event.getDrops();
        //     for (ItemStack drop : drops) {
        //         // For some reason mobs still drop legacy (v20.4)... so we need to check for both
        //         if ((drop.getType() == Material.BOW || drop.getType() == Material.LEGACY_BOW)) {
        //             ItemMeta meta = drop.getItemMeta();
        //             meta.setDisplayName("Bow of the Skeleton Soldier");
        //             drop.setItemMeta(meta);
        //         }
        //     }
        // } else if (event.getEntityType() == EntityType.ZOMBIE) {
        //     List<ItemStack> drops = event.getDrops();
        //     for (ItemStack drop : drops) {
        //         if ((drop.getType() == Material.IRON_SWORD || drop.getType() == Material.LEGACY_IRON_SWORD)) {
        //             ItemMeta meta = drop.getItemMeta();
        //             meta.setDisplayName("Sword of the Zombie Soldier");
        //             drop.setItemMeta(meta);
        //         }
        //     }
        // }
    }

}
