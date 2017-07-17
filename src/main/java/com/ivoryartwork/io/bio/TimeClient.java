package com.ivoryartwork.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Yaochao
 * @version 1.0
 * @date 2017/7/17
 */
public class TimeClient {

    public static void main(String[] args) throws IOException {
        int port = 8888;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[1]);
            } catch (NumberFormatException e) {
            }
        }
        ExecutorService service = Executors.newFixedThreadPool(10000);
        final int finalPort = port;
        for (int i = 0; i < 10000; i++) {
            service.submit(new Runnable() {
                public void run() {
                    BufferedReader reader = null;
                    PrintWriter writer = null;
                    Socket socket = null;
                    try {
                        socket = new Socket("127.0.0.1", finalPort);
                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        writer = new PrintWriter(socket.getOutputStream(), true);
                        while (true) {
                            writer.println("QUERY TIME ORDER");
                            System.out.println("send order to server successed.");
                            String time = reader.readLine();
                            System.out.println("Now is:" + time);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (writer != null) {
                            writer.close();
                            writer = null;
                        }
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            reader = null;
                        }
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            socket = null;
                        }
                    }
                }
            });
        }
        service.shutdown();
    }
}
