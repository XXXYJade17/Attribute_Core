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
    private static final Map<String, UUID> playerUUID=new HashMap<>();

    public static void addUUID(Player player){
        playerUUID.put(player.getName().getString(), player.getUUID());
    }

    public static UUID getUUID(String playerName){
        return playerUUID.get(playerName);
    }

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
        ListTag celestialEssenceSavedData = new ListTag();
        for (Map.Entry<UUID, CelestialEssence> entry : playerData.entrySet()) {
            CompoundTag player = new CompoundTag();
                UUID uuid = entry.getKey();
                player.putUUID("UUID", uuid);
                CompoundTag celestialEssence = new CompoundTag();
                    entry.getValue().saveData(celestialEssence);
                player.put("CelestialEssence", celestialEssence);
            celestialEssenceSavedData.add(player);
        }
        compound.put("CelestialEssenceSavedData", celestialEssenceSavedData);

        ListTag uuidList = new ListTag();
        for (Map.Entry<String, UUID> entry : playerUUID.entrySet()) {
            CompoundTag uuidTag = new CompoundTag();
            uuidTag.putString("Name", entry.getKey());
            uuidTag.putUUID("UUID", entry.getValue());
            uuidList.add(uuidTag);
        }
        compound.put("UUIDList", uuidList);

        return compound;
    }

    public static CelestialEssenceSavedData load(CompoundTag compound) {
        CelestialEssenceSavedData savedData = new CelestialEssenceSavedData();

        ListTag celestialEssenceSavedData = compound.getList("CelestialEssenceSavedData", Tag.TAG_COMPOUND);
        for (int i = 0; i < celestialEssenceSavedData.size(); i++) {
            CompoundTag playerTag = celestialEssenceSavedData.getCompound(i);
            UUID uuid = playerTag.getUUID("UUID");
            CompoundTag celestialEssence = playerTag.getCompound("CelestialEssence");
            CelestialEssence data = new CelestialEssence();
                data.loadData(celestialEssence);
            savedData.playerData.put(uuid, data);
        }

        ListTag uuidList = compound.getList("UUIDList", Tag.TAG_COMPOUND);
        for (int i = 0; i < uuidList.size(); i++) {
            CompoundTag uuidTag = uuidList.getCompound(i);
                String name = uuidTag.getString("Name");
                UUID uuid = uuidTag.getUUID("UUID");
            savedData.playerUUID.put(name, uuid);
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
