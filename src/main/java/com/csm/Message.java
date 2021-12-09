package com.csm;


import java.io.Serializable;

public class Message implements Serializable {
    public static final int TAKE_SCREEN_SHOT = 1;
    public static final int LOGOUT = 999;
    public String data;
    public String toId;
    public int command;
}
