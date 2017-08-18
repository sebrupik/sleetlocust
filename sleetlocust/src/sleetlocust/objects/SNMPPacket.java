package sleetlocust.objects;

import java.io.ByteArrayInputStream;
//import java.io.PrintStream;
import java.net.InetAddress;

public class SNMPPacket implements java.io.Serializable {
    public InetAddress sourceIP, proxyIP;
    public int sourcePort, proxyPort, dataSize, version, PDUType;
    public int[] RequestID;
    public boolean isSNMP;
    public String versionString, communityString;
    //public Vector ObjIDs;
    public byte[] data;
    private ByteArrayInputStream recDatastream;
    
    
    public SNMPPacket(InetAddress sourceIP, int sourcePort, InetAddress proxyIP, int proxyPort) {
        this.sourceIP = sourceIP;
        this.sourcePort = sourcePort;
        
        try {
        this.proxyIP = InetAddress.getByName("127.0.0.1");
        this.proxyPort = proxyPort;
        } catch (java.net.UnknownHostException uhe) { System.out.println(uhe); }
        
    }
    
    public void setData(byte[] data, int dataSize) {
        this.data = data;
        this.dataSize = dataSize;
    }
    
    public boolean isValidPacket() {
        return (sourceIP != null & sourcePort != 0) & (proxyIP != null & proxyPort != 0);
    }
    
    public void printMe() {
        System.out.println(sourceIP != null);
        System.out.println(sourcePort != 0);
        System.out.println(proxyIP != null);
        System.out.println(proxyPort != 0);
        
        for(int i =0; i<data.length; i++)
            System.out.print(data[i]+" ");
    }
    
    @Override public String toString() {
        String s = sourceIP.toString()+":"+sourcePort+" Proxy : "+proxyIP.toString()+":"+proxyPort;
        
        return s;
    }
}