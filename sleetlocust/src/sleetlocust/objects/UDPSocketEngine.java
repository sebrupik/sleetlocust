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
public class UDPSocketEngine extends ThreadEngine {
    private final String _CLASS;
    
    Sleetlocust owner;
    
    UDPSocketThread udplt;
    
    public UDPSocketEngine(Sleetlocust owner) {
        super(1,1,10);
        
        this._CLASS = this.getClass().getName();
        this.owner = owner;
        
        /*try {
            this.udplt = new UDPListenerThread(owner, InetAddress.getLocalHost(), 30000);
        } catch(UnknownHostException uhe) { }*/
    }
    
    public void execute(UDPSocketThread udplt) {
        executorPool.execute(udplt);
    }
}