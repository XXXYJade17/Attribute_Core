package com.XXXYJade17.AttributeCore.Config;

import com.XXXYJade17.AttributeCore.AttributeCore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private static Config INSTANCE;
    private static final Logger LOGGER = AttributeCore.getLOGGER();
    private static final Gson gson= new Gson();
    private static String[] CultivationRealm;
    private static int[][] EtherealEssence;

    private Config() {
        try {
            loadCultivationRealm();
            loadStageRank();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Config getINSTANCE(){
        if(INSTANCE==null){
            INSTANCE=new Config();
        }
        return INSTANCE;
    }

    private void loadCultivationRealm() throws IOException {
        Path path = Path.of("config/attributecore/CultivationRealm.json");
        if(Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        if (Files.notExists(path)) {
            try (InputStream inputStream = Config.class.getResourceAsStream("/"+path)) {
                if (inputStream != null) {
                    Files.copy(inputStream, path);
                } else {
                    LOGGER.warn("CultivationRealm File is empty");
                    return;
                }
            }
        }
        Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        JsonObject cultivationRealm = gson.fromJson(reader, JsonObject.class);
        CultivationRealm = new String[cultivationRealm.size() + 1];
        for(int i=1;i<=cultivationRealm.size();i++){
            LOGGER.info("77");
            CultivationRealm[i]=cultivationRealm.get(String.valueOf(i)).getAsString();
        }
        LOGGER.info("CultivationRealm File has loaded successfully!");
    }

    private void loadStageRank() throws IOException {
        Path path = Path.of("config/attributecore/StageRank.json");
        if(Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        if (Files.notExists(path)) {
            try (InputStream inputStream = Config.class.getResourceAsStream("/"+path)) {
                if (inputStream != null) {
                    Files.copy(inputStream, path);
                } else {
                    LOGGER.warn("StageRank File is empty");
                    return;
                }
            }
        }
        Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        JsonObject stageRank = gson.fromJson(reader, JsonObject.class);
        EtherealEssence = new int[stageRank.size() + 1][];
        for(int i=1;i<=stageRank.size();i++){
            JsonObject etherealEssence= stageRank.get(String.valueOf(i)).getAsJsonObject();
            EtherealEssence[i] = new int[etherealEssence.size() + 1];
            for(int j=1;j<=etherealEssence.size();j++){
                EtherealEssence[i][j]=etherealEssence.get(String.valueOf(i)).getAsInt();
            }
        }
        LOGGER.info("StageRank File has loaded successfully!");
    }
}
