package org.wstone.premock;

import java.io.File;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DistributedStorageSystem {


    // this is the approach involving the use of a priority queue and a server class, etc.
    PriorityQueue<Server> queue;
    public DistributedStorageSystem(char[] servers) throws SocketException {
        // note how the instructions ask you for a specific task, nothing about
        // simulating sockets or anything beyond writing the one method
        queue = new PriorityQueue<>();
        for (int i = 0; i < servers.length; i++) {
            queue.add(new Server(servers[i], ThreadLocalRandom.current().nextInt(0,10000)));
        }
    }
    char storeFile(String path, int size){
        Server server = queue.poll();
        server.addFile(path, size);
        queue.add(server);
        return server.order;
    }
    class Server implements Comparable<Server>{
        char order;
        int usedStorage = 0;
        int port;
        ArrayList<File> files;
        DatagramSocket socket = null;

        public Server(char order, int port) throws SocketException {
            this.order = order;
            this.port = port;
            socket = new DatagramSocket(port);
            files = new ArrayList<>();
        }
        void addFile(String path, int size){
            File file = new File(path);
            files.add(file);
            usedStorage += size;
        }

        @Override
        public int compareTo(Server other) {
            int compare = Integer.compare(this.usedStorage, other.usedStorage);
            if (compare == 0) {
                compare = Integer.compare(this.order, other.order);
            }
            return compare;
        }
    }

    public static void main(String[] args) throws SocketException {
        char[] servers = {'a', 'b', 'c'};
        DistributedStorageSystem system = new DistributedStorageSystem(servers);
        System.out.println(system.storeFile("one.txt", 100));
        System.out.println(system.storeFile("two.txt", 50));
        System.out.println(system.storeFile("three.txt", 50));
        System.out.println(system.storeFile("four.txt", 50));
    }
}