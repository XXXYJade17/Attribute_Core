package com.XXXYJade17.AttributeCore.Capability.Shackle;

import net.minecraft.nbt.CompoundTag;

import java.util.Random;

public class Shackle implements IShackle{
    private boolean shackle=false;
    private int break_rate=0;
    private int rate_per_add=0;

    @Override
    public boolean hasShackle() {
        return shackle;
    }

    @Override
    public void setShackle(boolean shackle) {
        this.shackle=shackle;
    }

    @Override
    public void addBreakRate(int rate) {
        break_rate+=rate;
    }

    @Override
    public void setBreakRate(int rate) {
        break_rate=rate;
    }

    @Override
    public void setRatePerAdd(int rate) {
        rate_per_add=rate;
    }

    @Override
    public int getBreakRate() {
        return break_rate;
    }

    @Override
    public int getRatePerAdd() {
        return rate_per_add;
    }

    @Override
    public boolean breakShackle() {
        Random random = new Random();
        if(random.nextInt(100)<break_rate){
            shackle=false;
            return true;
        }else{
            break_rate+=rate_per_add;
            return false;
        }
    }

    @Override
    public void saveData(CompoundTag nbt) {
        nbt.putBoolean("shackle", shackle);
        nbt.putInt("break_rate", break_rate);
        nbt.putInt("rate_per_add", rate_per_add);
    }

    @Override
    public void loadData(CompoundTag nbt) {
        shackle = nbt.getBoolean("shackle");
        break_rate = nbt.getInt("break_rate");
        rate_per_add = nbt.getInt("rate_per_add");
    }
}
