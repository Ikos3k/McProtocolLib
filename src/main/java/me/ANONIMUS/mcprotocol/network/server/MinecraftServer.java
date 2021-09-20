package me.ANONIMUS.mcprotocol.network.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.Future;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.ANONIMUS.mcprotocol.network.objects.Session;
import me.ANONIMUS.mcprotocol.network.protocol.ProtocolType;
import me.ANONIMUS.mcprotocol.network.protocol.data.ConnectionState;
import me.ANONIMUS.mcprotocol.network.protocol.data.HandshakeIntent;
import me.ANONIMUS.mcprotocol.network.protocol.handlers.PacketCodec;
import me.ANONIMUS.mcprotocol.network.protocol.handlers.VarInt21FrameCodec;
import me.ANONIMUS.mcprotocol.network.protocol.packet.Packet;
import me.ANONIMUS.mcprotocol.network.protocol.packet.PacketDirection;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client.HandshakePacket;
import me.ANONIMUS.mcprotocol.network.protocol.packet.impl.client.play.ClientKeepAlivePacket;
import me.ANONIMUS.mcprotocol.network.threads.KeepAliveThread;
import me.ANONIMUS.mcprotocol.objects.ServerData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class MinecraftServer {
    private final Map<HandshakeIntent, ServerHandler> handlers = new HashMap<>();
    private final List<Session> sessions = new ArrayList<>();
    private KeepAliveThread keepAliveThread;
    private boolean keepaliveTask = true;
    private ServerListener listener;
    private EventLoopGroup group;

    private final String ip;
    private final int port;

    public MinecraftServer(final ServerData serverData) {
        this(serverData.getIp(), serverData.getPort());
    }

    public MinecraftServer(final int port) {
        this("0.0.0.0", port);
    }

    public void bind() {
        if (this.listener == null && this.group != null) {
            return;
        }

        this.group = new NioEventLoopGroup();

        new ServerBootstrap()
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) {
                        final ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast("timer", new ReadTimeoutHandler(10));
                        pipeline.addLast("frameCodec", new VarInt21FrameCodec());
                        pipeline.addLast("packetCodec", new PacketCodec(ConnectionState.HANDSHAKE, PacketDirection.SERVERBOUND));
                        pipeline.addLast(new SimpleChannelInboundHandler<Packet>() {
                            private Session session;

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                super.exceptionCaught(ctx, cause);

                                cause.printStackTrace();
                            }

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) {
                                session = new Session(ctx.channel());

                                sessions.add(session);
                                listener.sessionAdded(session);
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) {
                                sessions.remove(session);
                                listener.sessionRemoved(session);

                                session.disconnect();
                            }


                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
                                if (packet instanceof HandshakePacket) {
                                    final HandshakePacket handshake = (HandshakePacket) packet;
                                    session.setConnectionState(handshake.getIntent().getConnectionState());
                                    session.setHandler(handlers.get(handshake.getIntent()));

                                    if (handshake.getIntent() == HandshakeIntent.LOGIN) {
                                        session.setProtocolID(handshake.getProtocolId());
                                        if (keepaliveTask) {
                                            keepAliveThread.update(sessions);
                                        }

                                        for (ProtocolType protocol : ProtocolType.values()) {
                                            if (handshake.getProtocolId() == protocol.getProtocol()) {
                                                return;
                                            }
                                        }

                                        session.getChannel().close();
                                    }
                                } else {
                                    if (!(packet instanceof ClientKeepAlivePacket)) {
                                        session.getHandler().handlePacket(session, packet);
                                    }
                                }

                                listener.packetReceived(session, packet);
                            }
                        });
                    }
                }).group(this.group).localAddress(this.ip, this.port).bind().addListener((Future<? super Void> future) -> {
                    if (!future.isSuccess()) {
                        close();
                    }
                });

        if (keepaliveTask) {
            keepAliveThread = new KeepAliveThread(sessions);
            keepAliveThread.start();
        }
    }

    public ServerData getServerData() {
        return new ServerData(ip, port);
    }

    public void setHandler(HandshakeIntent intent, ServerHandler handler) {
        if (handlers.containsKey(intent)) {
            return;
        }

        handlers.put(intent, handler);
    }

    public void close() {
        sessions.forEach(Session::disconnect);
        sessions.clear();

        keepAliveThread.stop();
        group.shutdownGracefully();
        listener.serverClosed();
    }
}