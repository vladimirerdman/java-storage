package core;

import common.utils.State;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Object> {
    private State currentState;
    private int commandLength = 0;
    private StringBuilder stringBuilder;
    //@Setter
    //private ServiceMessage callback;

    public ClientHandler() { currentState = State.IDLE; }

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
                        System.out.println("Client: download");
                        break;
                    case "/enterToDirectory":
                        System.out.println("Client: enter to dir");
                        break;
                    case "/delete":
                        System.out.println("Client: delete");
                        break;
                    case "/rename":
                        System.out.println("Client: rename");
                        break;
                    case "/createDirectory":
                        System.out.println("Client: create dir");
                        break;
                    case "/updateFileList":
                        currentState = State.FILE_LIST;
                        System.out.println("Client: update file list");
                        break;
                    case "/FileList":
                        //callback.callback(stringBuilder.toString());
                        System.out.println("Client: file list");
                        currentState = State.IDLE;
                        break;

                    default:
                        // TODO: сделать своё исключение
                        currentState = State.IDLE;
                        throw new IllegalArgumentException("Client: Unknown command: " + stringBuilder.toString());
                }
            }

            /**
             * Receiving file
             */
            if (currentState == State.FILE_NAME_LENGTH) {
                System.out.println("Client: Receiving file");
            }

            if (currentState == State.NAME) {
                System.out.println("Client: Receiving name");
            }

            if (currentState == State.FILE_SIZE) {
                System.out.println("Client: Receiving file size");
            }

            if (currentState == State.FILE) {
                System.out.println("Client: File");
            }

            /**
             * Sending file list
             */
            if (currentState == State.FILE_LIST) {
                System.out.println("Client: File list");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
