package com.company;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class SiasunClient{

    private Socket socket;
    private String s;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private int len;
    private byte[] buffer=new byte[512];
    private byte[] bytes=new byte[25];
    private String host;
    private int port;
    private int count=0;

    public SiasunClient(String host,int port){
        this.host=host;
        this.port=port;
    }

    public void start() {
        try {
            socket=new Socket(host,port);
            inputStream=new DataInputStream(socket.getInputStream());
            outputStream=new DataOutputStream(socket.getOutputStream());
            System.out.println("连接主机 "+socket.getInetAddress().toString().substring(1)+":"+socket.getPort()+" 成功");
            System.out.println("本机地址："+socket.getLocalAddress().toString().substring(1)+":"+socket.getLocalPort());
            while ((len=inputStream.read(buffer))!=-1){
                System.arraycopy(buffer,0,bytes,0,len);
                count++;
                if (count%60==0){
                    System.out.println(port+"已收到"+count+"条信息");
                }
                int code=ByteUtils.bytes2Int(bytes,5);
                byte[] response;
                switch (code){
                    case 0x0205:
                        response=new byte[44];
                        for (int i=0;i<6;i++){
                            System.arraycopy(ByteUtils.float2Bytes((float)(i*0.5)),0,response,20+i*4,4);
                        }
                        outputStream.write(response);
                        outputStream.flush();
                        break;
                    case 0x0302:
                        response=new byte[71];
                        System.arraycopy(ByteUtils.short2Bytes((short)0x1234),0,response,20,2);
                        outputStream.write(response);
                        outputStream.flush();
                        break;
                    case 0x0307:
                        response=new byte[21];
                        byte[] state=new byte[1];
                        state[0]=(byte)0x01;
                        System.arraycopy(state,0,response,20,1);
                        outputStream.write(response);
                        outputStream.flush();
                        break;
                    case 0x0202:
                        response=new byte[56];
                        for (int i=0;i<9;i++){
                            System.arraycopy(ByteUtils.float2Bytes((float)(i*0.1)),0,response,20+i*4,4);
                        }
                        outputStream.write(response);
                        outputStream.flush();
                        break;
                    case 0x1201:
                        response=new byte[56];
                        for (int i=0;i<9;i++){
                            System.arraycopy(ByteUtils.float2Bytes((float)(i*0.05)),0,response,20+i*4,4);
                        }
                        outputStream.write(response);
                        outputStream.flush();
                        break;
                    case 0x1200:
                        response=new byte[56];
                        for (int i=0;i<9;i++){
                            System.arraycopy(ByteUtils.float2Bytes((float)(i*1.5)),0,response,20+i*4,4);
                        }
                        outputStream.write(response);
                        outputStream.flush();
                        break;
                    default:
                        response=new byte[20];
                        outputStream.write(response);
                        outputStream.flush();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
