package com.farast.utu_apibased.custom_views.additional_infos;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.ToStringConverter;
import com.farast.utu_apibased.UtuDescribedSpinnerAdapter;
import com.farast.utuapi.data.AdditionalInfo;
import com.farast.utuapi.data.Subject;
import com.farast.utuapi.util.CollectionUtil;
import com.farast.utuapi.util.functional_interfaces.Predicate;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr_000 on 19.08.2016.
 */

public class AdditionalInfosSelectDialog extends DialogFragment {

    ArrayList<AdditionalInfo> mModifiedInfos;
    Spinner mSubjectSelectView;
    RecyclerView mAdditionalInfosView;

    public static AdditionalInfosSelectDialog newInstance(ArrayList<Integer> selectedAIIds) {
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList("selected_ai_ids", selectedAIIds);

        AdditionalInfosSelectDialog dialog = new AdditionalInfosSelectDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle storedData;
        if (savedInstanceState != null)
            storedData = savedInstanceState;
        else
            storedData = getArguments();

        ArrayList<Integer> ids = storedData.getIntegerArrayList("selected_ai_ids");
        mModifiedInfos = CollectionUtil.findByIds(Bullshit.dataLoader.getAdditionalInfosList(), ids);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View customDialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_additional_infos_select, null);
        builder.setView(customDialogView);

        mSubjectSelectView = (Spinner) customDialogView.findViewById(R.id.dais_subject_select);

        final UtuDescribedSpinnerAdapter<Subject> utuDescribedSpinnerAdapter = new UtuDescribedSpinnerAdapter<>(getActivity(), getDescribedSubjects(), new ToStringConverter<Subject>() {
            @Override
            public String stringify(Subject object) {
                return object.getName();
            }
        });
        mSubjectSelectView.setAdapter(utuDescribedSpinnerAdapter);

        mAdditionalInfosView = (RecyclerView) customDialogView.findViewById(R.id.dais_additional_infos_list);
        mAdditionalInfosView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final AdditionalInfosSelectAdapter adapter = new AdditionalInfosSelectAdapter(getActivity(), new ArrayList<>(mModifiedInfos));
        adapter.setInfoSelectedListener(new AdditionalInfosSelectAdapter.OnInfoSelectedListener() {
            @Override
            public void onInfoSeleted(boolean checked, AdditionalInfo info) {
                if (checked)
                    mModifiedInfos.add(info);
                else
                    mModifiedInfos.remove(info);
                EventBus.getDefault().post(new SelectionsChangedEvent(AdditionalInfosSelectDialog.this));
                utuDescribedSpinnerAdapter.setDescribedSubjectsList(getDescribedSubjects());
            }
        });
        mAdditionalInfosView.setAdapter(adapter);

        mSubjectSelectView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setSubjectFilter(((UtuDescribedSpinnerAdapter.DescribedItem<Subject>) adapterView.getItemAtPosition(i)).getItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setTitle(R.string.choose_additional_infos);

        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setGravity(Gravity.TOP);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("selected_ai_ids", getModifiedInfos());
    }

    private List<UtuDescribedSpinnerAdapter.DescribedItem<Subject>> getDescribedSubjects()
    {
        List<AdditionalInfo> totalInfos = Bullshit.dataLoader.getAdditionalInfosList();
        List<UtuDescribedSpinnerAdapter.DescribedItem<Subject>> describedSubjects = new ArrayList<>();
        List<Subject> subjects = Bullshit.dataLoader.getSubjects();
        for (final Subject subject : subjects) {
            int selectedInfosForSubject = CollectionUtil.filter(mModifiedInfos, new Predicate<AdditionalInfo>() {
                @Override
                public boolean test(AdditionalInfo additionalInfo) {
                    return additionalInfo.getSubject() == subject;
                }
            }).size();
            int totalInfosForSubject = CollectionUtil.filter(totalInfos, new Predicate<AdditionalInfo>() {
                @Override
                public boolean test(AdditionalInfo additionalInfo) {
                    return additionalInfo.getSubject() == subject;
                }
            }).size();
            describedSubjects.add(new UtuDescribedSpinnerAdapter.DescribedItem<Subject>(subject, String.valueOf(selectedInfosForSubject) + "/" + String.valueOf(totalInfosForSubject)));
        }
        return describedSubjects;
    }

    public ArrayList<Integer> getModifiedInfos() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (AdditionalInfo info : mModifiedInfos)
            ids.add(info.getId());
        return ids;
    }

    public class SelectionsChangedEvent
    {
        private AdditionalInfosSelectDialog caller;

        public SelectionsChangedEvent(AdditionalInfosSelectDialog caller)
        {
            this.caller = caller;
        }

        public AdditionalInfosSelectDialog getCaller() {
            return caller;
        }
    }
}
