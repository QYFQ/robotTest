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
    private int id;

    public Client(int id){
        this.id=id;
    }

    @Override
    public void run(){
        try {
            socket=new Socket("127.0.0.1",8888);
            bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            while((s=bufferedReader.readLine())!=null){
                System.out.println(s+" "+id);
                String response;
                float f=(float)(id+0.5);
                if (s.equals("alarm_info")){
                    if (id==1) response="999\r";
                    else if (id==2) response="404\r";
                    else response="\r";
                }
                else if (s.equals("cart_pos")) response="cartPosOK"+id+"\r";
                else if (s.equals("jnt_vel")) response=f+","+2*f+","+3*f+","+4*f+","+5*f+","+6*f+"\r";
                else if (s.equals("jnt_pos")) response=10*f+","+20*f+","+30*f+","+40*f+","+50*f+","+60*f+"\r";
                else if (s.equals("jnt_trq")) response=0.1*f+","+0.2*f+","+0.3*f+","+0.4*f+","+0.5*f+","+0.6*f+"\r";
                else if (s.equals("state")){
                    if (id%2==0) response="MOTOR_ON\r";
                    else response="MOTOR_OFF\r";
                }
                else if (s.equals("query_space_para")) response=40*f+","+4*f+","+0.4*f+";"+30*f+","+3*f+","+0.3*f+"\r";
                else if (s.equals("query_axis_max_speed")) response=2*f+","+3*f+","+4*f+","+5*f+","+6*f+","+7*f+"\r";
                else if (s.equals("query_axis_max_acc")) response=0.2*f+","+0.3*f+","+0.4*f+","+0.5*f+","+0.6*f+","+0.7*f+"\r";
                else if (s.equals("query_axis_soft_limit")) response=50*f+","+40*f+","+50*f+","+60*f+","+30*f+","+60*f+
                        ";"+(-50)*f+","+(-40)*f+","+(-50)*f+","+(-60)*f+","+(-30)*f+","+(-60)*f+"\r";
                else response="\r";
                printWriter.print(response);
                printWriter.flush();
            }
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
