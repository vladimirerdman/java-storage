package core;

import common.utils.Callback;
import common.utils.State;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LogManager.getLogger(ServerHandler.class.getName());
    private Callback callback;
    private State currentState;
    private int commandLength = 0;
    private StringBuilder sb;

    public ServerHandler() { currentState = State.IDLE; }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        try {
            while (buf.readableBytes() > 0) {
                if (currentState.equals(State.IDLE)) {
                    byte readByte = buf.readByte();

                    //readByte = (byte) 16;
                    if (readByte == (byte) 16) {
                        log.info("Received 16 bytes . " + ctx.channel().localAddress());
                        currentState = State.COMMAND;
                    } else if (readByte == (byte) 11) {
                        log.info("Received 11 bytes . " + ctx.channel().localAddress());
                        currentState = State.FILE_NAME_LENGTH;
                    } else {
                        currentState = State.IDLE;
                        log.error(String.format("[ip: %s]: Unknown byte command arrived: " + readByte, ctx.channel().remoteAddress()));
                        //throw new RuntimeException("Unknown byte command arrived: " + readByte);
                    }
                }

                if (currentState.equals(State.COMMAND)) {
                    if (buf.readableBytes() >= 4) {
                        commandLength = buf.readInt();
                        currentState = State.COMMAND_READ;
                        System.out.println("commandLength: " + commandLength);
                        System.out.println("currentState: " + currentState);
                    }
                }
                if (currentState.equals(State.COMMAND_READ)) {
                    sb = new StringBuilder();
                    while (buf.readableBytes() > 0 && commandLength != 0) {
                        System.out.println("Here");
                        commandLength--;
                        sb.append((char) buf.readByte());
                    }
                    currentState = State.COMMAND_DO;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            buf.release();
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Client with ip: " + ctx.channel().localAddress() + " connected");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Client with ip: " + ctx.channel().localAddress() + " disconnected");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Exception occurred on server side");
        cause.printStackTrace();
        ctx.close();
    }
}
