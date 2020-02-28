package com.android.lab2_calculator.Models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class MyCalculusRunnable implements Runnable {
    private Socket sock;

    public MyCalculusRunnable(Socket s) {
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

            double res = CalculusServer.doOp(op1, op2, op);

            // send back result
            dos.writeDouble(res);

            dis.close();
            dos.close();
            sock.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static class CalculusServer {

        static double doOp(double op1, double op2, char op) throws Exception {
            switch (op) {

                case '+':
                    return op1 + op2;

                case '-':
                    return op1 - op2;

                case '*':
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
    }
}
