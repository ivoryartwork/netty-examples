package com.ivoryartwork.io.bio.pool;

import com.ivoryartwork.io.bio.TimeServerHanlder;

import java.io.IOException;
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
            TimeServerHandleExecutePool pool = new TimeServerHandleExecutePool(50, 10000);
            while (true) {
                Socket socket = serverSocket.accept();
                pool.execute(new TimeServerHanlder(socket));
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
