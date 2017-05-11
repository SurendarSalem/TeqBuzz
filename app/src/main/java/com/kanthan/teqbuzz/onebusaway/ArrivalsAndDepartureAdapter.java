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
import com.kanthan.teqbuzz.utilities.Utility;

import java.util.ArrayList;

/**
 * Created by suren on 8/1/2016.
 */
public class ArrivalsAndDepartureAdapter extends BaseAdapter {
    ArrayList<OBAArrivalDepartureEntity.Data.ArrivalAndDeparture> arrivalAndDepartures;
    Context context;
    LayoutInflater inflater;


    public ArrivalsAndDepartureAdapter(Context context, ArrayList<OBAArrivalDepartureEntity.Data.ArrivalAndDeparture> arrivalAndDepartures) {
        this.arrivalAndDepartures = arrivalAndDepartures;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return arrivalAndDepartures.size();
    }

    @Override
    public OBAArrivalDepartureEntity.Data.ArrivalAndDeparture getItem(int i) {
        return arrivalAndDepartures.get(i);
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



        holder.txt_title.setText("Departure at : " + Utility.getDate(Long.parseLong(arrivalAndDepartures.get(i).getScheduledArrivalTime())));
        holder.vehicle_num.setText("Arrive at : " + Utility.getDate(Long.parseLong(arrivalAndDepartures.get(i).getScheduledDepartureTime())));


        return view;
    }
}
