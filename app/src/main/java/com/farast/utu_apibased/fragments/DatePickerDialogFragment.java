package com.farast.utu_apibased.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by cendr_000 on 17.08.2016.
 */

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DatePickerDialog.OnDateSetListener mListener;

    Date mDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        if (mDate != null)
            calendar.setTime(mDate);
        return new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        if (mListener != null)
            mListener.onDateSet(datePicker, i, i1, i2);
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.mListener = listener;
    }
}
