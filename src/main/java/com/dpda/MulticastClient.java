package com.dpda;

import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;

public class MulticastClient extends Thread {
    private final String group;
    private final Integer port;

    private MulticastSocket socket;
    private Topology topology;

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

        if(topology == null){
            this.topology = new Topology();
        }
    }

    public void setTopology(Topology topology){
        this.topology = topology;
    }

    public void run() {
        try {
            this.initClient();

            DatagramPacket packet = new DatagramPacket(buffer, bufSize);

            while(isRunning){
                socket.receive(packet);

                InetAddress addr = packet.getAddress();
                topology.update(addr.getHostAddress(), packet.getPort());

                System.out.println("Received message from: " + addr.getHostAddress() + ":" + packet.getPort() + " at: " + LocalDateTime.now());
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
