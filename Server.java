/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.test;

/**
 *
 * @author Natanel
 */
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private final ServerSocket ss;
    int port = 8888;
    String ip = "localhost";
    private static Player p1;
    private static Player p2;
    static   int c=0;//to send players if its their turn
    public static void main(String[] args) throws IOException {
        Server s = new Server();
        try {
            while(c<50){
            p1 = new Player(s.accept(), true,c++);
            p2 = new Player(s.accept(), false,c++);
            p1.setOpp(p2);
            p2.setOpp(p1);
            //this uses 2 threads per game , also possible to implement with ThreadPool to limit the amout of games and add logic for it.
            p1.start();
            p2.start();
            }
        } catch (Exception e) {
          //handle exception as needed
        } finally {
            s.close();
        }
    }

    public Server() throws IOException {
        this.ss = new ServerSocket(port);
        //possible to crete a Game class to handle the game logic and more
    }

    public Socket accept() {
        try {
            return this.ss.accept();
        } catch (Exception e) {
        }
        return null;
    }

    public void close() throws IOException {
        this.ss.close();
    }

    static class Player extends Thread {

        public Player opp;
        Socket s;
        public BufferedReader reader;
        public PrintWriter writer;
        boolean white;
        boolean ourTurn;


        public Player(Socket s, boolean ourTurn,int c) {
            this.s = s;
            this.ourTurn=ourTurn;
            try {
                reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                writer = new PrintWriter(s.getOutputStream(), true);
                writer.println(c+"");//send the isOurTurn to the player, if c%2==0 then its ourTurn, possible to add a random turn firstTurn decision.
                writer.println("Welcome Player !");
            } catch (Exception e) {
                System.out.println("Player died:(");
            }
        }

        public void setTurn(boolean t) {
            this.ourTurn = t;
        }

        public void setOpp(Player p) {
            this.opp = p;
        }

        @Override
        public void run() {
            String msg;
                while (true) {
                    msg="";
                     try {
                    if (ourTurn) {
                        System.out.println("recive and send to opp");
                        msg =this.reader.readLine();
                        System.out.println("got it");
                        if (msg != null && !msg.equals("")) {
                            System.out.print("inside "+msg);
                            this.opp.writer.println(msg);
                            ourTurn = !ourTurn;
                        }

                    } else {
                        System.out.println("recieve from opp and send to self");
                        msg=this.opp.reader.readLine();
                        System.out.println("got it");
                        if (msg != null && !msg.equals("")) {
                        System.out.println("inside "+msg);
                        this.writer.println(msg);
                        ourTurn = !ourTurn;
                    }
                  }
                    } catch (IOException ex) {
                        this.writer.close();
                        try {
                            this.reader.close();
                            this.s.close();
                        } catch (IOException ex1) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                System.out.println("Exception");
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

                }
            
        }
    }

}
