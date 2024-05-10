package tv.bermu.cloverrpg.utils;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CustomInventory {
    private JavaPlugin plugin;
    private Inventory inventory;
    private Map<Integer, String> slotCommands = new HashMap<>();

    /**
     * Constructor
     * @param plugin    The plugin
     * @param inventoryName The name of the inventory
     * @param slots         The number of slots
     * @param section       The configuration section
     */
    public CustomInventory(JavaPlugin plugin, String inventoryName, int slots, ConfigurationSection section) {
        this.plugin = plugin;
        this.inventory = FakeInventoryUtil.createFakeInventory(plugin, inventoryName, slots, section);
        for (String key : section.getKeys(false)) {
            ConfigurationSection configSection = section.getConfigurationSection(key);
            if (configSection != null) {
                int slot = configSection.getInt("inventory_slot");
                String command = configSection.getString("command");
                if (command != null) {
                    slotCommands.put(slot, command);
                }
            }
        }
    }

    /**
     * Get the inventory
     * @return The inventory
     */
    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Get the slot commands
     * @return The slot commands
     */
    public Map<Integer, String> getSlotCommands() {
        return this.slotCommands;
    }
}