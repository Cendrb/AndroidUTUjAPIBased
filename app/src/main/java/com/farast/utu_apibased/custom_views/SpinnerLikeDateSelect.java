package com.farast.utu_apibased.custom_views;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.farast.utu_apibased.R;
import com.farast.utu_apibased.UnitsUtil;
import com.farast.utu_apibased.fragments.DatePickerDialogFragment;
import com.farast.utuapi.util.DateUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by cendr_000 on 18.08.2016.
 */

public class SpinnerLikeDateSelect extends TextView {
    Date mSelectedDate;
    FragmentManager mFragmentManager;
    String mLabelText;
    Paint mLabelTextPaint;
    OnDateSetListener mListener;

    public SpinnerLikeDateSelect(Context context, AttributeSet attrs) {
        super(context, attrs);

        mLabelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLabelTextPaint.setColor(ContextCompat.getColor(context, android.R.color.darker_gray));

        mLabelTextPaint.setTextSize(UnitsUtil.dpToPx(12));
        mLabelTextPaint.setTypeface(Typeface.DEFAULT);

        setPadding(UnitsUtil.dpToPx(3), UnitsUtil.dpToPx(20), UnitsUtil.dpToPx(3), UnitsUtil.dpToPx(10));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        setSelectedDate(calendar.getTime());

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SpinnerLikeDateSelect, 0, 0);
        try {
            String labelText = a.getString(R.styleable.SpinnerLikeDateSelect_labelText);
            if (labelText != null)
                mLabelText = labelText.toUpperCase();
        } finally {
            a.recycle();
        }

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogFragment dialogFragment = new DatePickerDialogFragment();
                dialogFragment.setDate(mSelectedDate);
                dialogFragment.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        if (mFragmentManager == null)
                            throw new FragmentManagerRequiredException();
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(i, i1, i2);
                        mSelectedDate = calendar.getTime();
                        setText(DateUtil.CZ_DATE_FORMAT.format(mSelectedDate));
                        if (mListener != null)
                            mListener.onDateSet(mSelectedDate);
                    }
                });
                dialogFragment.show(mFragmentManager, "datePickerGeneric");
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(mLabelText, UnitsUtil.dpToPx(3), UnitsUtil.dpToPx(14), mLabelTextPaint);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.selectedDate = getSelectedDate();
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState)state;
        super.onRestoreInstanceState(savedState.getSuperState());

        setSelectedDate(savedState.selectedDate);
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    public void setOnDateSelectedListener(OnDateSetListener listener) {
        mListener = listener;
    }

    public void setSelectedDate(Date date) {
        mSelectedDate = new Date(date.getTime());
        setText(DateUtil.CZ_DATE_FORMAT.format(mSelectedDate));
    }

    public void setLabelText(String text) {
        mLabelText = text.toUpperCase();
        invalidate();
        requestLayout();
    }

    public Date getSelectedDate() {
        return new Date(mSelectedDate.getTime());
    }

    private static class SavedState extends BaseSavedState {

        Date selectedDate;

        public SavedState(Parcelable parcelable)
        {
            super(parcelable);
        }

        public SavedState(Parcel source) {
            super(source);
            selectedDate = new Date(source.readLong());
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeLong(selectedDate.getTime());
        }
    }

    private class FragmentManagerRequiredException extends RuntimeException {
        @Override
        public String getMessage() {
            return "You need to setFragmentManager() in order ro use SpinnerDateLikeSelect";
        }
    }

    public interface OnDateSetListener {
        void onDateSet(Date date);
    }
}
