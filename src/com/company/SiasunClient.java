package com.company;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SiasunClient implements Runnable {

    private Socket socket;
    private String s;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private int len;
    private byte[] buffer=new byte[512];
    private byte[] bytes=new byte[25];

    @Override
    public void run() {
        try {
            socket=new Socket("127.0.0.1",8888);
            inputStream=new DataInputStream(socket.getInputStream());
            outputStream=new DataOutputStream(socket.getOutputStream());
            while ((len=inputStream.read(buffer))!=-1){
                System.arraycopy(buffer,0,bytes,0,len);
                ByteUtils.printHexString("",bytes);
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
