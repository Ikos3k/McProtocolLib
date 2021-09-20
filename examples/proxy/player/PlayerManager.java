package examples.proxy.player;

import me.ANONIMUS.mcprotocol.network.objects.Session;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private static final List<Player> players = new ArrayList<>();

    public static void createPlayer(Session session) {
        players.add(new Player(session));
    }

    public static void removePlayer(Session session) {
        players.remove(getPlayer(session));
    }

    public static Player getPlayer(Session session) {
        for (Player p : players) {
            if (p.getSession() == session) {
                return p;
            }
        }
        return null;
    }

    public static List<Player> getPlayers() {
        return players;
    }
}