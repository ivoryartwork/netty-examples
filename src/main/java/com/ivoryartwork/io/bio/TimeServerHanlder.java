package com.ivoryartwork.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * @author Yaochao
 * @version 1.0
 * @date 2017/7/17
 */
public class TimeServerHanlder implements Runnable {

    private Socket socket;

    public TimeServerHanlder(Socket socket) {
        this.socket = socket;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            String msg = null;
            String currentTime = null;
            while (true) {
                msg = reader.readLine();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (msg == null) {
                    break;
                }
                System.out.println("The time server receive order:" + msg);
                currentTime = "QUERY TIME ORDER".equalsIgnoreCase(msg) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                writer.println(currentTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reader = null;
            }
            if (writer != null) {
                writer.close();
                writer = null;
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
}
