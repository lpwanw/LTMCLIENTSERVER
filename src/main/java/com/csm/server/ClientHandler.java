package com.csm.server;

import com.csm.Message;
import com.csm.SIModel;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable{
    public String name;
    final ObjectInputStream dis;
    final ObjectOutputStream dos;
    Socket s;
    boolean isloggedin;
    public SIModel info;
    // constructor
    public ClientHandler(Socket s, String name,
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
                received = (Message) this.dis.readObject();

                System.out.println("client get: " + received.command);

                if(received.command == Message.LOGOUT){
                    this.isloggedin=false;
                    this.s.close();
                    break;
                }
//                String MsgToSend = received.data;
//                String recipient = received.toId;
                // break the string into message and recipient part
                // search for the recipient in the connected devices list.
                // ar is the vector storing client of active users
                if(Server.admin.isloggedin){
                    Server.admin.dos.writeObject(received);
                }
            } catch (IOException | ClassNotFoundException e) {
                Thread.currentThread().interrupt();
                this.isloggedin = false;
                return;
            }
        }
        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return;
        }
    }
}
