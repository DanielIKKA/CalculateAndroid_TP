package com.android.lab2_calculator.Views;

import android.content.Context;
import android.view.View;

import com.android.lab2_calculator.Controller.MainActivity;
import com.android.lab2_calculator.R;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class EqualButton extends AppCompatButton {

    MainActivity activity;

    public EqualButton(Context context) {
        super(context);

        this.activity = (MainActivity) context;
        this.initEqualButton();
    }

    /**-----------------**
     ** PRIVATE METHODS **
     **-----------------**/
    private void initEqualButton() {
        ConstraintLayout layout = this.activity.findViewById(R.id.layout);

        //button attributes
        this.setId(View.generateViewId());
        this.setText(this.getContext().getString(R.string.pad_equal));
        this.setTextSize(50);

        layout.addView(this);
        disappearButton();
    }

    public void appearButton() {
        ConstraintLayout layout = this.activity.findViewById(R.id.layout);
        ConstraintSet set = new ConstraintSet();

        //layout constrains
        set.connect(this.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        set.connect(this.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        set.setDimensionRatio(this.getId(), "2:1");
        set.constrainPercentWidth(this.getId(), (float) 0.5);
        set.applyTo(layout);
    }

    public void disappearButton() {
        ConstraintLayout layout = this.activity.findViewById(R.id.layout);
        ConstraintSet set = new ConstraintSet();

        //layout constrains
        set.connect(this.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START);
        set.applyTo(layout);
    }
}
