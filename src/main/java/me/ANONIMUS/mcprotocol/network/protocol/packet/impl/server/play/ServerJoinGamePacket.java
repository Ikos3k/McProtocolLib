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

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerJoinGamePacket extends Packet {
    private int entityId;
    private Gamemode gamemode;
    private Dimension dimension;
    private Difficulty difficulty;
    private int maxPlayers;
    private String levelType;
    private boolean reduced_debug;

    @Override
    public void write(PacketBuffer out, int protocol) {
        out.writeInt(this.entityId);
        out.writeGameMode(this.gamemode);
        if (protocol >= 108) {
            out.writeInt(this.dimension.ordinal() - 1);
        } else {
            out.writeByte(this.dimension.ordinal() - 1);
        }
        out.writeByte(this.difficulty.ordinal());
        out.writeByte(this.maxPlayers);
        out.writeString(this.levelType);
        out.writeBoolean(this.reduced_debug);
    }

    @Override
    public void read(PacketBuffer in, int protocol) {
        this.entityId = in.readInt();
        this.gamemode = in.readGameMode();
        if (protocol >= 108) {
            this.dimension = Dimension.values()[in.readInt() + 1];
        } else {
            this.dimension = Dimension.values()[in.readByte() + 1];
        }
        this.difficulty = Difficulty.values()[in.readUnsignedByte()];
        this.maxPlayers = in.readUnsignedByte();
        this.levelType = in.readString(16);

        if (this.levelType == null) {
            this.levelType = "default";
        }

        this.reduced_debug = in.readBoolean();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x01, 47), new Protocol(0x23, 107, 108, 109, 110, 210, 315, 316, 335, 338, 340));
    }
}