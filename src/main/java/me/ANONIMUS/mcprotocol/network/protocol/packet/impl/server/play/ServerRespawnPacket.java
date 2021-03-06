package me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.mcprotocol.network.protocol.Protocol;
import me.ANONIMUS.mcprotocol.network.protocol.data.Difficulty;
import me.ANONIMUS.mcprotocol.network.protocol.data.Dimension;
import me.ANONIMUS.mcprotocol.network.protocol.data.Gamemode;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketBuffer;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ServerRespawnPacket extends Packet {
    private Dimension dimension;
    private Difficulty difficulty;
    private Gamemode gamemode;
    private String level_type;

    @Override
    public void write(PacketBuffer out, int protocol) {
        out.writeInt(this.dimension.ordinal() - 1);
        out.writeByte(this.difficulty.ordinal());
        out.writeGameMode(this.gamemode);
        out.writeString(this.level_type);
    }

    @Override
    public void read(PacketBuffer in, int protocol) {
        this.dimension = Dimension.values()[in.readInt() + 1];
        this.difficulty = Difficulty.values()[in.readUnsignedByte()];
        this.gamemode = in.readGameMode();
        this.level_type = in.readString(16);
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x07, 47), new Protocol(0x33, 107, 108, 109, 110, 210, 315, 316), new Protocol(0x34, 335), new Protocol(0x35, 338, 340));
    }
}