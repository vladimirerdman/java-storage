import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class NioServer {
    static final ByteBuffer buffer = ByteBuffer.allocate(256);
    private static final int PORT = 8189;
    private static final String SOURCE_PATH = "/Volumes/Lib/Projects/Java/java-storage/src/main/resources/source/readme.txt";
    private static final String DESTINATION_PATH = "/Volumes/Lib/Projects/Java/java-storage/src/main/resources/destination/readme.txt";
    static int cnt = 1;

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Selector selector = Selector.open();
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(PORT));
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
        while (server.isOpen()) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey current = iterator.next();
                if (current.isAcceptable()) {
                    handleAccept(current, selector);
                }
                if (current.isReadable()) {
                    handleRead(current, selector);
                }
                iterator.remove();
            }
        }
    }

    private static void handleRead(SelectionKey current, Selector selector) throws IOException, ExecutionException, InterruptedException {
        SocketChannel channel = (SocketChannel) current.channel();

        Path sourcePath = Paths.get(SOURCE_PATH);
        Path destinationPath = Paths.get(DESTINATION_PATH);
        FileChannel fileChannelSource = FileChannel.open(sourcePath);
        FileChannel fileChannelDestionation = FileChannel.open(
                destinationPath,
                EnumSet.of(
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE
                )
        );

        System.out.println("Message handled!");
        StringBuilder s = new StringBuilder();
        int x;
        while (true) {
            x = fileChannelSource.read(buffer);
            if (x == -1) {
                fileChannelDestionation.close();
                channel.close();
                System.out.println("Client left");
                break;
            } else if (x == 0) {
                break;
            } else {
                buffer.flip();
                fileChannelDestionation.write(buffer);
            }
            while (buffer.hasRemaining()) {
                s.append((char) buffer.get());
            }
            buffer.clear();
        }
        //fileChannel.close();
        System.out.println("File received");
        //channel.close();

        if (current.isValid()) {
            for (SelectionKey key : selector.keys()) {
                if (key.isValid() && key.channel() instanceof SocketChannel) {
                    ((SocketChannel) key.channel()).write(
                            ByteBuffer.wrap((current.attachment() + ": " + s.toString()).getBytes())
                    );
                }
            }
        }
    }

    private static void handleAccept(SelectionKey current, Selector selector) throws IOException {
        SocketChannel channel = ((ServerSocketChannel)current.channel()).accept();
        channel.configureBlocking(false);
        System.out.println("Client accepted!");
        channel.register(selector, SelectionKey.OP_READ, "user" + (cnt++));
    }
}