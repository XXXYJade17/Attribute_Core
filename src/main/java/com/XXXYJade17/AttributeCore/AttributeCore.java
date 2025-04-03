package com.XXXYJade17.AttributeCore;

import com.XXXYJade17.AttributeCore.Command.AdminCommand;
import com.XXXYJade17.AttributeCore.Command.PlayerCommand;
import com.XXXYJade17.AttributeCore.Config.Config;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import java.awt.*;

@Mod(AttributeCore.MODID)
public class AttributeCore {
    public static final String MODID = "attributecore";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Config config=Config.getINSTANCE();
    private static AdminCommand adminCommand;
    private static PlayerCommand playerCommand;

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public AttributeCore(IEventBus bus, ModContainer modContainer) {
        adminCommand=AdminCommand.getINSTANCE();
        playerCommand=PlayerCommand.getINSTANCE();
        NeoForge.EVENT_BUS.addListener((ServerStartingEvent event) ->
                playerCommand.registerCommand(event.getServer().getCommands().getDispatcher()));
        NeoForge.EVENT_BUS.addListener((ServerStartingEvent event) ->
                adminCommand.registerCommand(event.getServer().getCommands().getDispatcher())
        );
    }
}
