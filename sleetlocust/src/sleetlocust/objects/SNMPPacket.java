package sleetlocust.objects;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.net.InetAddress;

public class SNMPPacket {
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
        this.proxyIP = proxyIP;
        this.proxyPort = proxyPort;
        
    }
    
    public void setData(byte[] data, int dataSize) {
        this.data = data;
        this.dataSize = dataSize;
    }
    
    
}
