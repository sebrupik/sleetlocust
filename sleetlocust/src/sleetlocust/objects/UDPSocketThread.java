package sleetlocust.objects;

import sleetlocust.Sleetlocust;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author snr
 */
public class UDPSocketThread implements Runnable {
    private final String _CLASS;
    Sleetlocust owner;
    
    private InetAddress listenAddr;
    private DatagramSocket listenSocket;
    private DatagramPacket inboundPacket;
    private byte[] inboundData;
    private SNMPPacket snmpPacket;
    private int port;
    
    public UDPSocketThread(Sleetlocust owner, Socket listenSocket) {
        this._CLASS = this.getClass().getName();
        this.owner = owner;
        //this.listenAddr = listenAddr;
        //this.port = port;
        
        /*try {
            listenSocket = new DatagramSocket(port, listenAddr);
        } catch (SocketException | SecurityException localException) {
            System.out.println(_CLASS+" "+localException.toString());
        }*/
    }
    
    @Override
    public void run() {
        while(true) {
            inboundData = new byte[65000];
            inboundPacket = new DatagramPacket(inboundData, 65000);
            
            snmpPacket = new SNMPPacket(inboundPacket.getAddress(), inboundPacket.getPort(), listenSocket.getLocalAddress(), listenSocket.getLocalPort());
            snmpPacket.setData(inboundPacket.getData(), inboundPacket.getLength());
            
            owner.sendToCore(snmpPacket);
            
        }
        
    }
    
}
