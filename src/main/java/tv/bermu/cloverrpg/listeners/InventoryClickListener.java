package tv.bermu.cloverrpg.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryClickListener implements Listener {
    private List<Inventory> fakeInventories;

    /**
     * Constructor
     */
    public InventoryClickListener() {
        this.fakeInventories = new ArrayList<>();
    }

    /**
     * Add a fake inventory to the list of fake inventories
     * (these are NOT clickable)
     * 
     * @param inventory The inventory to add
     */
    public void addFakeInventoryNoClick(Inventory inventory) {
        this.fakeInventories.add(inventory);
    }

    /**
     * Event handler for inventory click
     * 
     * @param event The event
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory != null && fakeInventories.contains(clickedInventory)) {
            event.setCancelled(true); // Cancel the event to prevent item taking
        }
    }
}