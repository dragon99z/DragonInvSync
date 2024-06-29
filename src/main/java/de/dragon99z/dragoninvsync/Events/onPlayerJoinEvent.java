package de.dragon99z.dragoninvsync.Events;

import de.dragon99z.dragoninvsync.Func.Sync;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Represents an event handler for when a player joins the server.
 * This class synchronizes the player's inventory, ender chest, armor, advancements, health, hunger, saturation, game mode, experience level, and experience progress.
 * It retrieves the player's data from the database using the player's UUID and updates the player's attributes accordingly.
 * If the player's data is not found in the database, it saves the player's current attributes to the database for synchronization.
 * Handles SQLException, IOException, and ClassNotFoundException by saving the player's current attributes to the database.
 */
public class onPlayerJoinEvent {

    /**
     * Constructs a new instance of onPlayerJoinEvent with the specified ServerPlayerEntity.
     * Synchronizes the player's inventory, ender chest, armor, advancements, health, hunger, saturation, game mode, experience level, and experience progress.
     * Retrieves the player's data from the database using the player's UUID and updates the player's attributes accordingly.
     * If the player's data is not found in the database, saves the player's current attributes to the database for synchronization.
     * Handles SQLException, IOException, and ClassNotFoundException by saving the player's current attributes to the database.
     *
     * @param player the ServerPlayerEntity to be synchronized upon joining the server
     */
    public onPlayerJoinEvent(ServerPlayerEntity player){
        joinPlayerSync(player);
    }

    /**
     * Synchronizes the player's inventory, ender chest, armor, advancements, health, hunger, saturation, game mode, experience level, and experience progress.
     * Retrieves the player's data from the database using the player's UUID and updates the player's attributes accordingly.
     * If the player's data is not found in the database, saves the player's current attributes to the database for synchronization.
     * Handles SQLException, IOException, and ClassNotFoundException by saving the player's current attributes to the database.
     *
     * @param player the ServerPlayerEntity whose data is being synchronized
     */
    public void joinPlayerSync(ServerPlayerEntity player) {
        try {
            Sync.syncAllToPlayer(player);
        }catch (SQLException | IOException e){
            Sync.syncAllToDatabase(player);
        }
    }

}
