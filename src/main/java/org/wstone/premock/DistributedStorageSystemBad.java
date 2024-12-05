package org.wstone.premock;

import java.net.SocketException;
import java.util.HashMap;
import java.util.PriorityQueue;

public class DistributedStorageSystemBad {
    // this is the approach involving the use of a priority queue and a server class, etc.
    PriorityQueue<Server> queue;
    // for part three, change servers to...
    // HashMap<Character, Integer> servers
    // and add parameter to Server class constructor for capacity
    // in the addFile method, do a capacity check and if none, return null.
    public DistributedStorageSystemBad(char[] servers) throws SocketException {
        // note how the instructions ask you for a specific task, nothing about
        // simulating sockets or anything beyond writing the one method
        queue = new PriorityQueue<>();
        for (int i = 0; i < servers.length; i++) {
            queue.add(new Server(servers[i]));
        }
    }
    char storeFile(String path, int size){
        Server server = queue.poll();
        server.addFile(path, size);
        queue.add(server);
        return server.order;
    }

    void deleteFile(String file) {
        Server temp = null;
        for(Server server : queue){
            // One mistake here would be to leave this as is
            // but due to the ordering of a pq being done when
            // items are added, we need to copy and add the server back
            // otherwise our ordering will be off.
            if (server.removeFile(file)){
                temp = server;
            }
        }
        if (temp != null){
            queue.remove(temp);
            queue.add(temp);
        }
    }


    class Server implements Comparable<Server>{
        char order;
        int usedStorage = 0;
        HashMap<String, Integer> files;
        //

        public Server(char order) throws SocketException {
            this.order = order;
            files = new HashMap<>();
            //
        }
        void addFile(String file, int size){
            files.put(file, size);
            usedStorage += size;
        }

        boolean removeFile(String file){
            if(files.containsKey(file)){
                usedStorage =- files.get(file);
                files.remove(file);
                return true;
            }
            return false;
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
        DistributedStorageSystemBad system = new DistributedStorageSystemBad(servers);
        System.out.println(system.storeFile("one.txt", 100));
        System.out.println(system.storeFile("two.txt", 50));
        System.out.println(system.storeFile("three.txt", 50));
        System.out.println(system.storeFile("four.txt", 50));
        system.deleteFile("two.txt");
        System.out.println(system.storeFile("five.txt", 60));
        system.deleteFile("three.txt");
        System.out.println(system.storeFile("six.txt", 70));

    }
}