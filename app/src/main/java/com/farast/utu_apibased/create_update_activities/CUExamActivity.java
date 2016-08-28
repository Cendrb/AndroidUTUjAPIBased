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
import com.farast.utu_apibased.UtuSubmitter;
import com.farast.utu_apibased.custom_views.SpinnerLikeAdditionalInfoDialoger;
import com.farast.utu_apibased.custom_views.SpinnerLikeDateSelect;
import com.farast.utu_apibased.custom_views.utu_spinner.UtuSpinner;
import com.farast.utuapi.data.AdditionalInfo;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.Exam;
import com.farast.utuapi.data.Sgroup;
import com.farast.utuapi.data.Subject;
import com.farast.utuapi.util.CollectionUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CUExamActivity extends AppCompatActivity {

    Exam mLoaded;

    TextInputEditText mTitleView;
    TextInputEditText mDescriptionView;
    SpinnerLikeDateSelect mDateView;
    UtuSpinner<Sgroup> mSgroupView;
    UtuSpinner<Subject> mSubjectView;
    SpinnerLikeAdditionalInfoDialoger mAISelectView;
    Button mSubmitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cu_te);

        mTitleView = (TextInputEditText) findViewById(R.id.cu_title);
        mDescriptionView = (TextInputEditText) findViewById(R.id.cu_description);
        mDateView = (SpinnerLikeDateSelect) findViewById(R.id.cu_date);
        mSgroupView = (UtuSpinner<Sgroup>) findViewById(R.id.cu_sgroup_selector);
        mSubjectView = (UtuSpinner<Subject>) findViewById(R.id.cu_subject_selector);
        mAISelectView= (SpinnerLikeAdditionalInfoDialoger) findViewById(R.id.cu_additional_info_select);
        mSubmitView = (Button) findViewById(R.id.cu_submit);

        mDateView.setFragmentManager(getFragmentManager());
        mAISelectView.setFragmentManager(getFragmentManager());

        int itemId = getIntent().getIntExtra("item_id", -1);
        if (itemId != -1) {
            mLoaded = CollectionUtil.findById(Bullshit.dataLoader.getExamsList(), itemId);
            mTitleView.setText(mLoaded.getTitle());
            mDescriptionView.setText(mLoaded.getDescription());
            mDateView.setSelectedDate(mLoaded.getDate());
            mSgroupView.setItem(mLoaded.getSgroup());
            mSubjectView.setItem(mLoaded.getSubject());

            ArrayList<Integer> selectedInfoIds = new ArrayList<>();
            List<AdditionalInfo> selectedInfos = mLoaded.getAdditionalInfos();
            for (AdditionalInfo info : selectedInfos)
                selectedInfoIds.add(info.getId());
            mAISelectView.setSelectedAIIds(selectedInfoIds);

            setTitle(getString(R.string.editing) + " " + getString(R.string.exam).toLowerCase());
        } else {
            setTitle(getString(R.string.creating) + " " + getString(R.string.exam).toLowerCase());
        }

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
        Date mDate;
        Sgroup mSgroup;
        Subject mSubject;
        List<AdditionalInfo> mSelectedInfos;

        public Submitter(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() {
            mTitle = mTitleView.getText().toString();
            mDescription = mDescriptionView.getText().toString();
            mDate = mDateView.getSelectedDate();
            mSgroup = mSgroupView.getItem();
            mSubject = mSubjectView.getItem();
            mSelectedInfos = CollectionUtil.findByIds(Bullshit.dataLoader.getAdditionalInfosList(), mAISelectView.getSelectAIIds());

            finish();
        }

        @Override
        protected String[] executeInBackground() throws IOException, DataLoader.AdminRequiredException, DataLoader.SclassUnknownException {
            return Bullshit.dataLoader.getEditor().requestCUExam(mLoaded, mTitle, mDescription, mDate, mSubject, mSgroup, mSelectedInfos, Exam.Type.written);
        }
    }
}
