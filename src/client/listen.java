/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dstok
 */
public class listen extends Thread{
    
    private BufferedReader in = null;

    /**
     *
     * @param in
     */
    public listen(BufferedReader in) {
        this.in = in;
      
    }

  
    
    /**
     *
     */
    @Override
    public void run(){
        receive();
        
    }
    
    
    
    private void receive(){
        
        
        while(true){
    
          
            try {
                String replyFromServer = in.readLine();
          
                System.out.println("Answer: " + replyFromServer);
                if(replyFromServer.trim().toLowerCase().equals("bye")){
                    break;
                }
            } catch (IOException ex) {
                Logger.getLogger(listen.class.getName()).log(Level.SEVERE, null, ex);
            }
                   }
    }
    
}
