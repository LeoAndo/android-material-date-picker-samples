package com.example.materialdatepickerjavasample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.datepicker.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private var datePicker: MaterialDatePicker<Long>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup: default opening date & selection date
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = today
        calendar.roll(Calendar.YEAR, -20)
        val defaultSelectionTimeInMillis = calendar.timeInMillis
        calendar.clear()

        // setup: validation
        calendar.timeInMillis = today
        calendar.roll(Calendar.YEAR, -100)
        val lowerBound = calendar.timeInMillis

        // Show a MaterialDatePicker when the button is clicked.
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener { v: View? ->
            val dateTimeInMillis = datePicker?.selection ?: defaultSelectionTimeInMillis
            // setup: CalendarConstraints.Builder
            val calendarConstraints =
                setupConstraintsBuilder(dateTimeInMillis, lowerBound, today)
            // show MaterialDatePicker.
            showMaterialDatePicker(calendarConstraints, dateTimeInMillis)
        }
    }

    /**
     * set to limit the display range of the calendar.
     *
     * @param openingDateTimeInMillis default opening Date (A UTC time In Milliseconds)
     * @param lowerBound              Lower limit setting of selection range (A UTC time In Milliseconds)
     * @param higherBound             Higher limit setting of selection range (A UTC time In Milliseconds)
     * @return Constraints for calender. [CalendarConstraints.Builder]
     */
    private fun setupConstraintsBuilder(
        openingDateTimeInMillis: Long,
        lowerBound: Long,
        higherBound: Long
    ): CalendarConstraints.Builder {
        val validators: MutableList<CalendarConstraints.DateValidator> = mutableListOf()
        validators.add(DateValidatorPointForward.from(lowerBound))
        validators.add(DateValidatorPointBackward.now())
        return CalendarConstraints.Builder()
            .setOpenAt(openingDateTimeInMillis)  // setup: opening Date.
            .setStart(lowerBound)
            .setEnd(higherBound)
            .setValidator(CompositeDateValidator.allOf(validators)) // setup: validation.
    }

    /**
     * show [MaterialDatePicker].
     *
     * @param calendarConstraints          Constraints for calender.
     * @param defaultSelectionTimeInMillis section
     */
    private fun showMaterialDatePicker(
        calendarConstraints: CalendarConstraints.Builder,
        defaultSelectionTimeInMillis: Long
    ) {
        datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(calendarConstraints.build())
            .setTitleText("Select date")
            .setSelection(defaultSelectionTimeInMillis)
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build()

        val datePicker = datePicker ?: return

        datePicker.show(supportFragmentManager, "tag_date_picker")

        datePicker.addOnPositiveButtonClickListener { section ->
            showToast("Respond to positive button click: " + datePicker.headerText)
            val headerText = findViewById<TextView>(R.id.headerText)
            headerText.text = datePicker.headerText
            val formatDateText = findViewById<TextView>(R.id.formatdateText)
            val text = DateFormat.format("yyyy/MM/dd", section)
            formatDateText.text = text
        }
        datePicker.addOnNegativeButtonClickListener {
            showToast("Respond to negative button click!")
        }
        datePicker.addOnCancelListener {
            showToast("Respond to cancel button click!")
        }
        datePicker.addOnDismissListener {
            showToast("Respond to dismiss events!")
        }
    }

    /**
     * show Toast message.
     *
     * @param message Toast message
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}