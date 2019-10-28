
/** 
 * @author: Ashwin Kamalakannan
 * last Edited: October 28, 2019
 * 
 * File server class used to run the file server, 
 * register a folder on rmi registry and enable security manager.
 */

import java.io.*;
import java.rmi.*;

public class FileServer {
    /**
     * Main method that will be run when file server is started.
     * 
     * @param argv
     */
    public static void main(String argv[]) {
        System.out.println("Starting music server.");
        // Use security manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        System.out.println("Using security manager.");
        // open a new file interface, bind to a new folder in the rmi registry
        try {
            System.out.println("Starting a new file interface");
            FileInterface fi = new FileImpl("MusicServer");
            Naming.rebind("//127.0.0.1/MusicServer", fi);
        } catch (Exception e) {
            System.out.println("MusicServer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}