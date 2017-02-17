package com.farast.utu_apibased.fragments.main_menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.activities.show.EventShowActivity;
import com.farast.utu_apibased.adapters.generic_utu.EventsAdapter;
import com.farast.utu_apibased.listeners.OnListFragmentInteractionListener;
import com.farast.utu_apibased.util.ItemUtil;
import com.farast.utuapi.data.Event;

public class EventsFragment extends Fragment {

    private EventsAdapter mAdapter;

    public EventsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.utu_list_recycler_view, container, false);

        setHasOptionsMenu(true);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            mAdapter = new EventsAdapter(context);
            mAdapter.setOnItemClickListener(new OnListFragmentInteractionListener<Event>() {
                @Override
                public void onListFragmentInteraction(Event item) {
                    Intent intent = new Intent(getContext(), EventShowActivity.class);
                    intent.putExtra(ItemUtil.ITEM_ID, item.getId());
                    getContext().startActivity(intent);
                }
            });
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // remember to call setHasOptionsMenu(true);
        super.onCreateOptionsMenu(menu, inflater);
        if (Bullshit.dataLoader.getCurrentUser() != null) {
            inflater.inflate(R.menu.hiding_options, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_hide_reveal) {
            mAdapter.setShowAll(!mAdapter.isShowAll());
            if (mAdapter.isShowAll()) {
                item.setIcon(R.drawable.ic_done);
                item.setTitle(R.string.nav_done);
            } else {
                item.setIcon(R.drawable.ic_edit_mode);
                item.setTitle(R.string.nav_edit_mode);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
