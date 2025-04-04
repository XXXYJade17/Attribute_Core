package com.XXXYJade17.AttributeCore.Capability.Shackle;

import net.minecraft.nbt.CompoundTag;

public interface IShackle {
    boolean hasShackle();
    void addBreakRate(int rate);
    void setBreakRate(int rate);
    void setRatePerAdd(int rate);
    boolean breakShackle();
    void saveData(CompoundTag nbt);
    void loadData(CompoundTag nbt);
}
