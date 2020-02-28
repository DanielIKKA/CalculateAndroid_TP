package com.android.lab2_calculator.Models;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.android.lab2_calculator.Controller.MainActivity;

import java.lang.ref.WeakReference;

public class CalculateHandleThread extends HandlerThread {
    /*
     * We'll need to modify UI of the activity, so we have to link it to
     * the current Activity
     */
    private WeakReference<MainActivity> activity;
    private String operation;

    private float result;


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
    //This one will calculate in an other Thread
    private class calculateRunnable implements Runnable {

        private String operation;

        calculateRunnable(String op) { this.operation = op; }

        @Override
        public void run() {
            result = activity.get().calculate(operation);

            /*run UIRunnable in an other Thread*/
            // 1- First we instantiate an Handler that loop on the MainThread, so with the MainLooper
            Handler handlerForUI = new Handler(Looper.getMainLooper());
            // 2- We post on it the UIRunnable to execute it now on the good Thread
            handlerForUI.post(UIRunnable);
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

    public void execute(final String operation) {

        //We have to start the thread but this one can be started once, so we have to check
        if(!this.isAlive()) this.start();

        calculateRunnable calRun = new calculateRunnable(operation);
        calRun.run();

        //This backgroundThread Thread need to run calculateRunnable
        new Handler(this.getLooper()).post(calRun);
    }
}
