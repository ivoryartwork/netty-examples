package com.ivoryartwork.io.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author Yaochao
 * @version 1.0
 * @date 2017/8/30
 */
public class ServerReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;

    public ServerReadHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if (result > 0) {
            attachment.flip();
            byte[] buf = new byte[attachment.remaining()];
            attachment.get(buf);
            String message = new String(buf, Charset.forName("UTF-8"));
            System.out.println("the time server received:" + message);
            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(message) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
            doWrite(currentTime);
        }
    }

    private void doWrite(String message) {
        ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes(Charset.forName("UTF-8")));
        channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if (attachment.hasRemaining()) {
                    //没有写完，继续写
                    channel.write(writeBuffer, writeBuffer, this);
                } else {
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    channel.read(readBuffer, readBuffer, new ServerReadHandler(channel));
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
        });
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
