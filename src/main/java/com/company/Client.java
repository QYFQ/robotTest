package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

    private Socket socket;
    private String s;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private String host;
    private int port;
    private Double[] sin=new Double[360];
    private int[][] phase=new int[][]{{80,60,30,40,50,160,270,180,90,100},{90,150,210,270,330,30},{0,60,120,180,240,300},{0,10,20,30,40,50}};
    private double[][] base=new double[][]{{300,0,800,120,-30,-60,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,100,-100,0,0,0}};
    private double[][] amp=new double[][]{{200,100,200,60,60,60,2,2,2,2},{90,70,100,60,40,80},{110,60,100,75,80,180},{100,200,100,100,100,100}};
    private Float[] vel=new Float[200];
    private Float[] pos=new Float[200];
    private Float[] trq=new Float[200];

    public Client(String host,int port){
        this.host=host;
        this.port = port;
    }

    private Double getSin(int phase,double base,double amp){
        phase=phase%360;
        return base+amp*sin[phase];
    }

    @Override
    public void run(){
        try {
            for (int i=0;i<360;i++){
                sin[i]=Math.sin(i*2*Math.PI/360);
            }
            socket=new Socket("127.0.0.1",8888);
            bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("连接主机 "+socket.getInetAddress().toString().substring(1)+":"+socket.getPort()+" 成功");
            System.out.println("本机地址："+socket.getLocalAddress().toString().substring(1)+":"+socket.getLocalPort());
            while((s=bufferedReader.readLine())!=null){
                String response="";
                StringBuilder sb=new StringBuilder();
                float f=(float)(port +0.5);
                switch (s) {
                    case "cart_pos":
                        for (int i=0;i<9;i++){
                            sb.append(getSin(phase[0][i],base[0][i],amp[0][i]));
                            sb.append(",");
                            phase[0][i]=(phase[0][i]+1)%360;
                        }
                        sb.append(getSin(phase[0][9],base[0][9],amp[0][9]));
                        sb.append("\r");
                        response = sb.toString();
                        break;
                    case "jnt_vel":
                        for (int i=0;i<5;i++){
                            sb.append(getSin(phase[1][i],base[1][i],amp[1][i]));
                            sb.append(",");
                            phase[1][i]=(phase[1][i]+1)%360;
                        }
                        sb.append(getSin(phase[1][5],base[1][5],amp[1][5]));
                        sb.append("\r");
                        response = sb.toString();
                        break;
                    case "jnt_pos":
                        for (int i=0;i<5;i++){
                            sb.append(getSin(phase[2][i],base[2][i],amp[2][i]));
                            sb.append(",");
                            phase[2][i]=(phase[2][i]+1)%360;
                        }
                        sb.append(getSin(phase[2][5],base[2][5],amp[2][5]));
                        sb.append("\r");
                        response = sb.toString();
                        break;
                    case "jnt_trq":
                        for (int i=0;i<5;i++){
                            sb.append(getSin(phase[3][i],base[3][i],amp[3][i]));
                            sb.append(",");
                            phase[3][i]=(phase[3][i]+5)%360;
                        }
                        sb.append(getSin(phase[3][5],base[3][5],amp[3][5]));
                        sb.append("\r");
                        response = sb.toString();
                        break;
                    case "state":
                        response = "MOTOR_ON\r";
                        break;
                    case "query_space_para":
                        response = 40 * f + "," + 4 * f + "," + 0.4 * f + ";" + 30 * f + "," + 3 * f + "," + 0.3 * f + "\r";
                        break;
                    case "query_axis_max_speed":
                        response = 2 * f + "," + 3 * f + "," + 4 * f + "," + 5 * f + "," + 6 * f + "," + 7 * f + "\r";
                        break;
                    case "query_axis_max_acc":
                        response = 0.2 * f + "," + 0.3 * f + "," + 0.4 * f + "," + 0.5 * f + "," + 0.6 * f + "," + 0.7 * f + "\r";
                        break;
                    case "query_axis_soft_limit":
                        response = 50 * f + "," + 40 * f + "," + 50 * f + "," + 60 * f + "," + 30 * f + "," + 60 * f +
                                ";" + (-50) * f + "," + (-40) * f + "," + (-50) * f + "," + (-60) * f + "," + (-30) * f + "," + (-60) * f + "\r";
                        break;
                    default:
                        response = "\r";
                        break;
                }
                printWriter.print(response);
                printWriter.flush();
            }
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
