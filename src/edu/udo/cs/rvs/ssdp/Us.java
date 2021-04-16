package edu.udo.cs.rvs.ssdp;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;

/**
 * Dieser Thread soll die Nutzereingaben lesen, verarbeiten und entsprechende Aktionen durchführen.
 * Er führt es die Befehle Scan, der über die Pakete sucht. List-Befehl,
 * der die UUID und Service-Typ ausgibt . Clear-Befehl entfernt die alle bekannten Geräte .
 * Exit-Befehl beendet sich das Programm.
 * @throws IOException . Der Thread soll dieses Exception werfen , insbesonders im Scan-Befehl wenn die Addresse Fehler hat
 * @author Asakrah,Majadly,Queadan
 */
public class Us implements Runnable {
    // um die anderen Klassen zu kennen:

    Listen peer1 = null;
    Worker1 peer2 = null;
    SpeID peer3 = new SpeID();

    public Us(Listen peer1, Worker1 peer2) {

        this.peer1 = peer1;
        this.peer2 = peer2;
    }

    @Override
    public void run() {
// der Nutzer kann mit Scanner irgendwas eingeben, damit er zu den Befehlen kommt.

        Scanner input = new Scanner(System.in);
        String inp;

        while (true) {

            System.out.println("\nWelchen Befhel wollen Sie ausführen:  \r\n\r\n SCAN, LIST, CLEAR, EXIT >>");
             inp = input.nextLine();


             if(inp.equals("EXIT")){
                 System.out.println("Das Programm wird beendet");

                 System.out.println(" * * * * * * *  * * * * * * *  * * * * * * *");

                 System.exit(0);
             }

             else if(inp.equals("SCAN")){
                 try {
                     scan();
                     System.out.println(" * * * * * * *  * * * * * * *  * * * * * * *");

                 }catch (Exception e){
                     e.printStackTrace();
                 }
             }

             else if(inp.equals("LIST")){
                 list_Befehl();

                 System.out.println(" * * * * * * *  * * * * * * *  * * * * * * *");
             }

             else if(inp.equals("CLEAR")){
                 clear();
                 System.out.println("Alle Geraete werden entfernt");
                 System.out.println(" * * * * * * *  * * * * * * *  * * * * * * *");
             }

             else {
                 System.out.println(" * * * * * * *  * * * * * * *  * * * * * * *");
                 System.out.println("Sie haben keinen Befehl gegeben, was wollen Sie genau ausfuehren? ");
                 System.out.println(" * * * * * * *  * * * * * * *  * * * * * * *");

             }



        }


    }


    public void clear() {

        synchronized (peer2.neuList) {

            peer2.neuList.clear();
            System.out.println("Die Liste enthaelt keine Geraete mehr ! ");
        }

    }
// Die bekannten Geraete , die wir in der zweiten Liste übergeben haben :
    public void list_Befehl() {

        try {
            if (!peer2.neuList.isEmpty()){
                System.out.println("Die Geraete Sind: ");

            synchronized (peer2.neuList) {

                for (int i = 0; i < peer2.neuList.size(); i++) {

                    System.out.println("UUID: " + peer2.neuList.get(i).uuid + " , Typ: " + peer2.neuList.get(i).typ1);

                }
            }
        }else{
                System.out.println("Es wurde keine Geraete gefunden !");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void scan() throws IOException {

        String Suchanfrage
                = "M-SEARCH * HTTP/1.1\r\n"
                + "S: uuid:" + UUID.randomUUID() + "\r\n"
                + "HOST: 239.255.255.250:1900\r\n"
                + "MAN: \"ssdp:discover\"\r\n"
                + "ST: ssdp:all\r\n"
                + "\r\n"; // leere Zeile schließt die Such-Anfrage ab.

        byte[] data = Suchanfrage.getBytes(StandardCharsets.UTF_8);
        DatagramPacket datagramPacket3 = new DatagramPacket(data, data.length, InetAddress.getByName("239.255.255.250"), 1900);

        try {
            peer1.multicastSocket.send(datagramPacket3);
            System.out.println("SSDP empfängt ... \n");

        } catch (Exception e) {
            System.out.println("Ein Fehler beim Empfang !! ");
            e.printStackTrace();
        }

    }


}
