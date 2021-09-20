package examples.proxy.handlers;

import examples.proxy.command.CommandManager;
import examples.proxy.player.Player;
import examples.proxy.player.PlayerManager;
import examples.proxy.util.ChatUtil;
import me.ANONIMUS.mcprotocol.network.objects.Session;
import me.ANONIMUS.mcprotocol.network.protocol.ProtocolType;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client.play.ClientChatPacket;
import me.ANONIMUS.mcprotocol.network.server.MinecraftServer;
import me.ANONIMUS.mcprotocol.network.server.ServerHandler;

public class ServerPlayHandler extends ServerHandler {
    private final Player player;

    public ServerPlayHandler(MinecraftServer server, Session session) {
        super(server);

        this.player = PlayerManager.getPlayer(session);
    }

    @Override
    public void disconnect() {
        if (player != null && player.getSession() != null) {
            System.out.println("PLAY -> disconnected " + player.getUsername());
        }
    }

    @Override
    public void handlePacket(Session session, Packet packet) {
        if (packet instanceof ClientChatPacket) {
            if (((ClientChatPacket) packet).getMessage().startsWith(",")) {
                CommandManager.handle(session, ((ClientChatPacket) packet).getMessage());
                return;
            }
            if (((ClientChatPacket) packet).getMessage().startsWith("@")) {
                ChatUtil.sendBroadcastMessage("&8(&f" + ProtocolType.getByProtocolID(player.getSession().getProtocolID()).getPrefix() + "&8) " + player.getUsername() + " &8>> &7" + ((ClientChatPacket) packet).getMessage().substring(1), false);
                return;
            }
        }

        if (player.isConnected()) {
            player.getRemoteSession().sendPacket(packet);
        }
    }
}