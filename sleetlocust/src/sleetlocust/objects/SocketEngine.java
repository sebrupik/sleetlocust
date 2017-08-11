/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sleetlocust.objects;

import sleetlocust.Sleetlocust;

import java.net.InetAddress;
import java.net.ServerSocket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
//import java.net.Socket;
import javax.net.ssl.SSLSocket;
//import java.net.UnknownHostException;

/**
 *
 * @author snr
 */
public class SocketEngine extends ThreadEngine {
    private final String _CLASS;
    public static final int UDP = 1;
    public static final int SSL = 2;
    private int listeningPort;
    
    private InetAddress vsauceIP;
    private int vsaucePort;
    
    Sleetlocust owner;
    
    int socketType;
    UDPSocketThread udplt;
    
    public SocketEngine(int socketType, int listeningPort) {
        super(1,1,10);
        
        this._CLASS = this.getClass().getName();
        this.socketType = socketType;
        this.listeningPort = listeningPort;
    }
    
    public SocketEngine(int socketType, int listeningPort, InetAddress vsauceIP, int vsaucePort) {
        this(socketType, listeningPort);
        
        this.vsauceIP = vsauceIP;
        this.vsaucePort = vsaucePort;
    }
    
    public void execute() {
        while(true) {
            System.out.println(_CLASS+"/run - listening...");
            if(socketType == SocketEngine.UDP) {
                try ( ServerSocket serverSocket = new ServerSocket(listeningPort) ) {
                    executorPool.execute(new UDPSocketThread(owner, serverSocket.accept(), vsauceIP, vsaucePort));

                } catch(java.io.IOException ioe) { System.out.println(_CLASS+" "+ioe.toString());
                }
            } else if(socketType == SocketEngine.SSL) {
                SSLServerSocketFactory sslFact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
                
                try ( SSLServerSocket serverSocket = (SSLServerSocket)sslFact.createServerSocket(listeningPort) ) {
                    serverSocket.setNeedClientAuth(false);
                    serverSocket.setUseClientMode(true);
                    serverSocket.setWantClientAuth(false);
                    
                    serverSocket.setEnabledCipherSuites(new String[]{"TLS_ECDH_RSA_WITH_AES_128_CBC_SHA"});
                    
                    executorPool.execute(new SSLServerSocketThread(owner, (SSLSocket)serverSocket.accept()));

                } catch(java.io.IOException ioe) { System.out.println(_CLASS+" "+ioe.toString());
                }
            }
        }
    }
    
    /*public void execute_old() {
        try {
        if(socketType == SocketEngine.UDP)
            executorPool.execute(new UDPSocketThread(owner, InetAddress.getLocalHost(), 30000));
        else if(socketType == SocketEngine.SSL)
            executorPool.execute(new SSLSocketThread(owner, InetAddress.getLocalHost(), 444));
        } catch (UnknownHostException uhe) { System.out.println(_CLASS+"/execute - "+uhe); }
    }*/
}