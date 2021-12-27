package com.example.materialdatepickerjavasample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private MaterialDatePicker<Long> datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup: default opening date & selection date
        final long today = MaterialDatePicker.todayInUtcMilliseconds();
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(today);
        calendar.roll(Calendar.YEAR, -20);
        final long defaultSelectionTimeInMillis = calendar.getTimeInMillis();

        calendar.clear();

        // setup: validation
        calendar.setTimeInMillis(today);
        calendar.roll(Calendar.YEAR, -100);
        final long lowerBound = calendar.getTimeInMillis();

        // Show a MaterialDatePicker when the button is clicked.
        final Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            // setup: CalendarConstraints.Builder
            final CalendarConstraints.Builder calendarConstraints =
                    setupConstraintsBuilder(defaultSelectionTimeInMillis, lowerBound);
            // show MaterialDatePicker.
            showMaterialDatePicker(calendarConstraints, defaultSelectionTimeInMillis);
        });
    }

    /**
     * set to limit the display range of the calendar.
     *
     * @param openingDateTimeInMillis default opening Date (A UTC time In Milliseconds)
     * @param lowerBound              Lower limit setting of selection range (A UTC time In Milliseconds)
     * @return Constraints for calender. {@link CalendarConstraints.Builder}
     */
    private CalendarConstraints.Builder setupConstraintsBuilder(long openingDateTimeInMillis, long lowerBound) {
        if (datePicker != null && datePicker.getSelection() != null) {
            openingDateTimeInMillis = datePicker.getSelection();
        }

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        // setup: default opening Date.
        constraintsBuilder.setOpenAt(openingDateTimeInMillis);
        // setup: validation.
        constraintsBuilder.setValidator(DateValidatorPointForward.from(lowerBound));
        return constraintsBuilder;
    }

    /**
     * show {@link MaterialDatePicker}.
     *
     * @param calendarConstraints          Constraints for calender.
     * @param defaultSelectionTimeInMillis section
     */
    private void showMaterialDatePicker(
            CalendarConstraints.Builder calendarConstraints,
            long defaultSelectionTimeInMillis) {

        if (datePicker != null && datePicker.getSelection() != null) {
            defaultSelectionTimeInMillis = datePicker.getSelection();
        }

        datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setCalendarConstraints(calendarConstraints.build())
                        .setTitleText("Select date")
                        .setSelection(defaultSelectionTimeInMillis)
                        .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                        .build();
        datePicker.show(getSupportFragmentManager(), "tag_date_picker");

        datePicker.addOnPositiveButtonClickListener(section -> {
            showToast("Respond to positive button click: " + datePicker.getHeaderText());

            final TextView headerText = findViewById(R.id.headerText);
            headerText.setText(datePicker.getHeaderText());

            final TextView formatDateText = findViewById(R.id.formatdateText);
            final CharSequence text = DateFormat.format("yyyy/MM/dd", section);
            formatDateText.setText(text);
        });
        datePicker.addOnNegativeButtonClickListener(view -> {
            showToast("Respond to negative button click!");
        });
        datePicker.addOnCancelListener(dialog -> {
            showToast("Respond to cancel button click!");
        });
        datePicker.addOnDismissListener(dialog -> {
            showToast("Respond to dismiss events!");
        });
    }

    /**
     * show Toast message.
     *
     * @param message Toast message
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}