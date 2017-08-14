package sleetlocust.objects;

import sleetlocust.Sleetlocust;

import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author snr
 */
public class UDPSocketThread implements Runnable {
    private final String _CLASS;
    Sleetlocust owner;
    private String[] enabled_cipher_suites;
    
    private InetAddress listenAddr;
    private DatagramPacket inboundPacket;
    private byte[] inboundData;
    private SNMPPacket snmpPacket;
    //private int port;
    
    private InetAddress vsauceIP;
    private int vsaucePort;
    
    Socket incomingSocket;
    
    public UDPSocketThread(Sleetlocust owner, Socket incomingSocket, InetAddress vsauceIP, int vsaucePort, String[] enabled_cipher_suites) {
        this._CLASS = this.getClass().getName();
        this.owner = owner;
        this.incomingSocket = incomingSocket;
        this.vsauceIP = vsauceIP;
        this.vsaucePort = vsaucePort;
        this.enabled_cipher_suites = enabled_cipher_suites;
    }
    
    @Override
    public void run() {
        System.out.println(_CLASS+"/run - got a UDP connection. Processing...");

        inboundData = new byte[65000];
        inboundPacket = new DatagramPacket(inboundData, 65000);

        snmpPacket = new SNMPPacket(inboundPacket.getAddress(), inboundPacket.getPort(), incomingSocket.getLocalAddress(), incomingSocket.getLocalPort());
        snmpPacket.setData(inboundPacket.getData(), inboundPacket.getLength());

        
        
        SSLSocketFactory sslFact = (SSLSocketFactory)SSLSocketFactory.getDefault();
        try {
            SSLSocket sslsock = (SSLSocket)sslFact.createSocket(vsauceIP, vsaucePort);
            sslsock.setUseClientMode(true);
            //sslsock.setEnabledCipherSuites(enabled_cipher_suites);
            //sslsock.setSoTimeout(20);
            System.out.println("about to handshake");
            sslsock.startHandshake();
            System.out.println("handshake complete");
            
            System.out.print(_CLASS+"/run - "+sslsock.toString());
            
        } catch(java.io.IOException ioe) { System.out.println(_CLASS+"/run - "+ioe);  ioe.printStackTrace(); }
        
      
    }
}
