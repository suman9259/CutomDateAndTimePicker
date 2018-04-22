package com.example.dateandtimepicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.dateandtimepicker.widget.WheelAmPmPicker;
import com.example.dateandtimepicker.widget.WheelHourPicker;
import com.example.dateandtimepicker.widget.WheelMinutePicker;
import com.example.dateandtimepicker.widget.WheelPicker;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimePicker extends LinearLayout {

    public static final boolean IS_CYCLIC_DEFAULT = true;
    public static final boolean IS_CURVED_DEFAULT = false;
    public static final boolean MUST_BE_ON_FUTUR_DEFAULT = false;
    public static final int DELAY_BEFORE_CHECK_PAST = 200;
    private static final int VISIBLE_ITEM_COUNT_DEFAULT = 7;
    private static final int PM_HOUR_ADDITION = 12;
    private int lastHour=0;

    private static final CharSequence FORMAT_24_HOUR = "yyyy mm d HH:mm";
    private static final CharSequence FORMAT_12_HOUR = "yyyy mm d  h:mm a";

    private WheelMinutePicker minutesPicker;
    private WheelHourPicker hoursPicker;
    private WheelAmPmPicker amPmPicker;

    int minutes;
    int hour;

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
    private Date defaultDate;
    private int minutesStep;

    private boolean isAmPm;
    private int selectorHeight;

    int lastHrScrollPrevPos;
    int lastHrScrollCurrPos;
    int lastHrScrollDir;

    public TimePicker(Context context) {
        this(context, null);
    }

    public TimePicker(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        inflate(context, R.layout.time_picker, this);

        isAmPm = !(DateFormat.is24HourFormat(context));

        minutesPicker = (WheelMinutePicker) findViewById(R.id.tp_minutesPicker);
        hoursPicker = (WheelHourPicker) findViewById(R.id.tp_hoursPicker);
        amPmPicker = (WheelAmPmPicker) findViewById(R.id.tp_amPmPicker);
        dtSelector = findViewById(R.id.tp_time_Selector);

        final ViewGroup.LayoutParams dtSelectorLayoutParams = dtSelector.getLayoutParams();
        dtSelectorLayoutParams.height = selectorHeight;
        dtSelector.setLayoutParams(dtSelectorLayoutParams);


        minutesPicker.setOnMinuteSelectedListener(new WheelMinutePicker.OnMinuteSelectedListener() {
            @Override
            public void onMinuteSelected(WheelMinutePicker picker, int position, int minutes) {
                updateListener();
                checkMinMaxDate(picker);
            }

            @Override
            public void onMinuteCurrentScrolled(WheelMinutePicker picker, int position, int minutes) {

            }

            @Override
            public void onMinuteScrolledNewHour(WheelMinutePicker picker) {
                    hoursPicker.scrollTo(hoursPicker.getCurrentItemPosition() + 1);


            }
        });

        hoursPicker.setOnHourSelectedListener(new WheelHourPicker.OnHourSelectedListener() {
            @Override
            public void onHourSelected(final WheelHourPicker picker, int position, int hours) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateListener();
                        checkMinMaxDate(picker);
                    }
                }, 300);
            }

            @Override
            public void onHourCurrentScrolled(WheelHourPicker picker, int position, int hours) {
                picker.AddScrollTrail(position);
                if (picker.isScroll_Done()) {

                    List<Integer> posdir = picker.GetDirectionAndPos();

                    picker.ClearScrollTrail();
                    int prevPos = posdir.get(0);
                    int currPos = posdir.get(1);
                    // Some time currPos is negative index (convert that to positive index)
                    int positiveCurrPos = currPos;
                    if (currPos < 0) {
                        positiveCurrPos = 12 + currPos;
                    }
                    int dir = posdir.get(2);
                    String logger = "";
                    logger += prevPos + "->" + positiveCurrPos + " (" + currPos + ") Dir = " + dir;
                    Log.i("SCROLLTRACK", logger);
                    if (lastHrScrollPrevPos == prevPos && lastHrScrollCurrPos == currPos && dir == 0) {
                        //skip checking - this is a spurious call
                    } else {
                        if (dir == 1) {
                            if (prevPos > positiveCurrPos) {
                                if (amPmPicker.isAm()) {
                                    amPmPicker.scrollTo(WheelAmPmPicker.INDEX_PM);
                                } else {
                                    amPmPicker.scrollTo(WheelAmPmPicker.INDEX_AM);
                                }
                            }
                        } else {
                            if (prevPos < positiveCurrPos) {
                                if (amPmPicker.isAm()) {
                                    amPmPicker.scrollTo(WheelAmPmPicker.INDEX_PM);
                                } else {
                                    amPmPicker.scrollTo(WheelAmPmPicker.INDEX_AM);
                                }
                            }
                        }
                        lastHrScrollCurrPos = currPos;
                        lastHrScrollPrevPos = prevPos;
                        lastHrScrollDir = dir;
                    }
                }

            }

            @Override
            public void onHourCurrentNewDay(WheelHourPicker picker) {


            }
        });

        amPmPicker.setOnAmPmSelectedListener(new WheelAmPmPicker.OnAmPmSelectedListener() {
            @Override
            public void onAmSelected(WheelAmPmPicker picker) {
                updateListener();
                checkMinMaxDate(picker);
            }

            @Override
            public void onPmSelected(WheelAmPmPicker picker) {
                updateListener();
                checkMinMaxDate(picker);
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

    public void setIsAmPm(boolean isAmPm) {
        this.isAmPm = isAmPm;
        updateViews();
    }

    public boolean isAmPm() {
        return isAmPm;
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

    public Date getDefoaltDate(){return defaultDate;}

    public  void setDefoaltDate(Date defoaltDate){this.defaultDate=defoaltDate;}

    private void updatePicker() {
        if (minutesPicker != null && hoursPicker != null && amPmPicker != null) {
            for (WheelPicker wheelPicker : Arrays.asList(minutesPicker, hoursPicker, amPmPicker)) {
                wheelPicker.setItemTextColor(textColor);
                wheelPicker.setSelectedItemTextColor(selectedTextColor);
                wheelPicker.setItemTextSize(textSize);
                wheelPicker.setVisibleItemCount(visibleItemCount);
                wheelPicker.setCurved(isCurved);
                if (wheelPicker != amPmPicker) {
                    wheelPicker.setCyclic(isCyclic);
                }
            }
        }

        if (amPmPicker != null) {
            amPmPicker.setVisibility(isAmPm ? VISIBLE : GONE);
        }
        if (hoursPicker != null) {
            hoursPicker.setIsAmPm(isAmPm);
        }
    }

    private void updateViews() {
        dtSelector.setBackgroundColor(selectorColor);
    }

    private void checkMinMaxDate(final WheelPicker picker){
        checkBeforeMinDate(picker);
        checkAfterMaxDate(picker);
    }

    private void checkBeforeMinDate(final WheelPicker picker) {
        picker.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (minDate != null && isBeforeMinDate(getDate())) {

                    Date temp_minDate=new Date(minDate.getTime());
                    //scroll to Min position

                    minutesPicker.scrollTo(minutesPicker.findIndexOfDate(temp_minDate));
                    hoursPicker.scrollTo(hoursPicker.findIndexOfDate(temp_minDate));
                    //amPmPicker.scrollTo(amPmPicker.findIndexOfDate(temp_minDate));
                    Calendar calendar=Calendar.getInstance();
                    calendar.setTime(minDate);
                    int amORpm=calendar.get(Calendar.AM_PM);
                    if (amORpm==Calendar.AM){
                        amPmPicker.setAmSelected();
                    }
                    else amPmPicker.setPmSelected();



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
                    Date temp_maxDate=new Date(maxDate.getTime());

                    minutesPicker.scrollTo(minutesPicker.findIndexOfDate(temp_maxDate));
                    hoursPicker.scrollTo(hoursPicker.findIndexOfDate(temp_maxDate));
                    Calendar calendar=Calendar.getInstance();
                    calendar.setTime(maxDate);
                    int amORpm=calendar.get(Calendar.AM_PM);
                    if (amORpm==Calendar.AM){
                        amPmPicker.setAmSelected();
                    }
                    else amPmPicker.setPmSelected();
                    //amPmPicker.scrollTo(amPmPicker.findIndexOfDate(temp_maxDate));

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
        int hour = hoursPicker.getCurrentHour();
        if (isAmPm && amPmPicker.isPm()) {
            hour += PM_HOUR_ADDITION;
        }
        final int minute = minutesPicker.getCurrentMinute();

        final Calendar calendar = Calendar.getInstance();

            calendar.setTime(defaultDate);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

        final Date time = calendar.getTime();
        return time;
    }

    public void setStepMinutes(int minutesStep) {
        minutesPicker.setStepMinutes(minutesStep);
    }

    public void setHoursStep(int hoursStep) {
        hoursPicker.setHoursStep(hoursStep);
    }

    public void selectDate(Calendar calendar) {
        if (calendar == null) {
            return;
        }
        Date date = calendar.getTime();

        int indexOfHour = hoursPicker.findIndexOfDate(date);
        if (indexOfHour != -1) {
            if (isAmPm) {
                if(calendar.get(Calendar.HOUR_OF_DAY) >= WheelHourPicker.MAX_HOUR_AM_PM) {
                    amPmPicker.setPmSelected();
                } else {
                    amPmPicker.setAmSelected();
                }
            }
            hoursPicker.setSelectedItemPosition(indexOfHour);
        }
        int indexOfMin = minutesPicker.findIndexOfDate(date);
        if (indexOfMin != -1) {
            minutesPicker.setSelectedItemPosition(indexOfMin);
        }
    }

    private void updateListener() {
        final Date date = getDate();
        CharSequence format = isAmPm ? FORMAT_12_HOUR : FORMAT_24_HOUR;
        String displayed = DateFormat.format(format, date).toString();
        if (listener != null) {
            listener.onDateChanged(displayed, date);
        }
    }

    public void setMustBeOnFuture(boolean mustBeOnFuture) {
        this.mustBeOnFuture = mustBeOnFuture;
        if(mustBeOnFuture){
            minDate = Calendar.getInstance().getTime(); //minDate is Today
        }
    }

    public int getMinutesStep(){
        return this.minutesStep;
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
}
