package com.XXXYJade17.AttributeCore.NetWork.Handler;


import com.XXXYJade17.AttributeCore.Data.Client.CelestialEssenceData;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ClientPayloadHandler {
    private static ClientPayloadHandler INSTANCE;
    private static int clientCultivationRealm;
    private static int clientStageRank;
    private static int clientEtherealEssence;

    public static ClientPayloadHandler getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ClientPayloadHandler();
        }
        return INSTANCE;
    }

    public void handleXpData(CelestialEssenceData data, PlayPayloadContext context) {
        clientCultivationRealm = data.cultivationRealm();
        clientStageRank = data.stageRank();
        clientEtherealEssence = data.etherealEssence();
        System.out.println("ClientPayloadHandler:"+clientCultivationRealm+","+clientStageRank+","+clientEtherealEssence);
    }
}
