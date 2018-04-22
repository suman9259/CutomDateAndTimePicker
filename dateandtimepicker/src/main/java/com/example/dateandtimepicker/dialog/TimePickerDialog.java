package com.example.dateandtimepicker.dialog;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.dateandtimepicker.R;
import com.example.dateandtimepicker.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by $uman on 08-03-2017.
 */

public class TimePickerDialog extends BaseDialog{
    private Listener listener;
    private BottomSheetHelper bottomSheetHelper;
    private TimePicker pickerTime;
    @Nullable
    private String title;

    private TimePickerDialog(Context context){
        this(context, false);
    }
    private TimePickerDialog(Context context, Boolean bottomSheet){
        final int layout = bottomSheet ? R.layout.bottom_sheet_picker_bottom_sheet_time :
                R.layout.bottom_sheet_picker_time;
        this.bottomSheetHelper = new BottomSheetHelper(context, layout);

        this.bottomSheetHelper.setListener(new BottomSheetHelper.Listener() {
            @Override
            public void onOpen() {
            }

            @Override
            public void onLoaded(View view) {
                init(view);
            }

            @Override
            public void onClose() {
                TimePickerDialog.this.onClose();
            }
        });

    }
    private void init(View view) {
        pickerTime = (TimePicker) view.findViewById(R.id.bspt_picker_time);

        final TextView buttonOk = (TextView) view.findViewById(R.id.time_buttonOk);
        final TextView buttonCancel = (TextView) view.findViewById(R.id.time_buttonCancel);
        if (buttonOk != null) {
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    okClicked = true;
                    close();
                }
            });

            /*if (mainColor != null) {
                buttonOk.setTextColor(mainColor);
            }*/
        }
        if (buttonCancel != null) {
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    okClicked=false;
                    close();
                }
            });

            /*if (mainColor != null) {
                buttonCancel.setTextColor(mainColor);
            }*/
        }

        final View sheetContentLayout = view.findViewById(R.id.sheetContentLayout);
        if (sheetContentLayout != null) {
            sheetContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            if (backgroundColor != null) {
                sheetContentLayout.setBackgroundColor(backgroundColor);
            }
        }

        final TextView titleTextView = (TextView) view.findViewById(R.id.sheetTitle);
        if (titleTextView != null) {
            titleTextView.setText("Set Time");

            if (titleTextColor != null) {
                titleTextView.setTextColor(titleTextColor);
            }
        }


        final TextView titleTextViewn = (TextView) view.findViewById(R.id.time_Title);
        if (titleTextViewn != null) {
            titleTextViewn.setText(title);

        }

        final View pickerTitleHeader = view.findViewById(R.id.pickerTitleHeader);
        if (mainColor != null && pickerTitleHeader != null) {
            pickerTitleHeader.setBackgroundColor(mainColor);
        }

            if (curved) {
                pickerTime.setCurved(true);
                pickerTime.setVisibleItemCount(7);
            } else {
                pickerTime.setCurved(false);
                pickerTime.setVisibleItemCount(5);
            }


        pickerTime.setMustBeOnFuture(mustBeOnFuture);

        pickerTime.setStepMinutes(minutesStep);

        if (mainColor != null) {
            pickerTime.setSelectedTextColor(mainColor);
        }

        if (minDate != null) {
            pickerTime.setMinDate(minDate);
        }

        if (maxDate != null) {
            pickerTime.setMaxDate(maxDate);
        }

        if (defaultDate != null) {
            pickerTime.setDefoaltDate(defaultDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(defaultDate);
            pickerTime.selectDate(calendar);
        }
    }

    public TimePickerDialog setListener(Listener listener) {
        this.listener = (Listener) listener;
        return this;
    }
    public TimePickerDialog setCurved(boolean curved) {
        this.curved = curved;
        return this;
    }
    public TimePickerDialog setMinutesStep(int minutesStep) {
        this.minutesStep = minutesStep;
        return this;
    }
    public TimePickerDialog setTitle(@Nullable String title) {
        this.title = title;
        return this;
    }

    public TimePickerDialog setMinDateRange(Date minDate) {
        this.minDate = minDate;
        return this;
    }

    public TimePickerDialog setMaxDateRange(Date maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    public TimePickerDialog setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
        return this;
    }
    public TimePickerDialog setMustBeOnFuture(boolean mustBeOnFuture) {
        this.mustBeOnFuture = mustBeOnFuture;
        return this;
    }

    @Override
    public void display() {
        super.display();
        bottomSheetHelper.display();
    }

    @Override
    public void close() {
        super.close();
        bottomSheetHelper.hide();

        if (listener != null && okClicked) {
            listener.onDateSelected(pickerTime.getDate());
        }else if (listener != null && !okClicked) {
            listener.onCancel();
        }
    }
    public interface Listener {
        void onDateSelected(Date date);
        void onCancel();
    }

    public static class Builder {
        private final Context context;
        private TimePickerDialog dialog;

        @Nullable
        private Listener listener;

        @Nullable
        private String title;

        private boolean bottomSheet;

        private boolean curved;
        private boolean mustBeOnFuture;
        private int minutesStep;

        @ColorInt
        @Nullable
        private Integer backgroundColor = null;

        @ColorInt
        @Nullable
        private Integer mainColor = null;

        @ColorInt
        @Nullable
        private Integer titleTextColor = null;

        @Nullable
        private Date minDate;
        @Nullable
        private Date maxDate;
        @Nullable
        private Date defaultDate;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder title(@Nullable String title) {
            this.title = title;
            return this;
        }

        public Builder bottomSheet() {
            this.bottomSheet = true;
            return this;
        }

        public Builder curved() {
            this.curved = true;
            return this;
        }

        public Builder mustBeOnFuture() {
            this.mustBeOnFuture = true;
            return this;
        }

        public Builder minutesStep(int minutesStep) {
            this.minutesStep = minutesStep;
            return this;
        }

        public Builder listener(@Nullable Listener listener) {
            this.listener = listener;
            return this;
        }

        public Builder titleTextColor(@NonNull @ColorInt int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder backgroundColor(@NonNull @ColorInt int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder mainColor(@NonNull @ColorInt int mainColor) {
            this.mainColor = mainColor;
            return this;
        }

        public Builder minDateRange(Date minDate) {
            this.minDate = minDate;
            return this;
        }

        public Builder maxDateRange(Date maxDate) {
            this.maxDate = maxDate;
            return this;
        }

        public Builder defaultDate(Date defaultDate) {
            this.defaultDate = defaultDate;
            return this;
        }


        public TimePickerDialog build() {
            final TimePickerDialog dialog = new TimePickerDialog(context, bottomSheet)
                    .setTitle(title)
                    .setListener(listener)
                    .setCurved(curved)
                    .setMinutesStep(minutesStep)
                    .setMaxDateRange(maxDate)
                    .setMinDateRange(minDate)
                    .setDefaultDate(defaultDate)
                    .setMustBeOnFuture(mustBeOnFuture);

            if (mainColor != null) {
                dialog.setMainColor(mainColor);
            }

            if (backgroundColor != null) {
                dialog.setBackgroundColor(backgroundColor);
            }

            if (titleTextColor != null) {
                dialog.setTitleTextColor(titleTextColor);
            }

            return dialog;
        }

        public void display() {
            dialog = build();
            dialog.display();
        }

        public void close() {
            if (dialog != null) {
                dialog.close();
            }
        }


    }

}
