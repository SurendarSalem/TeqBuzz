package com.kanthan.teqbuzz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
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

/**
 * Created by suren on 10/11/2016.
 */
public class FbActivity extends AppCompatActivity {

    private LinearLayout nativeAdContainer;
    private LinearLayout adView;
    private AdChoicesView adChoicesView;
    private NativeAd nativeAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fb_test);
        showFacebookNativeAd();
    }

    private void showFacebookNativeAd() {
        //AdSettings.addTestDevice("18de018fdc6c5627749cb9765cd932ad");
        nativeAd = new NativeAd(this, "1112849102102984_1112852525435975");
        nativeAd.setAdListener(new AdListener() {

            @Override
            public void onError(Ad ad, AdError error) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (ad != nativeAd) {
                    return;
                }

                // Add ad into the ad container.
                nativeAdContainer = (LinearLayout) findViewById(R.id.native_ad_container);
                LayoutInflater inflater = LayoutInflater.from(FbActivity.this);
                adView = (LinearLayout) inflater.inflate(R.layout.fb_ad, nativeAdContainer, false);
                nativeAdContainer.addView(adView);

                // Create native UI using the ad metadata.
                ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.native_ad_icon);
                TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
                TextView nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
                MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
                TextView nativeAdSocialContext = (TextView) adView.findViewById(R.id.native_ad_social_context);
                Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

                // Setting the Text.
                nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
                nativeAdTitle.setText(nativeAd.getAdTitle());
                nativeAdBody.setText(nativeAd.getAdBody());

                // Downloading and setting the ad icon.
                NativeAd.Image adIcon = nativeAd.getAdIcon();
                NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

                // Download and setting the cover image.
                nativeAdMedia.setNativeAd(nativeAd);

                // Add adChoices icon
                if (adChoicesView == null) {
                    adChoicesView = new AdChoicesView(FbActivity.this, nativeAd, true);
                    adView.addView(adChoicesView, 0);
                }

                nativeAd.registerViewForInteraction(adView);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }
        });

        nativeAd.loadAd();
    }
}
