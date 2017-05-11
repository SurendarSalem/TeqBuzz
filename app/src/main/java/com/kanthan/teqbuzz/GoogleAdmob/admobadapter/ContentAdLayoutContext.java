package com.kanthan.teqbuzz.GoogleAdmob.admobadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.kanthan.teqbuzz.R;

public class ContentAdLayoutContext extends NativeAdLayoutContext{

    public static ContentAdLayoutContext getDefault(){
        return new ContentAdLayoutContext(R.layout.adcontentlistview_item);
    }

    public ContentAdLayoutContext(int mAdLayoutId){
        setAdLayoutId(mAdLayoutId);
    }

    @Override
    public void bind(NativeAdView nativeAdView, NativeAd nativeAd) throws ClassCastException{
        if (nativeAdView == null || nativeAd == null) return;
        if(!(nativeAd instanceof NativeContentAd) || !(nativeAdView instanceof NativeContentAdView))
            throw new ClassCastException();

        NativeContentAd ad = (NativeContentAd) nativeAd;
        NativeContentAdView adView = (NativeContentAdView) nativeAdView;

        // Locate the view that will hold the headline, set its text, and call the
        // NativeContentAdView's setHeadlineView method to register it.
        TextView tvHeader = (TextView) nativeAdView.findViewById(R.id.tvHeader);
        tvHeader.setText(ad.getHeadline());
        adView.setHeadlineView(tvHeader);

        TextView tvDescription = (TextView) nativeAdView.findViewById(R.id.tvDescription);
        tvDescription.setText(ad.getBody());
        adView.setBodyView(tvDescription);

        ImageView ivLogo = (ImageView) nativeAdView.findViewById(R.id.ivLogo);
        if(ad.getLogo()!=null)
            ivLogo.setImageDrawable(ad.getLogo().getDrawable());
        adView.setLogoView(ivLogo);

        Button btnAction = (Button) nativeAdView.findViewById(R.id.btnAction);
        btnAction.setText(ad.getCallToAction());
        adView.setCallToActionView(btnAction);

        TextView tvAdvertiser = (TextView) nativeAdView.findViewById(R.id.tvAdvertiser);
        tvAdvertiser.setText(ad.getAdvertiser());
        adView.setAdvertiserView(tvAdvertiser);

        ImageView ivImage = (ImageView) nativeAdView.findViewById(R.id.ivImage);
        if (ad.getImages() != null && ad.getImages().size() > 0) {
            ivImage.setImageDrawable(ad.getImages().get(0).getDrawable());
            ivImage.setVisibility(View.VISIBLE);
        } else ivImage.setVisibility(View.GONE);
        adView.setImageView(ivImage);

        // Call the NativeContentAdView's setNativeAd method to register the
        // NativeAdObject.
        nativeAdView.setNativeAd(nativeAd);
    }

}
