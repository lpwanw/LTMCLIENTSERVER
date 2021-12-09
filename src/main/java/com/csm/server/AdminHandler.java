package com.csm.server;

import com.csm.Message;

import java.io.*;
import java.net.Socket;

public class AdminHandler implements Runnable{
    private String name;
    final ObjectInputStream dis;
    final ObjectOutputStream dos;
    Socket s;
    boolean isloggedin;

    // constructor
    public AdminHandler(Socket s, String name,
                        ObjectInputStream dis, ObjectOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
    }

    @Override
    public void run() {

        Message received;
        while (!Thread.currentThread().isInterrupted())
        {
            try
            {
                // receive the string
                received = (Message) dis.readObject();
                System.out.println(received);
                if(received.command == Message.LOGOUT){
                    this.isloggedin=false;
                    this.s.close();
                    break;
                }
                String MsgToSend = received.data;
                String recipient = received.toId;

                // search for the recipient in the connected devices list.
                // ar is the vector storing client of active users
                for (ClientHandler mc : Server.ar)
                {
                    // if the recipient is found, write on its
                    // output stream
                    if (mc.name.equals(recipient) && mc.isloggedin)
                    {
                        mc.dos.writeObject(received);
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            Thread.currentThread().interrupt();
            return;
        }
    }
}
