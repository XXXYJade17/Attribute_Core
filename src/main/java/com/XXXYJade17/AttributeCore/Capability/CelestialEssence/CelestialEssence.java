package com.XXXYJade17.AttributeCore.Capability.CelestialEssence;

import com.XXXYJade17.AttributeCore.AttributeCore;
import com.XXXYJade17.AttributeCore.Config.Config;
import net.minecraft.nbt.CompoundTag;

public class CelestialEssence implements ICelestialEssence {
    private static final Config config= AttributeCore.getConfig();
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
    public void addEtherealEssence(int etherealEssence) {
        this.etherealEssence += etherealEssence;
        if(!hasShackle()){
            this.etherealEssence -= config.getEtherealEssence(cultivationRealm, stageRank);
            stageRank += 1;
        }else{
            if(this.etherealEssence >= config.getEtherealEssence(cultivationRealm, stageRank)) {
                this.etherealEssence = config.getEtherealEssence(cultivationRealm, stageRank);
            }
        }
    }

    @Override
    public boolean hasShackle() {
        return config.getShackle(cultivationRealm, stageRank);
    }

    @Override
    public boolean isReachShackle() {
        return hasShackle() && etherealEssence == config.getEtherealEssence(cultivationRealm, stageRank);
    }

    @Override
    public void breakShackle() {
        this.etherealEssence=0;
        if(stageRank==10){
            stageRank=1;
            cultivationRealm+=1;
        }else{
            stageRank+=1;
        }
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
