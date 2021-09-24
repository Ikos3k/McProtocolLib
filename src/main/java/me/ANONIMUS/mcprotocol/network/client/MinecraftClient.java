package me.ANONIMUS.mcprotocol.network.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Data;
import me.ANONIMUS.mcprotocol.network.objects.Session;
import me.ANONIMUS.mcprotocol.network.protocol.ProtocolType;
import me.ANONIMUS.mcprotocol.network.protocol.data.ConnectionState;
import me.ANONIMUS.mcprotocol.network.protocol.handlers.PacketCodec;
import me.ANONIMUS.mcprotocol.network.protocol.handlers.VarInt21FrameCodec;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketDirection;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client.play.ClientKeepAlivePacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.login.ServerLoginDisconnectPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.login.ServerLoginSetCompressionPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.login.ServerLoginSuccessPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.play.ServerDisconnectPacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.server.play.ServerKeepAlivePacket;
import me.ANONIMUS.mcprotocol.objects.GameProfile;
import me.ANONIMUS.mcprotocol.objects.ServerData;
import net.md_5.bungee.api.chat.BaseComponent;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

@Data
public class MinecraftClient {
    private final ConnectionState connectionState;
    private ClientListener listener;
    private Session remoteSession;
    private EventLoopGroup group;
    private final int protocol;

    public MinecraftClient(final ConnectionState connectionState, final ProtocolType protocolType) {
        this.connectionState = connectionState;
        this.protocol = protocolType.getProtocol();
    }

    public MinecraftClient(final ConnectionState connectionState, final int protocol) {
        this(connectionState, ProtocolType.getByProtocolID(protocol));
    }

    public MinecraftClient(final ConnectionState connectionState, final Session session) {
        this(connectionState, session.getProtocolID());
    }

    public void connect(final String ip, final int port) {
        connect(ip, port, Proxy.NO_PROXY);
    }

    public void connect(final String ip, final int port, final Proxy proxy) {
        if (this.group != null || this.remoteSession != null)
            return;

        this.group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.IP_TOS, 0x18)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        final ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("timer", new ReadTimeoutHandler(30));
                        pipeline.addLast("frameCodec", new VarInt21FrameCodec());
                        pipeline.addLast("packetCodec", new PacketCodec(connectionState, PacketDirection.CLIENTBOUND));
                        pipeline.addLast("handler", new SimpleChannelInboundHandler<Packet>() {

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws InterruptedException {
                                TimeUnit.MILLISECONDS.sleep(150);
                                listener.connected(remoteSession);
                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                cause.printStackTrace();
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) {
                                disconnect(null, ctx.disconnect().cause());
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
                                if (packet instanceof ServerLoginSetCompressionPacket) {
                                    remoteSession.setCompressionThreshold(((ServerLoginSetCompressionPacket) packet).getThreshold());
                                } else if (packet instanceof ServerLoginSuccessPacket) {
                                    remoteSession.setGameProfile(new GameProfile(((ServerLoginSuccessPacket) packet).getUuid(), ((ServerLoginSuccessPacket) packet).getUsername()));
                                    remoteSession.setConnectionState(ConnectionState.PLAY);
                                } else if (packet instanceof ServerLoginDisconnectPacket) {
                                    disconnect(BaseComponent.toLegacyText(((ServerLoginDisconnectPacket) packet).getReason()), null);
                                } else if (packet instanceof ServerDisconnectPacket) {
                                    disconnect(BaseComponent.toLegacyText(((ServerDisconnectPacket) packet).getReason()), null);
                                } else if (packet instanceof ServerKeepAlivePacket) {
                                    remoteSession.sendPacket(new ClientKeepAlivePacket(((ServerKeepAlivePacket) packet).getKeepaliveId()));
                                }

                                listener.packetReceived(remoteSession, packet);
                            }
                        });
                    }
                });
        try {
            remoteSession = (new Session(bootstrap.connect(ip, port).syncUninterruptibly().channel()));
            remoteSession.setProtocolID(protocol);
            remoteSession.setConnectionState(connectionState);
            remoteSession.setServerData(new ServerData(ip, port));
        } catch (Exception e) {
            disconnect("Connection problem!", null);
        }
    }

    public void disconnect(String cause, Throwable throwable) {
        listener.disconnected(remoteSession, cause, throwable);

        if (remoteSession != null) {
            remoteSession.disconnect();
        }

        group.shutdownGracefully();
    }
}