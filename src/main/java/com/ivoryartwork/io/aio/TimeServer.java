package com.ivoryartwork.io.aio;

/**
 * @author Yaochao
 * @version 1.0
 * @date 2017/8/30
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 8888;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[1]);
            } catch (NumberFormatException e) {
            }
        }
        AsyncTimeServerHandler serverHandler = new AsyncTimeServerHandler(port);
        new Thread(serverHandler, "Thread-TimeServer").start();
    }
}
