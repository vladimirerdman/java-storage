package core;

import common.utils.State;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {
    private State currentState;
    private int commandLength = 0;
    private StringBuilder stringBuilder;

    public ServerHandler() { currentState = State.IDLE; }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        while (buf.readableBytes() > 0) {
            if (currentState == State.IDLE) {
                byte readCode = buf.readByte();
                if (readCode == (byte)10) {
                    currentState = State.COMMAND;
                    System.out.println("Received code: " + readCode);
                } else {
                    currentState = State.IDLE;
                    System.out.println("Unknown byte command arrived: " + readCode);
                }
            }

            if (currentState == State.COMMAND) {
                if (buf.readableBytes() >= 4) {
                    commandLength = buf.readInt();
                    currentState = State.COMMAND_READ;
                    System.out.println("Received command: " + commandLength);
                }
            }
            if (currentState == State.COMMAND_READ) {
                stringBuilder = new StringBuilder();
                while (buf.readableBytes() > 0 && commandLength != 0) {
                    commandLength--;
                    stringBuilder.append((char) buf.readByte());
                }
                currentState = State.COMMAND_DO;
            }

            if (currentState == State.COMMAND_DO) {
                String[] command = stringBuilder.toString().split("\n");
                switch (command[0]) {
                    // TODO: Add switch for each command
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}
