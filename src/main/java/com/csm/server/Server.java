package com.csm.server;

import com.csm.Message;
import com.csm.SIModel;
import com.csm.model.OsHWModel;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Server {
    // Vector to store active clients
    static Vector<ClientHandler> ar = new Vector<>();
    static AdminHandler admin;
    // counter for clients
    static int i = 0;

    public static void main(String[] args) throws IOException
    {
        // server is listening on port 1234
        ServerSocket ss = new ServerSocket(1234);

        Socket s;

        // running infinite loop for getting
        // client request
        while (true)
        {
            // Accept the incoming request
            s = ss.accept();

            System.out.println("New client request received : " + s);

            // obtain input and output streams
            //Opject OutPut Stream
            ObjectOutputStream dos = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream dis = new ObjectInputStream(s.getInputStream());
//            DataInputStream dis = new DataInputStream(s.getInputStream());
            System.out.println("Creating a new handler for this client...");

            // Create a new handler object for handling this request.
            ClientHandler mtch = null;
            if(s.getPort()==3123) {
                admin = new AdminHandler(s, "admin", dis, dos);
                Thread t = new Thread(admin);
                System.out.println("Admin loggin");
                t.start();
            }else{
                mtch = new ClientHandler(s, "client " + i, dis, dos);
                Thread t = new Thread(mtch);
                System.out.println("Adding this client to active client list");
                // add this client to active clients list
                ar.add(mtch);
                t.start();
                i++;
            }
        }
    }
}
