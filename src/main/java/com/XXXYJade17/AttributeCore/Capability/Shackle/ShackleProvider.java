package com.XXXYJade17.AttributeCore.Capability.Shackle;

import com.XXXYJade17.AttributeCore.Capability.CelestialEssence.CelestialEssence;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

public class ShackleProvider implements ICapabilityProvider<Player, Void, Shackle>, INBTSerializable<CompoundTag> {
    private Shackle shackle;

    private Shackle getShackle(){
        if(shackle == null){
            shackle = new Shackle();
        }
        return shackle;
    }

    @Override
    public @Nullable Shackle getCapability(Player o, Void unused) {
        return getShackle();
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        getShackle().saveData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        getShackle().loadData(compoundTag);
    }
}
