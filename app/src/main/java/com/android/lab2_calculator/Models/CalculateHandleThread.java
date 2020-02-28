package com.android.lab2_calculator.Models;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.android.lab2_calculator.Controller.MainActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class CalculateHandleThread extends HandlerThread {
    /*
     * We'll need to modify UI of the activity, so we have to link it to
     * the current Activity
     */
    private WeakReference<MainActivity> activity;
    private String operation;

    private double result;

    //Creation of two Runnable
    //This one will'll change the UI of activity, WARMING this must execute in the MainThread
    private Runnable UIRunnable = new Runnable() {
        @Override
        public void run() {
            activity.get().getResultView().setText(String.valueOf(result));
            activity.get().getRelay().setText(operation);

            // reset all for new operation
            activity.get().getOperationView().setText("");
            activity.get().getPadEqual().disappearButton();
        }
    };
    //This one will connect and send op to a web socket in an other Thread
    private class ClientSocketRunnable implements Runnable {

        Socket socket;

        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName("10.0.2.2");
                socket = new Socket(serverAddr, 9876);
                System.out.println("Connected");

                sendOp(operation);

                receiveAndUpdateUI();

                activity.get().addOperationResult(operation, result);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void sendOp(String raw) {
            /*
             * https://stackoverflow.com/questions/13525024/how-to-split-a-mathematical-expression-on-operators-as-delimiters-while-keeping
             */

            /// Operation parsing
            ArrayList<String> operatorList = new ArrayList<>();
            ArrayList<String> operandList = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(raw, "+-x/", true);
            while (st.hasMoreTokens()) {
                String token = st.nextToken();

                if ("+-/x".contains(token)) {
                    operatorList.add(token);
                } else {
                    operandList.add(token);
                }
            }

            final double nb1 = Double.parseDouble(operandList.get(0));
            final double nb2 = Double.parseDouble(operandList.get(1));
            final char operator = operatorList.get(0).charAt(0);

            DataOutputStream outputStream = null;
            try {
                outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeDouble(nb1);
                outputStream.writeChar(operator);
                outputStream.writeDouble(nb2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        void receiveAndUpdateUI() {
            try {
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                result = inputStream.readDouble();
                /*run UIRunnable in an other Thread*/
                // 1- First we instantiate an Handler that loop on the MainThread, so with the MainLooper
                Handler handlerForUI = new Handler(Looper.getMainLooper());
                // 2- We post on it the UIRunnable to execute it now on the good Thread
                handlerForUI.post(UIRunnable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**-----------------**
     **   CONSTRUCTOR   **
     **-----------------**/
    public CalculateHandleThread(String name, MainActivity activity) {
        super(name);

        this.activity = new WeakReference<>(activity);
        this.operation = String.valueOf(this.activity.get().getOperationView().getText());
    }

    public void execute() {

        //We have to start the thread but this one can be started once, so we have to check
        if(!this.isAlive()) this.start();

        //calculateRunnable calRun = new calculateRunnable(operation);
        ClientSocketRunnable ClientSocket = new ClientSocketRunnable();
        //This backgroundThread Thread need to run calculateRunnable
        new Handler(this.getLooper()).post(ClientSocket);
    }
}
