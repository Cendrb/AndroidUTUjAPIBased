package com.farast.utu_apibased.adapters.generic_utu;

import android.content.Context;

import com.farast.utu_apibased.Bullshit;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.Task;
import com.farast.utuapi.util.functional_interfaces.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr on 14/02/2017.
 */

public class TasksAdapter extends TEsAdapter<Task> {
    public TasksAdapter(Context context) {
        super(context, new Function<List<Task>>() {
            @Override
            public List<Task> execute() {
                return Bullshit.dataLoader.getTasksList();
            }
        }, new ArrayList<DataLoader.EventType>() {{
            add(DataLoader.EventType.TASKS);
        }});
    }
}
