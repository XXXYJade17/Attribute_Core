package com.XXXYJade17.AttributeCore.NetWork.Handler;


import com.XXXYJade17.AttributeCore.Capability.Shackle.Shackle;
import com.XXXYJade17.AttributeCore.Data.Client.CelestialEssenceData;
import com.XXXYJade17.AttributeCore.Data.Client.ShackleData;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ClientPayloadHandler {
    private static ClientPayloadHandler INSTANCE;
    private static int clientCultivationRealm;
    private static int clientStageRank;
    private static int clientEtherealEssence;
    private static boolean clientShackle;
    private static int clientBreakRate;
    private static int clientRatePerAdd;

    public static ClientPayloadHandler getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ClientPayloadHandler();
        }
        return INSTANCE;
    }

    public void handleCelestialEssenceData(CelestialEssenceData data, PlayPayloadContext context) {
        clientCultivationRealm = data.cultivationRealm();
        clientStageRank = data.stageRank();
        clientEtherealEssence = data.etherealEssence();
        System.out.println("ClientPayloadHandler:"+clientCultivationRealm+","+clientStageRank+","+clientEtherealEssence);
    }

    public void handleShackleData(ShackleData data, PlayPayloadContext context) {
        clientShackle = data.shackle();
        clientBreakRate = data.break_rate();
        clientRatePerAdd = data.rate_per_add();
        System.out.println("ClientPayloadHandler:"+clientShackle+","+clientBreakRate+","+clientRatePerAdd);
    }
}
