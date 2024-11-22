/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unknown.ontap1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Sach;
import model.SachDAO;
import view.GUI;
import view.Table;

/**
 *
 * @author Admin
 */
class newThreadserver extends Thread{
    private Socket cl ;
    private InetAddress a_client;
    private int count;
    private int p_client;
    //Nhan sach chuyen len Database
    private Sach s;
    //Input
    //oup
    ObjectInputStream inp;
    ObjectOutputStream oup;
    BufferedReader input_request;
    BufferedReader id;
    PrintWriter p;
    public newThreadserver(Socket cl,int count) {
        super();
        this.count = count;
        this.cl = cl;
        start();
    }

    public void run(){
        try {
            
            inp = new ObjectInputStream(cl.getInputStream());
            oup = new ObjectOutputStream(cl.getOutputStream());
            String re = (String) inp.readObject();
            System.out.println(re);
            switch(re)
            {
                case "ADD":s = (Sach) inp.readObject(); SachDAO.getInstance().add(s);
                case "SEARCH": String id = (String) inp.readObject();
                               String kq = SachDAO.getInstance().search(id);
                               oup.writeObject(kq);
                case "DELETE": String id_xoa = (String) inp.readObject();
                               SachDAO.getInstance().detele(id_xoa);
                case "UPDATE": s = (Sach)inp.readObject();
                               String id_up = (String) inp.readObject();
                               SachDAO.getInstance().update(s, id_up);
            }
//                if(re.equalsIgnoreCase("add"))
//                {
//                    s = (Sach) inp.readObject();
//                    SachDAO.getInstance().add(s);
//                }
//                if(re.equalsIgnoreCase("delete"))
//                    {
//                    String id = input_request.readLine();
//                    SachDAO.getInstance().detele(id);
//                }
//                    
//                if(re.equalsIgnoreCase("search"))
//                {
//                    String id = input_request.readLine();
//                    p = new PrintWriter(cl.getOutputStream());
//                    String a = SachDAO.getInstance().search(id);
//                    p.print(a);
//                }
            
            
            
            
//            else if(){
//                
//            }
            Table t = new Table();
            t.setVisible(true);

        } catch (IOException ex) {
            Logger.getLogger(newThreadserver.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(newThreadserver.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } finally{
            try {
                if(cl!= null)cl.close();
                if(inp !=null)inp.close();
                if(oup!= null)oup.close();
               
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
        }

    }
}
public class Server {
    public static void main(String[] args) {
        ServerSocket svr = null;
        int s_port = 1234;
        Socket cl;
        int count;
        try {
            svr = new ServerSocket(s_port);
            count = 0;
            System.out.println("server is ready");
            while(true)
            {
                cl = svr.accept();
                new newThreadserver(cl, count);
                count++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally{
            try {
                if(svr!= null)svr.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
