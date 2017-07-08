/*
 * SNMP agent which receives (intercepts), encapsulates and forwards SNMP GET messages
 * and forwards them to our SNMP cache server.
 * The response will be sent back to the agent and source address spoofed.
 */
package sleetlocust;

import sleetlocust.objects.ShutdownThread;
import sleetlocust.objects.SNMPPacket;
import sleetlocust.objects.UDPSocketEngine;
import sleetlocust.objects.UDPSocketThread;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Sleetlocust {
    
    private final String _CLASS;
    private UDPSocketEngine _udple;
    
    private int listenPortProxy = 30000;
    private InetAddress cacheServer;
    boolean listening;
    
    
    public Sleetlocust() {
        this._CLASS = this.getClass().getName();
        
        Runtime.getRuntime().addShutdownHook(new ShutdownThread(this));
        
        this._udple = new UDPSocketEngine(this);
        listening = true;
        
        try {
            cacheServer = InetAddress.getByName("192.168.0.1");
        } catch(java.net.UnknownHostException uhe) { }
        
        Socket incomingSock;
        try (ServerSocket serverSocketProxy = new ServerSocket(listenPortProxy) ) {   
            while(listening) {
                System.out.println("listening loop...");
                incomingSock = serverSocketProxy.accept();
                
                if(incomingSock.getInetAddress().equals(cacheServer)) {
                    //create a SSLsocketthread
                } else {
                    _udple.execute(new UDPSocketThread(this, incomingSock));
                }
            }   
        } catch (java.io.IOException ioe) { System.out.println(_CLASS+"/init - "+ioe); }
    }
    
    
    public boolean sendToCore(SNMPPacket outbound) {
        
        
        return true;
    }
    
    public boolean recieveFromCore(SNMPPacket inbound) {
        
        
        return true;
    }
    
    public void shutdownThreads() {
        System.out.println(_CLASS+"/shutdownThreads - starting");
        _udple.shutdown();
    }
    
    public static void main(String[] args) {
        Sleetlocust sl = new Sleetlocust();
    }
}
