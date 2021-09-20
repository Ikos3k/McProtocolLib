package me.ANONIMUS.mcprotocol.network.protocol.data;

import lombok.Data;
import me.ANONIMUS.mcprotocol.objects.GameProfile;

@Data
public class PlayerInfo {
    private final int onlinePlayers, maxPlayers;
    private final GameProfile[] players;
}