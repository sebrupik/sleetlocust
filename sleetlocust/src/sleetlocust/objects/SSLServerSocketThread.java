
package sleetlocust.objects;

import sleetlocust.Sleetlocust;

import java.io.BufferedWriter;
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
    
    private final SSLSocket incomingSocket;
    
    public SSLServerSocketThread(Sleetlocust owner, SSLSocket incomingSocket) {
        this._CLASS = this.getClass().getName();
        this.owner = owner;
        
        this.incomingSocket = incomingSocket;
    }
    
    @Override
    public void run() {
        System.out.println(_CLASS+"/run - got a SSL connection. Processing...");
        
        System.out.println("Look at this incoming SSL socket : "+incomingSocket.toString());
        
        String[] a = incomingSocket.getEnabledCipherSuites();
        for (String a1 : a) {
            System.out.println(a1);
        }
        
        try {
            System.out.println("1");
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(incomingSocket.getOutputStream()));
            System.out.println("2");
            String s = "hello there!";
            
            
            w.write(s,0,s.length());
            System.out.println("3");
            w.newLine();
            System.out.println("4");
            w.flush();
            System.out.println("finished writing...");
            
        } catch(java.io.IOException ioe) { System.out.println(ioe); }
        
    }
}