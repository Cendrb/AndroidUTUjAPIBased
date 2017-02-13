package com.farast.utu_apibased.activities.show;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.exceptions.ItemIdNotSuppliedException;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.custom_views.utu_spinner.ToStringConverter;
import com.farast.utu_apibased.tasks.UtuDescribedSpinnerAdapter;
import com.farast.utu_apibased.tasks.UtuSubmitter;
import com.farast.utuapi.data.ClassMember;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.PlannedRakingEntry;
import com.farast.utuapi.data.PlannedRakingList;
import com.farast.utuapi.data.PlannedRakingRound;
import com.farast.utuapi.util.CollectionUtil;
import com.farast.utuapi.util.functional_interfaces.Predicate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr_000 on 27.08.2016.
 */

public class PlannedRakingListShowActivity extends AppCompatActivity {

    private PlannedRakingList mPlannedRakingList;
    private PlannedRakingRound mCurrentRound;
    private boolean mShowAlreadyRekt = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() == null)
            throw new ItemIdNotSuppliedException("Intent is null");
        int itemId = getIntent().getIntExtra("item_id", -1);
        if (itemId == -1)
            throw new ItemIdNotSuppliedException("Item id is not stored in this Intent");

        mPlannedRakingList = CollectionUtil.findById(Bullshit.dataLoader.getPlannedRakingsListsList(), itemId);

        setContentView(R.layout.activity_show_planned_raking_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(mPlannedRakingList.getTitle() + " (" + mPlannedRakingList.getSubject().getName() + ")");

        List<PlannedRakingRound> plannedRakingRounds = mPlannedRakingList.getPlannedRakingRounds();
        List<UtuDescribedSpinnerAdapter.DescribedItem<PlannedRakingRound>> describedRounds = new ArrayList<>();
        for (PlannedRakingRound round : plannedRakingRounds) {
            List<ClassMember> rektClassMembers = new ArrayList<>();
            for (PlannedRakingEntry entry : round.getPlannedRakingEntries()) {
                if (entry.isFinished() && !rektClassMembers.contains(entry.getClassMember())) {
                    rektClassMembers.add(entry.getClassMember());
                }
            }
            int totalPossiblyRekt;
            if (mPlannedRakingList.getSgroup() == Bullshit.dataLoader.getAllSgroup())
                totalPossiblyRekt = Bullshit.dataLoader.getLastSclass().getClassMembers().size();
            else
                totalPossiblyRekt = CollectionUtil.filter(Bullshit.dataLoader.getLastSclass().getClassMembers(), new Predicate<ClassMember>() {
                    @Override
                    public boolean test(ClassMember classMember) {
                        return classMember.getSgroups().contains(mPlannedRakingList.getSgroup());
                    }
                }).size();

            describedRounds.add(new UtuDescribedSpinnerAdapter.DescribedItem<PlannedRakingRound>(round, getString(R.string.rekt_x_out_of, rektClassMembers.size(), totalPossiblyRekt)));
        }

        RecyclerView entriesListView = (RecyclerView) findViewById(R.id.prl_entries_list);
        entriesListView.setLayoutManager(new LinearLayoutManager(this));
        final PlannedRakingEntriesRecyclerViewAdapter preAdapter = new PlannedRakingEntriesRecyclerViewAdapter(this, new ArrayList<PlannedRakingEntry>());

        CheckBox showAlreadyRekt = (CheckBox) findViewById(R.id.prl_show_rekt);
        Spinner roundsSelector = (Spinner) findViewById(R.id.prl_round_selector);

        roundsSelector.setAdapter(new UtuDescribedSpinnerAdapter<PlannedRakingRound>(this, describedRounds, new ToStringConverter<PlannedRakingRound>() {
            @Override
            public String stringify(PlannedRakingRound object) {
                return getString(R.string.nth_raking_round, object.getRoundNumber());
            }
        }));
        roundsSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                UtuDescribedSpinnerAdapter.DescribedItem<PlannedRakingRound> describedItem = (UtuDescribedSpinnerAdapter.DescribedItem<PlannedRakingRound>) adapterView.getSelectedItem();
                mCurrentRound = describedItem.getItem();
                updatePreAdapterData(preAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        roundsSelector.setSelection(plannedRakingRounds.indexOf(mPlannedRakingList.getCurrentRound())); // select the last round

        entriesListView.setAdapter(preAdapter);

        showAlreadyRekt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mShowAlreadyRekt = b;
                updatePreAdapterData(preAdapter);
            }
        });
    }

    private void updatePreAdapterData(PlannedRakingEntriesRecyclerViewAdapter adapter) {
        List<PlannedRakingEntry> entries = mCurrentRound.getPlannedRakingEntries();
        if (!mShowAlreadyRekt) {
            entries = CollectionUtil.filter(entries, new Predicate<PlannedRakingEntry>() {
                @Override
                public boolean test(PlannedRakingEntry plannedRakingEntry) {
                    return !plannedRakingEntry.isFinished();
                }
            });
        }
        adapter.setData(entries);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.prl_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_signup_for_rekt:

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class SignupForRekt extends UtuSubmitter {
        public SignupForRekt(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String[] executeInBackground() throws IOException, DataLoader.AdminRequiredException, DataLoader.SclassUnknownException {
            return new String[0];
        }
    }
}
