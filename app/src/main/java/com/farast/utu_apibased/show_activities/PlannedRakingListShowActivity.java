package com.farast.utu_apibased.show_activities;

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
import android.widget.Spinner;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.ItemIdNotSuppliedException;
import com.farast.utu_apibased.PlannedRakingEntriesRecyclerViewAdapter;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.ToStringConverter;
import com.farast.utu_apibased.UtuDescribedSpinnerAdapter;
import com.farast.utu_apibased.UtuSubmitter;
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

    PlannedRakingList plannedRakingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() == null)
            throw new ItemIdNotSuppliedException("Intent is null");
        int itemId = getIntent().getIntExtra("item_id", -1);
        if (itemId == -1)
            throw new ItemIdNotSuppliedException("Item id is not stored in this Intent");

        plannedRakingList = CollectionUtil.findById(Bullshit.dataLoader.getPlannedRakingsListsList(), itemId);


        setContentView(R.layout.activity_show_planned_raking_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(plannedRakingList.getTitle() + " (" + plannedRakingList.getSubject().getName() + ")");

        List<UtuDescribedSpinnerAdapter.DescribedItem<PlannedRakingRound>> describedRounds = new ArrayList<>();
        for (PlannedRakingRound round : plannedRakingList.getPlannedRakingRounds()) {
            int alreadyRekt = CollectionUtil.filter(round.getPlannedRakingEntries(), new Predicate<PlannedRakingEntry>() {
                @Override
                public boolean test(PlannedRakingEntry plannedRakingEntry) {
                    return false;
                }
            }).size();
            int totalPossiblyRekt;
            if (plannedRakingList.getSgroup() == Bullshit.dataLoader.getAllSgroup())
                totalPossiblyRekt = Bullshit.dataLoader.getLastSclass().getClassMembers().size();
            else
                totalPossiblyRekt = CollectionUtil.filter(Bullshit.dataLoader.getLastSclass().getClassMembers(), new Predicate<ClassMember>() {
                    @Override
                    public boolean test(ClassMember classMember) {
                        return classMember.getSgroups().contains(plannedRakingList.getSgroup());
                    }
                }).size();

            describedRounds.add(new UtuDescribedSpinnerAdapter.DescribedItem<PlannedRakingRound>(round, getString(R.string.rekt_x_out_of, alreadyRekt, totalPossiblyRekt)));
        }

        RecyclerView entriesListView = (RecyclerView) findViewById(R.id.prl_entries_list);
        entriesListView.setLayoutManager(new LinearLayoutManager(this));
        final PlannedRakingEntriesRecyclerViewAdapter plrAdapter = new PlannedRakingEntriesRecyclerViewAdapter(this, plannedRakingList.getCurrentRound().getPlannedRakingEntries());
        entriesListView.setAdapter(plrAdapter);

        Spinner roundSelector = (Spinner) findViewById(R.id.prl_round_selector);
        roundSelector.setAdapter(new UtuDescribedSpinnerAdapter<PlannedRakingRound>(this, describedRounds, new ToStringConverter<PlannedRakingRound>() {
            @Override
            public String stringify(PlannedRakingRound object) {
                return getString(R.string.nth_raking_round, object.getRoundNumber());
            }
        }));
        roundSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                UtuDescribedSpinnerAdapter.DescribedItem<PlannedRakingRound> describedItem = (UtuDescribedSpinnerAdapter.DescribedItem<PlannedRakingRound>)adapterView.getItemAtPosition(i);
                plrAdapter.setData(describedItem.getItem().getPlannedRakingEntries());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        roundSelector.setSelection(describedRounds.size() - 1); // select the last round
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
