package com.dpda;

public class Main {
    private final static String GROUP = "230.0.0.2";
    private final static int PORT = 4446;

    public static void main(String[] args) {
        Topology topology = new Topology();

        MulticastClient client = new MulticastClient(GROUP, PORT);
        MulticastSend server = new MulticastSend(GROUP, PORT);

        client.setTopology(topology);
        server.setTopology(topology);

        client.start();
        server.start();
    }
}
