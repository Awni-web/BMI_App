package com.example.bmiProject;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePick extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calender = Calendar.getInstance();
        int hour = calender.get(Calendar.HOUR_OF_DAY);
        int minute = calender.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(),
                (TimePickerDialog.OnTimeSetListener) getActivity(),
                hour,
                minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}
