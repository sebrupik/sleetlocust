
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
        
        try {
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(incomingSocket.getOutputStream()));
            String s = "hello there!";
            
            w.write(s,0,s.length());
            w.newLine();
            w.flush();
            
        } catch(java.io.IOException ioe) { System.out.println(ioe); }
        
    }
}