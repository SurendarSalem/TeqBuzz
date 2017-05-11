package com.kanthan.teqbuzz.GoogleAdmob;

import android.content.Context;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.formats.NativeAd;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suren on 10/21/2016.
 */
public class AdmobFetcher {
    private static final int PREFETCHED_ADS_SIZE = 2;
    private static final int MAX_FETCH_ATTEMPT = 4;

    private AdLoader adLoader;
    private List<NativeAd> mPrefetchedAdList = new ArrayList<NativeAd>();
    private Map<Integer, NativeAd> adMapAtIndex = new HashMap<Integer, NativeAd>();
    private int mNoOfFetchedAds;
    private int mFetchFailCount;
    private WeakReference<Context> mContext = new WeakReference<Context>(null);
    private String admobReleaseUnitId;
}
