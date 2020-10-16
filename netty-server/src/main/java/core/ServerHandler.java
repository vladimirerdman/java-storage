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
            /**
             * Getting signal byte
             */
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

            /**
             * Receiving command
             */
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
                    case "/download":
                        System.out.println("Server: download");
                        break;
                    case "/enterToDirectory":
                        System.out.println("Server: enter to dir");
                        break;
                    case "/delete":
                        System.out.println("Server: delete");
                        break;
                    case "/rename":
                        System.out.println("Server: rename");
                        break;
                    case "/createDirectory":
                        System.out.println("Server: create dir");
                        break;
                    case "/updateFileList":
                        currentState = State.FILE_LIST;
                        System.out.println("Server: update file list");
                        break;

                    default:
                        currentState = State.IDLE;
                        throw new IllegalArgumentException("Server: Unknown command: " + stringBuilder.toString());
                }
            }

            /**
             * Receiving file
             */
            if (currentState == State.FILE_NAME_LENGTH) {
                System.out.println("Server: Receiving file");
            }

            if (currentState == State.NAME) {
                System.out.println("Server: Receiving name");
            }

            if (currentState == State.FILE_SIZE) {
                System.out.println("Server: Receiving file size");
            }

            if (currentState == State.FILE) {
                System.out.println("Server: File");
            }

            /**
             * Sending file list
             */
            if (currentState == State.FILE_LIST) {
                System.out.println("Server: File list");
            }

        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
