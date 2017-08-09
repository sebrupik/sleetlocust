/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sleetlocust.objects;

import sleetlocust.Sleetlocust;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author snr
 */
public class SocketEngine extends ThreadEngine {
    private final String _CLASS;
    public static final int UDP = 1;
    public static final int SSL = 2;
    
    Sleetlocust owner;
    
    int socketType;
    UDPSocketThread udplt;
    
    public SocketEngine(Sleetlocust owner, int socketType) {
        super(1,1,10);
        
        this._CLASS = this.getClass().getName();
        this.owner = owner;
        this.socketType = socketType;
        
        /*try {
            this.udplt = new UDPListenerThread(owner, InetAddress.getLocalHost(), 30000);
        } catch(UnknownHostException uhe) { }*/
    }
    
    public void execute() {
        try {
        if(socketType == SocketEngine.UDP)
            executorPool.execute(new UDPSocketThread(owner, InetAddress.getLocalHost(), 30000));
        else if(socketType == SocketEngine.SSL)
            executorPool.execute(new SSLSocketThread(owner, InetAddress.getLocalHost(), 444));
        } catch (UnknownHostException uhe) { System.out.println(_CLASS+"/execute - "+uhe); }
    }
}