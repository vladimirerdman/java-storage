package core;

import common.utils.Callback;
import common.utils.State;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private Callback callback;
    private State currentState;
    private final MainSettingsController settingsValue = new MainSettingsController();
//    private final String localStoragePath = settingsValue.getLocalStoragePath(); // <- Null point exception

    public ClientHandler() { currentState = State.IDLE; }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        ByteBuf buf = (ByteBuf) msg;
        try {
            while (buf.readableBytes() > 0) {
                if (currentState == State.IDLE) {
                    byte readByte = buf.readByte();
                    if (readByte == (byte) 16) {
                        System.out.println("16 byte");
                        currentState = State.COMMAND;
                    } else if (readByte == (byte) 11) {
                        System.out.println("11 byte");
                        currentState = State.FILE_NAME_LENGTH;
                    } else {
                        currentState = State.IDLE;
                        System.out.println("currentState: " + currentState);
                        System.out.println("Unknown byte command arrived: " + readByte);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            buf.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

}
