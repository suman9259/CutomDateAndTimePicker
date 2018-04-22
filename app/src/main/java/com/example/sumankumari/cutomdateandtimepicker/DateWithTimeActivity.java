package com.example.sumankumari.cutomdateandtimepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.dateandtimepicker.DateAndTimePicker;

import java.util.Date;

/**
 * Created by $uman on 02-03-2017.
 */

public class DateWithTimeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_with_time);

        DateAndTimePicker singleDateAndTimePicker = (DateAndTimePicker) findViewById(R.id.dwt_single_day_picker);
        singleDateAndTimePicker.setListener(new DateAndTimePicker.Listener() {
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
