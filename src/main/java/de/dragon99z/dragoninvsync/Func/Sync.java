package de.dragon99z.dragoninvsync.Func;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static de.dragon99z.dragoninvsync.DragonInvSync.LOGGER;

/**
 * The Sync class provides methods to interact with a MySQL database for syncing player data.
 * It includes methods to set and get various player attributes such as inventory, armor, health, hunger, saturation,
 * game mode, experience points, level, ender chest contents, and advancements.
 * The class also includes a method to check if a player's data is already in the synchronization table.
 * All methods in this class require a valid UUID for the player whose data is being accessed or modified.
 */
public class Sync {

    /**
     * Sets the inventory hash for the player with the specified UUID in the synchronization table.
     * If the player's data is already in the synchronization table, updates the existing inventory hash.
     * If the player's data is not in the synchronization table, inserts a new entry with the UUID and inventory hash.
     *
     * @param uuid the UUID of the player whose inventory hash is being set
     * @param invHash the inventory hash to be set for the player
     * @throws SQLException if a database access error occurs
     */
    public static void setInvHash(String uuid, String invHash) throws SQLException {
        try (Statement st = MySQL.con.createStatement()) {
            String query;
            if (Sync.isInSyncTable(uuid)) {
                query = "UPDATE PlayerSync SET Inventory='" + invHash + "' WHERE UUID='" + uuid + "'";
            } else {
                query = "INSERT INTO PlayerSync(UUID,Inventory) VALUES('" + uuid + "','" + invHash + "')";
            }
            st.executeUpdate(query);
        }
    }

    /**
     * Retrieves the inventory hash for the player with the specified UUID from the synchronization table.
     *
     * @param uuid the UUID of the player whose inventory hash is being retrieved
     * @return the inventory hash of the player, or an empty string if the player's data is not found
     * @throws SQLException if a database access error occurs
     */
    public static String getInvHash(String uuid) throws SQLException {
        String invHash = "";
        
        try (Statement st = MySQL.con.createStatement();
             ResultSet rs = st.executeQuery("SELECT Inventory FROM PlayerSync WHERE UUID='" + uuid + "'")) {
            if (rs.next()) {
                invHash = rs.getString("Inventory");
            }
        }

        return invHash;
    }

    /**
     * Sets the armor hash for the player with the specified UUID in the synchronization table.
     * If the player's data is already in the synchronization table, updates the existing armor hash.
     * If the player's data is not in the synchronization table, inserts a new entry with the UUID and armor hash.
     *
     * @param uuid the UUID of the player whose armor hash is being set
     * @param armorHash the armor hash to be set for the player
     * @throws SQLException if a database access error occurs
     */
    public static void setArmorHash(String uuid, String armorHash) throws SQLException {
        try (Statement st = MySQL.con.createStatement()) {
            String query;
            if (Sync.isInSyncTable(uuid)) {
                query = "UPDATE PlayerSync SET Armor='" + armorHash + "' WHERE UUID='" + uuid + "'";
            } else {
                query = "INSERT INTO PlayerSync(UUID,Armor) VALUES('" + uuid + "','" + armorHash + "')";
            }
            st.executeUpdate(query);
        }
    }

    /**
     * Retrieves the armor hash for the player with the specified UUID from the synchronization table.
     *
     * @param uuid the UUID of the player whose armor hash is being retrieved
     * @return the armor hash of the player, or an empty string if the player's data is not found
     * @throws SQLException if a database access error occurs
     */
    public static String getArmorHash(String uuid) throws SQLException {
        String armorHash = "";
        
        try (Statement st = MySQL.con.createStatement();
             ResultSet rs = st.executeQuery("SELECT Armor FROM PlayerSync WHERE UUID='" + uuid + "'")) {
            if (rs.next()) {
                armorHash = rs.getString("Armor");
            }
        }

        return armorHash;
    }

    /**
     * Sets the health value for the player with the specified UUID in the synchronization table.
     * If the player's data is already in the synchronization table, updates the existing health value.
     * If the player's data is not in the synchronization table, inserts a new entry with the UUID and health value.
     *
     * @param uuid   the UUID of the player whose health value is being set
     * @param health the health value to be set for the player
     * @throws SQLException if a database access error occurs
     */
    public static void setHealth(String uuid, float health) throws SQLException {
        try (Statement st = MySQL.con.createStatement()) {
            String query;
            if (Sync.isInSyncTable(uuid)) {
                query = "UPDATE PlayerSync SET Health='" + health + "' WHERE UUID='" + uuid + "'";
            } else {
                query = "INSERT INTO PlayerSync(UUID,Health) VALUES('" + uuid + "','" + health + "')";
            }
            st.executeUpdate(query);
        }
    }

    /**
     * Retrieves the health value for the player with the specified UUID from the synchronization table.
     *
     * @param uuid the UUID of the player whose health value is being retrieved
     * @return the health value of the player, or -1 if the player's data is not found
     * @throws SQLException if a database access error occurs
     */
    public static float getHealth(String uuid) throws SQLException {
        float health = -1;
        
        try (Statement st = MySQL.con.createStatement();
             ResultSet rs = st.executeQuery("SELECT Health FROM PlayerSync WHERE UUID='" + uuid + "'")) {
            if (rs.next()) {
                health = rs.getFloat("Health");
            }
        }
        
        return health;
    }

    /**
     * Sets the hunger value for the player with the specified UUID in the synchronization table.
     * If the player's data is already in the synchronization table, updates the existing hunger value.
     * If the player's data is not in the synchronization table, inserts a new entry with the UUID and hunger value.
     *
     * @param uuid the UUID of the player whose hunger value is being set
     * @param hunger the hunger value to be set for the player
     * @throws SQLException if a database access error occurs
     */
    public static void setHunger(String uuid, int hunger) throws SQLException {
        try (Statement st = MySQL.con.createStatement()) {
            String query;
            if (Sync.isInSyncTable(uuid)) {
                query = "UPDATE PlayerSync SET Hunger='" + hunger + "' WHERE UUID='" + uuid + "'";
            } else {
                query = "INSERT INTO PlayerSync(UUID,Hunger) VALUES('" + uuid + "','" + hunger + "')";
            }
            st.executeUpdate(query);
        }
    }

    /**
     * Retrieves the hunger value for the player with the specified UUID from the synchronization table.
     *
     * @param uuid the UUID of the player whose hunger value is being retrieved
     * @return the hunger value of the player, or -1 if the player's data is not found
     * @throws SQLException if a database access error occurs
     */
    public static int getHunger(String uuid) throws SQLException {
        int hunger = -1;
        
        try (Statement st = MySQL.con.createStatement();
             ResultSet rs = st.executeQuery("SELECT Hunger FROM PlayerSync WHERE UUID='" + uuid + "'")) {
            if (rs.next()) {
                hunger = rs.getInt("Hunger");
            }
        }

        return hunger;
    }

    /**
     * Sets the saturation value for the player with the specified UUID in the synchronization table.
     * If the player's data is already in the synchronization table, updates the existing saturation value.
     * If the player's data is not in the synchronization table, inserts a new entry with the UUID and saturation value.
     *
     * @param uuid the UUID of the player whose saturation value is being set
     * @param saturation the saturation value to be set for the player
     * @throws SQLException if a database access error occurs
     */
    public static void setSaturation(String uuid, float saturation) throws SQLException {
        try (Statement st = MySQL.con.createStatement()) {
            String query;
            if (Sync.isInSyncTable(uuid)) {
                query = "UPDATE PlayerSync SET Saturation='" + saturation + "' WHERE UUID='" + uuid + "'";
            } else {
                query = "INSERT INTO PlayerSync(UUID,Saturation) VALUES('" + uuid + "','" + saturation + "')";
            }
            st.executeUpdate(query);
        }
    }

    /**
     * Retrieves the saturation value for the player with the specified UUID from the synchronization table.
     *
     * @param uuid the UUID of the player whose saturation value is being retrieved
     * @return the saturation value of the player, or -1 if the player's data is not found
     * @throws SQLException if a database access error occurs
     */
    public static float getSaturation(String uuid) throws SQLException {
        float saturation = -1;
        
        try (Statement st = MySQL.con.createStatement();
             ResultSet rs = st.executeQuery("SELECT Saturation FROM PlayerSync WHERE UUID='" + uuid + "'")) {
            if (rs.next()) {
                saturation = rs.getFloat("Saturation");
            }
        }

        return saturation;
    }

    /**
     * Sets the game mode for the player with the specified UUID in the synchronization table.
     * If the player's data is already in the synchronization table, updates the existing game mode.
     * If the player's data is not in the synchronization table, inserts a new entry with the UUID and game mode.
     *
     * @param uuid     the UUID of the player whose game mode is being set
     * @param gameMode the game mode to be set for the player
     * @throws SQLException if a database access error occurs
     */
    public static void setGameMode(String uuid, String gameMode) throws SQLException {
        try (Statement st = MySQL.con.createStatement()) {
            if (Sync.isInSyncTable(uuid)) {
                st.executeUpdate("UPDATE PlayerSync SET GameMode='" + gameMode + "' WHERE UUID='" + uuid + "'");
            } else {
                st.executeUpdate("INSERT INTO PlayerSync(UUID,GameMode) VALUES('" + uuid + "','" + gameMode + "')");
            }
        }
    }

    /**
     * Retrieves the game mode for the player with the specified UUID from the synchronization table.
     *
     * @param uuid the UUID of the player whose game mode is being retrieved
     * @return the game mode of the player, or an empty string if the player's data is not found
     * @throws SQLException if a database access error occurs
     */
    public static String getGameMode(String uuid) throws SQLException {
        String gameMode = "";
        
        try (Statement st = MySQL.con.createStatement();
             ResultSet rs = st.executeQuery("SELECT GameMode FROM PlayerSync WHERE UUID='" + uuid + "'")) {
            if (rs.next()) {
                gameMode = rs.getString("GameMode");
            }
        }
        
        return gameMode;
    }

    /**
     * Sets the experience points (XP) value for the player with the specified UUID in the synchronization table.
     * If the player's data is already in the synchronization table, updates the existing XP value.
     * If the player's data is not in the synchronization table, inserts a new entry with the UUID and XP value.
     *
     * @param uuid the UUID of the player whose XP value is being set
     * @param XP the experience points value to be set for the player
     * @throws SQLException if a database access error occurs
     */
    public static void setXP(String uuid, float XP) throws SQLException {
        try (Statement st = MySQL.con.createStatement()) {
            if (Sync.isInSyncTable(uuid)) {
                st.executeUpdate("UPDATE PlayerSync SET XP='" + XP + "' WHERE UUID='" + uuid + "'");
            } else {
                st.executeUpdate("INSERT INTO PlayerSync(UUID,XP) VALUES('" + uuid + "','" + XP + "')");
            }
        }
    }

    /**
     * Retrieves the experience points (XP) value for the player with the specified UUID from the synchronization table.
     *
     * @param uuid the UUID of the player whose XP value is being retrieved
     * @return the experience points value of the player, or -1 if the player's data is not found
     * @throws SQLException if a database access error occurs
     */
    public static float getXP(String uuid) throws SQLException {
        float XP = -1;
        
        try (Statement st = MySQL.con.createStatement();
             ResultSet rs = st.executeQuery("SELECT XP FROM PlayerSync WHERE UUID='" + uuid + "'")) {
            while (rs.next()) {
                XP = rs.getFloat("XP");
            }
        }

        return XP;
    }

    /**
     * Sets the level value for the player with the specified UUID in the synchronization table.
     * If the player's data is already in the synchronization table, updates the existing level value.
     * If the player's data is not in the synchronization table, inserts a new entry with the UUID and level value.
     *
     * @param uuid the UUID of the player whose level value is being set
     * @param LVL the level value to be set for the player
     * @throws SQLException if a database access error occurs
     */
    public static void setLvl(String uuid, int LVL) throws SQLException {
        try (Statement st = MySQL.con.createStatement()) {
            if (Sync.isInSyncTable(uuid)) {
                st.executeUpdate("UPDATE PlayerSync SET LVL='" + LVL + "' WHERE UUID='" + uuid + "'");
            } else {
                st.executeUpdate("INSERT INTO PlayerSync(UUID,LVL) VALUES('" + uuid + "','" + LVL + "')");
            }
        }
    }

    /**
     * Retrieves the level value for the player with the specified UUID from the synchronization table.
     *
     * @param uuid the UUID of the player whose level value is being retrieved
     * @return the level value of the player, or -1 if the player's data is not found
     * @throws SQLException if a database access error occurs
     */
    public static int getLvl(String uuid) throws SQLException {
        int level = -1;
        try (Statement statement = MySQL.con.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT LVL FROM PlayerSync WHERE UUID='" + uuid + "'")) {
            if (resultSet.next()) {
                level = resultSet.getInt("LVL");
            }
        }
        return level;
    }

    /**
     * Sets the ender chest hash for the player with the specified UUID in the synchronization table.
     * If the player's data is already in the synchronization table, updates the existing ender chest hash.
     * If the player's data is not in the synchronization table, inserts a new entry with the UUID and ender chest hash.
     *
     * @param uuid the UUID of the player whose ender chest hash is being set
     * @param invHash the ender chest hash to be set for the player
     * @throws SQLException if a database access error occurs
     */
    public static void setEChestHash(String uuid, String invHash) throws SQLException {
        try (Statement st = MySQL.con.createStatement()) {
            if (Sync.isInSyncTable(uuid)) {
                st.executeUpdate("UPDATE PlayerSync SET EChest='" + invHash + "' WHERE UUID='" + uuid + "'");
            } else {
                st.executeUpdate("INSERT INTO PlayerSync(UUID,EChest) VALUES('" + uuid + "','" + invHash + "')");
            }
        }
    }

    /**
     * Retrieves the ender chest hash for the player with the specified UUID from the synchronization table.
     *
     * @param uuid the UUID of the player whose ender chest hash is being retrieved
     * @return the ender chest hash of the player, or an empty string if the player's data is not found
     * @throws SQLException if a database access error occurs
     */
    public static String getEChestHash(String uuid) throws SQLException {
        String eChestHash = "";
        
        try (Statement st = MySQL.con.createStatement();
             ResultSet rs = st.executeQuery("SELECT EChest FROM PlayerSync WHERE UUID='" + uuid + "'")) {
            if (rs.next()) {
                eChestHash = rs.getString("EChest");
            }
        }

        return eChestHash;
    }

    /**
     * Sets the advancement hash for the player with the specified UUID in the synchronization table.
     * If the player's data is already in the synchronization table, updates the existing advancement hash.
     * If the player's data is not in the synchronization table, inserts a new entry with the UUID and advancement hash.
     *
     * @param uuid the UUID of the player whose advancement hash is being set
     * @param advancementHash the advancement hash to be set for the player
     * @throws SQLException if a database access error occurs
     */
    public static void setAdvancementHash(String uuid, String advancementHash) throws SQLException {
        try (Statement st = MySQL.con.createStatement()) {
            if (Sync.isInSyncTable(uuid)) {
                st.executeUpdate("UPDATE PlayerSync SET Advancement='" + advancementHash + "' WHERE UUID='" + uuid + "'");
            } else {
                st.executeUpdate("INSERT INTO PlayerSync(UUID,Advancement) VALUES('" + uuid + "','" + advancementHash + "')");
            }
        }
    }

    /**
     * Retrieves the advancement hash for the player with the specified UUID from the synchronization table.
     *
     * @param uuid the UUID of the player whose advancement hash is being retrieved
     * @return the advancement hash of the player, or an empty string if the player's data is not found
     * @throws SQLException if a database access error occurs
     */
    public static String getAdvancementHash(String uuid) throws SQLException {
        String advancementHash = "";
        
        try (Statement st = MySQL.con.createStatement();
             ResultSet rs = st.executeQuery("SELECT Advancement FROM PlayerSync WHERE UUID='" + uuid + "'")) {
            if (rs.next()) {
                advancementHash = rs.getString("Advancement");
            }
        }

        return advancementHash;
    }

    /**
     * Syncs all player data to the database.
     * Retrieves the player's ender chest, inventory, armor, advancements, game mode, health, hunger, saturation,
     * experience level, and experience progress. Converts them to respective hashes and updates the database entries.
     *
     * @param player the player entity whose data is being synced to the database
     */
    public static void syncAllToDatabase(ServerPlayerEntity player) {
        String uuid = player.getUuid().toString();
        String eChestHash = PlayerHash.toBase64(player, player.getEnderChestInventory());
        String[] playerInventory = PlayerHash.playerInventoryToBase64(player.getInventory());
        String advancementsHash = PlayerHash.advancementToBase64(player);

        int experienceLevel = player.experienceLevel;
        float experienceProgress = player.experienceProgress;

        try {
            setEChestHash(uuid, eChestHash);
            setInvHash(uuid, playerInventory[0]);
            setArmorHash(uuid, playerInventory[1]);
            setAdvancementHash(uuid, advancementsHash);
            setGameMode(uuid, player.interactionManager.getGameMode().asString().toUpperCase());
            setHealth(uuid, player.getHealth());
            setHunger(uuid, player.getHungerManager().getFoodLevel());
            setSaturation(uuid, player.getHungerManager().getSaturationLevel());
            setLvl(uuid, experienceLevel);
            setXP(uuid, experienceProgress);
        } catch (SQLException e) {
            LOGGER.error("Error syncing player data to database: {}", e.getMessage());
        }
    }

    /**
     * Syncs all player data from the synchronization table to the specified player.
     * Retrieves the ender chest, inventory, armor, advancements, game mode, health, hunger, saturation,
     * experience level, and experience progress from the synchronization table using the player's UUID.
     * Sets these values to the player accordingly.
     *
     * @param player the player entity to sync the data to
     * @throws SQLException if a database access error occurs
     * @throws IOException if an I/O error occurs during deserialization
     */
    public static void syncAllToPlayer(ServerPlayerEntity player) throws SQLException, IOException {
        String uuid = player.getUuid().toString();
        // Sync ender chest
        Inventory eInv = PlayerHash.fromBase64(player,Sync.getEChestHash(uuid));
        for(int i=0; i < eInv.size(); i++){
            player.getEnderChestInventory().setStack(i,eInv.getStack(i));
        }
        // Sync inventory
        Inventory inv = PlayerHash.fromBase64(player,Sync.getInvHash(uuid));
        for(int i=0; i < inv.size(); i++){
            player.getInventory().setStack(i,inv.getStack(i));
        }
        // Sync armor
        ItemStack[] armor = PlayerHash.itemStackArrayFromBase64(player,Sync.getArmorHash(uuid));
        for(int i = 0; i < armor.length;i++){
            player.getInventory().armor.set(i,armor[i]);
        }
        // Sync advancements, game mode, health, hunger, saturation, experience level, and experience progress
        PlayerHash.advancementFromBase64(player,Sync.getAdvancementHash(uuid));
        player.interactionManager.changeGameMode(GameMode.valueOf(Sync.getGameMode(uuid).toUpperCase()));
        player.setHealth(Sync.getHealth(uuid));
        player.getHungerManager().setFoodLevel(Sync.getHunger(uuid));
        player.getHungerManager().setSaturationLevel(Sync.getSaturation(uuid));
        player.experienceLevel = Sync.getLvl(uuid);
        player.experienceProgress = Sync.getXP(uuid);
    }

    /**
     * Checks if the specified UUID exists in the synchronization table 'PlayerSync' in the MySQL database.
     *
     * @param uuid the UUID to check for in the synchronization table
     * @return true if the UUID exists in the table, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean isInSyncTable(String uuid) throws SQLException {
        try (Statement st = MySQL.con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM PlayerSync WHERE UUID='" + uuid + "'")) {
            return rs.next();
        }
    }


}
