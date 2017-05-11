package com.kanthan.teqbuzz.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.kanthan.teqbuzz.MainActivity;
import com.kanthan.teqbuzz.R;
import com.kanthan.teqbuzz.fragments.BusMapFragment;
import com.kanthan.teqbuzz.models.Vehicle;
import com.kanthan.teqbuzz.utilities.AppDatabaseHelper;
import com.kanthan.teqbuzz.utilities.Preferences;
import com.kanthan.teqbuzz.utilities.Utility;
import com.kanthan.teqbuzz.utilities.WebService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by user on 8/18/2015.
 */
public class VehicleListAdapter extends BaseAdapter {


    Context context;
    LayoutInflater inflater;
    ArrayList<Vehicle> vehicles;
    BusMapFragment busMapFragment;
    private String selectedVehicleId;
    Preferences myPreferences;
    double userLatitude, userLongitude;
    AppDatabaseHelper teqBuzzDatabaseHelper;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    int AD_POSITION = 4;
    private boolean isAdsLoaded, isFavorite;

    public VehicleListAdapter(BusMapFragment busMapFragment, final Context applicationContext, ArrayList<Vehicle> vehicles) {
        this.context = applicationContext;
        this.vehicles = vehicles;
        this.busMapFragment = busMapFragment;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myPreferences = new Preferences(this.context.getApplicationContext());
        userLatitude = Double.parseDouble(myPreferences.getUser().getTemp_latitude());
        userLongitude = Double.parseDouble(myPreferences.getUser().getTemp_longitude());
        teqBuzzDatabaseHelper = new AppDatabaseHelper(this.context);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        myPreferences = new Preferences(context.getApplicationContext());
    }

    @Override
    public int getCount() {
        return vehicles.size();
    }

    @Override
    public Vehicle getItem(int i) {
        return vehicles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setSelectedVehicle(String vehicle_id) {
        selectedVehicleId = vehicle_id;
    }

    public String getSelectedVehicle() {
        return selectedVehicleId;
    }

    public void onAdsLoaded(NativeAd nativeAd) {
    }

    public void sortDistanceWise() {
        /*Collections.sort(vehicles, new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle p1, Vehicle p2) {
                int distance1 = (int) Double.parseDouble(p1.getDistance());
                int distance2 = (int) Double.parseDouble(p2.getDistance());
                return distance1 - distance2; // Ascending
            }
        });
        //return vehicles;
        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });*/
        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vehicles = sort();
                notifyDataSetChanged();
            }
        });

    }

    private ArrayList<Vehicle> sort() {
        Comparator<Vehicle> myComparator = new Comparator<Vehicle>() {
            public int compare(Vehicle obj1, Vehicle obj2) {
                return obj2.getDistance().compareTo(obj1.getDistance());
            }
        };

        Collections.sort(vehicles, myComparator);
        return vehicles;
    }

    public void setData(ArrayList<Vehicle> teqBuzzVehicles) {
        this.vehicles = teqBuzzVehicles;
    }

    public static class Holder {
        RelativeLayout vehicle_list_container;
        TextView next_stop, vehicle_num, now_stop, vehicleDistance, fromStopTV, toStopTV, vehicleAgency;
        ImageView img_icon, favIcon, clearSelected;
        NativeExpressAdView adView;
        int position;
        CardView busCardView;
        RelativeLayout sub_container, click_view;

        /*Ads declaration*/
        CardView adCardView;
        ImageView nativeAdIcon, adCoverImage;
        TextView nativeAdTitle;
        TextView nativeAdBody;
        MediaView nativeAdMedia;
        TextView nativeAdSocialContext;
        Button nativeAdCallToAction;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final Holder holder;
        Vehicle vehicle = vehicles.get(i);

        if (view == null) {

            holder = new Holder();
            holder.position = i;
            view = inflater.inflate(R.layout.vehicle_list_item, viewGroup, false);

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
            view.startAnimation(animation);
            holder.fromStopTV = (TextView) view.findViewById(R.id.from_stop);
            holder.toStopTV = (TextView) view.findViewById(R.id.to_stop);
            holder.vehicle_list_container = (RelativeLayout) view.findViewById(R.id.vehicle_list_container);
            holder.busCardView = (CardView) view.findViewById(R.id.bus_card);
            holder.next_stop = (TextView) view.findViewById(R.id.next_stop);
            holder.vehicle_num = (TextView) view.findViewById(R.id.vehicle_num);
            holder.now_stop = (TextView) view.findViewById(R.id.now_stop);
            holder.vehicleDistance = (TextView) view.findViewById(R.id.distance);
            holder.vehicleAgency = (TextView) view.findViewById(R.id.busDistance);
            holder.img_icon = (ImageView) view.findViewById(R.id.vehicle_icon);
            holder.favIcon = (ImageView) view.findViewById(R.id.favIcon);
            holder.clearSelected = (ImageView) view.findViewById(R.id.clear_selected);
            holder.sub_container = (RelativeLayout) view.findViewById(R.id.sub_container);
            holder.click_view = (RelativeLayout) view.findViewById(R.id.click_view);

            // holder.adView = (NativeExpressAdView) view.findViewById(R.id.ad);

            try {
             /*   Picasso.with(context).load(WebService.VEHICLE_IMAGE_URL + vehicles.get(i).getImages()).fit().
                        error(R.drawable.bus_white).into(holder.img_icon);
*/
            } catch (IndexOutOfBoundsException e) {

            }


            /*Ads declaration*/
            holder.adCardView = (CardView) view.findViewById(R.id.ad_card);
            holder.nativeAdIcon = (ImageView) view.findViewById(R.id.native_ad_icon);
            //holder.adCoverImage = (ImageView) view.findViewById(R.id.ad_cover_image);
            holder.nativeAdTitle = (TextView) view.findViewById(R.id.native_ad_title);
            holder.nativeAdBody = (TextView) view.findViewById(R.id.native_ad_body);
            holder.nativeAdMedia = (MediaView) view.findViewById(R.id.native_ad_media);
            holder.nativeAdSocialContext = (TextView) view.findViewById(R.id.native_ad_social_context);
            holder.nativeAdCallToAction = (Button) view.findViewById(R.id.native_ad_call_to_action);
            /*Ads declaration*/

            showNativeAd(holder.adCardView, holder);
            view.setTag(holder);

        } else {
            holder = (Holder) view.getTag();
        }

        try {


            holder.vehicle_num.setText(vehicle.getVehicle_line_number());// + " " + String.valueOf(vehicles.get(i).isMoving()));

            holder.vehicleDistance.setText(vehicle.getDistance() + " km");

            holder.vehicleAgency.setText(vehicle.getVehicle_agency());
            if (vehicle.getStart_stop() != null && !vehicle.getStart_stop().equalsIgnoreCase("")) {
                holder.fromStopTV.setText("From - " + vehicle.getStart_stop());
            } else {
                holder.fromStopTV.setText("From - " + "Not available");
            }

            if (vehicle.getEnd_stop() != null && !vehicle.getEnd_stop().equalsIgnoreCase("")) {
                holder.toStopTV.setText("To - " + vehicle.getEnd_stop());
            } else {
                holder.toStopTV.setText("To - " + "Not available");
            }

            if (vehicle.getNext_stop() != null && !vehicle.getNext_stop().equalsIgnoreCase("")) {
                holder.next_stop.setText("Next Stop - " + vehicle.getNext_stop());
            } else {
                holder.next_stop.setText("Next Stop - " + "Not available");
            }

            if (vehicle.getPrev_stop() != null && !vehicle.getPrev_stop().equalsIgnoreCase("")) {
                holder.now_stop.setText("Previous stop - " + vehicle.getPrev_stop());
            } else {
                holder.now_stop.setText("Previous stop - " + "Not available");
            }


            /*if (teqBuzzDatabaseHelper.isVehicleFav(vehicle) && myPreferences.isLoginned()) {
                holder.favIcon.setVisibility(View.VISIBLE);
            } else {
                holder.favIcon.setVisibility(View.GONE);
            }*/

            if (vehicle.isFavourite()/* || teqBuzzDatabaseHelper.isVehicleFav(vehicle)*/) {
                holder.favIcon.setVisibility(View.VISIBLE);
            } else {
                holder.favIcon.setVisibility(View.GONE);
            }
            /*if (vehicle.isSelected() && busMapFragment.getSelectedBusRouteLineLine() != null) {
                holder.clearSelected.setVisibility(View.VISIBLE);
            } else {
                holder.clearSelected.setVisibility(View.GONE);
            }*/

        } catch (IndexOutOfBoundsException e) {

        }


        if (i % AD_POSITION != 0 || i == 0) {
            holder.adCardView.setVisibility(View.GONE);
        } else {
            if (isAdsLoaded)
                holder.adCardView.setVisibility(View.VISIBLE);
            else
                holder.adCardView.setVisibility(View.GONE);
        }


        holder.click_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busMapFragment.onBusListItemSelcted(i);
            }
        });
        holder.clearSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busMapFragment.clearSelectedVehicle();
                vehicles.get(i).setSelected(false);
                notifyDataSetChanged();
            }
        });

        return view;
    }


    private View showNativeAd(final View fbAdView, final Holder holder) {
        AdSettings.addTestDevice("54502114bc8a5a9c70587ba8088d6d46");
        final NativeAd nativeAd = new NativeAd(context, "1112849102102984_1112852525435975");


        nativeAd.setAdListener(new com.facebook.ads.AdListener() {

            AdChoicesView adChoicesView;

            @Override
            public void onError(Ad ad, AdError error) {
                isAdsLoaded = false;
                Log.e("suren ", error.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (ad != nativeAd) {
                    return;
                }
                isAdsLoaded = true;
                // Setting the Text.
                holder.nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                holder.nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
                holder.nativeAdTitle.setText(nativeAd.getAdTitle());
                holder.nativeAdBody.setText(nativeAd.getAdBody());

                // Downloading and setting the ad icon.
                NativeAd.Image adIcon = nativeAd.getAdIcon();
                NativeAd.downloadAndDisplayImage(adIcon, holder.nativeAdIcon);

                // Download and setting the cover image.
                //NativeAd.Image adCoverImage = nativeAd.getAdCoverImage();
                //nativeAd.downloadAndDisplayImage(adCoverImage, holder.adCoverImage);
                holder.nativeAdMedia.setNativeAd(nativeAd);

                // Add adChoices icon
                if (adChoicesView == null) {
                    adChoicesView = new AdChoicesView(context, nativeAd, true);
                    //fbAdView.addView(adChoicesView, 0);
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

    @Override
    public void notifyDataSetInvalidated() {
        //sortDistanceWise();
        super.notifyDataSetInvalidated();
        Log.d("VehicleListAdapter", "notifyDataSetInvalidated()");
        Log.d("Sorting", "notified");

    }

}

