package examples.proxy.util;

import examples.proxy.player.PlayerManager;
import me.ANONIMUS.mcprotocol.network.objects.Session;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.play.ServerChatPacket;
import net.md_5.bungee.api.ChatColor;

public class ChatUtil {
    public static String fixColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text
                .replace(">>", "»")
                .replace("<<", "«")
                .replace("(o)", "●")
                .replace("(*)", "•"));
    }

    public static void sendChatMessage(String message, Session player, boolean prefix) {
        player.sendPacket(new ServerChatPacket(fixColor((prefix ? "TestProxy &8>> " : "") + "&7" + message)));
    }

    public static void sendBroadcastMessage(String s, boolean b) {
        PlayerManager.getPlayers().forEach(player -> sendChatMessage(s, player.getSession(), b));
    }
}