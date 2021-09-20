package me.ANONIMUS.mcprotocol.network.protocol.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VersionInfo {
    private final String versionName;
    private final int protocolVersion;
}