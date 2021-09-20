package me.ANONIMUS.mcprotocol.network.protocol.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum HandshakeIntent {
    STATUS(1), LOGIN(2);

    private final int id;

    public ConnectionState getConnectionState() {
        switch (this) {
            case LOGIN:
                return ConnectionState.LOGIN;
            case STATUS:
                return ConnectionState.STATUS;
            default:
                return null;
        }
    }

    public static HandshakeIntent getById(int id) {
        return Arrays.stream(values()).filter(v -> v.id == id).findFirst().orElse(STATUS);
    }
}