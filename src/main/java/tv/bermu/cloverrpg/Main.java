package tv.bermu.cloverrpg;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
    private MessageFormatter messageFormatter;

    @Override
    public void onEnable() {
        getLogger().info("Enabling.");

        ConfigManager configManager = new ConfigManager(this);
        configManager.loadConfig("races");
        defaultConfig = configManager.loadConfig("config");
        FileConfiguration classesConfig = configManager.loadConfig("classes");

        database = new Database(this, defaultConfig.getConfigurationSection("database"));
        messageFormatter = new MessageFormatter(configManager, defaultConfig);
        PartyHandler partyHandler = new PartyHandler(database, messageFormatter);

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new InventoryClickListener(), this);
        pluginManager.registerEvents(new EntityDeath(this), this);

        FileConfiguration partyConfig = configManager.loadConfig("party");
        if (partyConfig.getBoolean("enable")) {
            getCommand("party").setExecutor(new PartyCommand(this, partyHandler, messageFormatter));
        }

        getCommand("classes").setExecutor(
                new ClassesCommand(this, classesConfig.getConfigurationSection("classes"),
                        new InventoryClickListener()));
        getCommand("crpg").setExecutor(new CRPGCommand());

        getLogger().info("Enabled.");
    }

    /**
     * Add (temporary) exp to the player
     * 
     * @param killedEntity  The entity that was killed
     * @param exp        Amount of exp to add
     */
    public void addExp(LivingEntity killedEntity, int exp) {
        getLogger().info("Adding exp to user.");
        Player player = killedEntity.getKiller();
        UUID playerUUID = player.getUniqueId();

        PlayerModel playerModel = playerData.get(playerUUID);
        if (playerModel == null) {
            playerModel = new PlayerModel(playerUUID);
            playerData.put(playerUUID, playerModel);
        }
        playerModel.addUnsavedExp(exp);

        HashMap<String, Object> slugs = new HashMap<>();
        slugs.put("message_prefix", defaultConfig.getString("messageprefix"));
        // TODO check if we can do something here, since the name can be changed with a name tag...
        slugs.put("killed_monster", killedEntity.getName());
        slugs.put("exp_gained", exp);
        String formattedMessage = messageFormatter.formatMessage("player_kills_mob", player.getLocale().toLowerCase(),
                slugs);
        if (formattedMessage != null) {
            player.sendMessage(formattedMessage);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Closing.");
        database.closeConnection();
    }
}