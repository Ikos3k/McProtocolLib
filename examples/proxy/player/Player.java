package examples.proxy.player;

import lombok.Data;
import me.ANONIMUS.mcprotocol.network.objects.Session;

@Data
public class Player {
    private final Session session;
    private Session remoteSession;
    private ConnectedType connectedType = ConnectedType.DISCONNECTED;

    public boolean isConnected() {
        return remoteSession != null &&
                remoteSession.getChannel().isOpen() &&
                connectedType == ConnectedType.CONNECTED;
    }

    public String getUsername() {
        return session.getGameProfile().getName();
    }

    public enum ConnectedType { CONNECTED, DISCONNECTED }
}