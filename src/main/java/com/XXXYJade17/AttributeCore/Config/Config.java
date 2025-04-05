package com.XXXYJade17.AttributeCore.Config;

import com.XXXYJade17.AttributeCore.AttributeCore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private static Map<String, String> translations = new HashMap<>();
    private static final Map<Integer,String> CultivationRealm = new HashMap<>();
    private static Map<Integer,Map<Integer,Integer>> StageRank = new HashMap<>();
    private static Map<Integer, Map<Integer,Boolean>> Shackle= new HashMap<>();
    private static Map<Integer, Map<Integer,Integer>> InitialBreakRate = new HashMap<>();
    private static Map<Integer, Map<Integer,Integer>> BreakRatePerAdd = new HashMap<>();

    private JsonObject config;

    private Config() {
        try {
            loadConfig();
            loadTranslations();
            loadCultivationRealm();
            loadStageRank();
            loadShackle();
        } catch (IOException e) {
            LOGGER.error(getLogMessage("file.failed","config.json"),e);
        }
    }

    public static Config getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }
        return INSTANCE;
    }

    private void loadConfig() throws IOException {
        Path path = Path.of("config/attributecore/config.json");
        Files.createDirectories(path.getParent());
        if (Files.notExists(path)) {
            try (InputStream inputStream = Config.class.getResourceAsStream("/"+path)) {
                if (inputStream != null) {
                    Files.copy(inputStream, path);
                } else {
                    LOGGER.warn(getLogMessage("file.empty","config.json"));
                    return;
                }
            }
        }
        Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        config = gson.fromJson(reader, JsonObject.class);
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
        JsonObject celestialEssence = gson.fromJson(reader, JsonObject.class);
        for (int i = 1; i <= celestialEssence.size(); i++) {
            JsonObject cultivationRealm = celestialEssence.get(String.valueOf(i)).getAsJsonObject();
            Map<Integer,Boolean> shackle = new HashMap<>();
            for (int j = 1; j <= cultivationRealm.size(); j++) {
                JsonObject stageRank = cultivationRealm.get(String.valueOf(j)).getAsJsonObject();
                boolean isShackle = stageRank.get("shackle").getAsBoolean();
                shackle.put(j,isShackle);
                if(isShackle){
                    int breakRate = stageRank.get("initial_break_rate").getAsInt();
                    if (InitialBreakRate.get(i) == null) {
                        Map<Integer,Integer> initialBreakRate = new HashMap<>();
                        initialBreakRate.put(j, breakRate);
                        InitialBreakRate.put(i,initialBreakRate);
                    } else {
                        InitialBreakRate.get(i).put(j, breakRate);
                    }
                    int addRate = stageRank.get("rate_per_add").getAsInt();
                    if (BreakRatePerAdd.get(i) == null) {
                        Map<Integer,Integer> breakRatePerAdd = new HashMap<>();
                        breakRatePerAdd.put(j,addRate);
                        BreakRatePerAdd.put(i,breakRatePerAdd);
                    } else {
                        BreakRatePerAdd.get(i).put(j,addRate);
                    }
                }
            }
            Shackle.put(i,shackle);
        }
    }

    public void loadTranslations() throws IOException {
            Path langDir = Path.of("config/attributecore/lang");
            Files.createDirectories(langDir);

            loadLanguage(langDir,"en_us.json");
            loadLanguage(langDir,"zh_cn.json");

            String fileName = config.get("language").getAsString()+".json";
            Path langPath = langDir.resolve(fileName);
            if(Files.exists(langPath)){
                Reader reader = Files.newBufferedReader(langPath, StandardCharsets.UTF_8);
                translations = new Gson().fromJson(reader, new TypeToken<Map<String, String>>(){}.getType());
            } else{
                loadDefaultLanguage();
            }
    }

    private void loadLanguage(Path langDir, String fileName) throws IOException {
        Path path=langDir.resolve(fileName);
        if (Files.notExists(path)) {
            InputStream inputStream = Config.class.getResourceAsStream("/assets/attributecore/lang/"+fileName);
            if (inputStream != null) {
                Files.copy(inputStream, path);
            }else{
                LOGGER.warn(getLogMessage("file.empty",fileName));
            }
        }
    }

    private void loadDefaultLanguage(){
        InputStream inputStream = Config.class.getResourceAsStream("/assets/attributecore/lang/en_us.json");
        if (inputStream != null) {
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            translations = new Gson().fromJson(reader, new TypeToken<Map<String, String>>(){}.getType());
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
            case "config.failed" -> "AttributeCore配置文件加载失败:";
            case "file.empty" -> "{}文件为空！";
            case "file.failed" -> "{}文件加载失败:";
            default -> key;
        };
    }

    public String getLogMessage(String key,Object... args){
        return String.format(getLogMessage(key),args);
    }

    public static Component getMessage(String key) {
        String text = translations.getOrDefault(key, key);
        return Component.literal(text);
    }

    public static Component getMessage(String key, Object... args) {
        String text = translations.getOrDefault(key, key);
        return Component.literal(String.format(text, args));
    }
}
