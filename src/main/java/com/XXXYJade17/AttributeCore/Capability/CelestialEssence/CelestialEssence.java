package com.XXXYJade17.AttributeCore.Capability.CelestialEssence;

import net.minecraft.nbt.CompoundTag;

public class CelestialEssence implements ICelestialEssence {
    private int cultivationRealm =1;
    private int stageRank =1;
    private int etherealEssence=0;

    @Override
    public int getCultivationRealm() {
        return cultivationRealm;
    }

    @Override
    public void setCultivationRealm(int cultivationRealm) {
        this.cultivationRealm = cultivationRealm;
    }

    @Override
    public int getStageRank() {
        return stageRank;
    }

    @Override
    public void setStageRank(int stageRank) {
        this.stageRank = stageRank;
    }

    @Override
    public int getEtherealEssence() {
        return etherealEssence;
    }

    @Override
    public void setEtherealEssence(int etherealEssence) {
        this.etherealEssence = etherealEssence;
    }

    @Override
    public void saveData(CompoundTag nbt) {
        nbt.putInt("cultivation_realm", cultivationRealm);
        nbt.putInt("stage_rank", stageRank);
        nbt.putInt("ethereal_essence", etherealEssence);
    }

    @Override
    public void loadData(CompoundTag nbt) {
        cultivationRealm = nbt.getInt("cultivation_realm");
        stageRank = nbt.getInt("stage_rank");
        etherealEssence = nbt.getInt("ethereal_essence");
    }
}
