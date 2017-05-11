/*
 *  Copyright 2015 Yahoo Inc. All rights reserved.
 * Copyright 2015 Clockbyte LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kanthan.teqbuzz.GoogleAdmob.admobadapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.Collection;
import java.util.Collections;

/**
 * Adapter that has common functionality for any adapters that need to show ads in-between
 * other data.
 */
public class AdmobExpressAdapterWrapper extends BaseAdapter implements AdmobFetcherBase.AdmobListener {

    private final String TAG = AdmobExpressAdapterWrapper.class.getCanonicalName();

    private BaseAdapter mAdapter;

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(new DataSetObserver() {
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

    private AdmobFetcherExpress adFetcher;
    private Context mContext;
    private AdmobAdapterCalculator AdapterCalculator = new AdmobAdapterCalculator();
    /*
    * Gets an object which incapsulates transformation of the source and ad blocks indices
    */
    public AdmobAdapterCalculator getAdapterCalculator(){return AdapterCalculator;}
    /*
* Injects an object which incapsulates transformation of the source and ad blocks indices. You could override calculations
* by inheritance of AdmobAdapterCalculator class
*/
    public void setAdapterCalculator(AdmobAdapterCalculator adapterCalculatordmob){AdapterCalculator = adapterCalculatordmob;}

    private static final int VIEW_TYPE_COUNT = 1;
    private static final int VIEW_TYPE_AD_EXPRESS = 0;

    private final static int DEFAULT_NO_OF_DATA_BETWEEN_ADS = 10;
    private final static int DEFAULT_LIMIT_OF_ADS = 3;
    private static final AdSize DEFAULT_AD_SIZE = new AdSize(AdSize.FULL_WIDTH, 150);

    /**
     * Gets the number of ads that have been fetched so far.
     *
     * @return the number of ads that have been fetched
     */
    public int getFetchedAdsCount() {
        return adFetcher.getFetchedAdsCount();
    }

    private int getViewTypeAdExpress(){
        return mAdapter.getViewTypeCount() + VIEW_TYPE_AD_EXPRESS;
    }

    /*
    * Gets the number of your data items between ad blocks, by default it equals to 10.
    * You should set it according to the Admob's policies and rules which says not to
    * display more than one ad block at the visible part of the screen
    * so you should choose this parameter carefully and according to your item's height and screen resolution of a target devices
    */
    public int getNoOfDataBetweenAds() {
        return AdapterCalculator.getNoOfDataBetweenAds();
    }
    /*
    * Sets the number of your data items between ad blocks, by default it equals to 10.
    * You should set it according to the Admob's policies and rules which says not to
    * display more than one ad block at the visible part of the screen
    * so you should choose this parameter carefully and according to your item's height and screen resolution of a target devices
    */
    public void setNoOfDataBetweenAds(int mNoOfDataBetweenAds) {
        AdapterCalculator.setNoOfDataBetweenAds(mNoOfDataBetweenAds);
    }

    public int getFirstAdIndex() {
        return AdapterCalculator.getFirstAdIndex();
    }
    /*
    * Sets the first ad block index (zero-based) in the adapter, by default it equals to 0
    */
    public void setFirstAdIndex(int firstAdIndex) {
        AdapterCalculator.setFirstAdIndex(firstAdIndex);
    }

    /*
    * Gets the max count of ad blocks per dataset, by default it equals to 3 (according to the Admob's policies and rules)
    */
    public int getLimitOfAds() {
        return AdapterCalculator.getLimitOfAds();
    }

    /*
    * Sets the max count of ad blocks per dataset, by default it equals to 3 (according to the Admob's policies and rules)
    */
    public void setLimitOfAds(int mLimitOfAds) {
        AdapterCalculator.setLimitOfAds(mLimitOfAds);
    }

    private AdSize mAdSize;

    /**
     * Use this constructor for test purposes. if you are going to release the live version
     * please use the appropriate constructor
     * @see #AdmobExpressAdapterWrapper(Context, String)
     * @param testDevicesId sets a devices ID to test ads interaction.
     * You could pass null but it's better to set ids for all your test devices
     * including emulators. for emulator just use the
     * @see {AdRequest.DEVICE_ID_EMULATOR}
     */
    public AdmobExpressAdapterWrapper(Context context, String[] testDevicesId) {
        init(context, null, testDevicesId, null);
    }
    /**
     * @param admobReleaseUnitId sets a release unit ID for admob banners.
     * If you are testing the ads please use constructor for tests
     * @see #AdmobExpressAdapterWrapper(Context, String[])
     * ID should be active, please check it in your Admob's account.
     * Be careful: don't set it or set to null if you still haven't deployed a Release.
     * Otherwise your Admob account could be banned
     */
    public AdmobExpressAdapterWrapper(Context context, String admobReleaseUnitId) {
        this(context, admobReleaseUnitId, null, null);
    }

    /**
     * @param admobReleaseUnitIds sets a collection of release unit IDs for admob banners.
     * It works like FIFO (first in = first out). Each ad block will get one from the queue.
     * If the desired count of ad blocks is greater than this collection size
     * then the last entry will be duplicated to remaining ad blocks.
     * If you are testing the ads please use constructor for tests
     * @see #AdmobExpressAdapterWrapper(Context, String[])
     * ID should be active, please check it in your Admob's account.
     * Be careful: don't set it or set to null if you still haven't deployed a Release.
     * Otherwise your Admob account could be banned
     */
    public AdmobExpressAdapterWrapper(Context context, Collection<String> admobReleaseUnitIds) {
        this(context, admobReleaseUnitIds, null, null);
    }

    /**
     * @param admobReleaseUnitId sets a release unit ID for admob banners.
     * ID should be active, please check it in your Admob's account.
     * Be careful: don't set it or set to null if you still haven't deployed a Release.
     * Otherwise your Admob account could be banned
     * @param testDevicesId sets a devices ID to test ads interaction.
     * You could pass null but it's better to set ids for all your test devices
     * including emulators. for emulator just use the
     * @see {AdRequest.DEVICE_ID_EMULATOR}
     */
    public AdmobExpressAdapterWrapper(Context context, String admobReleaseUnitId, String[] testDevicesId) {
        this(context, admobReleaseUnitId, testDevicesId, null);
    }

    /**
     * @param admobReleaseUnitIds sets a collection of release unit IDs for admob banners.
     * It works like FIFO (first in = first out). Each ad block will get one from the queue.
     * If the desired count of ad blocks is greater than this collection size
     * then the last entry will be duplicated to remaining ad blocks.
     * ID should be active, please check it in your Admob's account.
     * Be careful: don't set it or set to null if you still haven't deployed a Release.
     * Otherwise your Admob account could be banned
     * @param testDevicesId sets a devices ID to test ads interaction.
     * You could pass null but it's better to set ids for all your test devices
     * including emulators. for emulator just use the
     * @see {AdRequest.DEVICE_ID_EMULATOR}
     */
    public AdmobExpressAdapterWrapper(Context context, Collection<String> admobReleaseUnitIds, String[] testDevicesId) {
        this(context, admobReleaseUnitIds, testDevicesId, null);
    }

    /**
     * @param admobReleaseUnitId sets a release unit ID for admob banners.
     * If you are testing the ads please use constructor for tests
     * @see #AdmobExpressAdapterWrapper(Context, String[])
     * ID should be active, please check it in your Admob's account.
     * Be careful: don't set it or set to null if you still haven't deployed a Release.
     * Otherwise your Admob account could be banned
     * @param testDevicesId sets a devices ID to test ads interaction.
     * You could pass null but it's better to set ids for all your test devices
     * including emulators. for emulator just use the
     * @see {AdRequest.DEVICE_ID_EMULATOR}
     * @param adSize sets ad size. By default it equals to AdSize(AdSize.FULL_WIDTH, 150);
     */
    public AdmobExpressAdapterWrapper(Context context, String admobReleaseUnitId, String[] testDevicesId, AdSize adSize) {
        Collection<String> releaseUnitIds = admobReleaseUnitId==null
                ? null
                : Collections.singletonList(admobReleaseUnitId);
        init(context, releaseUnitIds, testDevicesId, adSize);
    }

    /**
     * @param admobReleaseUnitIds sets a collection of release unit IDs for admob banners.
     * It works like FIFO (first in = first out). Each ad block will get one from the queue.
     * If the desired count of ad blocks is greater than this collection size
     * then the last entry will be duplicated to remaining ad blocks.
     * ID should be active, please check it in your Admob's account.
     * Be careful: don't set it or set to null if you still haven't deployed a Release.
     * Otherwise your Admob account could be banned
     * @param testDevicesId sets a devices ID to test ads interaction.
     * You could pass null but it's better to set ids for all your test devices
     * including emulators. for emulator just use the
     * @see {AdRequest.DEVICE_ID_EMULATOR}
     * @param adSize sets ad size. By default it equals to AdSize(AdSize.FULL_WIDTH, 150);
     */
    public AdmobExpressAdapterWrapper(Context context, Collection<String> admobReleaseUnitIds, String[] testDevicesId, AdSize adSize) {
        init(context, admobReleaseUnitIds, testDevicesId, adSize);
    }

    /**
     * Use this constructor for test purposes. if you are going to release the live version
     * please use the appropriate constructor
     * @see #AdmobExpressAdapterWrapper(Context, String, AdSize)
     * @param testDevicesId sets a devices ID to test ads interaction.
     * You could pass null but it's better to set ids for all your test devices
     * including emulators. for emulator just use the
     * @see {AdRequest.DEVICE_ID_EMULATOR}
     * @param adSize sets ad size. By default it equals to AdSize(AdSize.FULL_WIDTH, 150);
     */
    public AdmobExpressAdapterWrapper(Context context, String[] testDevicesId, AdSize adSize) {
        init(context, null, testDevicesId, adSize);
    }
    /**
     * @param admobReleaseUnitId sets a release unit ID for admob banners.
     * If you are testing the ads please use constructor for tests
     * @see #AdmobExpressAdapterWrapper(Context, String[], AdSize)
     * ID should be active, please check it in your Admob's account.
     * Be careful: don't set it or set to null if you still haven't deployed a Release.
     * Otherwise your Admob account could be banned
     * @param adSize sets ad size. By default it equals to AdSize(AdSize.FULL_WIDTH, 150);
     */
    public AdmobExpressAdapterWrapper(Context context, String admobReleaseUnitId, AdSize adSize) {
        this(context, admobReleaseUnitId, null, adSize);
    }

    /**
     * @param admobReleaseUnitIds sets a collection of release unit IDs for admob banners.
     * It works like FIFO (first in = first out). Each ad block will get one from the queue.
     * If the desired count of ad blocks is greater than this collection size
     * then the last entry will be duplicated to remaining ad blocks.
     * If you are testing the ads please use constructor for tests
     * @see #AdmobExpressAdapterWrapper(Context, String[], AdSize)
     * ID should be active, please check it in your Admob's account.
     * Be careful: don't set it or set to null if you still haven't deployed a Release.
     * Otherwise your Admob account could be banned
     * @param adSize sets ad size. By default it equals to AdSize(AdSize.FULL_WIDTH, 150);
     */
    public AdmobExpressAdapterWrapper(Context context, Collection<String> admobReleaseUnitIds, AdSize adSize) {
        this(context, admobReleaseUnitIds, null, adSize);
    }

    private void init(Context context, Collection<String> admobReleaseUnitIds, String[] testDevicesId, AdSize adSize) {
        setNoOfDataBetweenAds(DEFAULT_NO_OF_DATA_BETWEEN_ADS);
        setLimitOfAds(DEFAULT_LIMIT_OF_ADS);

        this.mAdSize = adSize==null?DEFAULT_AD_SIZE:adSize;
        mContext = context;

        adFetcher = new AdmobFetcherExpress(mContext);
        if(testDevicesId!=null)
            for (String testId: testDevicesId)
                adFetcher.addTestDeviceId(testId);
        adFetcher.addListener(this);
        adFetcher.createUnitIdsQueue(admobReleaseUnitIds);

        prefetchAds(AdmobFetcherExpress.PREFETCHED_ADS_SIZE);
    }

    /**
     * Will start async prefetch of ad block to use its further
     * @return last created NativeExpressAdView
     */
    private NativeExpressAdView prefetchAds(int cntToPrefetch){
        NativeExpressAdView last = null;
        for (int i = 0; i < cntToPrefetch; i++){
            final NativeExpressAdView item = AdViewHelper.getExpressAdView(mContext, this.mAdSize,
                    adFetcher.dequeueUnitId());
            adFetcher.setupAd(item);
            //2 sec throttling to prevent a high-load of server
            new Handler(mContext.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    adFetcher.fetchAd(item);
                }
            }, 2000*i);
            last = item;
        }
        return last;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(getItemViewType(position) == getViewTypeAdExpress()) {
            int adPos = AdapterCalculator.getAdIndex(position);
            NativeExpressAdView item = adFetcher.getAdForIndex(adPos);
            if (item == null)
                item = prefetchAds(1);
            return item;
        }
        else{
            int origPos = AdapterCalculator.getOriginalContentPosition(position,
                    adFetcher.getFetchedAdsCount(), mAdapter.getCount());
            return mAdapter.getView(origPos, convertView, parent);
        }
    }

    /**
     * <p>Gets the count of all data, including interspersed ads.</p>
     * <p/>
     * <p>If data size is 10 and an ad is to be showed after every 5 items starting at the index 0, this method
     * will return 12.</p>
     *
     * @return the total number of items this adapter can show, including ads.
     * @see AdmobExpressAdapterWrapper#setNoOfDataBetweenAds(int)
     * @see AdmobExpressAdapterWrapper#getNoOfDataBetweenAds()
     */
    @Override
    public int getCount() {

        if (mAdapter != null) {
            /* Cnt of currently fetched ads, as long as it isn't more than no of max ads that can
            fit dataset. */
            int noOfAds = AdapterCalculator.getAdsCountToPublish(adFetcher.getFetchedAdsCount(), mAdapter.getCount());
            return mAdapter.getCount() > 0 ? mAdapter.getCount() + noOfAds : 0;
        } else {
            return 0;
        }
    }

    /**
     * Gets the item in a given position in the dataset. If an ad is to be returned,
     * a {@link NativeExpressAdView} object is returned.
     *
     * @param position the adapter position
     * @return the object or ad contained in this adapter position
     */
    @Override
    public Object getItem(int position) {
        if (AdapterCalculator.canShowAdAtPosition(position, adFetcher.getFetchedAdsCount())) {
            int adPos = AdapterCalculator.getAdIndex(position);
            return adFetcher.getAdForIndex(adPos);
        } else {
            int origPos = AdapterCalculator.getOriginalContentPosition(position,
                    adFetcher.getFetchedAdsCount(), mAdapter.getCount());
            return mAdapter.getItem(origPos);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT + getAdapter().getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        checkNeedFetchAd(position);
        if (AdapterCalculator.canShowAdAtPosition(position, adFetcher.getFetchedAdsCount())) {
            return getViewTypeAdExpress();
        } else {
            int origPos = AdapterCalculator.getOriginalContentPosition(position,
                    adFetcher.getFetchedAdsCount(), mAdapter.getCount());
            return mAdapter.getItemViewType(origPos);
        }
    }

    private void checkNeedFetchAd(int position){
        if(AdapterCalculator.hasToFetchAd(position, adFetcher.getFetchingAdsCount()))
            prefetchAds(1);
    }

    /**
     * Destroys all currently fetched ads
     */
    public void destroyAds() {
        adFetcher.destroyAllAds();
    }

    /**
     * Clears all currently displaying ads to update them
     */
    public void requestUpdateAd() {
        adFetcher.updateFetchedAds();
    }

    @Override
    public void onAdChanged(int adIdx) {
        notifyDataSetChanged();
    }

    /**
     * Raised when the number of ads have changed. Adapters that implement this class
     * should notify their data views that the dataset has changed.
     */
    @Override
    public void onAdChanged() {
        notifyDataSetChanged();
    }

}
