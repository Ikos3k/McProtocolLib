package me.ANONIMUS.mcprotocol.network.protocol.packet;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import lombok.AllArgsConstructor;
import me.ANONIMUS.mcprotocol.network.protocol.data.Gamemode;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.apache.commons.codec.Charsets;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@AllArgsConstructor
public class PacketBuffer {
    private final ByteBuf byteBuf;

    public void writeGameMode(Gamemode gamemode) {
        if(gamemode.ordinal() == 4) {
            this.writeByte(8);
        } else {
            this.writeByte(gamemode.ordinal());
        }
    }

    public Gamemode readGameMode() {
        int gamedId = this.readUnsignedByte();
        if(gamedId == 8) {
            return Gamemode.values()[4];
        } else {
            return Gamemode.values()[gamedId];
        }
    }

    public byte[] readByteArray(int length) {
        byte[] bytes = new byte[length];
        this.readBytes(bytes);

        return bytes;
    }

    public byte[] readByteArray() {
        return this.readByteArray(this.readableBytes());
    }

    public void writeBytes(byte[] b, int length) {
        this.writeBytes(b, 0, length);
    }

    public int readVarInt() {
        int i = 0;
        int j = 0;

        while (true) {
            byte b0 = this.readByte();
            i |= (b0 & 127) << j++ * 7;

            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }

            if ((b0 & 128) != 128) {
                break;
            }
        }

        return i;
    }

    public void writeVarInt(int input) {
        while ((input & -128) != 0) {
            this.writeByte(input & 127 | 128);
            input >>>= 7;
        }

        this.writeByte(input);
    }

    public void writeChatComponent(BaseComponent... component) {
        this.writeString(ComponentSerializer.toString(component));
    }

    public BaseComponent[] readChatComponent() {
        return ComponentSerializer.parse(readString());
    }

    public String readString(int maxLength) {
        final int i = this.readVarInt();

        if (i > maxLength * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");
        } else if (i < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        }

        final String s = this.toString(this.readerIndex(), i, StandardCharsets.UTF_8);
        this.readerIndex(this.readerIndex() + i);

        if (s.length() > maxLength) {
            throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
        }

        return s;
    }

    public String readString() {
        int len = this.readVarInt();
        if (len > Short.MAX_VALUE) {
            throw new DecoderException(String.format("Cannot receive string longer than Short.MAX_VALUE (got %s characters)", len));
        }

        byte[] b = new byte[len];
        this.readBytes(b);

        return new String(b, Charsets.UTF_8);
    }

    public void writeString(String string) {
        final byte[] abyte = string.getBytes(StandardCharsets.UTF_8);

        if (abyte.length > 32767) {
            throw new EncoderException("String too big (was " + abyte.length + " bytes encoded, max " + 32767 + ")");
        }

        this.writeVarInt(abyte.length);
        this.writeBytes(abyte);
    }

    public void writeUuid(UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
    }

    public UUID readUuid() {
        return new UUID(this.readLong(), this.readLong());
    }

    public int readableBytes() {
        return byteBuf.readableBytes();
    }

    public int readerIndex() {
        return byteBuf.readerIndex();
    }

    public void readerIndex(int i) {
        byteBuf.readerIndex(i);
    }

    public boolean readBoolean() {
        return byteBuf.readBoolean();
    }

    public byte readByte() {
        return byteBuf.readByte();
    }

    public short readUnsignedByte() {
        return byteBuf.readUnsignedByte();
    }

    public short readShort() {
        return byteBuf.readShort();
    }

    public int readInt() {
        return byteBuf.readInt();
    }

    public long readLong() {
        return byteBuf.readLong();
    }

    public float readFloat() {
        return byteBuf.readFloat();
    }

    public double readDouble() {
        return byteBuf.readDouble();
    }

    public ByteBuf readBytes() {
        return byteBuf.readBytes(readableBytes());
    }

    public void readBytes(byte[] p_readBytes_1_) {
        byteBuf.readBytes(p_readBytes_1_);
    }

    public void writeBoolean(boolean p_writeBoolean_1_) {
        byteBuf.writeBoolean(p_writeBoolean_1_);
    }

    public void writeByte(int p_writeByte_1_) {
        byteBuf.writeByte(p_writeByte_1_);
    }

    public void writeShort(int p_writeShort_1_) {
        this.byteBuf.writeShort(p_writeShort_1_);
    }

    public void writeInt(int p_writeInt_1_) {
        byteBuf.writeInt(p_writeInt_1_);
    }

    public void writeLong(long p_writeLong_1_) {
        byteBuf.writeLong(p_writeLong_1_);
    }

    public void writeFloat(float p_writeFloat_1_) {
        byteBuf.writeFloat(p_writeFloat_1_);
    }

    public void writeDouble(double p_writeDouble_1_) {
        byteBuf.writeDouble(p_writeDouble_1_);
    }

    public void writeBytes(byte[] p_writeBytes_1_) {
        byteBuf.writeBytes(p_writeBytes_1_);
    }

    public void writeBytes(byte[] p_writeBytes_1_, int p_writeBytes_2_, int p_writeBytes_3_) {
        byteBuf.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_3_);
    }

    public void writeBytes(ByteBuf p_writeBytes_1_) {
        byteBuf.writeBytes(p_writeBytes_1_);
    }

    public String toString(Charset p_toString_1_) {
        return byteBuf.toString(p_toString_1_);
    }

    public String toString(int p_toString_1_, int p_toString_2_, Charset p_toString_3_) {
        return byteBuf.toString(p_toString_1_, p_toString_2_, p_toString_3_);
    }

    public void release() {
        byteBuf.release();
    }

    public ByteBuf getByteBuf() {
        return byteBuf;
    }
}

