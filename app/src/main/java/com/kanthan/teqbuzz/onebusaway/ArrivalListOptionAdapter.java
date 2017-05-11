package com.kanthan.teqbuzz.onebusaway;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kanthan.teqbuzz.R;

/**
 * Created by suren on 8/9/2016.
 */
public class ArrivalListOptionAdapter extends BaseAdapter {

    String[] options;

    public ArrivalListOptionAdapter(Context context) {
        options = context.getResources().getStringArray(R.array.options_array);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
