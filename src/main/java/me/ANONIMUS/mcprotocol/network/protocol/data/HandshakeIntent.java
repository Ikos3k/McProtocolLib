package me.ANONIMUS.mcprotocol.network.protocol.data;

public enum HandshakeIntent {
    STATUS, LOGIN;

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
}