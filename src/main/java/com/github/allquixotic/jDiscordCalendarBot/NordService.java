package com.github.allquixotic.jDiscordCalendarBot;

import com.google.api.client.util.Key;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class NordService {
    @Key
    private String identifier;
}


/*
    "services": [
      {
        "id": 1,
        "name": "VPN",
        "identifier": "vpn",
        "created_at": "2017-03-21 12:00:45",
        "updated_at": "2017-05-25 13:12:31"
      },
      {
        "id": 5,
        "name": "Proxy",
        "identifier": "proxy",
        "created_at": "2017-05-29 19:38:30",
        "updated_at": "2017-05-29 19:38:30"
      }
    ],
 */