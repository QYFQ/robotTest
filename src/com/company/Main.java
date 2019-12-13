package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {

    private static final int threadNum=4;

    public static void main(String[] args) {
        try {
            for (int i=0;i<threadNum;i++){
                Thread thread=new Thread(new Client(i));
                thread.setDaemon(true);
                thread.start();
                Thread.sleep(2000);
            }
            System.in.read();
        }catch (Exception e){
            e.printStackTrace();
        }
	// write your code here
    }
}
