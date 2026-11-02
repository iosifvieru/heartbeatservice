package com.dpda;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastSend extends Thread {
    private final String group;
    private final Integer port;

    private DatagramSocket socket;
    private DatagramPacket packet;

    public MulticastSend(String group, int port){
        this.group = group;
        this.port = port;
    }

    private void initSend() throws IOException {
        this.socket = new DatagramSocket();

        String message = "Healthy!";
        this.packet = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName(group), port);

        System.out.println("Server socket and packet initialized!");
    }

    public void run() {
        try {
            initSend();

            while(true) {
                socket.send(packet);
                sleep(2000);
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            if (this.socket != null){
                socket.close();
            }
        }
    }
}
