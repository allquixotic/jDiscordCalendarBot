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
import lombok.val;

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

        Main.log.info(String.format("Got %d proxies from Recommendations list.", recs != null ? recs.size() : 0));

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
        String silent = Strings.isNullOrEmpty(conf.getSilent()) ? "-s" : conf.getSilent().equalsIgnoreCase("false") ? "" : "-s";
        String req = String.format("\"%s\" -f %s -k %s --proxy-anyauth -x https://%s:%d -U \"%s:%s\" https://www.google.com/ncr",
                Strings.isNullOrEmpty(conf.getCurlPath()) ? "curl" : conf.getCurlPath(),
                silent,
                Strings.isNullOrEmpty(conf.getCurlExtra()) ? "" : conf.getCurlExtra(),
                hostname,
                conf.getProxyPort() == 0 ? 89 : conf.getProxyPort(),
                conf.getProxyUsername(),
                conf.getProxyPassword());
        Main.log.info("Executing " + req);
        String text = null;

        try {
            Main.log.info("Testing proxy " + hostname);
            Process p = Runtime.getRuntime().exec(req);
            int retval = p.waitFor();
            text = "" + new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8)).lines()
                        .collect(Collectors.joining("\n")) + new BufferedReader(new InputStreamReader(p.getErrorStream(), StandardCharsets.UTF_8)).lines()
                    .collect(Collectors.joining("\n"));
            if(retval == 0 && !Strings.isNullOrEmpty(text)) {
                Main.log.info("Proxy works!");
                return true;
            }
            else {
                Main.log.info(String.format("Proxy %s failed. Return code: %d, Output: %s", hostname, retval, text));
                return false;
            }
        }
        catch(Exception e) {
            Main.log.info("Exception received trying to test proxy!");
            Main.logSevere(e);
            return false;
        }
    }
}

