package me.ANONIMUS.mcprotocol.network.protocol.packet;

import lombok.SneakyThrows;
import me.ANONIMUS.mcprotocol.network.protocol.Protocol;
import me.ANONIMUS.mcprotocol.network.protocol.data.ConnectionState;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.CustomPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client.HandshakePacket;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class PacketRegistry {
    @SneakyThrows
    public static void init() {
        Arrays.asList(PacketDirection.values()).forEach(direction -> Arrays.stream(ConnectionState.values()).filter(connectionState -> connectionState != ConnectionState.HANDSHAKE).forEach(state -> new Reflections("me.ANONIMUS.mcprotocol.network.protocol.packet.impl." + direction.packetsPackageName + "." + state.name().toLowerCase()).getSubTypesOf(Packet.class).forEach(p -> {
            try {
                Packet packet = p.newInstance();
                if (!Modifier.isPublic(packet.getClass().getModifiers())) {
                    throw new IllegalAccessException("Packet " + packet.getClass().getSimpleName() + " has a non public default constructor.");
                }

                state.getPacketsByDirection(direction).add(packet);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        })));
    }

    public static Packet createPacket(ConnectionState connectionState, PacketDirection direction, int id, int protocol) {
        Packet packet = getPacket(connectionState, direction, id, protocol);

        if (packet == null) return new CustomPacket(id);

        try {
            Constructor<? extends Packet> constructor = packet.getClass().getDeclaredConstructor();

            constructor.setAccessible(true);

            return constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate packet \"" + packet.getClass().getName() + "\".", e);
        }
    }

    private static Packet getPacket(ConnectionState connectionState, PacketDirection direction, int id, int protocol) {
        if (connectionState == ConnectionState.HANDSHAKE) {
            return new HandshakePacket();
        }

        for (Packet packet : connectionState.getPacketsByDirection(direction)) {
            if (packet.getProtocolList().size() == 1) {
                if (packet.getProtocolList().get(0).getId() == id) {
                    return packet;
                }
            }

            for (Protocol protocol2 : packet.getProtocolList()) {
                if (protocol2.getId() == id) {
                    for (int p : protocol2.getProtocols()) {
                        if (p == protocol) {
                            return packet;
                        }
                    }
                }
            }
        }

        return null;
    }
}