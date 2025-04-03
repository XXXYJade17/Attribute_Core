package com.XXXYJade17.AttributeCore.Config;

import com.XXXYJade17.AttributeCore.AttributeCore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static Config INSTANCE;
    private static final Logger LOGGER = AttributeCore.getLOGGER();
    private static final Gson gson = new Gson();
    private static final Map<Integer,String> CultivationRealm = new HashMap<>();
    private static Map<Integer,Map<Integer,Integer>> StageRank = new HashMap<>();
    private static Map<Integer, Map<Integer,Boolean>> Shackle= new HashMap<>();

    private Config() {
        try {
            loadCultivationRealm();
            loadStageRank();
            loadShackle();
        } catch (IOException e) {
            LOGGER.error(getLogMessage("file.failed","config"),e);
        }
    }

    public static Config getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }
        return INSTANCE;
    }

    private void loadCultivationRealm() throws IOException {
        Path path = Path.of("config/attributecore/CultivationRealm.json");
        if (Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        if (Files.notExists(path)) {
            try (InputStream inputStream = Config.class.getResourceAsStream("/" + path)) {
                if (inputStream != null) {
                    Files.copy(inputStream, path);
                } else {
                    LOGGER.warn(getLogMessage("file.empty","CultivationRealm.json"));
                    return;
                }
            }
        }
        Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        JsonObject cultivationRealm = gson.fromJson(reader, JsonObject.class);
        for (int i = 1; i <= cultivationRealm.size(); i++) {
            String name = cultivationRealm.get(String.valueOf(i)).getAsString();
            CultivationRealm.put(i,name);
        }
    }

    private void loadStageRank() throws IOException {
        Path path = Path.of("config/attributecore/StageRank.json");
        if (Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        if (Files.notExists(path)) {
            try (InputStream inputStream = Config.class.getResourceAsStream("/" + path)) {
                if (inputStream != null) {
                    Files.copy(inputStream, path);
                } else {
                    LOGGER.warn(getLogMessage("file.empty","StageRank.json"));
                    return;
                }
            }
        }
        Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        JsonObject cultivationRealm = gson.fromJson(reader, JsonObject.class);
        for (int i = 1; i <= cultivationRealm.size(); i++) {
            JsonObject stageRank = cultivationRealm.get(String.valueOf(i)).getAsJsonObject();
            Map<Integer,Integer> EtherealEssence = new HashMap<>();
            for (int j = 1; j <= stageRank.size(); j++) {
                int ee = stageRank.get(String.valueOf(i)).getAsInt();
                EtherealEssence.put(j,ee);
            }
            StageRank.put(i,EtherealEssence);
        }
    }

    private void loadShackle() throws IOException {
        Path path = Path.of("config/attributecore/Shackle.json");
        if (Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        if (Files.notExists(path)) {
            try (InputStream inputStream = Config.class.getResourceAsStream("/" + path)) {
                if (inputStream != null) {
                    Files.copy(inputStream, path);
                } else {
                    LOGGER.warn(getLogMessage("file.empty","Shackle.json"));
                    return;
                }
            }
        }
        Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        JsonObject cultivationRealm = gson.fromJson(reader, JsonObject.class);
        for (int i = 1; i <= cultivationRealm.size(); i++) {
            JsonObject stageRank = cultivationRealm.get(String.valueOf(i)).getAsJsonObject();
            Map<Integer,Boolean> shackle = new HashMap<>();
            for (int j = 1; j <= stageRank.size(); j++) {
                boolean isShackle = stageRank.get(String.valueOf(i)).getAsBoolean();
                shackle.put(j,isShackle);
            }
            Shackle.put(i,shackle);
        }
    }

    public String getCultivationRealm(int cultivationRealm) {
        return CultivationRealm.get(cultivationRealm);
    }

    public int getEtherealEssence(int cultivationRealm, int stageRank) {
        return StageRank.get(cultivationRealm).get(stageRank);
    }

    public boolean getShackle(int cultivationRealm, int stageRank) {
        return Shackle.get(cultivationRealm).get(stageRank);
    }

    public String getLogMessage(String key) {
        return switch (key) {
            case "file.empty" -> "{}文件为空！";
            case "file.failed" -> "{}文件加载失败:";
            default -> key;
        };
    }

    public String getLogMessage(String key,Object... args){
        return String.format(getLogMessage(key),args);
    }
}
