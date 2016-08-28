package com.farast.utu_apibased.custom_views.utu_spinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.ToStringConverter;
import com.farast.utuapi.data.OnelineRepresentable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by cendr_000 on 18.08.2016.
 */

public class UtuSpinner<T extends OnelineRepresentable> extends Spinner {

    T mItem;
    List<T> mData;

    public UtuSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPadding(0,0,0,0);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.UtuSpinner, 0, 0);
        try {
            String typeString = a.getString(R.styleable.UtuSpinner_utuItemType);
            if (typeString == null)
                throw new UtuTypeRequiredException();
            if (Objects.equals(typeString, "additional_info")) {
                mData = (List<T>) Bullshit.dataLoader.getAdditionalInfosList();
            } else if (Objects.equals(typeString, "sclass")) {
                mData = (List<T>) Bullshit.dataLoader.getSclasses();
            } else if (Objects.equals(typeString, "sgroup")) {
                mData = (List<T>) Bullshit.dataLoader.getSgroupsList();
                Collections.reverse(mData);
            } else if (Objects.equals(typeString, "subject")) {
                mData = (List<T>) Bullshit.dataLoader.getSubjects();
            } else
                throw new UtuTypeRequiredException();
        } finally {
            a.recycle();
        }

        final ArrayAdapter<T> androidSucksAdapter = new UtuSpinnerAdapter<>(context, mData, new ToStringConverter<T>() {
            @Override
            public String stringify(T object) {
                return object.getOnelineRepresentation();
            }
        });
        setAdapter(androidSucksAdapter);
        setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mItem = (T) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public T getItem() {
        return mItem;
    }

    public void setItem(T item) {
        mItem = item;
        setSelection(mData.indexOf(item));
    }

    public static class UtuTypeRequiredException extends RuntimeException {
        @Override
        public String getMessage() {
            return "You need to specify utuItemType in order to use this spinner";
        }
    }
}
