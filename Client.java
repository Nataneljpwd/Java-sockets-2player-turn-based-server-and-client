package net.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    private static final int PORT = 8888;
    private Socket s;
    private BufferedReader in;
    private PrintWriter out;
    boolean ourTurn;
    Scanner sc = new Scanner(System.in);//this is for direct player input from console, in your game just add a getMove func and when a move is made call getMove. 
    
    public Client() throws Exception {
        s = new Socket("localhost", PORT);
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(),true);
    }

    public void play() {
        String res;
        int a;
        try {
            a=Integer.parseInt(in.readLine());
            ourTurn=a%2==0;
            System.out.println(in.readLine());
        } catch (Exception e) {
        }
        while (true) {
            try {
                    if (ourTurn) {
                    res =sc.nextLine();//here we can get the move from an outside func for the turn based gameusing a getMove function that returns the move output as a string.
                    if (res != null && !res.equals("")) {
                        out.println(res);
                        ourTurn = !ourTurn;
                    }
                } else {
                    res = in.readLine();
                    if (res != null && !res.equals("")) {
                        System.out.println(res+" msg from opp");
                        ourTurn = !ourTurn;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Err");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Client c = new Client();
        c.play();
    }

}
