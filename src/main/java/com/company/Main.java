package com.company;

import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {

    private static final int threadNum=10;

    public static void main(String[] args) {
        String host;
        int port;
        CommandLineParser parser=new DefaultParser();
        Options options=new Options();
        options.addOption("host",true,"Host to connect to");
        options.addOption("port",true,"Port for client to connect to or for server to bind");
        options.addOption("sensor",false,"Use sensor gateway server instead of robot Client");
        try {
            /*for (int i=0;i<threadNum;i++){
                Thread thread=new Thread(new Client(i));
                thread.setDaemon(true);
                thread.start();
                Thread.sleep(2000);
            }*/
            CommandLine cmd=parser.parse(options,args);
            host="127.0.0.1";
            port=8888;
            boolean sensorMode=false;
            if (cmd.hasOption("host")){
                host=cmd.getOptionValue("host");
            }
            if (cmd.hasOption("port")){
                port=Integer.parseInt(cmd.getOptionValue("port"));
            }
            if (cmd.hasOption("sensor")){
                sensorMode=true;
            }
            if (!sensorMode){
                System.out.println("robotMode");
                Thread thread=new Thread(new Client(host,port));
                thread.start();
            }
            else {
                System.out.println("sensorMode");
                SensorGatewayServer sensorGatewayServer=new SensorGatewayServer(port);
                sensorGatewayServer.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
	// write your code here
    }
}
