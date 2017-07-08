package sleetlocust.objects;

import sleetlocust.Sleetlocust;


public class ShutdownThread extends Thread {
    Sleetlocust sl;
    public ShutdownThread(Sleetlocust sl) {
        this.sl = sl;
    }
    
    @Override public void run() {
        System.out.println("Shutdown hook ran!");
        if(sl!=null)
            sl.shutdownThreads();
    }
}