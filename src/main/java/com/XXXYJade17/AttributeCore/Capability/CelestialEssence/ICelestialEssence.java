package com.XXXYJade17.AttributeCore.Capability.CelestialEssence;

import net.minecraft.nbt.CompoundTag;

public interface ICelestialEssence {
    int getCultivationRealm();
    void setCultivationRealm(int cultivationRealm);
    int getStageRank();
    void setStageRank(int stageRank);
    int getEtherealEssence();
    void addEtherealEssence(int etherealEssence);
    void setEtherealEssence(int etherealEssence);
    boolean hasShackle();
    boolean isReachShackle();
    void breakShackle();
    void saveData(CompoundTag nbt);
    void loadData(CompoundTag nbt);
}
