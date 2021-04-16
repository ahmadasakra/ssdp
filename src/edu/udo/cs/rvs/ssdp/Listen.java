package edu.udo.cs.rvs.ssdp;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.LinkedList;

/**
 * dieser Thread soll ein MulticastSocket auf Port 1900 öffnen, der
 * Multicast-Gruppe „239.255.255.250“ beitreten und bis zum Programmende endlos Datagramme
 * empfangen und dem Worker-Thread zur Verfügung stellen
 * Die Methode run() öffnet ein MulticastSocket und bindet an den Port 1900 unt tritt es ein .
 * Es empfaengt die Datagramme und fuegt sie in einer Liste hinzu.
 * @throws IOException Tritt ein wenn der Port oder die Addresse in Multicast nicht gefunden wurden.
 * @author Asakra, Majadly, Qeadean
 */


public class Listen implements Runnable {

    public MulticastSocket multicastSocket;
    final int port = 1900; // Standard Port für SSDP.
    final String hostIPv4 = "239.255.255.250"; // in IPv4 lautet die Multicast-Adresse diese IP.

    public LinkedList<DatagramPacket> list = new LinkedList<DatagramPacket>();
    public DatagramPacket datagramPacket; // (byte[] buffer, length.buffer)


    public Listen() throws IOException {

        this.multicastSocket = new MulticastSocket(1900);
        this.multicastSocket.joinGroup(InetAddress.getByName("239.255.255.250"));

    }


    @Override
    public void run() {


        try {

            this.multicastSocket = new MulticastSocket(port); // Neues MulticastSocket erstellen und an den Port (1900) binden
            InetAddress group = InetAddress.getByName(hostIPv4);
            multicastSocket.joinGroup(group); // die JoinGroup(InetAdress m) tritt einer Multicast-Gruppe bei.

        } catch (Exception e) {
            e.printStackTrace();
        }

        while (!this.multicastSocket.isClosed()) {  // public boolean isClosed, return true , wenn der Socket geschlossen wurde.

            try {

                int lengthBuffer = this.multicastSocket.getReceiveBufferSize(); // damit nimmt die ganze Size, die für Datagramme-Pakete benötigt.
                byte[] buffer = new byte[this.multicastSocket.getReceiveBufferSize()];
                datagramPacket = new DatagramPacket(buffer, lengthBuffer); // (byte[] buffer, length.buffer)

                multicastSocket.receive(datagramPacket); // empfaengt die Pakete.

                synchronized (list) { // Die Pakete kommen in einer Liste.
                    list.add(datagramPacket);
                }

            } catch (Exception e) {
                System.out.println("Die Receive hat fehlgeschlagen , " + e);
            }
        }

    }


}
