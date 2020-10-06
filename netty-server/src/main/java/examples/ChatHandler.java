package examples;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ChatHandler extends ChannelInboundHandlerAdapter {
    static ConcurrentLinkedDeque<Channel> channels =
            new ConcurrentLinkedDeque<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client with ip: " + ctx.channel().localAddress()
                + " connected!");
        channels.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object m) throws Exception {
        ByteBuf msg = (ByteBuf) m;
        System.out.println(msg);
        ByteBuf ans = ctx.alloc().directBuffer();
        while (msg.isReadable()) {
            char c = (char) msg.readByte();
            System.out.print(c);
            ans.writeByte(c);
        }
        System.out.println();
        channels.forEach(channel ->
                channel.writeAndFlush(ans)
                        .addListener(f -> {
                            if (f.isSuccess()) {
                                System.out.println("LOL");
                            }
                        }));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
