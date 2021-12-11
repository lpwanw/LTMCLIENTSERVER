package com.csm.server;

import com.csm.Message;
import com.csm.SIModel;
import com.csm.client.Client;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                System.out.println("adminget: "+ received.command);
                if(received.command == Message.LOGOUT){
                    this.isloggedin=false;
                    this.s.close();
                    break;
                }
                if(received.command == Message.GET_LIST_USER && Objects.equals(received.toId, "all")){
                    List<String> list = new ArrayList<>();
                    for (ClientHandler mc : Server.ar)
                    {
                        if(mc.isloggedin)
                        list.add(mc.name);
                    }
                    received.data = listSIModeltoJson(list);
                    received.toId="admin";
                    System.out.println(received.data);
                    dos.writeObject(received);
                    continue;
                }
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
    public static String listSIModeltoJson(List<String> list){
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().toJson(list,listType);
    }
}
