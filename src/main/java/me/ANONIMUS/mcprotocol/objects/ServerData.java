package me.ANONIMUS.mcprotocol.objects;

import lombok.Getter;

@Getter
public class ServerData {
    private final String ip;
    private final String host;
    private final int port;

    public ServerData(String host) {
        if (!host.contains(":")) {
            host += ":25565";
        }

        this.host = host;
        this.ip = host.split(":")[0];
        this.port = Integer.parseInt(host.split(":")[1]);
    }

    public ServerData(final String ip, final int port) {
        this.host = ip + ":" + port;
        this.ip = ip;
        this.port = port;
    }
}