package com.farast.utu_apibased.custom_views.utu_spinner;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.farast.utu_apibased.R;
import com.farast.utu_apibased.ToStringConverter;

import java.util.List;

/**
 * Created by cendr_000 on 26.08.2016.
 */

public class UtuAdapter<T> extends ArrayAdapter<T> {

    private List<T> mData;
    private ToStringConverter<T> mConverter;

    public UtuAdapter(Context context, List<T> objects, ToStringConverter<T> converter) {
        super(context, R.layout.simple_spinner_item, objects);
        mConverter = converter;
        mData = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UtuAdapter.Holder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(R.layout.simple_spinner_item, parent, false);

            holder = new UtuAdapter.Holder();
            holder.theOnlyTextView = (TextView) convertView.findViewById(R.id.text1);

            convertView.setTag(holder);
        } else {
            holder = (UtuAdapter.Holder) convertView.getTag();
        }

        T item = mData.get(position);
        holder.theOnlyTextView.setText(mConverter.stringify(item));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private class Holder
    {
        TextView theOnlyTextView;
    }
}
