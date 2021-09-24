package examples.proxy.handlers;

import me.ANONIMUS.mcprotocol.network.objects.Session;
import me.ANONIMUS.mcprotocol.network.protocol.data.PlayerInfo;
import me.ANONIMUS.mcprotocol.network.protocol.data.ServerStatusInfo;
import me.ANONIMUS.mcprotocol.network.protocol.data.VersionInfo;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client.status.ClientStatusPingPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client.status.ClientStatusRequestPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.status.ServerStatusPongPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.status.ServerStatusResponsePacket;
import me.ANONIMUS.mcprotocol.network.server.MinecraftServer;
import me.ANONIMUS.mcprotocol.network.server.ServerHandler;
import me.ANONIMUS.mcprotocol.objects.GameProfile;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.UUID;

public class ServerStatusHandler extends ServerHandler {
    public ServerStatusHandler(MinecraftServer server) {
        super(server);
    }

    @Override
    public void disconnect() {}

    @Override
    public void handlePacket(Session session, Packet packet) {
        if (packet instanceof ClientStatusRequestPacket) {
            System.out.println("> Ping packet received from: " + session.getChannel().localAddress());

            VersionInfo versionInfo = new VersionInfo("EXAMPLE", session.getProtocolID());
            GameProfile[] gp = new GameProfile[2];
            gp[0] = new GameProfile(UUID.randomUUID(), "xd1");
            gp[1] = new GameProfile(UUID.randomUUID(), "xd2");
            int size = server.getSessions().size() - 1;
            if (size < 0) {
                size = 0;
            }
            PlayerInfo playerInfo = new PlayerInfo(size, 0, gp);
            BaseComponent[] desc = new ComponentBuilder("EXAMPLE MOTD\nEXAMPLE MOTD").create();

            session.sendPacket(new ServerStatusResponsePacket(new ServerStatusInfo(versionInfo, playerInfo, desc, null)));
            session.sendPacket(new ServerStatusPongPacket(0));

            session.disconnect();
        } else if (packet instanceof ClientStatusPingPacket) {
            session.sendPacket(new ServerStatusPongPacket(((ClientStatusPingPacket) packet).getTime()));
            session.disconnect();
        }
    }
}
