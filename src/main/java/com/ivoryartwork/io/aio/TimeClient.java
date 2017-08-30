package com.ivoryartwork.io.aio;

import java.io.IOException;

/**
 * @author Yaochao
 * @version 1.0
 * @date 2017/8/30
 */
public class TimeClient {

    public static void main(String[] args) throws IOException {
        int port = 8888;
        String host = "127.0.0.1";
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[1]);
            } catch (NumberFormatException e) {
            }
        }
        new Thread(new AsyncTimeClientHandler(host, port), "Thread-TimeClient").start();
    }
}
