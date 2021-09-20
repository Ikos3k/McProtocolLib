package examples.proxy.command.impl;

import examples.proxy.command.Command;
import examples.proxy.player.Player;
import examples.proxy.util.ChatUtil;
import examples.proxy.util.ConnectionUtils;
import examples.proxy.util.WorldUtil;
import me.ANONIMUS.mcprotocol.network.client.ClientListener;
import me.ANONIMUS.mcprotocol.network.client.MinecraftClient;
import me.ANONIMUS.mcprotocol.network.objects.Session;
import me.ANONIMUS.mcprotocol.network.protocol.data.ConnectionState;
import me.ANONIMUS.mcprotocol.network.protocol.data.HandshakeIntent;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client.HandshakePacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client.login.ClientLoginStartPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.play.ServerJoinGamePacket;

import java.net.Proxy;

public class CommandJoin extends Command {
    public CommandJoin() {
        super("join");
    }

    @Override
    public void execute(Player player, String[] args) {
        String ip = args[1];
        int port = 25565;

        if (ip.contains(":")) {
            final String[] split = ip.split(":", 2);
            ip = split[0];
            port = Integer.parseInt(split[1]);
        }

        if (ConnectionUtils.hasConnectionError(ip, port, 500)) {
            final String[] resolved = ConnectionUtils.getServerAddress(ip);
            ip = resolved[0];
            port = Integer.parseInt(resolved[1]);
            if (ConnectionUtils.hasConnectionError(ip, port, 500)) {
                ChatUtil.sendChatMessage("&cThe server has a connection problem or is down!", player.getSession(), true);
                return;
            }
        }

        MinecraftClient client = new MinecraftClient(ConnectionState.LOGIN, player.getSession().getProtocolID());
        client.setListener(new ClientListener() {
            @Override
            public void connected(Session session) {
                session.sendPacket(new HandshakePacket(client.getProtocol(), session.getServerData().getIp(), session.getServerData().getPort(), HandshakeIntent.LOGIN));
                session.sendPacket(new ClientLoginStartPacket(args[2]));

                player.setRemoteSession(session);
            }

            @Override
            public void disconnected(Session session, String reason, Throwable cause) {
                player.setConnectedType(Player.ConnectedType.DISCONNECTED);
            }

            @Override
            public void packetReceived(Session session, Packet packet) {
                if (packet instanceof ServerJoinGamePacket) {
                    WorldUtil.dimSwitch(player.getSession(), (ServerJoinGamePacket) packet);
                    player.setConnectedType(Player.ConnectedType.CONNECTED);
                } else if (session.getConnectionState() == ConnectionState.PLAY && player.isConnected()) {
                    player.getSession().sendPacket(packet);
                }
            }
        });

        client.connect(ip, port, Proxy.NO_PROXY);
    }
}