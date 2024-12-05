package org.wstone.mock;

import org.wstone.premock.DistributedStorageSystemBad;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.PriorityQueue;

public class DistributedStorageSystem {

    PriorityQueue<Server> servers = new PriorityQueue<>();

    public DistributedStorageSystem(char[] servers){
        for (int i = 0; i < servers.length; i++) {
            this.servers.add(new Server(servers[i]));
        }
    }

    public char storeFile(String file, int weight){
        Server s = servers.poll();
        int w = s.size += weight;
        s.files.put(file, w);
        servers.add(s);
        return s.id;
    }


    /**
     * Part two
     * @param file
     */
    public void deleteFile(String file){

    }


    /**
     * Part three
     *
     * @param args
     * @throws SocketException
     */


    class Server implements Comparable<Server> {
        char id;
        int size;
        HashMap<String, Integer> files;

        public Server(char id) {
            files = new HashMap<>();
            this.id = id;
            size = 0;
        }


        @Override
        public int compareTo(Server o) {
            if (this.size == 0 && o.size == 0){
                return Character.compare(this.id, o.id);
            } else {
                int compare = Integer.compare(this.size, o.size);
                return compare;
            }
        }

    }



    public static void main(String[] args) throws SocketException {
        char[] servers = {'a', 'b', 'c'};
        DistributedStorageSystem system = new DistributedStorageSystem(servers);
        System.out.println(system.storeFile("one.txt", 100));
        System.out.println(system.storeFile("two.txt", 50));
        System.out.println(system.storeFile("three.txt", 50));
        System.out.println(system.storeFile("four.txt", 50));
//        system.deleteFile("two.txt");
//        System.out.println(system.storeFile("five.txt", 60));
//        system.deleteFile("three.txt");
//        System.out.println(system.storeFile("six.txt", 70));

    }
}