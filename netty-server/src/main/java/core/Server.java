package core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class Server {
    private final int PORT;
    private final ServerHandler serverHandler = new ServerHandler();
    private static final Logger log = LogManager.getLogger(Server.class);

    public Server(int port) { this.PORT = port; }

    public Server run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(
                                    //new ObjectEncoder(),
                                    //new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                    serverHandler
                            );
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            log.info("Server started");
            ChannelFuture future = bootstrap.bind(PORT).sync();// Bind and start to accept incoming connections
            future.channel().closeFuture().sync();// Shut down the server
            //log.info("Server closed");
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
        return null;
    }

}
