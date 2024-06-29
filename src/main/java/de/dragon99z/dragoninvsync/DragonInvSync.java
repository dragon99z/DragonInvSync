package de.dragon99z.dragoninvsync;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import de.dragon99z.dragoninvsync.Events.onPlayerJoinEvent;
import de.dragon99z.dragoninvsync.Events.onPlayerLeaveEvent;
import de.dragon99z.dragoninvsync.Events.onServerStoppingEvent;
import de.dragon99z.dragoninvsync.Func.MyConfig;
import de.dragon99z.dragoninvsync.Func.MySQL;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * Represents the main class for the DragonInvSync mod.
 * Handles initialization of the mod by creating directories, reading configuration from a TOML file,
 * validating MySQL configuration, establishing a connection to the MySQL database,
 * validating records in the 'PlayerSync' table, and registering events for player join, leave, and server stopping.
 * Provides methods for connecting to MySQL, creating the 'PlayerSync' table, and registering event handlers.
 */
public class DragonInvSync implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("DragonInvSync");

    public static MyConfig config = new MyConfig();

    /**
     * Establishes a connection to a MySQL database if one does not already exist, and creates a table 'PlayerSync' if the connection is successful.
     * If the connection to the database is not established, attempts to connect using the MySQL class.
     * If the connection is successful, creates a table 'PlayerSync' with specific columns for player data.
     * Logs any SQLException that may occur during the table creation process.
     */
    public static void connectMySQL() {
        if (!MySQL.hasConnection()) {
            MySQL.connect();
        }

        if (MySQL.hasConnection()) {
            try {
                Statement st = MySQL.con.createStatement();
                String createTableQuery = "CREATE TABLE IF NOT EXISTS `" + config.database.name + "`.`PlayerSync`  (  `UUID` varchar(255) NOT NULL, `Inventory` text NULL, `Armor` text NULL, `Health` double NULL, `Hunger` int NULL, `Saturation` float NULL, `GameMode` text NULL, `XP` float NULL, `LVL` int NULL, `EChest` text NULL, `Advancement` text NULL, PRIMARY KEY (`UUID`));";
                st.execute(createTableQuery);
            } catch (SQLException e) {
                LOGGER.error(e.toString());
            }
        }
    }

    /**
     * Initializes the mod by creating necessary directories, reading configuration from a TOML file,
     * validating the MySQL configuration, establishing a connection to the MySQL database,
     * validating records in the 'PlayerSync' table, and registering events for player join, leave, and server stopping.
     * Logs errors if any exceptions occur during the initialization process.
     */
    @Override
    public void onInitialize() {
        Path configDir = Path.of(FabricLoader.getInstance().getConfigDir() + "/DragonInvSync");
        configDir.toFile().mkdirs();

        try {
            File configFile = new File(configDir + "/config.toml");
            if (!configFile.exists()) {
                String data = new String(Objects.requireNonNull(DragonInvSync.class.getResourceAsStream("/config.toml")).readAllBytes());
                Files.write(configFile.toPath(), data.getBytes());
            }

            TomlMapper mapper = new TomlMapper();
            config = mapper.readValue(configFile, MyConfig.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        if (config.database.type.equalsIgnoreCase("MYSQL") && !config.validateMySQL()) {
            LOGGER.error("Invalid config - Stopping");
            System.exit(1);
        }

        connectMySQL();
        try {
            MySQL.validate("PlayerSync");
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        registerEvents();
    }

    /**
     * Registers event handlers for player join, player leave, and server stopping events.
     * When a player joins, it triggers the synchronization of the player's data.
     * When a player leaves, it syncs the player's data to a MySQL database.
     * When the server stops, it retrieves data from all online players and stores it in the database.
     */
    public void registerEvents() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> new onPlayerJoinEvent(handler.getPlayer()));

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> new onPlayerLeaveEvent(handler.getPlayer()));

        ServerLifecycleEvents.SERVER_STOPPING.register(new onServerStoppingEvent());
    }
}
