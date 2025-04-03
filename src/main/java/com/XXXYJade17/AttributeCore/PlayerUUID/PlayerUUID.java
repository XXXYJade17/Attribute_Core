package com.XXXYJade17.AttributeCore.PlayerUUID;

import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerUUID {
    private static final Map<String, UUID> playerUUID=new HashMap<>();

    public static void addPlayer(Player player){
        playerUUID.put(player.getName().getString(), player.getUUID());
    }

    public static UUID getUUID(String playerName){
        return playerUUID.get(playerName);
    }
}
