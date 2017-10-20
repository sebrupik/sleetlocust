
package sleetlocust.objects;

import sleetlocust.Sleetlocust;

import java.io.BufferedWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import javax.net.SocketFactory;
//import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * Takes SSL connections from sleetlocust agents and receives the SNMP queries 
 * The returned data is either taken from the local database  or the target
 * device queried directly and the database refreshed before returning the data
 * back to the sleetlocust agent.
 * 
 * @author srupik
 */
public class SSLServerSocketThread  implements Runnable {
    private final String _CLASS;
    Sleetlocust owner;
    
    private final SSLSocket sslsock;
    
    public SSLServerSocketThread(Sleetlocust owner, SSLSocket sslsock) {
        this._CLASS = this.getClass().getName();
        this.owner = owner;
        
        this.sslsock = sslsock;
    }
    
    @Override
    public void run() {
        System.out.println(_CLASS+"/run - got a SSL connection. Processing...");
        System.out.println("Look at this incoming SSL socket : "+sslsock.toString());
        
        /*String[] a = sslsock.getEnabledCipherSuites();
        for (String a1 : a) {
            System.out.println(a1);
        }*/
        
        try {
            ObjectOutputStream outgoingStream = new ObjectOutputStream(sslsock.getOutputStream());
            ObjectInputStream incomingStream = new ObjectInputStream(sslsock.getInputStream());
            
            SNMPPacket sp = (SNMPPacket)incomingStream.readObject();
            
            System.out.println(sp.toString());
            sp.printMe();
            
        } catch (java.lang.ClassNotFoundException cnfe) { System.out.println(cnfe);
        } catch(java.io.IOException ioe) { System.out.println(ioe); }
        
    }
}