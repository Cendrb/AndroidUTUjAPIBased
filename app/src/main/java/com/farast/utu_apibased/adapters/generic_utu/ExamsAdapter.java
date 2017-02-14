package com.farast.utu_apibased.adapters.generic_utu;

import android.content.Context;

import com.farast.utu_apibased.Bullshit;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.Exam;
import com.farast.utuapi.util.functional_interfaces.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr on 14/02/2017.
 */

public class ExamsAdapter extends TEsAdapter<Exam> {
    public ExamsAdapter(Context context) {
        super(context, new Function<List<Exam>>() {
            @Override
            public List<Exam> execute() {
                return Bullshit.dataLoader.getExamsList();
            }
        }, new ArrayList<DataLoader.EventType>() {{
            add(DataLoader.EventType.EXAMS);
        }});
    }
}
