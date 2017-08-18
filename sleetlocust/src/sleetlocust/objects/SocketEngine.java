/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sleetlocust.objects;

import java.net.DatagramSocket;
import sleetlocust.Sleetlocust;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.DatagramPacket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.KeyManager;

/**
 *
 * @author snr
 */
public class SocketEngine extends ThreadEngine {
    private final String _CLASS;
    public static final int UDP = 1;
    public static final int SSL = 2;
    private static final String[] _ENABLED_CIPHER_SUITES = new String[]{"TLS_ECDH_RSA_WITH_AES_128_CBC_SHA"};
    private int listeningPort;
    
    private InetAddress vsauceIP;
    private int vsaucePort;
    private String keystoreName;
    private KeyManager[] kms;
    
    Sleetlocust owner;
    
    int socketType;
    UDPSocketThread udplt;
    
    public SocketEngine(int socketType, int listeningPort, KeyManager[] kms) {
        super(1,1,10);
        
        this._CLASS = this.getClass().getName();
        this.socketType = socketType;
        this.listeningPort = listeningPort;
        this.kms = kms;
    }
    
    public SocketEngine(int socketType, int listeningPort, InetAddress vsauceIP, int vsaucePort) {
        this(socketType, listeningPort, null);
        
        this.vsauceIP = vsauceIP;
        this.vsaucePort = vsaucePort;
        this.keystoreName = keystoreName;
    }
    
    public void execute() {
        while(true) {
            System.out.println(_CLASS+"/run - listening...");
            if(socketType == SocketEngine.UDP) {
                //try ( ServerSocket serverSocket = new ServerSocket(listeningPort) ) {
                //    executorPool.execute(new UDPSocketThread(owner, serverSocket.accept(), vsauceIP, vsaucePort, _ENABLED_CIPHER_SUITES));
                try (DatagramSocket serverSocket = new DatagramSocket(listeningPort)) {
                    
                    DatagramPacket inboundPacket = new DatagramPacket(new byte[65000], 65000);
                    serverSocket.receive(inboundPacket);
                    System.out.println("what is the local socket address ? : "+serverSocket.getLocalSocketAddress());
                    
                    executorPool.execute(new UDPSocketThread(owner, inboundPacket, serverSocket, vsauceIP, vsaucePort, _ENABLED_CIPHER_SUITES));

                } catch(java.io.IOException ioe) { System.out.println(_CLASS+" "+ioe.toString());
                }
            } else if(socketType == SocketEngine.SSL) {
                SSLServerSocketFactory sslFact = genSSLFactory("TLS", kms);
                
                try ( SSLServerSocket serverSocket = (SSLServerSocket)sslFact.createServerSocket(listeningPort) ) {
                    serverSocket.setNeedClientAuth(false);
                    serverSocket.setUseClientMode(false);
                    serverSocket.setWantClientAuth(false);
                    
                    //serverSocket.setEnabledCipherSuites(_ENABLED_CIPHER_SUITES);
                    
                    executorPool.execute(new SSLServerSocketThread(owner, (SSLSocket)serverSocket.accept()));

                } catch(java.io.IOException ioe) { System.out.println(_CLASS+" "+ioe.toString());
                }
            }
        }
    }
    
    
    private SSLServerSocketFactory genSSLFactory(String provider, KeyManager[] kms) {
        SSLServerSocketFactory sslFact = null;
        
        try {
            SSLContext sslContext = SSLContext.getInstance(provider);
            sslContext.init(kms, null, null);
            sslFact = sslContext.getServerSocketFactory();
        } catch(java.security.NoSuchAlgorithmException | java.security.KeyManagementException nsae) { System.out.println(_CLASS+"/genSSLFactory - "+nsae); 
        } catch( java.lang.Exception e ) { System.out.println(_CLASS+"/genSSLFactory - "+e); 
        }
        
        if(sslFact==null) {
            sslFact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
            System.out.println(_CLASS+"/genSSLFactory - returning default factory");
        }
        
        return sslFact;
    }
     
    /*private KeyManagerFactory genKMFactory(String ksType, String ksName) throws java.lang.Exception {
        KeyStore ks = KeyStore.getInstance(ksType);
        
        ks.load(new java.io.FileInputStream(ksName), "SebJKS".toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, "keypassword".toCharArray());
        
        return kmf;
    }*/
}