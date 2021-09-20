package me.ANONIMUS.mcprotocol.network.objects;

import io.netty.channel.Channel;
import lombok.Data;
import me.ANONIMUS.mcprotocol.network.protocol.data.ConnectionState;
import me.ANONIMUS.mcprotocol.network.protocol.handlers.CompressionCodec;
import me.ANONIMUS.mcprotocol.network.protocol.handlers.PacketCodec;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.server.ServerHandler;
import me.ANONIMUS.mcprotocol.objects.GameProfile;
import me.ANONIMUS.mcprotocol.objects.ServerData;

@Data
public class Session {
    private final Channel channel;
    private ServerHandler handler;
    private GameProfile gameProfile;
    private ServerData serverData;

    public void sendPacket(Packet p) {
        channel.writeAndFlush(p);
    }

    public void setConnectionState(ConnectionState state) {
        getPacketCodec().setConnectionState(state);
    }

    public void setProtocolID(int protocol) {
        getPacketCodec().setProtocol(protocol);
    }

    public int getProtocolID() {
        return getPacketCodec().getProtocol();
    }

    public ConnectionState getConnectionState() {
        return getPacketCodec().getConnectionState();
    }

    public PacketCodec getPacketCodec() {
        return ((PacketCodec) channel.pipeline().get("packetCodec"));
    }

    public void setCompressionThreshold(final int threshold) {
        if (getConnectionState() == ConnectionState.LOGIN) {
            if (channel.pipeline().get("compression") == null) {
                channel.pipeline().addBefore("packetCodec", "compression", new CompressionCodec(threshold));
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void disconnect() {
        if (handler != null) {
            handler.disconnect();
        }
        if (channel != null) {
            channel.close();
        }
    }

    @Override
    public String toString() {
        return "channel: " + channel.remoteAddress()
                + " profile: " + (gameProfile != null ? gameProfile : "null");
    }
}