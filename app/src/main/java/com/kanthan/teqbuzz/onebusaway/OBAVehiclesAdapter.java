package com.kanthan.teqbuzz.onebusaway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.NativeExpressAdView;
import com.kanthan.teqbuzz.R;

import java.util.ArrayList;

/**
 * Created by suren on 8/1/2016.
 */
public class OBAVehiclesAdapter extends BaseAdapter {
    ArrayList<OBATripsForLocationEntity.Data.Trip> obaTripsForLocations;
    Context context;
    LayoutInflater inflater;


    public OBAVehiclesAdapter(Context context, ArrayList<OBATripsForLocationEntity.Data.Trip> obaTripsForLocations) {
        this.obaTripsForLocations = obaTripsForLocations;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return obaTripsForLocations.size();
    }

    @Override
    public OBATripsForLocationEntity.Data.Trip getItem(int i) {
        return obaTripsForLocations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class Holder {
        TextView txt_title, vehicle_num;
        ImageView img_icon;
        NativeExpressAdView adView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Holder holder;


        if (view == null) {
            holder = new Holder();
            view = inflater.inflate(R.layout.vehicle_list_item, viewGroup, false);
            holder.txt_title = (TextView) view.findViewById(R.id.next_stop);
            holder.vehicle_num = (TextView) view.findViewById(R.id.vehicle_num);
            holder.img_icon = (ImageView) view.findViewById(R.id.vehicle_icon);
            // holder.adView = (NativeExpressAdView) view.findViewById(R.id.ad);
            view.setTag(holder);

        } else {
            holder = (Holder) view.getTag();
        }


        holder.txt_title.setText("Bus no : " + obaTripsForLocations.get(i).getStatus().getVehicleId());
        holder.vehicle_num.setText("Bus no : " + obaTripsForLocations.get(i).getStatus().getVehicleId());


        return view;
    }
}
