package com.csm.server;

import com.csm.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AdminHandler implements Runnable{
    private String name;
    final ObjectInputStream dis;
    final ObjectOutputStream dos;
    Socket s;
    Socket sAdmin;
    Socket sClient;
    ServerSocket ssCPU;
    boolean isloggedin;
    Thread CPULOAD;
    int cpuPort = 40001;
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
                if(received.command == Message.OPEN_SOCKET_CPU){
                    cpuPort = new Random().nextInt()%10000 + 40000;
                    ssCPU = new ServerSocket(cpuPort);
                    received.data = cpuPort + "";
                    dos.writeObject(received);
                    Socket s;
                    sAdmin = ssCPU.accept();
                    ObjectOutputStream dosAdmin = new ObjectOutputStream(sAdmin.getOutputStream());
                    ObjectInputStream disAdmin = new ObjectInputStream(sAdmin.getInputStream());
                    Message sendConnectInfo = new Message();
                    sendConnectInfo.command = Message.OPEN_SOCKET_CPU;
                    sendConnectInfo.data = cpuPort + "";
                    for (ClientHandler mc : Server.ar)
                    {
                        if (mc.name.equals(received.toId) && mc.isloggedin)
                        {
                            mc.dos.writeObject(received);
                            break;
                        }
                    }
                    sClient = ssCPU.accept();
                    ObjectOutputStream dosClient = new ObjectOutputStream(sClient.getOutputStream());
                    ObjectInputStream disClient = new ObjectInputStream(sClient.getInputStream());
                    CPULOAD = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(!Thread.currentThread().isInterrupted()){
                                try {
                                    Message msg = (Message) disClient.readObject();
                                    dosAdmin.writeObject(msg);
                                } catch (IOException | ClassNotFoundException e) {
                                    Thread.currentThread().interrupt();
                                    try {
                                        ssCPU.close();
                                        sAdmin.close();
                                        sClient.close();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    CPULOAD.start();
                    continue;
                }
                String recipient = received.toId;
                // search for the recipient in the connected devices list.
                // ar is the vector storing client of active users
                boolean found = false;
                for (ClientHandler mc : Server.ar)
                {
                    // if the recipient is found, write on its
                    // output stream
                    if (mc.name.equals(recipient) && mc.isloggedin)
                    {
                        mc.dos.writeObject(received);
                        found = true;
                        break;
                    }
                }
                if(recipient.contains("client")&&!found){
                    switch (received.command){
                        case Message.TAKE_SCREEN_SHOT, Message.GET_CLIPBOARD, Message.GET_OS_INFO, Message.GET_LIST_PROCESS -> {
                            received.data = "error";
                            received.toId="admin";
                            dos.writeObject(received);
                        }
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
