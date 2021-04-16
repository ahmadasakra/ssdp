package edu.udo.cs.rvs.ssdp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.UUID;

/**
 * dieser Thread soll die empfangenen Datagramme aus dem Listen-Thread
 * verarbeiten und dem User-Thread mitteilen, welche Geräte gerade Dienste im Netzwerk anbieten.
 * Er soll zuerst das einzelne Pakett mit den Schemataen vergleichen, und dann nimmt sowohl die UUID als auch den Service-Type
 * Dies funktioniert, indem die Methode split teilt
 * Und dann speichert es in einer neue Liste, die muss in Klasse SpeID speichern .
 * @throws IOException Tritt ein wenn ein Fehler in Objekt von Klasse Listen vorkommt.
 * @author Asakrah,Majadly,Qeadan
 */

public class Worker1 implements Runnable {
    Listen peerS; // Objekt von SSDPPEER(LIST-THREAD);
    Us user = new Us(peerS, this);

    LinkedList<SpeID> neuList = new LinkedList<SpeID>();

    UUID uuid;
    String typ;

    public Worker1(Listen listen) throws IOException {
        peerS = listen;
    }


    @Override
    public void run() {

        while (true) {

            try {

                DatagramPacket d1; // der DatagrammPacket, der überpruft werden muss.

                if (!peerS.list.isEmpty()) {

                    synchronized (peerS.list) {

                        d1 = peerS.list.pop();
                    }

                    String str = new String(d1.getData(), StandardCharsets.UTF_8);
                    String[] parts = str.split("\r\n");
                    String[] arr = parts;
                    SpeID neuU = new SpeID();


                    try {

                        if (arr[0].equals("HTTP/1.1 200 OK")) { // Wenn die Zeile mit "USN: " angefangen hat, ist es (Unicast)


                            for (int i = 0; i < arr.length; i++) {

                                if (arr[i].startsWith("ST: ")) {

                                    typ = arr[i].substring(4); // Der Typ fängt vom Index(4) an.
                                } else if (arr[i].startsWith("USN: ")) {
                                    try {

                                        uuid = uuid.fromString(arr[i].split("uuid:", 2)[1].split(":", 2)[0]); // Hier muss [0] sein ! !!! !

                                    } catch (Exception e) {

                                        System.out.println("Es gibt kein UUID");
                                        e.printStackTrace();
                                    }

                                }
                            }

                            neuU.uuid = uuid;
                            neuU.typ1 = typ;
                            neuList.add(neuU);

                        } else if (arr[0].startsWith("NOTIFY * HTTP/1.1")) {// Wenn die Zeile mit "USN: " angefangen hat, ist es (Multicast)

                            for (int i = 0; i < arr.length; i++) {

                                if (arr[i].startsWith("NT: ")) {
                                    typ = arr[i].substring(4);

                                } else if (arr[i].startsWith("USN: ")) {

                                    uuid = uuid.fromString(arr[i].split("uuid:", 2)[1].split(":", 2)[0]);

                                }
                            }
                            neuU.uuid = uuid;
                            neuU.typ1 = typ;
                            neuList.add(neuU); // nimmt den uuid und typ1 von Klasse SpeID und speichert es in der neuen Liste neuList.


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Thread.sleep(10);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

    }

}


