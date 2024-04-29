package tv.bermu.cloverrpg;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import tv.bermu.cloverrpg.commands.CRPGCommand;
import tv.bermu.cloverrpg.commands.ClassesCommand;
import tv.bermu.cloverrpg.commands.PartyCommand;
import tv.bermu.cloverrpg.config.ConfigManager;
import tv.bermu.cloverrpg.db.Database;
import tv.bermu.cloverrpg.db.handlers.PartyHandler;
import tv.bermu.cloverrpg.listeners.EntityDeath;
import tv.bermu.cloverrpg.listeners.InventoryClickListener;

/**
 * Main class of the plugin
 */
public class Main extends JavaPlugin {

    private Database database;
    
    @Override
    public void onEnable() {
        getLogger().info("Enabling.");


        getLogger().info("Loading configs.");
        ConfigManager configManager = new ConfigManager(this);
        configManager.loadConfig("races");
        FileConfiguration defaultConfig = configManager.loadConfig("config");
        FileConfiguration classesConfig = configManager.loadConfig("classes");
        getLogger().info("Configs loaded.");

        Database database = new Database(this, defaultConfig.getConfigurationSection("database"));
        PartyHandler partyHandler = new PartyHandler(database);

        getLogger().info("Registering events.");
        InventoryClickListener inventoryClickListener = new InventoryClickListener();
        getServer().getPluginManager().registerEvents(inventoryClickListener, this);
        getServer().getPluginManager().registerEvents(new EntityDeath(this.getLogger()), this);
        getLogger().info("Events registered.");

        getLogger().info("Registering commands.");
        getCommand("party").setExecutor(new PartyCommand(partyHandler));
        getCommand("classes").setExecutor(new ClassesCommand(this, classesConfig.getConfigurationSection("classes"), inventoryClickListener));
        getCommand("crpg").setExecutor(new CRPGCommand());
        getLogger().info("Commands registered.");

        getLogger().info("Enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Closing.");
        database.closeConnection();
    }

}