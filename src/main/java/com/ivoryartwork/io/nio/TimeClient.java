package com.ivoryartwork.io.nio;

/**
 * @author Yaochao
 * @version 1.0
 * @date 2017/7/19
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = 8888;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
            }
        }
        MultiplexerTimeClient timeClient = new MultiplexerTimeClient(port);
        new Thread(timeClient, "MultiplexerTimeClient-thread").start();
    }
}
