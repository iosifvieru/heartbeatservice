package com.dpda;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Topology {
    private final Map<String, LocalDateTime> topology;
    private final List<String> markedForDeletion;
    private final static Integer MAX_TIMEOUT_DURATION = 20;

    public Topology() {
        topology = new HashMap<>();
        markedForDeletion = new ArrayList<>();
    }

    public void update(String ip, int port){
        String key = ip + ":" + port;
        topology.put(key, LocalDateTime.now());
    }

    public void cleanup() {
        for(Map.Entry<String, LocalDateTime> entry : topology.entrySet()) {
            LocalDateTime now = LocalDateTime.now();

            if (Duration.between(entry.getValue(), now).getSeconds() > MAX_TIMEOUT_DURATION) {
                markedForDeletion.add(entry.getKey());
            }
        }

        if(markedForDeletion.isEmpty()) {
            return;
        }

        for(String key : markedForDeletion) {
            System.out.println("Cleaned up - " + key + ", last timestamp: " + topology.get(key));
            topology.remove(key);
        }

        markedForDeletion.clear();
    }

    public void print() {
        System.out.println(topology);
    }
}
