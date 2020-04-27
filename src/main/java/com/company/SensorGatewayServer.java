package com.company;

import java.net.ServerSocket;
import java.net.Socket;

public class SensorGatewayServer {

    private int port;
    private ServerSocket serverSocket;

    public SensorGatewayServer(int port){
        this.port=port;
    }

    public void start(){
        try {
            serverSocket=new ServerSocket(port);
            while (true){
                Socket socket=serverSocket.accept();
                System.out.println("收到连接");
                Thread thread=new Thread(new SensorGatewaySender(socket));
                thread.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
