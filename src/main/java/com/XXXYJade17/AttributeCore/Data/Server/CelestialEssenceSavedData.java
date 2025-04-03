package com.XXXYJade17.AttributeCore.Data.Server;

import com.XXXYJade17.AttributeCore.Capability.CelestialEssence.CelestialEssence;
import com.XXXYJade17.AttributeCore.Capability.Handler.CapabilityHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CelestialEssenceSavedData extends SavedData {
    private static final String DATA_NAME = "CelestialEssenceSavedData";
    private static final Map<UUID, CelestialEssence> playerData = new HashMap<>();

    public static CelestialEssenceSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(CelestialEssenceSavedData::new, CelestialEssenceSavedData::load),
                DATA_NAME);

    }

    public static CelestialEssence getPlayerData(UUID playerUUID) {
        return playerData.computeIfAbsent(playerUUID, k -> new CelestialEssence());
    }

    public void savePlayerData(UUID playerUUID, CelestialEssence data) {
        playerData.put(playerUUID, data);
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        ListTag playerList = new ListTag();
        for (Map.Entry<UUID, CelestialEssence> entry : playerData.entrySet()) {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID("PlayerUUID", entry.getKey());
            CompoundTag dataTag = new CompoundTag();
            entry.getValue().saveData(dataTag);
            playerTag.put("StageRank.json", dataTag);
            playerList.add(playerTag);
        }
        compound.put("Players", playerList);
        return compound;
    }

    public static CelestialEssenceSavedData load(CompoundTag compound) {
        CelestialEssenceSavedData savedData = new CelestialEssenceSavedData();
        ListTag playerList = compound.getList("Players", Tag.TAG_COMPOUND);
        for (int i = 0; i < playerList.size(); i++) {
            CompoundTag playerTag = playerList.getCompound(i);
            UUID playerUUID = playerTag.getUUID("PlayerUUID");
            CompoundTag dataTag = playerTag.getCompound("StageRank.json");
            CelestialEssence data = new CelestialEssence();
            data.loadData(dataTag);
            savedData.playerData.put(playerUUID, data);
        }
        return savedData;
    }

    public static void saveAllPlayersData() {
        ServerLevel level = ServerLifecycleHooks.getCurrentServer().overworld();
        CelestialEssenceSavedData savedData = get(level);
        for (Player player : level.players()) {
            Optional<CelestialEssence> optionalPlayerXp =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.CELESTIAL_ESSENCE_HANDLER));
            optionalPlayerXp.ifPresent(CE -> {
                savedData.savePlayerData(player.getUUID(), CE);
            });
        }
    }

    public static void loadAllPlayersData() {
        ServerLevel level = ServerLifecycleHooks.getCurrentServer().overworld();
        CelestialEssenceSavedData savedData = get(level);
        for (Player player : level.players()) {
            CelestialEssence data = savedData.getPlayerData(player.getUUID());
            Optional.ofNullable(player.getCapability(CapabilityHandler.CELESTIAL_ESSENCE_HANDLER))
                    .ifPresent(CE -> {
                        CompoundTag playerData = new CompoundTag();
                        data.saveData(playerData);
                        CE.loadData(playerData);
                    });
        }
    }
}
