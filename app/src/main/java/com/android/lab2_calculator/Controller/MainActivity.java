package com.android.lab2_calculator.Controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.android.lab2_calculator.Models.CalculateAsyncTask;
import com.android.lab2_calculator.Models.CalculateHandleThread;
import com.android.lab2_calculator.R;
import com.android.lab2_calculator.Views.EqualButton;

import java.util.ArrayList;
import java.util.StringTokenizer;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    /**-----------------**
     **      VIEWS      **
     **-----------------**/
    private TextView operationView;
    private TextView resultView;
    private TextView relay;
    private Switch switchComponent;
    private EqualButton padEqual;

    /**--------------------**
     ** PRIVATE ATTRIBUTES **
     **--------------------**/
    private boolean writable = true;
    private boolean signEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /// find their reference
        this.operationView = findViewById(R.id.expressionText);
        this.resultView = findViewById(R.id.resultView);
        this.relay = findViewById(R.id.relayOperation);
        this.switchComponent = findViewById(R.id.switchId);

        this.padEqual = new EqualButton(this);

        /// add Listener
        this.operationView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                writable = count < 7;
                signEnable = count > 0;
            }
            @Override
            public void afterTextChanged(Editable s) {
                /// test if there is an ongoing operation (no more one operation each time)
                StringTokenizer st = new StringTokenizer(s.toString(), "+-x/", true);
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();

                    if ("+-/x".contains(token)) {
                        signEnable = false;
                        padEqual.appearButton();
                        break;
                    } else {
                        padEqual.disappearButton();
                    }
                }
                findViewById(R.id.pad_plus).setEnabled(signEnable);
                findViewById(R.id.pad_minus).setEnabled(signEnable);
                findViewById(R.id.pad_mult).setEnabled(signEnable);
                findViewById(R.id.pad_divide).setEnabled(signEnable);
            }
        });
        this.padEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClick(v);
            }
        });
    }

    /**-----------------**
     ** PUBLIC HANDLER **
     **-----------------**/
    public void handleClick(View view) {
        Button b = (Button) view;

        /// Check if it's not the padEqual
        if(!view.equals(this.padEqual) && writable) {
            //build the operation
            StringBuilder s = new StringBuilder(this.operationView.getText()).append(b.getText());
            this.operationView.setText(s.toString());
        } else if(view.equals(this.padEqual)) {
            String operation = this.operationView.getText().toString();

            //use different async method
            if(!this.switchComponent.isChecked()) {
                System.out.println("AsyncTask");
                CalculateAsyncTask asyncTask= new CalculateAsyncTask(this);
                asyncTask.execute(operation);
            } else {
                System.out.println("handleThread");
                CalculateHandleThread handleThread = new CalculateHandleThread("newThread", this);
                handleThread.execute(operation);
            }
        }
    }

    /**-----------------**
     ** PRIVATE METHODS **
     **-----------------**/
    private float operation(float nb1, float nb2, String operator) {

        switch (operator) {
            case "+" :
                return nb1 + nb2;
            case "-" :
                return nb1 - nb2;
            case "x" :
                return nb1 * nb2;
            case "/" :
                return nb1 / nb2;
            default:
                System.out.println("error sign");
        }

        return 0;
    }

    /**-------------------------**
     ** PACKAGE-PRIVATE METHODS **
     **-------------------------**/
    public float calculate(String raw) {
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

        float nb1 = Float.parseFloat(operandList.get(0));
        float nb2 = Float.parseFloat(operandList.get(1));

        return operation(nb1, nb2, operatorList.get(0));
    }

    /**-------------------------**
     **     GETTER METHODS      **
     **-------------------------**/
    public TextView getResultView() { return resultView; }

    public TextView getRelay() { return relay; }

    public TextView getOperationView() { return operationView; }

    public EqualButton getPadEqual() { return padEqual; }
}