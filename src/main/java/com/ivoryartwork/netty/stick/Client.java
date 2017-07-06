package com.ivoryartwork.netty.stick;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * @author Yaochao
 * @version 1.0
 */
public class Client {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 9999);
            String message = "hello world";
            byte[] bytes = message.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(4 + bytes.length);
            buffer.putInt(bytes.length);
            buffer.put(bytes);

            byte[] array = buffer.array();

            for (int i = 0; i < 1000; i++) {
                socket.getOutputStream().write(array);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}