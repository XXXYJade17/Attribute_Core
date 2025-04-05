package com.XXXYJade17.AttributeCore.NetWork;

import com.XXXYJade17.AttributeCore.AttributeCore;
import com.XXXYJade17.AttributeCore.Data.Client.CelestialEssenceData;
import com.XXXYJade17.AttributeCore.Data.Client.ShackleData;
import com.XXXYJade17.AttributeCore.NetWork.Handler.ClientPayloadHandler;
import com.XXXYJade17.AttributeCore.NetWork.Handler.ServerPayloadHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

@Mod.EventBusSubscriber(modid = AttributeCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetWork {
    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar registrar = event.registrar(AttributeCore.MODID);

        registrar.play(CelestialEssenceData.ID, CelestialEssenceData::new, handler ->
                handler.client(ClientPayloadHandler.getINSTANCE()::handleCelestialEssenceData)
                        .server(ServerPayloadHandler.getINSTANCE()::handleCelestialEssenceData));

        registrar.play(ShackleData.ID, ShackleData::new, handler ->
                handler.client(ClientPayloadHandler.getINSTANCE()::handleShackleData)
                        .server(ServerPayloadHandler.getINSTANCE()::handleShackleData));
    }
}