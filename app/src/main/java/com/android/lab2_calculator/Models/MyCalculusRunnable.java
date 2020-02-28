package com.android.lab2_calculator.Models;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class MyCalculusRunnable implements Runnable {
    private Socket sock;
    private BufferedReader input;

    MyCalculusRunnable(Socket s) {
        sock = s;
    }

    @Override
    public void run() {
        try {

            DataInputStream dis = new DataInputStream(sock.getInputStream());
            DataOutputStream dos = new DataOutputStream(sock.getOutputStream());

            // read op1, op2 and the opreation to make
            double op1 = dis.readDouble();
            char op = dis.readChar();
            double op2 = dis.readDouble();

            System.out.println("client: " + op1 + op + op2);

            double res = CalculusServer.doOp(op1, op2, op);

            System.out.println("server : " + res);
            // send back result
            dos.writeDouble(res);

            dis.close();
            dos.close();
            sock.close();

        } catch (Exception e) {
            try {
                DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
                dos.writeDouble(Double.POSITIVE_INFINITY);
                dos.close();
                System.out.println("server send : positive infinity because there was an error");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

class CalculusServer {

    static double doOp(double op1, double op2, char op) throws Exception {
        switch (op) {

            case '+':
                return op1 + op2;

            case '-':
                return op1 - op2;

            case'x':
                return op1 * op2;

            case '/':
                if (op2 != 0)
                    return op1 / op2;
                else
                    throw new Exception();

            default:
                throw new Exception();
        }

    }

    public static void main(String[] args) throws Exception {

        // Example of a distant calculator
        ServerSocket ssock = new ServerSocket(9876);

        while (!Thread.currentThread().isInterrupted()) { // infinite loop
            Socket comm = ssock.accept();
            System.out.println("connection established");

            new Thread(new MyCalculusRunnable(comm)).start();
        }
    }
}
