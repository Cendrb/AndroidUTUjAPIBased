package com.farast.utu_apibased.create_update_activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.UtuSubmitter;
import com.farast.utu_apibased.custom_views.additional_infos_select.SpinnerLikeAdditionalInfoDialoger;
import com.farast.utu_apibased.custom_views.date_select.SpinnerLikeDateSelect;
import com.farast.utu_apibased.custom_views.utu_spinner.UtuSpinner;
import com.farast.utuapi.data.AdditionalInfo;
import com.farast.utuapi.data.Article;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.Sgroup;
import com.farast.utuapi.util.CollectionUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CUArticleActivity extends AppCompatActivity {

    Article mLoaded;

    TextInputEditText mTitleView;
    TextInputEditText mDescriptionView;
    CheckBox mPublishedView;
    CheckBox mShowInDetailsView;
    SpinnerLikeDateSelect mShowInDetailsUntilView;
    UtuSpinner<Sgroup> mSgroupView;
    SpinnerLikeAdditionalInfoDialoger mAISelectView;
    Button mSubmitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cu_article);

        mTitleView = (TextInputEditText) findViewById(R.id.cu_title);
        mDescriptionView = (TextInputEditText) findViewById(R.id.cu_description);
        mPublishedView = (CheckBox) findViewById(R.id.cu_published);
        mShowInDetailsView = (CheckBox) findViewById(R.id.cu_show_in_details);
        mShowInDetailsUntilView = (SpinnerLikeDateSelect) findViewById(R.id.cu_show_in_details_until);
        mSgroupView = (UtuSpinner<Sgroup>) findViewById(R.id.cu_sgroup_selector);
        mAISelectView= (SpinnerLikeAdditionalInfoDialoger) findViewById(R.id.cu_additional_info_select);
        mSubmitView = (Button) findViewById(R.id.cu_submit);

        mShowInDetailsView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mShowInDetailsUntilView.setEnabled(true);
                else
                    mShowInDetailsUntilView.setEnabled(false);
            }
        });

        mShowInDetailsUntilView.setFragmentManager(getFragmentManager());
        mAISelectView.setFragmentManager(getFragmentManager());

        int itemId = getIntent().getIntExtra("item_id", -1);
        if (itemId != -1) {
            mLoaded = CollectionUtil.findById(Bullshit.dataLoader.getArticlesList(), itemId);
            mTitleView.setText(mLoaded.getTitle());
            mDescriptionView.setText(mLoaded.getDescription());
            mPublishedView.setChecked(mLoaded.isPublished());
            if (mLoaded.isShowInDetails()) {
                mShowInDetailsView.setChecked(true);
                mShowInDetailsUntilView.setSelectedDate(mLoaded.getShowInDetailsUntil());
            } else {
                mShowInDetailsView.setChecked(false);
            }
            mSgroupView.setItem(mLoaded.getSgroup());

            ArrayList<Integer> selectedInfoIds = new ArrayList<>();
            List<AdditionalInfo> selectedInfos = mLoaded.getAdditionalInfos();
            for (AdditionalInfo info : selectedInfos)
                selectedInfoIds.add(info.getId());
            mAISelectView.setSelectedAIIds(selectedInfoIds);

            setTitle(getString(R.string.editing) + " " + getString(R.string.item_article).toLowerCase());
        } else {
            setTitle(getString(R.string.creating) + " " + getString(R.string.item_article).toLowerCase());
        }

        mSubmitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTitleView.setError(null);
                if (TextUtils.isEmpty(mTitleView.getText())) {
                    mTitleView.setError(getString(R.string.error_field_required));
                    mTitleView.requestFocus();
                } else if (TextUtils.isEmpty(mDescriptionView.getText())) {
                    mDescriptionView.setError(getString(R.string.error_field_required));
                    mDescriptionView.requestFocus();
                } else
                    new Submitter(getApplicationContext()).execute();
            }
        });
    }

    class Submitter extends UtuSubmitter {
        String mTitle;
        String mDescription;
        Date mPublishedOn;
        Date mShowInDetailsUntil;
        Sgroup mSgroup;
        List<AdditionalInfo> mSelectedInfos;

        public Submitter(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() {
            mTitle = mTitleView.getText().toString();
            mDescription = mDescriptionView.getText().toString();
            if (mPublishedView.isChecked())
                mPublishedOn = Calendar.getInstance().getTime();
            else
                mPublishedOn = null;
            if (mShowInDetailsView.isChecked())
                mShowInDetailsUntil = mShowInDetailsUntilView.getSelectedDate();
            else
                mShowInDetailsUntil = null;
            mSgroup = mSgroupView.getItem();
            mSelectedInfos = CollectionUtil.findByIds(Bullshit.dataLoader.getAdditionalInfosList(), mAISelectView.getSelectAIIds());

            finish();
        }

        @Override
        protected String[] executeInBackground() throws IOException, DataLoader.AdminRequiredException, DataLoader.SclassUnknownException {
            return Bullshit.dataLoader.getEditor().requestCUArticle(mLoaded, mTitle, mDescription, mPublishedOn, mShowInDetailsUntil, mSgroup, mSelectedInfos);
        }
    }
}
