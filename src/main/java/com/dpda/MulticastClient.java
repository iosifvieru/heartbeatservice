package com.dpda;

import java.io.IOException;
import java.net.*;

public class MulticastClient extends Thread {
    private final String group;
    private final Integer port;

    private MulticastSocket socket;

    private byte[] buffer;
    private final int bufSize;

    private boolean isRunning = true;

    public MulticastClient(String group, int port){
        this.group = group;
        this.port = port;
        this.bufSize = 256;
    }

    private void initClient() throws IOException {
        this.socket = new MulticastSocket(port);
        System.out.println("Multicast client socket initialized.");

        socket.joinGroup(new InetSocketAddress(group, port), NetworkInterface.getByIndex(0));

        this.buffer = new byte[bufSize];
    }

    public void run() {
        try {
            this.initClient();

            DatagramPacket packet = new DatagramPacket(buffer, bufSize);

            while(isRunning){
                socket.receive(packet);
                String buf = new String(packet.getData(), packet.getOffset(), packet.getLength());

                InetAddress addr = packet.getAddress();
                System.out.println("buf: " + buf + "addr: " + addr.getHostAddress() + " port: " + packet.getPort());
            }

            System.out.println("Client closed.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
