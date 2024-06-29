package de.dragon99z.dragoninvsync.Events;

import de.dragon99z.dragoninvsync.Func.MySQL;
import de.dragon99z.dragoninvsync.Func.Sync;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Represents an event handler for the server stopping event.
 * When the server is stopping, this class retrieves data from all online players including their ender chest contents, inventory, armor, advancements,
 * game mode, health, hunger, saturation, experience level, and experience progress. It then stores this data in a MySQL database using the Sync class.
 * If an SQL exception occurs during the data storage process, it logs an error message.
 * Finally, it disconnects from the MySQL database.
 */
public class onServerStoppingEvent implements ServerLifecycleEvents.ServerStopping {

    /**
     * Handles the server stopping event by retrieving data from all online players, including their ender chest contents, inventory, armor, advancements,
     * game mode, health, hunger, saturation, experience level, and experience progress.
     * Stores this data in a MySQL database using the Sync class.
     * If an SQL exception occurs during the data storage process, logs an error message.
     * Finally, disconnects from the MySQL database.
     *
     * @param server the Minecraft server instance
     */
    @Override
    public void onServerStopping(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            Sync.syncAllToDatabase(player);
        }
        MySQL.disconnect();
    }
}
