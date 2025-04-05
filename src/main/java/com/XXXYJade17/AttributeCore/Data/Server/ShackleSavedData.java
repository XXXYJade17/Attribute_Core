package com.XXXYJade17.AttributeCore.Data.Server;

import com.XXXYJade17.AttributeCore.Capability.CelestialEssence.CelestialEssence;
import com.XXXYJade17.AttributeCore.Capability.Handler.CapabilityHandler;
import com.XXXYJade17.AttributeCore.Capability.Shackle.Shackle;
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

public class ShackleSavedData extends SavedData {
    private static final String DATA_NAME = "ShackleSavedData";
    private static final Map<UUID, Shackle> playerData=new HashMap<>();

    public static ShackleSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(ShackleSavedData::new, ShackleSavedData::load),
                DATA_NAME);
    }

    public static Shackle getPlayerData(UUID playerUUID) {
        return playerData.computeIfAbsent(playerUUID, k -> new Shackle());
    }

    public void savePlayerData(UUID playerUUID, Shackle data) {
        playerData.put(playerUUID, data);
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        ListTag playerList = new ListTag();
        for (Map.Entry<UUID, Shackle> entry : playerData.entrySet()) {
            CompoundTag player = new CompoundTag();
                UUID uuid = entry.getKey();
                player.putUUID("UUID", uuid);
                CompoundTag shackle = new CompoundTag();
                    entry.getValue().saveData(shackle);
                player.put("Shackle", shackle);
            playerList.add(player);
        }
        compound.put("ShackleSavedData", playerList);
        return compound;
    }

    public static ShackleSavedData load(CompoundTag compound) {
        ShackleSavedData savedData = new ShackleSavedData();

        ListTag shackleSavedData = compound.getList("ShackleSavedData", Tag.TAG_COMPOUND);
        for (int i = 0; i < shackleSavedData.size(); i++) {
            CompoundTag playerTag = shackleSavedData.getCompound(i);
            UUID uuid = playerTag.getUUID("UUID");
            CompoundTag shackle = playerTag.getCompound("Shackle");
            Shackle data = new Shackle();
            data.loadData(shackle);
            savedData.playerData.put(uuid, data);
        }
        return savedData;
    }

    public static void saveAllPlayersData() {
        ServerLevel level = ServerLifecycleHooks.getCurrentServer().overworld();
        ShackleSavedData savedData = get(level);
        for (Player player : level.players()) {
            Optional<Shackle> optionalPlayerShackle =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.SHACKLE_HANDLER));
            optionalPlayerShackle.ifPresent(shackle -> {
                savedData.savePlayerData(player.getUUID(), shackle);
            });
        }
    }

    public static void loadAllPlayersData() {
        ServerLevel level = ServerLifecycleHooks.getCurrentServer().overworld();
        ShackleSavedData savedData = get(level);
        for (Player player : level.players()) {
            Shackle data = savedData.getPlayerData(player.getUUID());
            Optional.ofNullable(player.getCapability(CapabilityHandler.SHACKLE_HANDLER))
                    .ifPresent(shackle -> {
                        CompoundTag playerData = new CompoundTag();
                        data.saveData(playerData);
                        shackle.loadData(playerData);
                    });
        }
    }
}
