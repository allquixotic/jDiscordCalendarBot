package com.github.allquixotic.jDiscordCalendarBot;

import com.google.api.client.util.Key;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;

import java.io.FileReader;
import java.io.IOException;

@Data
@Builder
public class Config {
    @Key private String loginUrl;
    @Key private String calendarUrl;
    @Key private String discordSecret;
    @Key private String discordChannel;
    @Key private String enjinUsername;
    @Key private String enjinPassword;
    @Key private String proxyListApi;
    @Key private String proxyUsername;
    @Key private String proxyPassword;
    @Key private String curlPath;
    @Key private String silent;
    @Key private String[] curlExtra;
    @Key private String testUrl;
    @Key private int proxyPort;
    @Key private int updateFrequency;
    @Key private int maxLoadFactor;

    public static Config readConfig() throws IOException {
        return readConfig("config.json");
    }

    public static Config readConfig(String file) throws IOException {
        return new Gson().fromJson(new FileReader(file), new TypeToken<Config>() {}.getType());
    }
}
