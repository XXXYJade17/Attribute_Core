package com.XXXYJade17.AttributeCore.Event;

import com.XXXYJade17.AttributeCore.AttributeCore;
import com.XXXYJade17.AttributeCore.Capability.CelestialEssence.CelestialEssence;
import com.XXXYJade17.AttributeCore.Capability.Handler.CapabilityHandler;
import com.XXXYJade17.AttributeCore.Capability.Shackle.Shackle;
import com.XXXYJade17.AttributeCore.Config.Config;
import com.XXXYJade17.AttributeCore.Data.Client.CelestialEssenceData;
import com.XXXYJade17.AttributeCore.Data.Server.CelestialEssenceSavedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = AttributeCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE,value = Dist.DEDICATED_SERVER)
public class CelestialEssenceEvent {
    private static final int TICKS_PER_MINUTE = 30*20;
    private static final Map<ServerPlayer, Integer> tickCounters = new HashMap<>();
    private static final Logger LOGGER = AttributeCore.getLOGGER();
    private static final Config config= Config.getINSTANCE();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.side == LogicalSide.SERVER) {
            if (event.player instanceof ServerPlayer player) {
                Optional<Shackle> optionalShackle=
                        Optional.ofNullable(player.getCapability(CapabilityHandler.SHACKLE_HANDLER));
                optionalShackle.ifPresent(shackle ->{
                    if(!shackle.hasShackle()){
                        Optional<CelestialEssence> optionalCE =
                                Optional.ofNullable(player.getCapability(CapabilityHandler.CELESTIAL_ESSENCE_HANDLER));
                        optionalCE.ifPresent(CE -> {
                            int tickCounter = tickCounters.getOrDefault(player, 0)+1;
                            if (tickCounter >= TICKS_PER_MINUTE) {
                                tickCounters.put(player, 0);
                                CE.addEtherealEssence(1);
                                player.sendSystemMessage(config.getMessage("online.reward",1));
                                if(CE.isReachShackle()){
                                    shackle.setShackle(true);
                                    shackle.setBreakRate(config.getInitialBreakRate(CE.getCultivationRealm(), CE.getStageRank()));
                                    shackle.setRatePerAdd(config.getBreakRatePerAdd(CE.getCultivationRealm(), CE.getStageRank()));
                                    CE.breakShackle();
                                }
                                PacketDistributor.PLAYER.with(player)
                                        .send(new CelestialEssenceData(CE.getCultivationRealm(), CE.getStageRank(), CE.getEtherealEssence()));
                            } else {
                                tickCounters.put(player, tickCounter);
                            }
                        });
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                CelestialEssenceSavedData.addUUID(player);
                Optional<CelestialEssence> optionalCE =
                        Optional.ofNullable(player.getCapability(CapabilityHandler.CELESTIAL_ESSENCE_HANDLER));
                optionalCE.ifPresent(CE -> {
                    CelestialEssenceSavedData savedData = CelestialEssenceSavedData.get((ServerLevel) event.getLevel());
                    CompoundTag data=new CompoundTag();
                    savedData.getPlayerData(player.getUUID()).saveData(data);
                    CE.loadData(data);
                    PacketDistributor.PLAYER.with(player)
                            .send(new CelestialEssenceData(CE.getCultivationRealm(), CE.getStageRank(), CE.getEtherealEssence()));
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLeave(EntityLeaveLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                Optional<CelestialEssence> optionalCE =
                        Optional.ofNullable(player.getCapability(CapabilityHandler.CELESTIAL_ESSENCE_HANDLER));
                optionalCE.ifPresent(CE -> {
                    CelestialEssenceSavedData savedData = CelestialEssenceSavedData.get((ServerLevel) event.getLevel());
                    savedData.savePlayerData(player.getUUID(), CE);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        CelestialEssenceSavedData.loadAllPlayersData();
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        CelestialEssenceSavedData.saveAllPlayersData();
    }
}
