package com.example.sumankumari.cutomdateandtimepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.example.dateandtimepicker.DatePicker;

import java.util.Date;

/**
 * Created by $uman on 09-03-2017.
 */

public class DatePickerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        DatePicker singleDateAndTimePicker = (DatePicker) findViewById(R.id.adp_date_picker);
        singleDateAndTimePicker.setListener(new DatePicker.Listener() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                display(displayed);
            }
        });
    }
    private void display(String toDisplay) {
        Toast.makeText(this, toDisplay, Toast.LENGTH_SHORT).show();
    }
}
