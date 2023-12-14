/*
 * Class issue du repo https://github.com/PactifyLauncherExamples/PactifyPlugin
 */
package fr.thebatteur.pacapi.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import pactify.client.api.mcprotocol.AbstractNotchianPacketBuffer;

public class PacketOutBuffer extends AbstractNotchianPacketBuffer<PacketOutBuffer> {
    private final ByteArrayDataOutput handle = ByteStreams.newDataOutput();

    public PacketOutBuffer writeBytes(byte[] src) {
        this.handle.write(src);
        return this;
    }

    public PacketOutBuffer writeBytes(byte[] src, int offset, int len) {
        this.handle.write(src, offset, len);
        return this;
    }

    public PacketOutBuffer writeBoolean(boolean value) {
        this.handle.writeBoolean(value);
        return this;
    }

    public PacketOutBuffer writeByte(int value) {
        this.handle.writeByte(value);
        return this;
    }

    public PacketOutBuffer writeShort(int value) {
        this.handle.writeShort(value);
        return this;
    }

    public PacketOutBuffer writeInt(int value) {
        this.handle.writeInt(value);
        return this;
    }

    public PacketOutBuffer writeLong(long value) {
        this.handle.writeLong(value);
        return this;
    }

    public PacketOutBuffer writeFloat(float value) {
        this.handle.writeFloat(value);
        return this;
    }

    public PacketOutBuffer writeDouble(double value) {
        this.handle.writeDouble(value);
        return this;
    }

    public PacketOutBuffer writeVarInt(int value) {
        while ((value & 0xFFFFFF80) != 0) {
            this.handle.writeByte(value & 0x7F | 0x80);
            value >>>= 7;
        }
        this.handle.writeByte(value);
        return this;
    }

    public PacketOutBuffer writeVarLong(long value) {
        while ((value & 0xFFFFFFFFFFFFFF80L) != 0L) {
            writeByte((int)(value & 0x7FL) | 0x80);
            value >>>= 7L;
        }
        writeByte((int)value);
        return this;
    }

    public PacketOutBuffer writeByteArray(byte[] bytes) {
        writeVarInt(bytes.length);
        writeBytes(bytes);
        return this;
    }

    public PacketOutBuffer writeVarIntArray(int[] ints) {
        writeVarInt(ints.length);
        byte b;
        int i, arrayOfInt[];
        for (i = (arrayOfInt = ints).length, b = 0; b < i; ) {
            int value = arrayOfInt[b];
            writeVarInt(value);
            b++;
        }
        return this;
    }

    public PacketOutBuffer writeLongArray(long[] longs) {
        writeVarInt(longs.length);
        byte b;
        int i;
        long[] arrayOfLong;
        for (i = (arrayOfLong = longs).length, b = 0; b < i; ) {
            long value = arrayOfLong[b];
            writeLong(value);
            b++;
        }
        return this;
    }

    public byte[] toBytes() {
        return this.handle.toByteArray();
    }
}
