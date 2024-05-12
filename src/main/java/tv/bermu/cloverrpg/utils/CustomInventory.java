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

public class CustomInventory {
    private Inventory inventory;
    private Map<Integer, String> slotCommands = new HashMap<>();

    /**
     * Constructor
     * 
     * @param plugin        The plugin
     * @param inventoryName The name of the inventory
     * @param slots         The number of slots
     * @param section       The configuration section
     */
    public CustomInventory(JavaPlugin plugin, String inventoryName, int slots, ConfigurationSection section) {
        Inventory inventory = plugin.getServer().createInventory(null, slots, inventoryName);
        for (String key : section.getKeys(false)) {
            if (key != "title_tag") {
                plugin.getLogger().info("Key: " + key);
                ConfigurationSection configSection = section.getConfigurationSection(key);
                if (configSection != null) {
                    int slot = configSection.getInt("inventory_ui.slot");
                    plugin.getLogger().info("Slot: " + slot);
                    List<String> lore = configSection.getStringList("lore_tag");
                    String itemName = configSection.getString("name_tag");
                    Material material = Material.getMaterial(configSection.getString("inventory_ui.material"));
                    plugin.getLogger().info("Material: " + material.toString());

                    ItemStack itemStack = writeItem(
                            itemName,
                            lore,
                            material);
                    inventory.setItem(slot, itemStack);

                    String command = configSection.getString("command");
                    if (command != null) {
                        slotCommands.put(slot, command);
                    }
                }
            }
        }
        this.inventory = inventory;
    }

    /**
     * Write an item to the inventory
     * 
     * @param itemName     The name of the item
     * @param itemLore     The lore of the item
     * @param itemMaterial The material of the item
     * @return The item stack
     */
    private static ItemStack writeItem(String itemName, List<String> itemLore, Material itemMaterial) {
        ItemStack itemStack = new ItemStack(itemMaterial, 1);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return null;
        }
        meta.setDisplayName(itemName);
        meta.setLore(itemLore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Get the inventory
     * 
     * @return The inventory
     */
    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Get the slot commands
     * 
     * @return The slot commands
     */
    public Map<Integer, String> getSlotCommands() {
        return this.slotCommands;
    }
}