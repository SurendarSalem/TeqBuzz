package com.kanthan.teqbuzz.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kanthan.teqbuzz.R;

import it.carlom.stikkyheader.core.StikkyHeaderBuilder;
import it.carlom.stikkyheader.core.animator.AnimatorBuilder;
import it.carlom.stikkyheader.core.animator.HeaderStikkyAnimator;

/**
 * Created by suren on 6/9/2016.
 */
public class AboutUsFragment extends Fragment {

    private View rootView;
    ScrollView scrollView;
    RelativeLayout aboutus_container, container;
    TextView link;
    private Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_aboutus, container, false);
        scrollView = (ScrollView) rootView.findViewById(R.id.textScrollView);
        aboutus_container = (RelativeLayout) rootView.findViewById(R.id.aboutus_container);
        container = (RelativeLayout) rootView.findViewById(R.id.container);
        link = (TextView) rootView.findViewById(R.id.link);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri adress = Uri.parse("www.teqbuzz.com");
                Intent browser = new Intent(Intent.ACTION_VIEW, adress);
                mActivity.startActivity(browser);
            }
        });
        StikkyHeaderBuilder.stickTo(scrollView)
                .setHeader(R.id.aboutus_container, container)
                .minHeightHeader(100)
                .animator(new ScheduleListStikkyAnimator())
                .build();


        return rootView;
    }

    class ScheduleListStikkyAnimator extends HeaderStikkyAnimator {
        @Override
        public AnimatorBuilder getAnimatorBuilder() {
            View mHeader_image = getHeader().findViewById(R.id.aboutus_image);
            return AnimatorBuilder.create().applyVerticalParallax(mHeader_image);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}
