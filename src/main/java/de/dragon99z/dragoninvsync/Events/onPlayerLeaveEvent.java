package de.dragon99z.dragoninvsync.Events;

import de.dragon99z.dragoninvsync.Func.Sync;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * The 'onPlayerLeaveEvent' class handles events when a player leaves the server.
 * It syncs the player's ender chest contents, inventory, armor, advancements, game mode, health, hunger, saturation,
 * experience level, and experience progress to a MySQL database.
 * The class provides a method to update the synchronization table with the player's data upon leaving the server.
 */
public class onPlayerLeaveEvent {

    /**
     * Constructor for the 'onPlayerLeaveEvent' class.
     * Initializes the synchronization of the player's data upon leaving the server.
     *
     * @param player The player entity leaving the server.
     */
    public onPlayerLeaveEvent(ServerPlayerEntity player){
        leftPlayerSync(player);
    }

    /**
     * Synchronizes the player's data to a MySQL database upon leaving the server.
     *
     * @param player the ServerPlayerEntity object representing the player leaving the server
     */
    public void leftPlayerSync(ServerPlayerEntity player) {
        Sync.syncAllToDatabase(player);
    }

}
