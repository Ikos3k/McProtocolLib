package examples.proxy.util;

import me.ANONIMUS.mcprotocol.network.objects.Session;
import me.ANONIMUS.mcprotocol.network.protocol.data.Difficulty;
import me.ANONIMUS.mcprotocol.network.protocol.data.Dimension;
import me.ANONIMUS.mcprotocol.network.protocol.data.Gamemode;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.play.ServerJoinGamePacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.play.ServerPlayerAbilitiesPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.play.ServerPlayerPosLookPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.play.ServerRespawnPacket;

public class WorldUtil {
    public static void dimSwitch(Session player, ServerJoinGamePacket packet) {
        player.sendPacket(new ServerRespawnPacket(Dimension.END, Difficulty.PEACEFULL, Gamemode.SURVIVAL, "default_1_1"));
        player.sendPacket(packet);
        player.sendPacket(new ServerRespawnPacket(packet.getDimension(), packet.getDifficulty(), packet.getGamemode(), packet.getLevelType()));
    }

    public static void emptyWorld(Session player) {
        dimSwitch(player, new ServerJoinGamePacket(0, Gamemode.SURVIVAL, Dimension.OVERWORLD, Difficulty.PEACEFULL, 1, "default_1_1", false));
        player.sendPacket(new ServerPlayerAbilitiesPacket(false, true, false, false, 0f, 0f));
        player.sendPacket(new ServerPlayerPosLookPacket(0, 70, 0, 180, 90));
    }
}