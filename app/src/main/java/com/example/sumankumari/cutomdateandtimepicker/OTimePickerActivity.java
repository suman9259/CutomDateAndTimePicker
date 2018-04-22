package com.example.sumankumari.cutomdateandtimepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.dateandtimepicker.TimePicker;

import java.util.Date;

/**
 * Created by $uman on 05-03-2017.
 */

public class OTimePickerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.only_time_picker_activity);
        TimePicker timePicker=(TimePicker)findViewById(R.id.otpa_time_picker);
        timePicker.setListener(new TimePicker.Listener() {
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
