package com.example.dateandtimepicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by $uman on 16-03-2017.
 */

public class WheelYearPicker extends WheelPicker {
    public static final int YEARS_PADDING = 100;
    private int defaultIndex;
    private int todayPosition;
    private int lastScrollPosition;

    private SimpleDateFormat simpleDateFormat;

    private OnYearSelectedListener onYearSelectedListener;

    WheelPicker.Adapter adapter;

    public WheelYearPicker(Context context) {
        this(context, null);
    }

    public WheelYearPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.simpleDateFormat = new SimpleDateFormat("yyyy", getCurrentLocale());
        this.adapter = new Adapter();
        setAdapter(adapter);

        updateYears();

        updateDefaultYear();
    }

    @Override
    protected void onItemSelected(int position, Object item) {
        if (null != onYearSelectedListener) {
            final String itemText = (String) item;
            final Date date = convertItemToDate(position);
            onYearSelectedListener.onYearSelected(this, position, itemText, date);
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, Object item) {
        final String itemText = (String) item;
        if (onYearSelectedListener!=null) {
            onYearSelectedListener.onYearCurrentScroll(this,position,Integer.valueOf(itemText));

        }
    }

    @Override
    public int getDefaultItemPosition() {
        return defaultIndex;
    }

    private void updateYears() {
        final List<String> data = new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, -1 * YEARS_PADDING - 1);
        for (int i = (-1) * YEARS_PADDING; i < 0; ++i) {
            instance.add(Calendar.YEAR, 1);
            data.add(getFormattedValue(instance.getTime()));
        }
        todayPosition = data.size();
        defaultIndex = todayPosition;

        for (int i = 0; i < YEARS_PADDING; ++i) {
            instance.add(Calendar.YEAR, 1);
            data.add(getFormattedValue(instance.getTime()));
        }

        adapter.setData(data);
    }

    protected String getFormattedValue(Object value) {
        return simpleDateFormat.format(value);
    }

    public void setOnYearSelectedListener(OnYearSelectedListener onYearSelectedListener) {
        this.onYearSelectedListener = onYearSelectedListener;
    }

    private void updateDefaultYear() {
        setSelectedItemPosition(defaultIndex);
    }

    public int getDefaultYearIndex() {
        return defaultIndex;
    }

    public Date getCurrentDate() {
        return convertItemToDate(super.getCurrentItemPosition());
    }

    private Date convertItemToDate(int itemPosition) {
        Date date = null;
        String itemText = adapter.getItemText(itemPosition);
        final Calendar todayCalendar = Calendar.getInstance();
        if (itemPosition == todayPosition) {
            date = todayCalendar.getTime();
        } else {
            try {
                date = simpleDateFormat.parse(itemText);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        if (date != null) {
            final Calendar dateCalendar = Calendar.getInstance();
            dateCalendar.setTime(date);
            todayCalendar.add(Calendar.YEAR, (itemPosition - todayPosition));
            date = dateCalendar.getTime();
        }

        return date;
    }

    public String getCurrentYear() {
        return adapter.getItemText(getCurrentItemPosition());
       // return convertItemToDate((Integer) adapter.getItem(getCurrentItemPosition()));
    }

    public interface OnYearSelectedListener {
        void onYearSelected(WheelYearPicker picker, int position, String name, Date date);
        void onYearCurrentScroll(WheelYearPicker picker, int position, int Year);
    }
}
