package com.XXXYJade17.AttributeCore.NetWork.Handler;


import com.XXXYJade17.AttributeCore.Capability.Handler.CapabilityHandler;
import com.XXXYJade17.AttributeCore.Data.Client.CelestialEssenceData;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public class ServerPayloadHandler {
    private static ServerPayloadHandler INSTANCE;

    public static ServerPayloadHandler getINSTANCE() {
        if(INSTANCE==null){
            INSTANCE=new ServerPayloadHandler();
        }
        return INSTANCE;
    }

    public void handleXpData(CelestialEssenceData data, PlayPayloadContext context) {
        context.player().ifPresent(player -> {
            if (player instanceof ServerPlayer serverPlayer) {
                Optional.ofNullable(player.getCapability(CapabilityHandler.CELESTIAL_ESSENCE_HANDLER))
                        .ifPresent(CE -> {
                            PacketDistributor.PLAYER.with(serverPlayer)
                                    .send(new CelestialEssenceData(CE.getCultivationRealm(), CE.getStageRank(), CE.getEtherealEssence()));
                        });
            }
        });
    }
}
