/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserverd;

import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author dstok
 */
public class FireWall implements Runnable{
    
    private List<User> users = Collections.synchronizedList(new ArrayList<>());
    private ServerSocket server;
    
    public FireWall(ServerSocket socket){
        server = socket;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void monitor(){
        while(true){
            for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
                User next = iterator.next();
              Socket socket = next.getConnection();
              
           //  server.getLocalSocketAddress()
                
            }
        }
    }
    
    
    
}
