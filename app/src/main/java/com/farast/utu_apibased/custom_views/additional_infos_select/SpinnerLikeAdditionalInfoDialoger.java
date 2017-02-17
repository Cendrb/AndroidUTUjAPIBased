package com.farast.utu_apibased.custom_views.additional_infos_select;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.util.UnitsUtil;
import com.farast.utuapi.data.AdditionalInfo;
import com.farast.utuapi.util.CollectionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr_000 on 19.08.2016.
 */

public class SpinnerLikeAdditionalInfoDialoger extends TextView {

    FragmentManager mFragmentManager;
    Paint mLabelTextPaint;
    ArrayList<Integer> mSelectedAIIds;

    public SpinnerLikeAdditionalInfoDialoger(Context context, AttributeSet attrs) {
        super(context, attrs);

        mLabelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLabelTextPaint.setColor(ContextCompat.getColor(context, android.R.color.darker_gray));
        mLabelTextPaint.setTextSize(UnitsUtil.dpToPx(12));
        mLabelTextPaint.setTypeface(Typeface.DEFAULT);

        setPadding(UnitsUtil.dpToPx(3), UnitsUtil.dpToPx(20), UnitsUtil.dpToPx(3), UnitsUtil.dpToPx(10));

        mSelectedAIIds = new ArrayList<>();

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFragmentManager == null)
                    throw new FragmentManagerRequiredException();

                // mSelectedAIIds gets updated througn EventBus and onAdditionalInfosSelectionChanged()
                final AdditionalInfosSelectDialog dialogFragment = AdditionalInfosSelectDialog.newInstance(mSelectedAIIds);
                dialogFragment.show(mFragmentManager, "datePickerGeneric");
            }
        });
    }

    private CharSequence getCurrentlySelectedText() {
        if (mSelectedAIIds.size() > 0) {
            List<String> infoTexts = new ArrayList<>();
            List<AdditionalInfo> selectedInfos = CollectionUtil.findByIds(Bullshit.dataLoader.getAdditionalInfosList(), mSelectedAIIds);
            for (AdditionalInfo info : selectedInfos)
                infoTexts.add(info.getName() + " (" + info.getSubject().getName() + ")");
            return TextUtils.join("\n", infoTexts);
        } else
            return getContext().getString(R.string.none);
    }

    public void setSelectedAIIds(ArrayList<Integer> ids) {
        mSelectedAIIds = ids;
        setText(getCurrentlySelectedText());
    }

    public ArrayList<Integer> getSelectAIIds() {
        return mSelectedAIIds;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(getContext().getString(R.string.additional_infos), UnitsUtil.dpToPx(3), UnitsUtil.dpToPx(14), mLabelTextPaint);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.selectedIds = getSelectAIIds();
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        setSelectedAIIds(savedState.selectedIds);
    }

    @Subscribe
    public void onAdditionalInfosSelectionChanged(AdditionalInfosSelectDialog.SelectionsChangedEvent event) {
        setSelectedAIIds(event.getCaller().getModifiedInfos());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    private static class SavedState extends BaseSavedState {

        ArrayList<Integer> selectedIds;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public SavedState(Parcel source) {
            super(source);
            selectedIds = source.readArrayList(Integer.class.getClassLoader());
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
            parcel.writeList(selectedIds);
        }
    }

    private class FragmentManagerRequiredException extends RuntimeException {
        @Override
        public String getMessage() {
            return "You need to setFragmentManager() in order ro use SpinnerLikeAdditionalInfoDialoger";
        }
    }
}
