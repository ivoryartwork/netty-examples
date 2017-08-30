package com.ivoryartwork.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

/**
 * @author Yaochao
 * @version 1.0
 * @date 2017/8/30
 */
public class AsyncTimeClientHandler implements Runnable {

    private int port;
    private String host;
    public AsynchronousSocketChannel channel;
    private CountDownLatch latch;

    public AsyncTimeClientHandler(String host, int port) {
        this.port = port;
        this.host = host;
        try {
            channel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        channel.connect(new InetSocketAddress(host, port), this, new CompletionHandler<Void, AsyncTimeClientHandler>() {
            @Override
            public void completed(Void result, AsyncTimeClientHandler attachment) {
                String cmd = "QUERY TIME ORDER";
                ByteBuffer buffer = ByteBuffer.wrap(cmd.getBytes(Charset.forName("UTF-8")));
                attachment.channel.write(buffer, buffer, new ClientWriteHandler(channel, latch));
            }

            @Override
            public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
                try {
                    attachment.channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
