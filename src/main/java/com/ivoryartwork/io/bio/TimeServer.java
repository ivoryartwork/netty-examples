package com.ivoryartwork.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Yaochao
 * @version 1.0
 * @date 2017/7/17
 */
public class TimeServer {

    public static void main(String[] args) throws IOException {
        int port = 8888;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[1]);
            } catch (NumberFormatException e) {
            }
        }
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("The time server start int port:" + port);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new TimeServerHanlder(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
                System.out.println("The time server close");
                serverSocket = null;
            }
        }
    }
}
