package core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class Network {
    private static Network network;
    private SocketChannel channel;
    private ClientHandler clientHandler = new ClientHandler();
    private final String HOST;
    private final int PORT;

    public static Network getInstance() {
        return network;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public Network(String host, int port) {
        network = this;
        this.HOST = host;
        this.PORT = port;

        new Thread(() ->{
            EventLoopGroup eventGroup = new NioEventLoopGroup();
            try{
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(eventGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                socketChannel.pipeline()
                                        .addLast(
                                                new ObjectEncoder(),
                                                new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                                clientHandler
                                        );
                            }
                        });
                System.out.println("Client started");
                ChannelFuture future = bootstrap.connect(HOST, PORT).sync();
                future.channel().closeFuture().sync();
                System.out.println("Client closed");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                eventGroup.shutdownGracefully();
            }
        }).start();
    }

    public void close() {
        channel.close();
    }

}
