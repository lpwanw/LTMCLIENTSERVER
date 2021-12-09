package com.csm.client;

import com.csm.Message;

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

    public static void main(String args[]) throws UnknownHostException, IOException
    {
        Scanner scn = new Scanner(System.in);

        // getting localhost ip
        InetAddress ip = InetAddress.getByName("localhost");

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
                        object.data = "data";
                        object.toId = "admin";
                        switch (msg.command){
                            case Message.TAKE_SCREEN_SHOT -> {
                                object.data = takeScreenShot();
                            }
                            default -> {

                            }
                        }
                        dos.writeObject(object);
                    } catch (IOException e) {
                        Thread.currentThread().interrupt();
                        return;
                    } catch (ClassNotFoundException e){
                        e.printStackTrace();
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
}
