package com.farast.utu_apibased.activities.cu;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.custom_views.additional_infos_select.SpinnerLikeAdditionalInfoDialoger;
import com.farast.utu_apibased.custom_views.date_select.SpinnerLikeDateSelect;
import com.farast.utu_apibased.custom_views.utu_spinner.UtuSpinner;
import com.farast.utu_apibased.tasks.UtuSubmitter;
import com.farast.utu_apibased.util.TextUtil;
import com.farast.utuapi.data.AdditionalInfo;
import com.farast.utuapi.data.Sgroup;
import com.farast.utuapi.data.Subject;
import com.farast.utuapi.data.Task;
import com.farast.utuapi.exceptions.AdminRequiredException;
import com.farast.utuapi.exceptions.SclassUnknownException;
import com.farast.utuapi.util.CollectionUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CUTaskActivity extends AppCompatActivity {

    Task mLoaded;

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
            mLoaded = CollectionUtil.findById(Bullshit.dataLoader.getTasksList(), itemId);
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

            setTitle(TextUtil.capitalize(getString(R.string.operation_edit_x, getString(R.string.item_task)).toLowerCase()));
        } else {
            setTitle(TextUtil.capitalize(getString(R.string.operation_new_x, getString(R.string.item_task)).toLowerCase()));
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
        protected String[] executeInBackground() throws IOException, AdminRequiredException, SclassUnknownException {
            return Bullshit.dataLoader.getEditor().requestCUTask(mLoaded, mTitle, mDescription, mDate, mSubject, mSgroup, mSelectedInfos);
        }
    }
}
