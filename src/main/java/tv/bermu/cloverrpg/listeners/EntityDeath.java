package tv.bermu.cloverrpg.listeners;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tv.bermu.cloverrpg.Main;

public class EntityDeath implements Listener {

    private final Main main;

    /**
     * Constructor
     * @param plugin    Main class of the plugin
     */
    public EntityDeath(Main main) {
        this.main = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        entity.getType();
        if (entity instanceof Monster && entity.getKiller() != null) {
            // The entity was killed by a player
            this.main.addExp(entity.getKiller(), entity, 10);
        }
        // The entity is a mob

        if (event.getEntityType() == EntityType.SKELETON) {
            List<ItemStack> drops = event.getDrops();
            for (ItemStack drop : drops) {
                // Drops are sometimes legacy... I dont know how to solve this since legacy items dont exist in 1.13 and up...
                if ((drop.getType() == Material.BOW)) {
                    ItemMeta meta = drop.getItemMeta();
                    meta.setDisplayName("Bow of the Skeleton Soldier");
                    drop.setItemMeta(meta);
                }
            }
        } else if (event.getEntityType() == EntityType.ZOMBIE) {
            List<ItemStack> drops = event.getDrops();
            for (ItemStack drop : drops) {
                if ((drop.getType() == Material.IRON_SWORD)) {
                    ItemMeta meta = drop.getItemMeta();
                    meta.setDisplayName("Sword of the Zombie Soldier");
                    drop.setItemMeta(meta);
                }
            }
        }
    }

}
