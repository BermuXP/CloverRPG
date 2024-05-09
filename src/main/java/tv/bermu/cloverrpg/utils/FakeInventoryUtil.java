package tv.bermu.cloverrpg.utils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class FakeInventoryUtil {

    /**
     * Create a fake inventory
     * 
     * @param plugin        The plugin
     * @param inventoryName The name of the inventory
     * @param slots         The number of slots
     * @param section       The configuration section
     * @return              The fake inventory
     */
    public static Inventory createFakeInventory(JavaPlugin plugin, String inventoryName, int slots,
            ConfigurationSection section) {
        // Create fake inventory
        Inventory inventory = plugin.getServer().createInventory(null, slots, inventoryName);
        for (String itemName : section.getKeys(false)) {
            ConfigurationSection ItemContentSection = section.getConfigurationSection(itemName + ".gui");
            if (ItemContentSection != null) {
                List<String> desc = ItemContentSection.getStringList("description");
                ItemStack itemStack = writeItem(
                        itemName,
                        desc,
                        Material.getMaterial(ItemContentSection.getString("item")));
                inventory.setItem(ItemContentSection.getInt("item_location"), itemStack);
            }
        }

        return inventory;
    }

    /**
     * Write an item to the inventory
     * 
     * @param itemName      The name of the item
     * @param itemLore      The lore of the item
     * @param itemMaterial  The material of the item
     * @return              The item stack
     */
    private static ItemStack writeItem(String itemName, List<String> itemLore, Material itemMaterial) {
        ItemStack itemStack = new ItemStack(itemMaterial, 1);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return null;
        meta.setDisplayName(itemName);
        meta.setLore(itemLore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
