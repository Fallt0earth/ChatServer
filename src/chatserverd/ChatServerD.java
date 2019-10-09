/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserverd;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dstok
 */
public class ChatServerD {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
     
        Tracker track = new Tracker();
        Handshake begin = null;
		
        try {
            begin = new Handshake(track);
			System.out.println("29 in main");
       // Thread execute = new Thread(begin);
        
        } catch (IOException ex) {
            System.out.println("io error main");
        }
		
		
       // Thread execute = new Thread(begin);
       // track.getManager().execute(begin);
        
       // execute.start();
        
    }
    
}
