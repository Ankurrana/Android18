package com.example.ankurrana.helloandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ankur.Rana on 11/16/2016.
 */

public class TaskListAdapter extends ArrayAdapter<Task> {
    public TaskListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public TaskListAdapter(Context context, int resource, List<Task> items) {
        super(context, resource, items);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(android.R.layout.simple_list_item_1, null);
        }

        Task p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(android.R.id.text1);

            if (tt1 != null) {
                tt1.setText(p.Description());
            }
        }

        return v;
    }
}
