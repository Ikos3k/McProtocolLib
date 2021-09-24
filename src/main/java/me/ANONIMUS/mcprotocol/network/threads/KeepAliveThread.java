package me.ANONIMUS.mcprotocol.network.threads;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import me.ANONIMUS.mcprotocol.network.objects.Session;
import me.ANONIMUS.mcprotocol.network.protocol.data.ConnectionState;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.play.ServerKeepAlivePacket;

import java.util.List;

@AllArgsConstructor
public class KeepAliveThread extends Thread {
    private List<Session> sessions;

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            sessions.stream()
                .filter(session -> session.getConnectionState() == ConnectionState.PLAY)
                .forEach(session -> session.sendPacket(new ServerKeepAlivePacket((int) System.currentTimeMillis())));

            sleep(3000);
        }
    }

    public void update(List<Session> sessions) {
        this.sessions = sessions;
    }
}