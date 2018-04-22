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
 * Created by $uman on 15-03-2017.
 */

public class WheelMonthPicker extends WheelPicker {
    public static final int MONTHS_PADDING = 12;
    private int defaultIndex;
    private int newPosition;

    private int lastScrollPosition;

    private SimpleDateFormat simpleDateFormat;

    private OnMonthSelectedListener onMonthSelectedListener;

    Adapter adapter;

    public WheelMonthPicker(Context context) {
        this(context, null);
    }

    public WheelMonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.simpleDateFormat = new SimpleDateFormat("MMMM", getCurrentLocale());
        this.adapter = new Adapter();
        setAdapter(adapter);

        updateMonths();

        updateDefaultMonth();
    }

    @Override
    protected void onItemSelected(int position, Object item) {
        if (null != onMonthSelectedListener) {
            final String itemText = (String) item;
            final Date date = convertItemToDate(position);
            onMonthSelectedListener.onMonthSelected(this, position, itemText, date);
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, Object item) {
        final String itemText = (String) item;
        //final Date date = convertItemToDate(position);
        if (onMonthSelectedListener!=null) {
            onMonthSelectedListener.onMonthCurrentScrolled(this,position,itemText);
        }
        if (lastScrollPosition != position) {
            onMonthSelectedListener.onMonthCurrentScrolled(this,position,itemText);
            if (lastScrollPosition == MONTHS_PADDING && position == 0)
                if (onMonthSelectedListener != null) {
                    onMonthSelectedListener.onMonthCurrentNewYear(this);
                }
            lastScrollPosition = position;
        }

    }

    @Override
    public int getDefaultItemPosition() {
        return defaultIndex;
    }

    private void updateMonths() {
        final List<String> data = new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH, -1 * MONTHS_PADDING - 1);
        for (int i = (-1) * MONTHS_PADDING; i < 0; ++i) {
            instance.add(Calendar.MONTH,1);
            data.add(getFormattedValue(instance.getTime()));
        }
        newPosition = data.size();
        defaultIndex = newPosition;
        for (int i = 0; i < MONTHS_PADDING; ++i) {
            instance.add(Calendar.MONTH, 1);
            data.add(getFormattedValue(instance.getTime()));
        }

        adapter.setData(data);
    }

    protected String getFormattedValue(Object value) {
        return simpleDateFormat.format(value);
    }

    public void setOnMonthSelectedListener(OnMonthSelectedListener onMonthSelectedListener) {
        this.onMonthSelectedListener = onMonthSelectedListener;
    }

    private void updateDefaultMonth() {
        setSelectedItemPosition(defaultIndex);
    }

    public int getDefaultMonthIndex() {
        return defaultIndex;
    }

    public Date getCurrentDate() {
        return convertItemToDate(super.getCurrentItemPosition());
    }

    private Date convertItemToDate(int itemPosition) {
        Date date = null;
        String itemText = adapter.getItemText(itemPosition);
        final Calendar todayCalendar = Calendar.getInstance();
        if (itemPosition == newPosition) {
            date = todayCalendar.getTime();
        } else {
            try {
                date = simpleDateFormat.parse(itemText);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


            if (date != null) {
            //try to know the year
            final Calendar dateCalendar = Calendar.getInstance();
            dateCalendar.setTime(date);
                todayCalendar.add(Calendar.MONTH, (itemPosition - newPosition));
            date = dateCalendar.getTime();
        }

        return date;
    }

    public String getCurrentMonth() {
        return adapter.getItemText(getCurrentItemPosition());
        //return convertItemToDate((Integer) adapter.getItem(getCurrentItemPosition()));
    }

    public interface OnMonthSelectedListener {
        void onMonthSelected(WheelMonthPicker picker, int position, String name, Date date);
        void onMonthCurrentScrolled(WheelMonthPicker picker, int position, String name);

        void onMonthCurrentNewYear(WheelMonthPicker picker);
    }

}
