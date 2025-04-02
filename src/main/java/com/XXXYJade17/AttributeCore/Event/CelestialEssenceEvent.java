package com.XXXYJade17.AttributeCore.Event;

import com.XXXYJade17.AttributeCore.AttributeCore;
import com.XXXYJade17.AttributeCore.Capability.CelestialEssence.CelestialEssence;
import com.XXXYJade17.AttributeCore.Capability.Handler.CapabilityHandler;
import com.XXXYJade17.AttributeCore.Data.Client.CelestialEssenceData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = AttributeCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CelestialEssenceEvent {
    private static final int TICKS_PER_MINUTE = 10*20;
    private static final Map<ServerPlayer, Integer> tickCounters = new HashMap<>();
    private static final Logger LOGGER = AttributeCore.getLOGGER();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer player) {
            Optional<CelestialEssence> optionalPlayerXp =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.CELESTIAL_ESSENCE_HANDLER));
            optionalPlayerXp.ifPresent(CE -> {
                int tickCounter = tickCounters.getOrDefault(player, 0);
                if (++tickCounter >= TICKS_PER_MINUTE) {
                    tickCounters.put(player, 0);
                    CE.setEtherealEssence(CE.getEtherealEssence()+1);
                    PacketDistributor.PLAYER.with(player)
                            .send(new CelestialEssenceData(CE.getCultivationRealm(),CE.getStageRank(),CE.getEtherealEssence()));
                    player.sendSystemMessage(Component.literal("当前EE:" + CE.getEtherealEssence()));
                }else{
                    tickCounters.put(player, tickCounter);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                Optional<CelestialEssence> optionalPlayerXp =
                        Optional.ofNullable(player.getCapability(CapabilityHandler.CELESTIAL_ESSENCE_HANDLER));
                optionalPlayerXp.ifPresent(CE -> {
                    CompoundTag playerData = new CompoundTag();
                    CE.loadData(playerData);
                    if (playerData.contains("ethereal_essence")) {
                        CompoundTag etherealEssence = playerData.getCompound("ethereal_essence");
                        LOGGER.info("Player {}'s Ethereal Essence data has been loaded. Ethereal Essence: {}",etherealEssence);
                    }});
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLeave(EntityLeaveLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                Optional<CelestialEssence> optionalPlayerXp =
                        Optional.ofNullable(player.getCapability(CapabilityHandler.CELESTIAL_ESSENCE_HANDLER));
                optionalPlayerXp.ifPresent(CE -> {
                    CompoundTag playerData = player.getPersistentData();
                    CE.saveData(playerData);
                    LOGGER.info("Player {}'s Ethereal Essence data has been saved.", player.getName().getString());
                });
            }
        }
    }
}
