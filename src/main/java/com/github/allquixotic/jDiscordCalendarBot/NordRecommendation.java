package com.github.allquixotic.jDiscordCalendarBot;

import com.google.api.client.util.Key;
import lombok.*;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class NordRecommendation {
    @Key
    private String name;

    @Key
    private String hostname;

    @Key
    private String status;

    @Key
    private int load;

    @Key
    private List<NordService> services;
}


    /*
     * Example of the JSON output of the recommendations API:
     * [
  {
    "id": 965768,
    "created_at": "2020-06-22 10:10:56",
    "updated_at": "2021-01-05 04:02:24",
    "name": "United States #6053",
    "station": "23.82.11.201",
    "hostname": "us6053.nordvpn.com",
    "load": 17,
    "status": "online",
    "locations": [
      {
        "id": 21,
        "created_at": "2017-06-15 14:06:47",
        "updated_at": "2017-06-15 14:06:47",
        "latitude": 38.7508333,
        "longitude": -77.4755556,
        "country": {
          "id": 228,
          "name": "United States",
          "code": "US",
          "city": {
            "id": 9113744,
            "name": "Manassas",
            "latitude": 38.7508333,
            "longitude": -77.4755556,
            "dns_name": "manassas",
            "hub_score": 0
          }
        }
      }
    ],
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
    "technologies": [
      {
        "id": 1,
        "name": "IKEv2/IPSec",
        "identifier": "ikev2",
        "created_at": "2017-03-21 12:00:24",
        "updated_at": "2017-09-05 14:20:16",
        "metadata": [],
        "pivot": {
          "technology_id": 1,
          "server_id": 965768,
          "status": "online"
        }
      },
      {
        "id": 3,
        "name": "OpenVPN UDP",
        "identifier": "openvpn_udp",
        "created_at": "2017-05-04 08:03:24",
        "updated_at": "2017-05-09 19:27:37",
        "metadata": [],
        "pivot": {
          "technology_id": 3,
          "server_id": 965768,
          "status": "online"
        }
      },
      {
        "id": 5,
        "name": "OpenVPN TCP",
        "identifier": "openvpn_tcp",
        "created_at": "2017-05-09 19:28:14",
        "updated_at": "2017-05-09 19:28:14",
        "metadata": [],
        "pivot": {
          "technology_id": 5,
          "server_id": 965768,
          "status": "online"
        }
      },
      {
        "id": 21,
        "name": "HTTP Proxy (SSL)",
        "identifier": "proxy_ssl",
        "created_at": "2017-10-02 12:45:14",
        "updated_at": "2017-10-02 12:45:14",
        "metadata": [],
        "pivot": {
          "technology_id": 21,
          "server_id": 965768,
          "status": "online"
        }
      },
      {
        "id": 23,
        "name": "HTTP CyberSec Proxy (SSL)",
        "identifier": "proxy_ssl_cybersec",
        "created_at": "2017-10-02 12:50:49",
        "updated_at": "2017-10-02 12:50:49",
        "metadata": [],
        "pivot": {
          "technology_id": 23,
          "server_id": 965768,
          "status": "online"
        }
      },
      {
        "id": 35,
        "name": "Wireguard",
        "identifier": "wireguard_udp",
        "created_at": "2019-02-14 14:08:43",
        "updated_at": "2019-02-14 14:08:43",
        "metadata": [
          {
            "name": "public_key",
            "value": "ruufUgS1zKM1Q2UKdfSRYOGpZUFtpAbVfWCHzKmdJCc="
          }
        ],
        "pivot": {
          "technology_id": 35,
          "server_id": 965768,
          "status": "online"
        }
      }
    ],
    "groups": [
      {
        "id": 11,
        "created_at": "2017-06-13 13:43:00",
        "updated_at": "2017-06-13 13:43:00",
        "title": "Standard VPN servers",
        "identifier": "legacy_standard",
        "type": {
          "id": 3,
          "created_at": "2017-06-13 13:40:17",
          "updated_at": "2017-06-13 13:40:23",
          "title": "Legacy category",
          "identifier": "legacy_group_category"
        }
      },
      {
        "id": 15,
        "created_at": "2017-06-13 13:43:38",
        "updated_at": "2017-06-13 13:43:38",
        "title": "P2P",
        "identifier": "legacy_p2p",
        "type": {
          "id": 3,
          "created_at": "2017-06-13 13:40:17",
          "updated_at": "2017-06-13 13:40:23",
          "title": "Legacy category",
          "identifier": "legacy_group_category"
        }
      },
      {
        "id": 21,
        "created_at": "2017-10-27 14:23:03",
        "updated_at": "2017-10-30 08:09:48",
        "title": "The Americas",
        "identifier": "the_americas",
        "type": {
          "id": 5,
          "created_at": "2017-10-27 14:16:30",
          "updated_at": "2017-10-27 14:16:30",
          "title": "Regions",
          "identifier": "regions"
        }
      }
    ],
    "specifications": [
      {
        "id": 8,
        "title": "Version",
        "identifier": "version",
        "values": [
          {
            "id": 257,
            "value": "2.1.0"
          }
        ]
      }
    ],
    "ips": [
      {
        "id": 217847,
        "created_at": "2020-06-22 10:10:56",
        "updated_at": "2020-06-22 10:10:56",
        "server_id": 965768,
        "ip_id": 64426,
        "type": "entry",
        "ip": {
          "id": 64426,
          "ip": "23.82.11.201",
          "version": 4
        }
      }
    ]
  },
     *
     */