package com.XXXYJade17.AttributeCore.Capability.CelestialEssence;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

public class CelestialEssenceProvider implements ICapabilityProvider<Player, Void, CelestialEssence>, INBTSerializable<CompoundTag> {
    private CelestialEssence celestialEssence;

    private CelestialEssence getCelestialEssence(){
        if(celestialEssence == null){
            celestialEssence = new CelestialEssence();
        }
        return celestialEssence;
    }

    @Override
    public @Nullable CelestialEssence getCapability(Player o, Void unused) {
        return getCelestialEssence();
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        getCelestialEssence().saveData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        getCelestialEssence().loadData(compoundTag);
    }
}
