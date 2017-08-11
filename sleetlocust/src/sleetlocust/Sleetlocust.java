package sleetlocust;

import sleetlocust.objects.ShutdownThread;
import sleetlocust.objects.SNMPPacket;
import sleetlocust.objects.SocketEngine;

import java.net.InetAddress;
//import java.net.ServerSocket;
//import java.net.Socket;

/**
 * SNMP agent which receives (intercepts), encapsulates and forwards SNMP GET messages
 * and forwards them to our SNMP cache server.
 * The response will be sent back to the agent and source address spoofed.
 */
public class Sleetlocust {
    private final String _CLASS;
    private final SocketEngine _udple;
    
    private final int SNMP_LISTEN_PORT;
    private final InetAddress VALUESAUCE_IP;
    private final int VALUESAUCE_PORT;
    private InetAddress cacheServer;
    boolean listening;
    
    
    public Sleetlocust(int SNMP_LISTEN_PORT, InetAddress VALUESAUCE_IP, int VALUESAUCE_PORT) {
        this._CLASS = this.getClass().getName();
        this.SNMP_LISTEN_PORT = SNMP_LISTEN_PORT;
        this.VALUESAUCE_IP = VALUESAUCE_IP;
        this.VALUESAUCE_PORT = VALUESAUCE_PORT;
        
        
        Runtime.getRuntime().addShutdownHook(new ShutdownThread(this));
        
        this._udple = new SocketEngine(SocketEngine.UDP, SNMP_LISTEN_PORT, VALUESAUCE_IP, VALUESAUCE_PORT);
        this._udple.execute();
    }
    
    public void shutdownThreads() {
        System.out.println(_CLASS+"/shutdownThreads - starting");
        if(_udple != null)
            _udple.shutdown();
    }
    
    public static void main(String[] args) {
        try {
            Sleetlocust sl = new Sleetlocust(30000, InetAddress.getByName("127.0.0.1"), 44005);
        } catch(java.net.UnknownHostException uhe) { System.out.println(uhe); }
    }
}