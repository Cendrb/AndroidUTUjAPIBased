package com.farast.utu_apibased.custom_views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.farast.utu_apibased.R;

/**
 * Created by cendr_000 on 21.08.2016.
 */

public class UtuLineGenericViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mAvatarTextView;
    public final TextView mTitleView;
    public final TextView mRightView;
    public final TextView mLeftBottomView;
    public final View mAvatarParent;

    public UtuLineGenericViewHolder(View view) {
        super(view);
        mView = view;
        mAvatarParent = view.findViewById(R.id.utu_line_subject_circle_layout);
        mAvatarTextView = (TextView) view.findViewById(R.id.utu_line_subject_circle);
        mTitleView = (TextView) view.findViewById(R.id.utu_line_title);
        mRightView = (TextView) view.findViewById(R.id.utu_line_right);
        mLeftBottomView = (TextView) view.findViewById(R.id.utu_line_bottom_left);
    }
}
