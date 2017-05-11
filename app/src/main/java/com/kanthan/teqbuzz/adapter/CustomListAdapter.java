package com.kanthan.teqbuzz.adapter;

/**
 * Created by user on 5/8/2016.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kanthan.teqbuzz.R;

public class CustomListAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    public CustomListAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*TextView textView = (TextView) convertView;
        if (textView == null)
            textView = (TextView) inflater.inflate(R.layout.item, null);
        textView.setText("Item " + position);*/
        View view  = inflater.inflate(R.layout.vehicle_list_item, parent, false);

        return view;
    }
}