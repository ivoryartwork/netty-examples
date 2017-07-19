package com.ivoryartwork.io.nio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author yaochao
 * @version 1.0
 * @date 17-7-18
 */
public class MultiplexerTimeServer implements Runnable {

    private ServerSocketChannel serverSocketChannel = null;
    private Selector selector = null;
    private volatile boolean stop;

    public MultiplexerTimeServer(int port) {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.accept();
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("The time server start");
        while (!stop) {
            try {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    doAccept(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
//                if (serverSocketChannel != null) {
//                    try {
//                        serverSocketChannel.close();
//                        serverSocketChannel.socket().close();
//                        serverSocketChannel = null;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        }
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            selector = null;
        }
    }

    private void doAccept(SelectionKey key) {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                try {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (key.isReadable()) {
                doRead(key);
            }
        }
    }

    private void doRead(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            socketChannel.read(buffer);
            buffer.flip();
            if (buffer.hasRemaining()) {
                Charset charset = Charset.forName("UTF-8");
                CharBuffer charBuffer = charset.decode(buffer);
                String body = charBuffer.toString();
                System.out.println("The time server receive:" + body);
                String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date().toString() : "BAD ORDER";
                doWrite(socketChannel, currentTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                    socketChannel.socket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socketChannel = null;
            }
        }
    }

    private void doWrite(SocketChannel socketChannel, String currentTime) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(currentTime.getBytes("UTF-8"));
            socketChannel.write(buffer);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
