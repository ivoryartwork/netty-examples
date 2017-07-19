package com.ivoryartwork.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Yaochao
 * @version 1.0
 * @date 2017/7/19
 */
public class MultiplexerTimeClient implements Runnable {

    private SocketChannel socketChannel = null;
    private Selector selector;
    private int port;
    private volatile boolean stop;

    public MultiplexerTimeClient(int port) {
        try {
            this.port = port;
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        doConnect();
        while (!stop) {
            try {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    handler(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
//                if (socketChannel != null) {
//                    try {
//                        socketChannel.close();
//                        socketChannel.socket().close();
//                        socketChannel = null;
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

    private void handler(SelectionKey key) {
        if (key.isValid()) {
            if (key.isConnectable()) {
                doWrite(key);
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
                String msg = new String(buffer.array(), "UTF-8");
                System.out.println("Now is " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWrite(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            if (socketChannel.finishConnect()) {
                String msg = "QUERY TIME ORDER";
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes("UTF-8"));
                socketChannel.write(buffer);
                socketChannel.register(selector, SelectionKey.OP_READ);
            } else {
                stop = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            stop = true;
        }
    }

    private void doConnect() {
        try {
            socketChannel.connect(new InetSocketAddress(port));
            if (socketChannel.isConnected()) {
                socketChannel.register(selector, SelectionKey.OP_READ);
            } else {
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
