package com.XXXYJade17.AttributeCore;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(AttributeCore.MODID)
public class AttributeCore {
    public static final String MODID = "attributecore";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static Logger getLOGGER() {
        return LOGGER;
    }
}
