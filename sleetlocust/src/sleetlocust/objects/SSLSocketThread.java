
package sleetlocust.objects;


import sleetlocust.Sleetlocust;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;


public class SSLSocketThread  implements Runnable {
    private final String _CLASS;
    Sleetlocust owner;
    InetAddress dstAddr;
    int dstPort;
    InetSocketAddress inSA;
    
    SocketFactory socketFactory;
    Socket SSLsock;
    
    public SSLSocketThread(Sleetlocust owner, InetAddress dstAddr, int dstPort) {
        this._CLASS = this.getClass().getName();
        this.owner = owner;
        this.inSA = new InetSocketAddress(dstAddr, dstPort);
        
        this.socketFactory =  SSLSocketFactory.getDefault();
        
        try {
            this.initSSLSocket();
        } catch(IOException ioe) {
            System.out.println(_CLASS+"/SSLSocketThread - "+ioe);
        }
    }
    
    private void initSSLSocket() throws IOException {
        if(SSLsock == null) {
            SSLsock = socketFactory.createSocket(dstAddr, dstPort);
            SSLsock.setKeepAlive(true);
        } else {
            SSLsock.connect(inSA);
        }
    }
    
    private boolean isSSLsockUP() {
        if(SSLsock != null) {
            return SSLsock.isConnected();
        } 
        
        return false;
    }
    
    @Override
    public void run() {
        while(true) {
            
        }
    }
}
