package me.ANONIMUS.mcprotocol;

import me.ANONIMUS.mcprotocol.network.protocol.ProtocolType;
import me.ANONIMUS.mcprotocol.network.protocol.data.ConnectionState;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketDirection;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketRegistry;
import me.ANONIMUS.mcprotocol.utils.ColorUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MinecraftProtocol {
    public static final String prefix = "BetterMcProtocol";
    public static final float version = 1.0F;

    public static void init() {
        System.out.println(ColorUtil.ANSI_BRIGHT_WHITE + "Setting up " + prefix + " " + ColorUtil.ANSI_BRIGHT_BLACK + "(" + ColorUtil.ANSI_BRIGHT_YELLOW + version + ColorUtil.ANSI_BRIGHT_BLACK + ")" + ColorUtil.ANSI_BRIGHT_WHITE + " created by " + ColorUtil.ANSI_BRIGHT_RED + "Ikos3k");
        System.out.println();
        PacketRegistry.init();

        System.out.println(ColorUtil.ANSI_BRIGHT_YELLOW + "Supported versions: " + ColorUtil.ANSI_BRIGHT_WHITE + Arrays.stream(ProtocolType.values()).filter(protocolType ->
                protocolType != ProtocolType.PROTOCOL_UNKNOWN).map(ProtocolType::getPrefix).collect(Collectors.joining(ColorUtil.ANSI_BRIGHT_RED + ", " + ColorUtil.ANSI_BRIGHT_WHITE)));
        System.out.println(ColorUtil.ANSI_BRIGHT_YELLOW + "Successfully loaded: " + ColorUtil.ANSI_BRIGHT_WHITE + getPacketsSize() + ColorUtil.ANSI_BRIGHT_YELLOW + " packets" + ColorUtil.ANSI_RESET);
    }

    private static int getPacketsSize() {
        int i = 0;
        for (PacketDirection direction : PacketDirection.values()) {
            for (ConnectionState connectionState : ConnectionState.values()) {
                i += connectionState.getPacketsByDirection(direction).size();
            }
        }
        return i;
    }
}