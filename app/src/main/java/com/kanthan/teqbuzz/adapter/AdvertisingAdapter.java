package com.kanthan.teqbuzz.adapter;

/**
 * Created by suren on 10/20/2016.
 */

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.ads.NativeAd;


/**
 * List adapter decorator that inserts adverts into the list.
 *
 * @author Daniel Dyer
 */
public class AdvertisingAdapter extends BaseAdapter {
    private static final String ADMOB_PUBLISHER_ID = "YOUR_ADMOB_ID_HERE";

    private final Activity activity;
    private final BaseAdapter delegate;
    private boolean isAdsLoaded;

    public AdvertisingAdapter(Activity activity, BaseAdapter delegate, boolean isAdsLoaded) {
        this.activity = activity;
        this.delegate = delegate;
        delegate.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                notifyDataSetInvalidated();
            }
        });
    }

    public int getCount() {
        return delegate.getCount() + 1;
    }

    public Object getItem(int i) {
        return delegate.getItem(i - 1);
    }

    public long getItemId(int i) {
        return delegate.getItemId(i - 1);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 3) {
            if (convertView instanceof TextView) {
                return convertView;
            } else {
                TextView adView = new TextView(activity);
                adView.setText("dfvdfbv");
                return adView;
            }
        } else {
            return delegate.getView(position - 1, convertView, parent);
        }
    }

    @Override
    public int getViewTypeCount() {
        return delegate.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 3 ? delegate.getViewTypeCount()
                : delegate.getItemViewType(position - 1);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return position != 3 && delegate.isEnabled(position - 1);
    }

    public void onAdsLoaded(NativeAd nativeAd) {
        isAdsLoaded = true;
    }
}