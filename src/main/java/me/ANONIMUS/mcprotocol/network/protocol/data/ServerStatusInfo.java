package me.ANONIMUS.mcprotocol.network.protocol.data;

import lombok.Data;
import net.md_5.bungee.api.chat.BaseComponent;

@Data
public class ServerStatusInfo {
    private final VersionInfo versionInfo;
    private final PlayerInfo playerInfo;
    private final BaseComponent[] description;
    private final String icon;
}