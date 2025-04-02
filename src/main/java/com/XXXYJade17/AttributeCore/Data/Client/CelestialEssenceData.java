package com.XXXYJade17.AttributeCore.Data.Client;

import com.XXXYJade17.AttributeCore.AttributeCore;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CelestialEssenceData(int cultivationRealm, int stageRank,int etherealEssence) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(AttributeCore.MODID, "celestial_essence_data");

    public CelestialEssenceData(FriendlyByteBuf buf){
        this(buf.readInt(), buf.readInt(),buf.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(cultivationRealm);
        buf.writeInt(stageRank);
        buf.writeInt(etherealEssence);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
