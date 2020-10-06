package examples;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.stream.ChunkedFile;
import java.io.File;
import java.io.IOException;

public class Util {

    public static <Person> void main(String[] args) throws IOException {
        Bootstrap bootstrap; // config
        ServerBootstrap serverBootstrap;
        ByteBuf buf;
        NioServerSocketChannel srvChannel;
        ChannelInboundHandlerAdapter in;
        ChannelOutboundHandlerAdapter out;
        NioSocketChannel channel;
        ChunkedFile chunkedFile = new ChunkedFile(new File(""), 8192);
        // chunkedFile.readChunk(null);
        Person person = (Person) examples.Person.builder()
                .setName("Ivan")
                .setSurname("Ivanov")
                .setFatherName("Ivanovich")
                .setEmail("ivanov@mail.ru")
                .build();
    }
}
