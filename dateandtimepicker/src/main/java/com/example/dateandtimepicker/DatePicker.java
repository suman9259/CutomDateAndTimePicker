package com.example.dateandtimepicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.dateandtimepicker.widget.WheelMonthPicker;
import com.example.dateandtimepicker.widget.WheelOnlyDayPicker;
import com.example.dateandtimepicker.widget.WheelPicker;
import com.example.dateandtimepicker.widget.WheelYearPicker;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by $uman on 09-03-2017.
 */

public class DatePicker extends LinearLayout {

    public static final boolean IS_CYCLIC_DEFAULT = true;
    public static final boolean IS_CURVED_DEFAULT = false;
    public static final boolean MUST_BE_ON_FUTUR_DEFAULT = false;
    public static final int DELAY_BEFORE_CHECK_PAST = 200;
    private static final int VISIBLE_ITEM_COUNT_DEFAULT = 7;

    private static final CharSequence DATE_FORMAT = "dd MMMM yyyy";

    private WheelOnlyDayPicker daysPicker;
    private WheelMonthPicker monthsPicker;
    private WheelYearPicker yearPicker;

    int year;
    int month;
    int day;
    Date date_for_days;

    private Listener listener;

    private int textColor;
    private int selectedTextColor;
    private int textSize;
    private int selectorColor;
    private boolean isCyclic;
    private boolean isCurved;
    private int visibleItemCount;
    private View dtSelector;
    private boolean mustBeOnFuture;

    private Date minDate;
    private Date maxDate;

    private int selectorHeight;

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        inflate(context, R.layout.day_picker, this);


        daysPicker = (WheelOnlyDayPicker) findViewById(R.id.dp_dayPicker);
        monthsPicker = (WheelMonthPicker) findViewById(R.id.dp_monthPicker);
        yearPicker = (WheelYearPicker) findViewById(R.id.dp_yearPicker);
        dtSelector = findViewById(R.id.dp_day_Selector);

        final ViewGroup.LayoutParams dtSelectorLayoutParams = dtSelector.getLayoutParams();
        dtSelectorLayoutParams.height = selectorHeight;
        dtSelector.setLayoutParams(dtSelectorLayoutParams);

        daysPicker.setOnDaySelectedListener(new WheelOnlyDayPicker.OnDaySelectedListener() {


            @Override
            public void onDaySelected(WheelOnlyDayPicker picker, int position, int days) {
                updateListener();
                checkMinMaxDate(picker);
                day = days;
            }

            @Override
            public void onDayCurrentScrolled(WheelOnlyDayPicker picker, int position, int days) {

            }

            @Override
            public void onDayCurrentNewMonth(WheelOnlyDayPicker picker) {
                daysPicker.scrollTo(daysPicker.getCurrentItemPosition() + 1);

            }
        });

        monthsPicker.setOnMonthSelectedListener(new WheelMonthPicker.OnMonthSelectedListener() {


            @Override
            public void onMonthSelected(WheelMonthPicker picker, int position, String name, Date date) {
                updateListener();
                checkMinMaxDate(picker);
                month = getCurrentMonth(name);
                daysPicker.updateAdapter(month, year);
            }

            @Override
            public void onMonthCurrentScrolled(WheelMonthPicker picker, int position, String name) {
                if (picker.isScroll_Done()) {
                    month = getCurrentMonth(name);
                    daysPicker.updateAdapter(month, year);
                    isDayScrollOnLastDay(name);
                }
            }

            @Override
            public void onMonthCurrentNewYear(WheelMonthPicker picker) {
                yearPicker.scrollTo(yearPicker.getCurrentItemPosition() + 1);

            }
        });

        yearPicker.setOnYearSelectedListener(new WheelYearPicker.OnYearSelectedListener() {
            @Override
            public void onYearSelected(WheelYearPicker picker, int position, String name, Date date) {
                updateListener();
                checkMinMaxDate(picker);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                year = calendar.get(Calendar.YEAR);
                daysPicker.updateAdapter(month, year);
            }

            @Override
            public void onYearCurrentScroll(WheelYearPicker picker, int position, int date) {
                if (picker.isScroll_Done()) {
                    boolean isLeapYear = ((date % 4 == 0) && (date % 100 != 0) || (date % 400 == 0));
                    if (!isLeapYear) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.MONTH, month);
                        int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        if (maxDayOfMonth < day) {
                            daysPicker.scrollTo(maxDayOfMonth - 1);
                            day = maxDayOfMonth;
                        }
                    }
                }
                year = date;

            }
        });

        updatePicker();
        updateViews();
    }

    public void setCurved(boolean curved) {
        isCurved = curved;
        updatePicker();
    }

    public void setCyclic(boolean cyclic) {
        isCyclic = cyclic;
        updatePicker();
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        updatePicker();
    }

    public void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
        updatePicker();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        updatePicker();
    }

    public void setSelectorColor(int selectorColor) {
        this.selectorColor = selectorColor;
        updateViews();
    }

    public void setVisibleItemCount(int visibleItemCount) {
        this.visibleItemCount = visibleItemCount;
        updatePicker();
    }


    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    private void updatePicker() {
        if (daysPicker != null && monthsPicker != null && yearPicker != null) {
            for (WheelPicker wheelPicker : Arrays.asList(daysPicker, monthsPicker, yearPicker)) {
                wheelPicker.setItemTextColor(textColor);
                wheelPicker.setSelectedItemTextColor(selectedTextColor);
                wheelPicker.setItemTextSize(textSize);
                wheelPicker.setVisibleItemCount(visibleItemCount);
                wheelPicker.setCurved(isCurved);
                wheelPicker.setCyclic(isCyclic);
            }
        }

    }

    private void updateViews() {
        dtSelector.setBackgroundColor(selectorColor);
    }

    private void checkMinMaxDate(final WheelPicker picker) {
        checkBeforeMinDate(picker);
        checkAfterMaxDate(picker);
    }

    private void checkBeforeMinDate(final WheelPicker picker) {
        picker.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (minDate != null && isBeforeMinDate(getDate())) {
                    //scroll to Min position
                    daysPicker.scrollTo(daysPicker.findIndexOfDate(minDate));
                    monthsPicker.scrollTo(monthsPicker.findIndexOfDate(minDate));
                    yearPicker.scrollTo(yearPicker.findIndexOfDate(minDate));

                }
            }
        }, DELAY_BEFORE_CHECK_PAST);
    }

    private void checkAfterMaxDate(final WheelPicker picker) {
        picker.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (maxDate != null && isAfterMaxDate(getDate())) {
                    //scroll to Max position
                    daysPicker.scrollTo(daysPicker.findIndexOfDate(maxDate));
                    monthsPicker.scrollTo(monthsPicker.findIndexOfDate(maxDate));
                    yearPicker.scrollTo(yearPicker.findIndexOfDate(maxDate));
                }
            }
        }, DELAY_BEFORE_CHECK_PAST);
    }

    private boolean isBeforeMinDate(Date date) {
        final Calendar minDateCalendar = Calendar.getInstance();
        minDateCalendar.setTime(minDate);
        minDateCalendar.set(Calendar.MILLISECOND, 0);
        minDateCalendar.set(Calendar.SECOND, 0);

        final Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        dateCalendar.set(Calendar.MILLISECOND, 0);
        dateCalendar.set(Calendar.SECOND, 0);

        return dateCalendar.before(minDateCalendar);
    }

    private boolean isAfterMaxDate(Date date) {
        final Calendar maxDateCalendar = Calendar.getInstance();
        maxDateCalendar.setTime(maxDate);
        maxDateCalendar.set(Calendar.MILLISECOND, 0);
        maxDateCalendar.set(Calendar.SECOND, 0);

        final Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        dateCalendar.set(Calendar.MILLISECOND, 0);
        dateCalendar.set(Calendar.SECOND, 0);

        return dateCalendar.after(maxDateCalendar);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Date getDate() {
        final int day = daysPicker.getCurrentDay();
        final String month = monthsPicker.getCurrentMonth();
        final String year = yearPicker.getCurrentYear();

        final Calendar calendar = Calendar.getInstance();

        calendar.set(calendar.DAY_OF_MONTH, day);
        calendar.set(calendar.MONTH, getCurrentMonth(month));
        Log.e("Month", month);
        calendar.set(calendar.YEAR, Integer.parseInt(year));


        final Date time = calendar.getTime();

        return time;
    }

    public void selectDate(Calendar calendar) {
        if (calendar == null) {
            return;
        }
        Date date = calendar.getTime();
        daysPicker.setDefaultDate(date);
        // day 31 condition
        date_for_days = date;
        updated_day();

        int indexOfMonth = monthsPicker.findIndexOfDate(date);

        int indexOfDay = daysPicker.findIndexOfDate(date);
        if (indexOfDay != -1) {
            daysPicker.setSelectedItemPosition(indexOfDay);
        }

        monthsPicker.setSelectedItemPosition(indexOfMonth);

        int indexOfYear = yearPicker.findIndexOfDate(date);
        yearPicker.setSelectedItemPosition(indexOfYear);
    }

    private void updateListener() {
        final Date date = getDate();
        Log.e("DAte", String.valueOf(date));
        String displayed = DateFormat.format(DATE_FORMAT, date).toString();
        if (listener != null) {
            listener.onDateChanged(displayed, date);
        }
    }

    public void setMustBeOnFuture(boolean mustBeOnFuture) {
        this.mustBeOnFuture = mustBeOnFuture;
        if (mustBeOnFuture) {
            minDate = Calendar.getInstance().getTime(); //minDate is Today
        }
    }

    public boolean mustBeOnFuture() {
        return mustBeOnFuture;
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DateAndTimePicker);

        final Resources resources = getResources();
        textColor = a.getColor(R.styleable.DateAndTimePicker_picker_textColor,
                resources.getColor(R.color.picker_default_text_color));
        selectedTextColor = a.getColor(R.styleable.DateAndTimePicker_picker_selectedTextColor,
                resources.getColor(R.color.picker_default_selected_text_color));
        selectorColor = a.getColor(R.styleable.DateAndTimePicker_picker_selectorColor,
                resources.getColor(R.color.picker_default_selector_color));
        selectorHeight = a.getDimensionPixelSize(R.styleable.DateAndTimePicker_picker_selectorHeight, resources.getDimensionPixelSize(R.dimen.wheelSelectorHeight));
        textSize = a.getDimensionPixelSize(R.styleable.DateAndTimePicker_picker_textSize,
                resources.getDimensionPixelSize(R.dimen.WheelItemTextSize));
        isCurved = a.getBoolean(R.styleable.DateAndTimePicker_picker_curved, IS_CURVED_DEFAULT);
        isCyclic = a.getBoolean(R.styleable.DateAndTimePicker_picker_cyclic, IS_CYCLIC_DEFAULT);
        mustBeOnFuture = a.getBoolean(R.styleable.DateAndTimePicker_picker_mustBeOnFuture, MUST_BE_ON_FUTUR_DEFAULT);
        visibleItemCount = a.getInt(R.styleable.DateAndTimePicker_picker_visibleItemCount, VISIBLE_ITEM_COUNT_DEFAULT);

        a.recycle();
    }

    public interface Listener {
        void onDateChanged(String displayed, Date date);
    }

    public int getCurrentMonth(String month) {
        int month_int = 0;
        if (month.equals("January"))
            month_int = 0;
        else if (month.equals("February"))
            month_int = 1;
        else if (month.equals("March"))
            month_int = 2;
        else if (month.equals("April"))
            month_int = 3;
        else if (month.equals("May"))
            month_int = 4;
        else if (month.equals("June"))
            month_int = 5;
        else if (month.equals("July"))
            month_int = 6;
        else if (month.equals("August"))
            month_int = 7;
        else if (month.equals("September"))
            month_int = 8;
        else if (month.equals("October"))
            month_int = 9;
        else if (month.equals("November"))
            month_int = 10;
        else if (month.equals("December"))
            month_int = 11;

        return month_int;
    }

    public void isDayScrollOnLastDay(String name) {
        Calendar calendar = Calendar.getInstance();
        month = getCurrentMonth(name);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (maxDayOfMonth < day) {
            daysPicker.scrollTo(maxDayOfMonth - 1);
            day = maxDayOfMonth;
        }
    }

    public void updated_day() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date_for_days);
        year = calendar1.get(Calendar.YEAR);
        day = calendar1.get(Calendar.DAY_OF_MONTH);
        month = calendar1.get(Calendar.MONTH);
    }


}
