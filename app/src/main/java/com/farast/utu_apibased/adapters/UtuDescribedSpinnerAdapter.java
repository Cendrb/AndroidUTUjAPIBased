package com.farast.utu_apibased.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.farast.utu_apibased.R;
import com.farast.utu_apibased.custom_views.utu_spinner.ToStringConverter;

import java.util.List;

/**
 * Created by cendr_000 on 21.08.2016.
 */

public class UtuDescribedSpinnerAdapter<T> extends ArrayAdapter<UtuDescribedSpinnerAdapter.DescribedItem<T>> {
    private List<DescribedItem<T>> mData;
    private final int layoutResourceId = R.layout.utu_description_spinner_line;
    private ToStringConverter<T> mConverter;

    public UtuDescribedSpinnerAdapter(Context context, List<DescribedItem<T>> objects, ToStringConverter<T> converter) {
        super(context, R.layout.utu_description_spinner_line, objects);
        mData = objects;
        mConverter = converter;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UtuHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            holder = new UtuHolder();
            holder.mainText = (TextView) convertView.findViewById(R.id.udsl_mainText);
            holder.descText = (TextView) convertView.findViewById(R.id.udsl_descText);

            convertView.setTag(holder);
        } else {
            holder = (UtuHolder) convertView.getTag();
        }

        DescribedItem<T> item = mData.get(position);
        holder.mainText.setText(mConverter.stringify(item.getItem()));
        holder.descText.setText(item.getDescription());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    public void setDescribedSubjectsList(List<DescribedItem<T>> objects) {
        mData.clear();
        mData.addAll(objects);
        notifyDataSetChanged();
    }

    public static class DescribedItem<T> {
        private final T item;
        private final String description;

        public DescribedItem(T item, String description) {
            this.item = item;
            this.description = description;
        }

        public T getItem() {
            return item;
        }

        public String getDescription() {
            return description;
        }
    }

    private static class UtuHolder {
        TextView mainText;
        TextView descText;
    }
}
