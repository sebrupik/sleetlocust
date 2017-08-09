package sleetlocust.objects;

import sleetlocust.Sleetlocust;

import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author snr
 */
public class UDPSocketThread implements Runnable {
    private final String _CLASS;
    Sleetlocust owner;
    
    private InetAddress listenAddr;
    private DatagramPacket inboundPacket;
    private byte[] inboundData;
    private SNMPPacket snmpPacket;
    private int port;
    
    public UDPSocketThread(Sleetlocust owner, InetAddress listenAddr, int port) {
        this._CLASS = this.getClass().getName();
        this.owner = owner;
    }
    
    @Override
    public void run() {
        Socket incomingSocket;
        
        while(true) {
            System.out.println(_CLASS+"/run - listening...");
            try ( ServerSocket serverSocket = new ServerSocket(port) ) {
                incomingSocket = serverSocket.accept();
                System.out.println(_CLASS+"/run - got a connection. Processing...");
                
                inboundData = new byte[65000];
                inboundPacket = new DatagramPacket(inboundData, 65000);

                snmpPacket = new SNMPPacket(inboundPacket.getAddress(), inboundPacket.getPort(), incomingSocket.getLocalAddress(), incomingSocket.getLocalPort());
                snmpPacket.setData(inboundPacket.getData(), inboundPacket.getLength());

                owner.sendToCore(snmpPacket);
                
            } catch(java.io.IOException ioe) { System.out.println(_CLASS+" "+ioe.toString());
            }
        }
        
    }
}
