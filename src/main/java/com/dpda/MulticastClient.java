package com.dpda;

import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;

public class MulticastClient extends Thread {
    private final String group;
    private final Integer port;

    private MulticastSocket socket;
    private final Topology topology;

    private byte[] buffer;
    private final int bufSize;

    private boolean isRunning = true;
    private int index;

    public MulticastClient(String group, int port){
        this.group = group;
        this.port = port;
        this.bufSize = 256;
        this.index = 0;

        this.topology = new Topology();
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
                this.index += 1;

                InetAddress addr = packet.getAddress();
                topology.update(addr.getHostAddress(), packet.getPort());

                System.out.println("Received message from: " + addr.getHostAddress() + ":" + packet.getPort() + " at: " + LocalDateTime.now());

                if(index % 3 == 0){
                    topology.cleanup();
                    topology.print();
                }
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
