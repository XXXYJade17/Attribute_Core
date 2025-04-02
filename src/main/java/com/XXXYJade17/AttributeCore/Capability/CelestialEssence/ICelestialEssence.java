package com.XXXYJade17.AttributeCore.Capability.CelestialEssence;

import net.minecraft.nbt.CompoundTag;

public interface ICelestialEssence {
    int getCultivationRealm();
    void setCultivationRealm(int cultivationRealm);
    int getStageRank();
    void setStageRank(int stageRank);
    int getEtherealEssence();
    void setEtherealEssence(int etherealEssence);
    void saveData(CompoundTag nbt);
    void loadData(CompoundTag nbt);
}
