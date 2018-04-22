package com.example.dateandtimepicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by $uman on 09-03-2017.
 */

public class WheelOnlyDayPicker extends WheelPicker {
    public static final int MIN_DAY_DEFAULT = 1;
    public static final int STEP_DAY_DEFAULT = 1;

    private OnDaySelectedListener daysSelectedListener;

    private int defaultDay;
    private int minDay;
    private int maxDay;
    private int daysStep = STEP_DAY_DEFAULT;

    private int lastScrollPosition;

    private Adapter adapter;

    public Date getDefaultDate() {
        return defaultDate;
    }

    private Date defaultDate;

    public WheelOnlyDayPicker(Context context) {
        this(context, null);
    }

    public WheelOnlyDayPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        maxDateOfMonth();
        initAdapter();
        updateDefaultDay();
    }

    private void initAdapter() {
        final List<String> days = new ArrayList<>();

        for (int day = MIN_DAY_DEFAULT; day <= maxDay; day += daysStep) {
            days.add(getFormattedValue(day));
        }

        adapter = new Adapter(days);
        setAdapter(adapter);
        defaultDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        updateDefaultDay();
    }
    public void updateAdapter(int month, int year){
        List<String> days = new ArrayList<>();
        Calendar calendar=Calendar.getInstance();
        calendar.clear();
        calendar.set(calendar.MONTH,month);
        calendar.set(calendar.YEAR,year);
        maxDay=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int day = MIN_DAY_DEFAULT; day <= maxDay; day += daysStep) {
            days.add(getFormattedValue(day));
        }
        adapter.setData(days);
        notifyDatasetChanged();

    }

    @Override
    protected void onItemSelected(int position, Object item) {
        if (daysSelectedListener != null) {
            daysSelectedListener.onDaySelected(this, position, convertItemToDay(item));

        }
    }

    @Override
    protected void onItemCurrentScroll(int position, Object item) {
        if (daysSelectedListener != null) {
            daysSelectedListener.onDayCurrentScrolled(this, position, convertItemToDay(item));
        }

        if (lastScrollPosition != position) {
            daysSelectedListener.onDayCurrentScrolled(this, position, convertItemToDay(item));
            if (lastScrollPosition == maxDay && position == 0)
                if (daysSelectedListener != null) {
                    daysSelectedListener.onDayCurrentNewMonth(this);
                }
            lastScrollPosition = position;
        }
    }

    @Override
    public int  findIndexOfDate(Date date) {
        final int days = date.getDate();
        //return super.findIndexOfDate(date);
        final int itemCount = adapter.getItemCount();
        for (int i = 0; i < itemCount; ++i) {
            final String object = adapter.getItemText(i);
            final Integer value = Integer.valueOf(object);
            if (days < value) {
                return i - 1;
            }else if (i==itemCount-1 && days==value) {
                return i;
            }
        }
        return 0;

    }

    protected String getFormattedValue(Object value) {
        Object valueItem = value;
        if (value instanceof Date) {
            Calendar instance = Calendar.getInstance();
            instance.setTime((Date) value);
            valueItem = instance.get(Calendar.DAY_OF_MONTH);
        }
        return String.format(getCurrentLocale(), FORMAT, valueItem);
    }

    private void updateDefaultDay() {
        setSelectedItemPosition(defaultDay);
    }
     //Adapter update according to default date
    public void setDefaultDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        maxDay= Integer.parseInt(String.valueOf(cal.getActualMaximum(Calendar.DATE)));
        initAdapter();
    }

    @Override
    public int getDefaultItemPosition() {
        return defaultDay;
    }

    public void setOnDaySelectedListener(OnDaySelectedListener daysSelectedListener) {
        this.daysSelectedListener = daysSelectedListener;
    }

    public void setDefaultDay(int day) {
        defaultDay = day;
        updateDefaultDay();
    }


    public void setMaxDay(int maxDay) {
        if (maxDay >= MIN_DAY_DEFAULT && maxDay <= maxDay) {
            this.maxDay = maxDay;
        }
        initAdapter();
    }

    public void setMinDay(int minDay) {
        if (minDay >= MIN_DAY_DEFAULT && minDay <= maxDay) {
            this.minDay = minDay;
        }
        initAdapter();
    }

    public void setDayStep(int daysStep) {
        if (daysStep >= MIN_DAY_DEFAULT && daysStep <= maxDay) {
            this.daysStep = daysStep;
        }
        initAdapter();
    }

    private int convertItemToDay(Object item) {
        Integer day = Integer.valueOf(String.valueOf(item));

        return day;
    }

    public int getCurrentDay() {
        return convertItemToDay(adapter.getItem(getCurrentItemPosition()));
    }

    public interface OnDaySelectedListener {
        void onDaySelected(WheelOnlyDayPicker picker, int position, int days);

        void onDayCurrentScrolled(WheelOnlyDayPicker picker, int position, int days);

        void onDayCurrentNewMonth(WheelOnlyDayPicker picker);
    }
    public void maxDateOfMonth(){
        Calendar cal = Calendar.getInstance();
        maxDay= Integer.parseInt(String.valueOf(cal.getActualMaximum(Calendar.DATE)));

    }

}
