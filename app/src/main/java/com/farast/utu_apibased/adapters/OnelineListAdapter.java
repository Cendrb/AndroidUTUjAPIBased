package com.farast.utu_apibased.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;

import java.util.List;

/**
 * Created by cendr_000 on 03.08.2016.
 */

public class OnelineListAdapter<ItemType> extends RecyclerView.Adapter<OnelineListAdapter.ViewHolder> {

    private List<ItemType> objects;

    public OnelineListAdapter(List<ItemType> objects) {
        super();
        this.objects = objects;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(Bullshit.TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            textView = (TextView) v.findViewById(R.id.list_line_text);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_line, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }
}