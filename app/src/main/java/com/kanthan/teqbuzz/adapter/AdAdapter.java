package com.kanthan.teqbuzz.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.google.android.gms.ads.NativeExpressAdView;
import com.kanthan.teqbuzz.R;
import com.kanthan.teqbuzz.fragments.BusMapFragment;
import com.kanthan.teqbuzz.models.Vehicle;
import com.kanthan.teqbuzz.utilities.AppDatabaseHelper;
import com.kanthan.teqbuzz.utilities.Preferences;
import com.kanthan.teqbuzz.utilities.Utility;
import com.kanthan.teqbuzz.utilities.WebService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by user on 8/18/2015.
 */
public class AdAdapter extends BaseAdapter {


    Context context;
    LayoutInflater inflater;
    ArrayList<Vehicle> vehicles;
    BusMapFragment busMapFragment;
    private String selectedVehicleId;
    Preferences myPreferences;
    double userLatitude, userLongitude;
    AppDatabaseHelper teqBuzzDatabaseHelper;
    int OFFSET = 3;

    public AdAdapter(BusMapFragment busMapFragment, final Context applicationContext, ArrayList<Vehicle> vehicles) {
        this.context = applicationContext;
        this.vehicles = new ArrayList<Vehicle>();
        this.vehicles.addAll(vehicles);
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

    public static class Holder {
        RelativeLayout vehicle_list_container;
        TextView txt_title, vehicle_num, vehicle_place, vehicleDistance, fromStopTV, toStopTV;
        ImageView img_icon, favIcon;
        //NativeExpressAdView adView;
        int position;
        CardView busCardView;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        Holder holder;


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
            holder.txt_title = (TextView) view.findViewById(R.id.next_stop);
            holder.vehicle_num = (TextView) view.findViewById(R.id.vehicle_num);
            holder.vehicle_place = (TextView) view.findViewById(R.id.now_stop);
            holder.vehicleDistance = (TextView) view.findViewById(R.id.busDistance);
            holder.img_icon = (ImageView) view.findViewById(R.id.vehicle_icon);
            holder.favIcon = (ImageView) view.findViewById(R.id.favIcon);
            // holder.adView = (NativeExpressAdView) view.findViewById(R.id.ad);

            try {
                Picasso.with(context).load(WebService.VEHICLE_IMAGE_URL + vehicles.get(i).getImages()).fit().
                        error(R.drawable.bus_white).into(holder.img_icon);

            } catch (IndexOutOfBoundsException e) {

            }

            view.setTag(holder);

        } else {
            holder = (Holder) view.getTag();
        }

        try {


            holder.vehicle_list_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    busMapFragment.onBusListItemSelcted(i);
                }
            });
            holder.txt_title.setText("Next Stop is Hosur");
            holder.vehicle_num.setText(String.valueOf(i) + " M-" + vehicles.get(i).getVehicle_id());// + " " + String.valueOf(vehicles.get(i).isMoving()));

            double vehicleLatitude = Double.parseDouble(vehicles.get(i).getLatitude());
            double vehicleLongitude = Double.parseDouble(vehicles.get(i).getLongitude());

            double busDistance = Utility.calculateDistance(userLatitude, userLongitude, vehicleLatitude, vehicleLongitude);

            holder.vehicleDistance.setText("" + Math.round(busDistance) + " km");

            if (teqBuzzDatabaseHelper.isVehicleFav(vehicles.get(i)) && myPreferences.isLoginned()) {
                holder.favIcon.setVisibility(View.VISIBLE);
            } else {
                holder.favIcon.setVisibility(View.GONE);
            }

        } catch (IndexOutOfBoundsException e) {

        }

        return view;
    }


}
