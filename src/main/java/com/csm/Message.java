package com.csm;


import java.io.Serializable;

public class Message implements Serializable {
    public static final int TAKE_SCREEN_SHOT = 1;
    public static final int LOGOUT = 999;
    public static final int GET_LIST_USER = 2;
    public static final int innit = 0;
    public String data;
    public String toId;
    public int command;
}
