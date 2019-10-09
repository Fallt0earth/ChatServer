 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserverd;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author dstok
 */
public class Handshake implements Runnable {
    
    private Socket clientSocket;

    private ServerSocket listen;
    private Socket current;
    private ArrayList<Integer> ports;
    private Tracker track;

    public Handshake(Tracker track) throws IOException {
          this.track = track;
		  this.startUp();
		  
        
      
    }
    
    

    private void startUp() throws IOException {
        this.genPorts();
        int i = 0;
        for (i = 0; i < 100; i++) {
            
        
        //may want to make index a class field

        InetAddress addr = InetAddress.getByName("127.0.0.1");
        listen = new ServerSocket(ports.get(i), 128, addr);
    
        
        clientSocket = listen.accept();
        User x = new User(clientSocket, track);
      
         track.setUsers(x);
        x.init();
       // Thread exe = new Thread(track.getRooms().get(0));
        }
	
       // track.getManager().execute(track.getRooms().get(i));
	
		User x = new User(clientSocket, track);
             track.setUsers(x);
        
        
      
      
        //track.init();
    }
    
    

    //start port 10007 and go from there
    private void genPorts() {
        ports = new ArrayList();
        int portStart;
        if (ports.isEmpty()) {
            portStart = 10007;
        } else {
            portStart = ports.get(ports.size() - 1);
        }
        for (int i = 0; i < 100; i++) {
            ports.add(portStart + i);
        }

    }

    
    @Override
    public void run() {
		
        //this may need to be implememnted could 
        //determines if the current IP already has a user associated if so attack new socket connection to that existing user otherwise create new
      // ArrayList<User> users =  track.getUsers();
       boolean flag = false;
       
	   /*
        for (User i : users) {
            if(i.getIp() == clientSocket.getInetAddress()){
                i.addSocket(clientSocket);
                flag = true;
                break;
            }
        }
*/

       if(!flag){
            User x = new User(clientSocket, track);
             track.setUsers(x);
        }
        
    }

}

