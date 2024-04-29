package tv.bermu.cloverrpg;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import tv.bermu.cloverrpg.commands.CRPGCommand;
import tv.bermu.cloverrpg.commands.ClassesCommand;
import tv.bermu.cloverrpg.commands.PartyCommand;
import tv.bermu.cloverrpg.config.ConfigManager;
import tv.bermu.cloverrpg.db.Database;
import tv.bermu.cloverrpg.db.handlers.PartyHandler;
import tv.bermu.cloverrpg.listeners.EntityDeath;
import tv.bermu.cloverrpg.listeners.InventoryClickListener;
import tv.bermu.cloverrpg.models.PlayerModel;

/**
 * Main class of the plugin
 */
public class Main extends JavaPlugin {

    private Database database;
    private HashMap<UUID, PlayerModel> playerData = new HashMap<>();
    private FileConfiguration defaultConfig;

    @Override
    public void onEnable() {
        getLogger().info("Enabling.");

        getLogger().info("Loading configs.");
        ConfigManager configManager = new ConfigManager(this);
        configManager.loadConfig("races");
        defaultConfig = configManager.loadConfig("config");
        FileConfiguration classesConfig = configManager.loadConfig("classes");
        getLogger().info("Configs loaded.");

        Database database = new Database(this, defaultConfig.getConfigurationSection("database"));
        PartyHandler partyHandler = new PartyHandler(database);

        getLogger().info("Registering events.");
        InventoryClickListener inventoryClickListener = new InventoryClickListener();
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(inventoryClickListener, this);
        pluginManager.registerEvents(new EntityDeath(this), this);
        getLogger().info("Events registered.");

        getLogger().info("Registering commands.");
        getCommand("party").setExecutor(new PartyCommand(partyHandler));
        getCommand("classes").setExecutor(
                new ClassesCommand(this, classesConfig.getConfigurationSection("classes"), inventoryClickListener));
        getCommand("crpg").setExecutor(new CRPGCommand());
        getLogger().info("Commands registered.");

        getLogger().info("Enabled.");
    }

    /**
     * Add (temporary) exp to the player
     * 
     * @param playerUUID UUID of the player
     * @param monster    Entity of the monster
     * @param exp        Amount of exp to add
     */
    public void addExp(Player player, Entity monster, int exp) {
        getLogger().info("Adding exp to user.");
        UUID playerUUID = player.getUniqueId();
        PlayerModel playerModel = playerData.get(playerUUID);
        if (playerModel == null) {
            playerModel = new PlayerModel(playerUUID);
            playerData.put(playerUUID, playerModel);
        }
        playerModel.addUnsavedExp(exp);
        player.sendMessage(defaultConfig.getString("messageprefix") + ChatColor.WHITE + "You have gained "
                + ChatColor.BOLD + ChatColor.GREEN + exp + ChatColor.YELLOW + " EXP " + ChatColor.RESET
                + ChatColor.WHITE + "for killing " + ChatColor.GREEN + monster.getName() + '.');
    }

    @Override
    public void onDisable() {
        getLogger().info("Closing.");
        database.closeConnection();
    }
}