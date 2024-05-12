package tv.bermu.cloverrpg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import tv.bermu.cloverrpg.commands.CRPGCommand;
import tv.bermu.cloverrpg.commands.CharacterCommand;
import tv.bermu.cloverrpg.commands.ClassCommand;
import tv.bermu.cloverrpg.commands.PartyCommand;
import tv.bermu.cloverrpg.db.Database;
import tv.bermu.cloverrpg.db.handlers.PartyHandler;
import tv.bermu.cloverrpg.listeners.EntityDeath;
import tv.bermu.cloverrpg.listeners.InventoryClickListener;
import tv.bermu.cloverrpg.listeners.PlayerChat;
import tv.bermu.cloverrpg.listeners.PlayerJoin;
import tv.bermu.cloverrpg.managers.ConfigManager;
import tv.bermu.cloverrpg.utils.CustomInventory;

/**
 * Main class of the plugin
 */
public class Main extends JavaPlugin {

    private Database database;
    private FileConfiguration defaultConfig;
    private MessageFormatter messageFormatter;
    private Set<Player> creatingCharacter = new HashSet<>();

    public static String uniqueInventoryIdentifier;
    public static String baseCommandPermission = "cloverrpg.command.";

    @Override
    public void onEnable() {
        getLogger().info("Enabling.");
        uniqueInventoryIdentifier = UUID.randomUUID().toString();
        ConfigManager configManager = new ConfigManager(this);

        configManager.loadConfig("characters");
        defaultConfig = configManager.loadConfig("config");
        FileConfiguration classesConfig = configManager.loadConfig("classes");
        FileConfiguration racesConfig = configManager.loadConfig("races");

        database = new Database(this, defaultConfig.getConfigurationSection("database"));
        messageFormatter = new MessageFormatter(configManager, defaultConfig);

        PluginManager pluginManager = getServer().getPluginManager();
        InventoryClickListener inventoryClickListener = new InventoryClickListener(this);
        pluginManager.registerEvents(inventoryClickListener, this);
        pluginManager.registerEvents(new EntityDeath(this), this);
        pluginManager.registerEvents(new PlayerJoin(), this);
        pluginManager.registerEvents(new PlayerChat(creatingCharacter, messageFormatter), this);

        PartyHandler partyHandler = new PartyHandler(database, messageFormatter);
        FileConfiguration partyConfig = configManager.loadConfig("party");
        if (partyConfig.getBoolean("enable")) {
            getCommand("party").setExecutor(new PartyCommand(this, partyHandler, messageFormatter));
        }

        Boolean showInventoryClasses = classesConfig.getBoolean("show_as_inventory");
        CustomInventory classesInventory = null;
        if (showInventoryClasses) {
            ConfigurationSection classesSection = classesConfig.getConfigurationSection("classes");

            classesInventory = new CustomInventory(this,
                    classesSection.getString("title_tag"),
                    classesConfig.getInt("inventory_max_slots"),
                    classesSection);
            inventoryClickListener.addCustomInventory(classesInventory);
        }

        Boolean showInventoryRaces = racesConfig.getBoolean("show_as_inventory");
        if (showInventoryRaces) {
            ConfigurationSection racesSection = racesConfig.getConfigurationSection("races");

            CustomInventory racesInventory = new CustomInventory(this,
                    "select race",
                    racesConfig.getInt("inventory_max_slots"),
                    racesSection);
            inventoryClickListener.addCustomInventory(racesInventory);
        }

        getCommand("class").setExecutor(
                new ClassCommand(this, classesConfig, messageFormatter));
        getCommand("crpg").setExecutor(new CRPGCommand());
        getCommand("character")
                .setExecutor(new CharacterCommand(this, messageFormatter, classesInventory, creatingCharacter));

        getLogger().info("Enabled.");
    }

    /**
     * Add (temporary) exp to the player
     * 
     * @param killedEntity The entity that was killed
     * @param exp          Amount of exp to add
     */
    public void addExp(LivingEntity killedEntity, int exp) {
        getLogger().info("Adding exp to user.");
        Player player = killedEntity.getKiller();
        UUID playerUUID = player.getUniqueId();

        HashMap<String, Object> slugs = new HashMap<>();
        slugs.put("message_prefix", defaultConfig.getString("messageprefix"));
        // TODO check if we can do something here, since the name can be changed with a
        // name tag...
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