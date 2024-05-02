package tv.bermu.cloverrpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import tv.bermu.cloverrpg.listeners.InventoryClickListener;
import tv.bermu.cloverrpg.utils.FakeInventoryUtil;

public class ClassesCommand implements CommandExecutor {

    private Inventory fakeInventory = null;

    /**
     * Constructor
     * 
     * @param plugin
     * @param config
     * @param inventoryClickListener
     */
    public ClassesCommand(JavaPlugin plugin, ConfigurationSection classesSection,
            InventoryClickListener inventoryClickListener) {
        // Create the fake inventory
        if (classesSection != null) {
            this.fakeInventory = FakeInventoryUtil.createFakeInventory(plugin, "Classes", 9, classesSection);
            inventoryClickListener.addFakeInventoryNoClick(fakeInventory);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute commands!");
            return true;
        }

        Player player = (Player) sender;
        if (fakeInventory != null) {
            player.openInventory(fakeInventory);
        }
        return true;
    }

}
