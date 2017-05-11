package com.kanthan.teqbuzz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kanthan.teqbuzz.R;
import com.kanthan.teqbuzz.fragments.BusMapFragment;
import com.kanthan.teqbuzz.fragments.SettingsFragment;
import com.kanthan.teqbuzz.models.Vehicle;
import com.rey.material.widget.CheckBox;

import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.ArrayList;


/**
 * Created by user on 8/18/2015.
 */
public class SharedVehicleListAdapter extends BaseAdapter {


    Context context;
    LayoutInflater inflater;
    ArrayList<Vehicle> sharedVehicles;
    private int selectedPosition;
    BusMapFragment settingsFragment;
    private int count;

    public SharedVehicleListAdapter(Context applicationContext, BusMapFragment settingsFragment, ArrayList<Vehicle> sharedVehicles) {
        this.context = applicationContext;
        this.sharedVehicles = sharedVehicles;
        this.settingsFragment = settingsFragment;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.settingsFragment = settingsFragment;
    }

    public ArrayList<Vehicle> getSharedVehicles() {
        return sharedVehicles;
    }


    @Override
    public int getCount() {
        return sharedVehicles.size();
    }

    @Override
    public Vehicle getItem(int i) {
        return sharedVehicles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class Holder {
        TextView bus_number, bus_agency;
        CheckBox select;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        Holder holder;

        if (view == null) {
            holder = new Holder();
            view = inflater.inflate(R.layout.shared_vehicle_item, viewGroup, false);
            holder.bus_number = (TextView) view.findViewById(R.id.bus_number);
            holder.bus_agency = (TextView) view.findViewById(R.id.bus_agency);
            holder.select = (CheckBox) view.findViewById(R.id.select);
            view.setTag(R.id.select, holder.select);
            view.setTag(holder);

        } else {
            holder = (Holder) view.getTag();
        }

        holder.select.setTag(i); // This line is important.
        Vehicle vehicle = getVehicle(i);

        holder.bus_number.setText("Bus No : " + vehicle.getVehicle_line_number());
        holder.bus_agency.setText("To : " + vehicle.getVehicle_agency());
        holder.select.setCheckedImmediately(vehicle.isSelected);

        /*holder.select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sharedVehicles.get(i).setSelected(b);
                notifyDataSetChanged();
                if (b) {
                    count++;
                } else {
                    count--;
                }
                settingsFragment.showSharedVehicleDeleteBtn(count);
            }
        });*/
        holder.select.setOnCheckedChangeListener(myCheckChangList);

        return view;
    }

    Vehicle getVehicle(int position) {
        return ((Vehicle) getItem(position));
    }

    OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            getVehicle((Integer) buttonView.getTag()).isSelected = isChecked;
            notifyDataSetChanged();
            if (isChecked) {
                count++;
            } else {
                count--;
            }
            settingsFragment.showSharedVehicleDeleteBtn(sharedVehicles);
        }
    };
}
