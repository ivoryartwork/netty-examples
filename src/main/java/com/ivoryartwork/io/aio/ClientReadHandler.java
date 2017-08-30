package com.ivoryartwork.io.aio;

import java.io.IOException;
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
public class ClientReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;
    private CountDownLatch latch;

    public ClientReadHandler(AsynchronousSocketChannel channel, CountDownLatch latch) {
        this.channel = channel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if (result > 0) {
            attachment.flip();
            byte[] buf = new byte[attachment.remaining()];
            attachment.get(buf);
            String message = new String(buf, Charset.forName("UTF-8"));
            System.out.println("当前时间：" + message);
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            latch.countDown();
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
