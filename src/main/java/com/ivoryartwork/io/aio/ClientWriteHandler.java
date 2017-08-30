package com.ivoryartwork.io.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author Yaochao
 * @version 1.0
 * @date 2017/8/30
 */
public class ClientWriteHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;
    private CountDownLatch latch;

    public ClientWriteHandler(AsynchronousSocketChannel channel, CountDownLatch latch) {
        this.channel = channel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if (attachment.hasRemaining()) {
            channel.write(attachment, attachment, this);
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer, buffer, new ClientReadHandler(channel, latch));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
