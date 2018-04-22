package com.example.sumankumari.cutomdateandtimepicker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dateandtimepicker.dialog.DateAndTimePickerDialog;
import com.example.dateandtimepicker.dialog.DatePickerDialog;
import com.example.dateandtimepicker.dialog.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by suman on 30/03/17.
 */

public class DatePickerClass extends Activity implements View.OnClickListener{
    SimpleDateFormat simpleDateFormat, onlyTimeFormat, dateFormat, simpleDateFormatWithYear;
    TextView result;
    Button time,date,dateAndTime;

    DateAndTimePickerDialog.Builder dateAndTimeBuilder;
    TimePickerDialog.Builder timeBuilder;
    DatePickerDialog.Builder dateBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isInitView();
        time.setOnClickListener(this);
        date.setOnClickListener(this);
        dateAndTime.setOnClickListener(this);

    }
    public void isInitView(){
        result=(TextView)findViewById(R.id.result_txt);
        time=(Button)findViewById(R.id.btn_time);
        date=(Button)findViewById(R.id.btn_Date);
        dateAndTime=(Button)findViewById(R.id.btn_date_and_time);
    }

    public DatePickerClass() {

        this.simpleDateFormat = new SimpleDateFormat("EEE d MMM h:mm a", Locale.getDefault());
        this.simpleDateFormatWithYear = new SimpleDateFormat("EEE d MMM yyyy h:mm a", Locale.getDefault());
        this.onlyTimeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        this.dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    }
    public void isTimePickerClicked( Context context) {

        final Calendar calendar = Calendar.getInstance();
        final Date minDate = calendar.getTime();
        calendar.set(Calendar.HOUR,10);
        final Date maxDate = calendar.getTime();

        timeBuilder = new TimePickerDialog.Builder(context)
                .bottomSheet()
                .curved()
                .backgroundColor(Color.WHITE)
                .mainColor(Color.BLACK)
                .mustBeOnFuture()
                //.minutesStep(15)
                .mustBeOnFuture()
                .defaultDate(minDate)
                .minDateRange(minDate)
                .maxDateRange(maxDate)
                .title("Select Sit Start Time")
                .listener(new TimePickerDialog.Listener() {
                    //onclick done button
                    @Override
                    public void onDateSelected(Date date) {
                        result.setText(simpleDateFormat.format(date));
                    }
                 //on click cancel button
                    @Override
                    public void onCancel() {

                    }
                });
        timeBuilder.display();
    }
    public void isDatePickerClicked( Context context) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 25);
        calendar.set(Calendar.MONTH, 6);
        calendar.set(Calendar.YEAR, 2015);
        final Date minDate = calendar.getTime();
        calendar.set(Calendar.YEAR, 2019);
        final Date maxDate = calendar.getTime();

        dateBuilder = new DatePickerDialog.Builder(context)
                .bottomSheet()
                .curved()
                .backgroundColor(Color.WHITE)
                .mainColor(Color.BLACK)
                .mustBeOnFuture()
                .defaultDate(minDate)
                .minDateRange(minDate)
                .maxDateRange(maxDate)
                .title("Select Sit Start Time")
                .listener(new DatePickerDialog.Listener() {
                    //onclick done button
                    @Override
                    public void onDateSelected(Date date) {
                        result.setText(simpleDateFormat.format(date));
                    }
                    //on click cancel button
                    @Override
                    public void onCancel() {

                    }
                });
        dateBuilder.display();
    }
    public void isDateAndTimePickerClicked( Context context) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 25);
        calendar.set(Calendar.MONTH, 6);
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 45);
        final Date minDate = calendar.getTime();
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DAY_OF_MONTH, 30);
        final Date maxDate = calendar.getTime();

        dateAndTimeBuilder = new DateAndTimePickerDialog.Builder(context)
                .bottomSheet()
                .curved()
                .backgroundColor(Color.WHITE)
                .mainColor(Color.BLACK)
                .mustBeOnFuture()
                .defaultDate(minDate)
                .minDateRange(minDate)
                .maxDateRange(maxDate)
                .title("Select Sit Start Time")
                .listener(new DateAndTimePickerDialog.Listener() {
                    //onclick done button
                    @Override
                    public void onDateSelected(Date date) {
                        result.setText(simpleDateFormat.format(date));
                    }
                    //on click cancel button
                    @Override
                    public void onCancel() {

                    }
                });
        dateAndTimeBuilder.display();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_time:
                isTimePickerClicked(this);
                break;
            case  R.id.btn_Date:
                isDatePickerClicked(this);
                break;
            case R.id.btn_date_and_time:
                isDateAndTimePickerClicked(this);
                break;
        }
    }
}

    /*SimpleDateFormat simpleDateFormat, onlyTimeFormat, dateFormat, simpleDateFormatWithYear;

     DateAndTimePickerDialog dateAndTimeBuilder;
    TimePickerDialog.Builder timeBuilder;
    DatePickerDialog.Builder dateBuilder;

    public DatePickerClass() {

        this.simpleDateFormat = new SimpleDateFormat("EEE d MMM h:mm a", Locale.getDefault());
        this.simpleDateFormatWithYear = new SimpleDateFormat("EEE d MMM yyyy h:mm a", Locale.getDefault());
        this.onlyTimeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        this.dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    }

    public void timeClicked(Context context, Date min_Date, Date default_Date, Date max_Date, String mid_title, int minutes_Step, final NewCustomDatePicker datePicker) {
        final Calendar calendar = Calendar.getInstance();

        final Date defaultDate = getAlignmentDate(default_Date,minutes_Step);

        final Date minDate = getAlignmentDate(min_Date,minutes_Step);

        final Date maxDate = getAlignmentDate(max_Date,minutes_Step);
        final String midTitle = mid_title;

        final int minutesStep = minutes_Step;

        timeBuilder = new TimePickerDialog.Builder(context)
                .bottomSheet()
                .curved()
                .minutesStep(minutesStep)
                .defaultDate(defaultDate)
                .minDateRange(minDate)
                .maxDateRange(maxDate)
                .title(midTitle)
                .listener(new TimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        if (date!=null) {
                            //TimeText.setText(onlyTimeFormat.format(date));
                            datePicker.OnDateSetClick(simpleDateFormatWithYear.format(date));
                        }

                    }

                    @Override
                    public void onCancel() {
                        datePicker.OnDateCancelClick();
                    }
                });
        timeBuilder.display();

    }

    public void dateClicked(final Context context, Date min_Date, final Date default_Date, Date max_Date, String mid_title, final NewCustomDatePicker datePicker) {

        final Calendar calendar = Calendar.getInstance();

        final Date defaultDate = default_Date;

        final Date minDate = min_Date;

        final Date maxDate = max_Date;

        final String midTitle = mid_title;


        dateBuilder = new DatePickerDialog.Builder(context)
                .bottomSheet()
                .curved()
                .defaultDate(defaultDate)
                .minDateRange(minDate)
                .maxDateRange(maxDate)
                .title(midTitle)
                .listener(new DatePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        if (date!=null) {
                            datePicker.OnDateSetClick(dateFormat.format(date));
                        }

                    }

                    @Override
                    public void onCancel() {
                        datePicker.OnDateCancelClick();
                    }


                });
        dateBuilder.display();
    }

    public void dateAndTimeClicked(Context context, Date min_Date, Date default_Date, Date max_Date, String mid_title, int minutes_Step, final NewCustomDatePicker datePicker) {

        final Calendar calendar = Calendar.getInstance();

        final Date defaultDate = getAlignmentDate(default_Date, minutes_Step);

        final Date minDate = getAlignmentDate(min_Date, minutes_Step);

        final Date maxDate = getAlignmentDate(max_Date, minutes_Step);

        final String midTitle = mid_title;

        final int minutesStep = minutes_Step;

        dateAndTimeBuilder = new DateAndTimePickerDialog.Builder(context)
                .bottomSheet()
                .curved()
                .minutesStep(minutesStep)
                .minDateRange(minDate)
                .defaultDate(defaultDate)
                .maxDateRange(maxDate)
                .title(midTitle)
                .listener(new DateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        if (date!=null) {
                            //DateWithTimeText.setText(simpleDateFormat.format(date));
                            datePicker.OnDateSetClick(simpleDateFormatWithYear.format(date));
                        }
                    }

                    @Override
                    public void onCancel() {
                        datePicker.OnDateCancelClick();
                    }
                });
        dateAndTimeBuilder.display();
    }

    public Date getAlignmentDate(Date alignmentDate, int minutesStep) {
        Date NewAlignmentDate;
        if (alignmentDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(alignmentDate);
            int new_minutes = calendar.get(Calendar.MINUTE);

            int mint_reminder = new_minutes % minutesStep;

            if (mint_reminder != 0) {
                int minutes = new_minutes - mint_reminder;
                int newAlignmentMinutes = minutes + minutesStep;
                if (newAlignmentMinutes >= 60) {
                    int checkValidMinutes = newAlignmentMinutes % 60;
                    calendar.add(Calendar.HOUR, 1);
                    calendar.set(Calendar.MINUTE, checkValidMinutes);
                    NewAlignmentDate = calendar.getTime();
                } else {
                    calendar.set(Calendar.MINUTE, newAlignmentMinutes);
                    NewAlignmentDate = calendar.getTime();
                }
            } else {
                NewAlignmentDate = alignmentDate;
            }
            return NewAlignmentDate;
        }
        return null;
    }
}*/
