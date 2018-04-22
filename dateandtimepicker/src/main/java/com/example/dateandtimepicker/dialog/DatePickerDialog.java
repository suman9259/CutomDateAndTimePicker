package com.example.dateandtimepicker.dialog;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.dateandtimepicker.DatePicker;
import com.example.dateandtimepicker.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by $uman on 09-03-2017.
 */

public class DatePickerDialog extends BaseDialog {

    private Listener listener;
    private BottomSheetHelper bottomSheetHelper;
    private DatePicker pickerDay;

    @Nullable
    private String title;

    private DatePickerDialog(Context context) {
        this(context, false);
    }

    private DatePickerDialog(Context context, boolean bottomSheet) {
        final int layout = bottomSheet ? R.layout.bottom_sheet_picker_bottom_sheet_day :
                R.layout.bottom_sheet_picker_day;
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
                DatePickerDialog.this.onClose();
            }
        });
    }


    private void init(View view) {
        pickerDay=(DatePicker)view.findViewById(R.id.bspbsd_date_picker);
        final TextView buttonOk = (TextView) view.findViewById(R.id.day_buttonOk);
        final TextView buttonCancel = (TextView) view.findViewById(R.id.day_buttonCancel);
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

        final TextView titleTextViewn = (TextView) view.findViewById(R.id.day_Title);
        if (titleTextViewn != null) {
            titleTextViewn.setText(title);

        }


        final View pickerTitleHeader = view.findViewById(R.id.pickerTitleHeader);
        if (mainColor != null && pickerTitleHeader != null) {
            pickerTitleHeader.setBackgroundColor(mainColor);
        }

        if (curved) {
            pickerDay.setCurved(true);
            pickerDay.setVisibleItemCount(7);
        } else {
            pickerDay.setCurved(false);
            pickerDay.setVisibleItemCount(5);
        }
        pickerDay.setMustBeOnFuture(mustBeOnFuture);


        if (mainColor != null) {
            pickerDay.setSelectedTextColor(mainColor);
        }

        if (minDate != null) {
            pickerDay.setMinDate(minDate);
        }

        if (maxDate != null) {
            pickerDay.setMaxDate(maxDate);
        }

        if (defaultDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(defaultDate);
            pickerDay.selectDate(calendar);
        }
    }

    public DatePickerDialog setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public DatePickerDialog setCurved(boolean curved) {
        this.curved = curved;
        return this;
    }


    public DatePickerDialog setTitle(@Nullable String title) {
        this.title = title;
        return this;
    }

    public DatePickerDialog setMustBeOnFuture(boolean mustBeOnFuture) {
        this.mustBeOnFuture = mustBeOnFuture;
        return this;
    }

    public DatePickerDialog setMinDateRange(Date minDate) {
        this.minDate = minDate;
        return this;
    }

    public DatePickerDialog setMaxDateRange(Date maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    public DatePickerDialog setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
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
            listener.onDateSelected(pickerDay.getDate());
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
        private DatePickerDialog dialog;

        @Nullable
        private Listener listener;

        @Nullable
        private String title;

        private boolean bottomSheet;

        private boolean curved;
        private boolean mustBeOnFuture;

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


        public DatePickerDialog build() {
            final DatePickerDialog dialog = new DatePickerDialog(context, bottomSheet)
                    .setTitle(title)
                    .setListener(listener)
                    .setCurved(curved)
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
