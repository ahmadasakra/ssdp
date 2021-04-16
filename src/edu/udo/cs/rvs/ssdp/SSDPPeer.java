package edu.udo.cs.rvs.ssdp;

/**
 * This class is first instantiated on program launch and IF (and only if) it
 * implements Runnable, a {@link Thread} is created and started.
 */
public class SSDPPeer implements Runnable {

    Listen listen;
    Worker1 worker1;
    Us us;


    public SSDPPeer() {

        try {
            listen = new Listen();
            worker1 = new Worker1(listen);
            us = new Us(listen, worker1);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Die Methode run implemntiert die 3 Threads (Listen, Worker, User-Thread ) gleichzeitig.
     * @Listen Thread
     * @Worker1 Thread
     * @Us Thread
     */

    @Override
    public void run() {


        Runnable peerRunnable = (Runnable) listen;
        Thread peerThread = new Thread(peerRunnable);
        peerThread.setName("Listen Thread");
        peerThread.setDaemon(false);
        peerThread.start();


        Runnable peerRunnable1 = (Runnable) worker1;
        Thread peerThread1 = new Thread(peerRunnable1);
        peerThread1.setName("Worker-Thread");
        peerThread1.setDaemon(false);
        peerThread1.start();

        Runnable peerRunnable2 = (Runnable) us;
        Thread peerThread2 = new Thread(peerRunnable2);
        peerThread2.setName("User-Thread");
        peerThread2.setDaemon(false);
        peerThread2.start();


    }


}

