package com.android.lab2_calculator.Models;

import android.os.AsyncTask;

import com.android.lab2_calculator.Controller.MainActivity;

import java.lang.ref.WeakReference;

public class CalculateAsyncTask extends AsyncTask<String, Void, Float> {
    /*
     * We'll need to modify UI of the activity, so we have to link it to
     * the current Activity
     */
    private WeakReference<MainActivity> activity;
    private String operation;

    public CalculateAsyncTask(MainActivity activity) {
        this.activity = new WeakReference<>(activity);
    }

    /*
     * This override method is executed in an other Thread
     * String 0 : should be the raw operation.toString()
     */
    @Override
    protected Float doInBackground(String... strings) {
        this.operation = strings[0];
        System.out.println(operation);
        return this.activity.get().calculate(this.operation);
    }

    /*
     * This is executed by the MainThread so we can modify UI
     * This function is called after result of doInBackground()
     * param Float it's giving by the doInBackground's return
     */
    @Override
    protected void onPostExecute(Float result) {
        super.onPostExecute(result);

        activity.get().getResultView().setText(String.valueOf(result));
        activity.get().getRelay().setText(this.operation);

        // reset all for new operation
        activity.get().getOperationView().setText("");
        activity.get().getPadEqual().disappearButton();
    }
}
