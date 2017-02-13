package com.farast.utu_apibased.create_update_activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.TextUtil;
import com.farast.utu_apibased.UtuSubmitter;
import com.farast.utu_apibased.custom_views.additional_infos_select.SpinnerLikeAdditionalInfoDialoger;
import com.farast.utu_apibased.custom_views.date_select.SpinnerLikeDateSelect;
import com.farast.utu_apibased.custom_views.utu_spinner.UtuSpinner;
import com.farast.utuapi.data.AdditionalInfo;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.Event;
import com.farast.utuapi.data.Sgroup;
import com.farast.utuapi.util.CollectionUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CUEventActivity extends AppCompatActivity {

    Event mLoaded;

    TextInputEditText mTitleView;
    TextInputEditText mDescriptionView;
    TextInputEditText mLocationView;
    TextInputEditText mPriceView;
    SpinnerLikeDateSelect mStartView;
    SpinnerLikeDateSelect mEndView;
    SpinnerLikeDateSelect mPayDateView;
    UtuSpinner<Sgroup> mSgroupView;
    SpinnerLikeAdditionalInfoDialoger mAISelectView;
    Button mSubmitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cu_event);

        mTitleView = (TextInputEditText) findViewById(R.id.cu_title);
        mDescriptionView = (TextInputEditText) findViewById(R.id.cu_description);
        mLocationView = (TextInputEditText) findViewById(R.id.cu_location);
        mPriceView = (TextInputEditText) findViewById(R.id.cu_price);
        mStartView = (SpinnerLikeDateSelect) findViewById(R.id.cu_event_start);
        mEndView = (SpinnerLikeDateSelect) findViewById(R.id.cu_event_end);
        mPayDateView = (SpinnerLikeDateSelect) findViewById(R.id.cu_pay_date);
        mSgroupView = (UtuSpinner<Sgroup>) findViewById(R.id.cu_sgroup_selector);
        mAISelectView = (SpinnerLikeAdditionalInfoDialoger) findViewById(R.id.cu_additional_info_select);
        mSubmitView = (Button) findViewById(R.id.cu_submit);

        mStartView.setFragmentManager(getFragmentManager());
        mEndView.setFragmentManager(getFragmentManager());
        mPayDateView.setFragmentManager(getFragmentManager());
        mAISelectView.setFragmentManager(getFragmentManager());

        int itemId = getIntent().getIntExtra("item_id", -1);
        if (itemId != -1) {
            mLoaded = CollectionUtil.findById(Bullshit.dataLoader.getEventsList(), itemId);
            mTitleView.setText(mLoaded.getTitle());
            mDescriptionView.setText(mLoaded.getDescription());
            mLocationView.setText(mLoaded.getLocation());
            mPriceView.setText(String.valueOf(mLoaded.getPrice()));
            mStartView.setSelectedDate(mLoaded.getStart());
            mEndView.setSelectedDate(mLoaded.getEnd());
            mPayDateView.setSelectedDate(mLoaded.getPayDate());
            mSgroupView.setItem(mLoaded.getSgroup());

            ArrayList<Integer> selectedInfoIds = new ArrayList<>();
            List<AdditionalInfo> selectedInfos = mLoaded.getAdditionalInfos();
            for (AdditionalInfo info : selectedInfos)
                selectedInfoIds.add(info.getId());
            mAISelectView.setSelectedAIIds(selectedInfoIds);

            setTitle(TextUtil.capitalize(getString(R.string.operation_edit_x, getString(R.string.item_event)).toLowerCase()));
        } else {
            setTitle(TextUtil.capitalize(getString(R.string.operation_new_x, getString(R.string.item_event)).toLowerCase()));
        }

        mStartView.setOnDateSelectedListener(new SpinnerLikeDateSelect.OnDateSetListener() {
            @Override
            public void onDateSet(Date date) {
                if (mEndView.getSelectedDate().before(date))
                    mEndView.setSelectedDate(date);
            }
        });
        mEndView.setOnDateSelectedListener(new SpinnerLikeDateSelect.OnDateSetListener() {
            @Override
            public void onDateSet(Date date) {
                if (mStartView.getSelectedDate().after(date))
                    mStartView.setSelectedDate(date);
            }
        });

        mSubmitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTitleView.setError(null);
                if (TextUtils.isEmpty(mTitleView.getText())) {
                    mTitleView.setError(getString(R.string.error_field_required));
                    mTitleView.requestFocus();
                } else
                    new Submitter(getApplicationContext()).execute();
            }
        });
    }

    class Submitter extends UtuSubmitter {
        String mTitle;
        String mDescription;
        String mLocation;
        int mPrice;
        Date mStart;
        Date mEnd;
        Date mPayDate;
        Sgroup mSgroup;
        List<AdditionalInfo> mSelectedInfos;

        public Submitter(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() {
            mTitle = mTitleView.getText().toString();
            mDescription = mDescriptionView.getText().toString();
            mLocation = mLocationView.getText().toString();
            mPrice = Integer.parseInt(mPriceView.getText().toString());
            mStart = mStartView.getSelectedDate();
            mEnd = mEndView.getSelectedDate();
            mPayDate = mPayDateView.getSelectedDate();
            mSgroup = mSgroupView.getItem();
            mSelectedInfos = CollectionUtil.findByIds(Bullshit.dataLoader.getAdditionalInfosList(), mAISelectView.getSelectAIIds());

            finish();
        }

        @Override
        protected String[] executeInBackground() throws IOException, DataLoader.AdminRequiredException, DataLoader.SclassUnknownException {
            return Bullshit.dataLoader.getEditor().requestCUEvent(mLoaded, mTitle, mDescription, mLocation, mPrice, mStart, mEnd, mPayDate, mSgroup, mSelectedInfos);
        }
    }
}
