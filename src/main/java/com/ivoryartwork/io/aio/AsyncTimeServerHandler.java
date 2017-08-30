package com.ivoryartwork.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @author Yaochao
 * @version 1.0
 * @date 2017/8/30
 */
public class AsyncTimeServerHandler implements Runnable {

    public AsynchronousServerSocketChannel channel;
    private CountDownLatch latch;

    private int port;

    public AsyncTimeServerHandler(int port) {
        this.port = port;
        try {
            channel = AsynchronousServerSocketChannel.open();
            channel.bind(new InetSocketAddress(port));
            System.out.println("Time server start...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        latch = new CountDownLatch(1);
        channel.accept(this, new ServerAcceptHandler());
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
