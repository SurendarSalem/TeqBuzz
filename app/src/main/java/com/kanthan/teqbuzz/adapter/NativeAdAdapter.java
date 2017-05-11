package com.kanthan.teqbuzz.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.kanthan.teqbuzz.R;

/**
 * Created by suren on 6/21/2016.
 */
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;

/**
 * List adapter decorator that inserts adverts into the list.
 *
 * @author Daniel Dyer
 */
public class NativeAdAdapter extends BaseAdapter {
    private static final String ADMOB_PUBLISHER_ID = "YOUR_ADMOB_ID_HERE";

    private final Activity activity;
    private final BaseAdapter delegate;
    LayoutInflater inflater;
    boolean isAdsLoaded;

    public NativeAdAdapter(Activity activity, BaseAdapter delegate, boolean isAdsLoaded) {
        this.activity = activity;
        this.delegate = delegate;
        this.isAdsLoaded = isAdsLoaded;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        if (position % 3 == 0) {
            if (convertView instanceof CardView) {
                return convertView;
            } else {
                CardView adView = showNativeAd(convertView, parent);
                /*TextView textView = new TextView(activity);
                textView.setBackgroundColor(Color.RED);*/
                return adView;
            }
        } else {
            return delegate.getView(position - 1, convertView, parent);
           /* return delegate.getView(position - (int) Math.ceil(position % 3) - 1,
                    convertView, parent);*/
        }
    }

    @Override
    public int getViewTypeCount() {
        return delegate.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position % 3 == 0) ? delegate.getViewTypeCount()
                : delegate.getItemViewType(position - 1);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return (position % 3 != 0) && delegate.isEnabled(position - 1);
    }

    private CardView showNativeAd(View convertView, ViewGroup parent) {
        //AdSettings.addTestDevice("5e87b7cf8d49f913432e0a6bc85d820d");
        final NativeAd nativeAd = new NativeAd(activity, "1683538608572212_1728364794089593");
        final CardView fbAdView = (CardView) inflater.inflate(R.layout.fb_ad, parent, false);


        nativeAd.setAdListener(new com.facebook.ads.AdListener() {

            AdChoicesView adChoicesView;

            @Override
            public void onError(Ad ad, AdError error) {
                Log.e("suren ", error.toString());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (ad != nativeAd) {
                    return;
                }
                // Create native UI using the ad metadata.
                ImageView nativeAdIcon = (ImageView) fbAdView.findViewById(R.id.native_ad_icon);
                TextView nativeAdTitle = (TextView) fbAdView.findViewById(R.id.native_ad_title);
                TextView nativeAdBody = (TextView) fbAdView.findViewById(R.id.native_ad_body);
                MediaView nativeAdMedia = (MediaView) fbAdView.findViewById(R.id.native_ad_media);
                TextView nativeAdSocialContext = (TextView) fbAdView.findViewById(R.id.native_ad_social_context);
                Button nativeAdCallToAction = (Button) fbAdView.findViewById(R.id.native_ad_call_to_action);

                // Setting the Text.
                nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
                nativeAdTitle.setText(nativeAd.getAdTitle());
                nativeAdBody.setText(nativeAd.getAdBody());

                // Downloading and setting the ad icon.
                NativeAd.Image adIcon = nativeAd.getAdIcon();
                NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

                // Download and setting the cover image.
                NativeAd.Image adCoverImage = nativeAd.getAdCoverImage();
                nativeAdMedia.setNativeAd(nativeAd);

                // Add adChoices icon
                if (adChoicesView == null) {
                    adChoicesView = new AdChoicesView(activity, nativeAd, true);
                    fbAdView.addView(adChoicesView, 0);
                }

                nativeAd.registerViewForInteraction(fbAdView);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }
        });

        nativeAd.loadAd();
        return fbAdView;


    }

    public void onAdsLoaded(NativeAd nativeAd) {
        isAdsLoaded = true;

        delegate.notifyDataSetChanged();
        notifyDataSetChanged();
    }
}