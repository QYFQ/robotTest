package com.company;

import java.io.DataOutputStream;
import java.net.Socket;

public class SensorGatewaySender implements Runnable {
    private Socket socket;
    private DataOutputStream outputStream;
    private byte[] bytes=new byte[65];
    private byte nodeNumber;
    private short temperature;
    private short dustConcentration;
    private short humidity;

    public SensorGatewaySender(Socket socket){
        try {
            this.socket=socket;
            this.outputStream=new DataOutputStream(socket.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()){
                nodeNumber=1;
                temperature=getRandomInteger(1500,3000);
                dustConcentration=getRandomInteger(0,9999);
                humidity=getRandomInteger(0,9999);
                System.out.println("temperature:"+temperature+" dustConcentration:"+dustConcentration+" humidity:"+humidity);
                this.byteAssign(bytes,0,nodeNumber);
                this.byteAssign(bytes,1,(short)0xA55A);
                this.byteAssign(bytes,41,dustConcentration);
                this.byteAssign(bytes,43,temperature);
                this.byteAssign(bytes,45,humidity);
                outputStream.write(bytes);
                outputStream.flush();
                Thread.sleep(5000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private short getRandomInteger(int min,int max){
        return (short) (Math.random()*(max-min)+min);
    }

    private void byteAssign(byte[] target,int index,int src){
        byte[] bytes= ByteUtils.int2Bytes(src);
        System.arraycopy(bytes,0,target,index,4);
    }

    private void byteAssign(byte[] target,int index,short src){
        byte[] bytes= ByteUtils.short2Bytes(src);
        System.arraycopy(bytes,0,target,index,2);
    }

    private void byteAssign(byte[] target,int index,byte src){
        target[index]=src;
    }
}
