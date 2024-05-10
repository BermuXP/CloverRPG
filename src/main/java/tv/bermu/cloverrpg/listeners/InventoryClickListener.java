package tv.bermu.cloverrpg.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import tv.bermu.cloverrpg.Main;
import tv.bermu.cloverrpg.utils.CustomInventory;

public class InventoryClickListener implements Listener {

    private List<CustomInventory> customInventories = new ArrayList<>();
    private JavaPlugin plugin;

    /**
     * Constructor
     * 
     * @param plugin The plugin
     */
    public InventoryClickListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Add a custom inventory
     * 
     * @param customInventory The custom inventory
     */
    public void addCustomInventory(CustomInventory customInventory) {
        this.customInventories.add(customInventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory != null) {
            for (CustomInventory customInventory : customInventories) {
                if (customInventory.getInventory().equals(clickedInventory)) {
                    event.setCancelled(true);

                    int slot = event.getSlot();
                    String command = customInventory.getSlotCommands().get(slot);
                    if (command != null) {
                        Player player = (Player) event.getWhoClicked();
                        plugin.getServer().dispatchCommand(player, command + " " + Main.uniqueInventoryIdentifier);
                    }
                    break;
                }
            }
        }

        InventoryAction action = event.getAction();
        if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY || action == InventoryAction.HOTBAR_MOVE_AND_READD) {
            for (CustomInventory customInventory : customInventories) {
                if (customInventory.getInventory().equals(event.getInventory())) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Inventory clickedInventory = event.getInventory();
        for (CustomInventory customInventory : customInventories) {
            if (customInventory.getInventory().equals(clickedInventory)) {
                event.setCancelled(true);
                break;
            }
        }
    }
}