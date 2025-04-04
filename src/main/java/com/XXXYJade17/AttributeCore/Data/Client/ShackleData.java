package com.XXXYJade17.AttributeCore.Data.Client;

import com.XXXYJade17.AttributeCore.AttributeCore;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ShackleData(boolean shackle, int break_rate, int rate_per_add) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(AttributeCore.MODID, "shackle_data");

    public ShackleData(FriendlyByteBuf buf){
        this(buf.readBoolean(), buf.readInt(), buf.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(shackle);
        buf.writeInt(break_rate);
        buf.writeInt(rate_per_add);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
