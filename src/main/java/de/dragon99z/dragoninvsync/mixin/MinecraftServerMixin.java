package de.dragon99z.dragoninvsync.mixin;

import de.dragon99z.dragoninvsync.Func.Sync;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

import static de.dragon99z.dragoninvsync.DragonInvSync.LOGGER;
import static de.dragon99z.dragoninvsync.DragonInvSync.config;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow
    private PlayerManager playerManager;

    @Shadow
    private boolean running;

    @Unique
    private int ticks = 0;
    @Unique
    private int sec = 0;

    @Unique
    boolean isRunning = false;

    @Inject(method = "loadWorld", at = @At("RETURN"))
    void runServer(CallbackInfo ci){
        LOGGER.info("Starting Player Sync!");
        isRunning = true;
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void syncPlayer(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
        if(isRunning && running){
            ticks++;
            if(ticks % 20 == 0){
                sec++;
            }
            if(sec >= config.general.syncDelay){
                for(ServerPlayerEntity player : playerManager.getPlayerList()){
                    Sync.syncAllToDatabase(player);
                }
                sec = 0;
            }
        }
    }

}
