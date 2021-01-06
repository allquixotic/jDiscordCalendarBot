package com.github.allquixotic.jDiscordCalendarBot;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Strings;
import com.google.common.reflect.TypeToken;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ProxyGetter {
    private Config conf = null;
    private static final String DEF_REC_API = "https://api.nordvpn.com/v1/servers/recommendations";
    private static final JsonFactory JSON_FACTORY = new GsonFactory();

    public ProxyGetter(@NonNull Config c) {
        conf = c;
    }

    public String getRandomProxy() throws IOException {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory((HttpRequest request) -> request.setParser(new JsonObjectParser(JSON_FACTORY)));
        HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(conf.getProxyListApi() != null ? conf.getProxyListApi() : DEF_REC_API));
        Type type = new TypeToken<List<NordRecommendation>>() {}.getType();
        HttpResponse rawResponse = request.execute();
        List<NordRecommendation> recs = new ArrayList<>(((List<NordRecommendation>) rawResponse.parseAs(type)).stream().filter((rec) ->{
            String status = rec.getStatus();
            boolean hasProxy = rec.getServices().stream().anyMatch((svc) -> !Strings.isNullOrEmpty(svc.getIdentifier()) && svc.getIdentifier().equalsIgnoreCase("proxy"));
            return  hasProxy && rec.getLoad() < conf.getMaxLoadFactor() && !Strings.isNullOrEmpty(status) && status.equalsIgnoreCase("online");
        }).collect(Collectors.toList()));

        if(recs.size() == 0)
            return null;

        String prox = null;
        boolean valid = false;
        do{
            int idx = ThreadLocalRandom.current().nextInt(recs.size());
            prox = recs.get(idx).getHostname();
            if(testProxy(prox)) {
                valid = true;
            }
            else {
                valid = false;
                prox = null;
            }
            recs.remove(idx);
        }
        while(!valid && recs.size() > 0);
        return prox;
    }

    public boolean testProxy(String hostname) {
        String req = String.format("curl -f -s -k --proxy-anyauth -x https://%s:%d -U \"%s:%s\" https://www.google.com/ncr", hostname, conf.getProxyPort(), conf.getProxyUsername(), conf.getProxyPassword());
        String text = null;

        try {
            Process p = Runtime.getRuntime().exec(req);
            int retval = p.waitFor();
            if(retval == 0)
                text = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8)).lines()
                        .collect(Collectors.joining("\n"));
            if(!Strings.isNullOrEmpty(text)) {
                return true;
            }
            else {
                return false;
            }
        }
        catch(Exception e) {
            return false;
        }
    }
}

