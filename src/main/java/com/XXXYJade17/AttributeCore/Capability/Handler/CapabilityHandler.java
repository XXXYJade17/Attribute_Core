package com.XXXYJade17.AttributeCore.Capability.Handler;

import com.XXXYJade17.AttributeCore.AttributeCore;
import com.XXXYJade17.AttributeCore.Capability.CelestialEssence.CelestialEssence;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.EntityCapability;

public class CapabilityHandler {
    public static final EntityCapability<CelestialEssence, Void> CELESTIAL_ESSENCE_HANDLER =
            EntityCapability.createVoid(new ResourceLocation(AttributeCore.MODID, "celestial_essence_handler"),
                    CelestialEssence.class);
}
