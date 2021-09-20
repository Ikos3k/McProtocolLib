package me.ANONIMUS.mcprotocol.network.protocol.packet;

public enum PacketDirection {
    SERVERBOUND("client"), CLIENTBOUND("server");

    final String packetsPackageName;

    PacketDirection(String s) {
        this.packetsPackageName = s;
    }
}