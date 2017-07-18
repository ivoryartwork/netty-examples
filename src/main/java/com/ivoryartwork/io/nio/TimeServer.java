package com.ivoryartwork.io.nio;

/**
 * @author yaochao
 * @version 1.0
 * @date 17-7-18
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 8888;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
            }
        }
        
    }
}
