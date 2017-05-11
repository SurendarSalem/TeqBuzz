package com.kanthan.teqbuzz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kanthan.teqbuzz.Entities.AlarmResponseEntity;
import com.kanthan.teqbuzz.MainActivity;
import com.kanthan.teqbuzz.R;
import com.kanthan.teqbuzz.fragments.BusMapFragment;
import com.kanthan.teqbuzz.models.Schedule;
import com.kanthan.teqbuzz.models.Stop;
import com.kanthan.teqbuzz.models.User;
import com.kanthan.teqbuzz.models.Vehicle;
import com.kanthan.teqbuzz.utilities.AppDatabaseHelper;
import com.kanthan.teqbuzz.utilities.Preferences;
import com.kanthan.teqbuzz.utilities.Utility;
import com.kanthan.teqbuzz.utilities.WebService;

import java.util.ArrayList;


/**
 * Created by user on 8/18/2015.
 */
public class VehicleScheduleAdapter extends BaseAdapter {


    private final BusMapFragment busMapFragment;
    Context context;
    LayoutInflater inflater;
    ArrayList<Schedule> schedules;
    MainActivity mainActivity;
    Preferences appPreferences;
    AppDatabaseHelper appDatabaseHelper;
    private Vehicle selectedVehicle;

    public VehicleScheduleAdapter(Context applicationContext, BusMapFragment busMapFragment, ArrayList<Schedule> schedules) {
        this.context = applicationContext;
        this.schedules = schedules;
        this.busMapFragment = busMapFragment;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainActivity = (MainActivity) applicationContext;
        appPreferences = new Preferences(context);
        appDatabaseHelper = new AppDatabaseHelper(context);
    }


    @Override
    public int getCount() {
        return schedules.size();
    }

    @Override
    public Schedule getItem(int i) {
        return schedules.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setSelectedVehicle(Vehicle selectedVehicle) {
        this.selectedVehicle = selectedVehicle;
    }

    public void onAlarmRequestDone(AlarmResponseEntity alarmResponseEntity) {
        if (alarmResponseEntity != null) {
            String selectedStopId = alarmResponseEntity.getStopId();
            for (Schedule schedule : schedules) {
                if (selectedStopId.equalsIgnoreCase(schedule.getStop_id())) {
                    if (alarmResponseEntity.getIsAlarmSet().equalsIgnoreCase("1"))
                        schedule.setAlarmSet(true);
                    else
                        schedule.setAlarmSet(false);
                    notifyDataSetChanged();
                }
            }
        } else {
            Utility.showToast(mainActivity, mainActivity.getResources().getString(R.string.some_error_occured));
        }
    }

    class Holder {
        TextView stop_name, arrival_time, departure_time, eta;
        ImageView alarm_btn;
        View reach_point, reach_above, reach_below;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final Holder holder;


        if (view == null) {
            holder = new Holder();
            view = inflater.inflate(R.layout.schedule_list_item, viewGroup, false);
            holder.stop_name = (TextView) view.findViewById(R.id.stop_name);
            holder.arrival_time = (TextView) view.findViewById(R.id.arrival_time);
            holder.departure_time = (TextView) view.findViewById(R.id.departure_time);
            holder.alarm_btn = (ImageView) view.findViewById(R.id.bell_img);
            holder.reach_point = (View) view.findViewById(R.id.reach_point);
            holder.reach_above = (View) view.findViewById(R.id.reach_above);
            holder.reach_below = (View) view.findViewById(R.id.reach_below);
            holder.eta = (TextView) view.findViewById(R.id.eta);
            view.setTag(holder);

        } else {
            holder = (Holder) view.getTag();
        }

        String arrival_time = schedules.get(i).getArrival_time();
        String departure_time = schedules.get(i).getDeparture_time();

        holder.stop_name.setText(schedules.get(i).getStop_name());
        holder.eta.setText(schedules.get(i).getEta() + " mins");
        holder.arrival_time.setText(arrival_time.substring(0, arrival_time.length() - 3));
        holder.departure_time.setText(departure_time.substring(0, arrival_time.length() - 3));

        if (!schedules.get(i).isAlarmSet()) {
            holder.alarm_btn.setImageResource(R.drawable.bell_inactive);
        } else {
            holder.alarm_btn.setImageResource(R.drawable.bell_active);
        }

        holder.alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (schedules.get(i).isAlarmSet()) {
                    //if (schedules.get(i).getIs_alarm_set().equalsIgnoreCase(Vehicle.IS_ALARM_SET)) {
                    //schedules.get(i).setIs_alarm_set(Vehicle.IS_ALARM_UNSET);
                    if (appPreferences.isLoginned()) {
                        sendAlarmRequest(schedules.get(i), Schedule.ALARM_NOT_SET);
                        //new AppDatabaseHelper(context).removeReminder(schedules.get(i).getStop_id(), appPreferences.getUser().getUser_id());
                        //schedules.get(i).setAlarmSet(false);
                    } else {

                    }
                    //int alarm_id = schedules.get(i).getAlarm_id();
                    //Utility.cancelAlarm(context, alarm_id);
                } else {

                    if (appPreferences.isLoginned()) {
                        sendAlarmRequest(schedules.get(i), Schedule.ALARM_SET);
                        //new AppDatabaseHelper(context).removeReminder(schedules.get(i).getStop_id(), appPreferences.getUser().getUser_id());
                        //schedules.get(i).setAlarmSet(false);
                    } else {

                    }

                    /*schedules.get(i).setIs_alarm_set(Vehicle.IS_ALARM_SET);
                    schedules.get(i).setAlarmSet(false);
                    appDatabaseHelper.removeReminder(schedules.get(i).getStop_id(), appPreferences.getUser().getUser_id());
                    int alarm_id = (int) System.currentTimeMillis();
                    schedules.get(i).setAlarm_id(alarm_id);
                    Utility.setAlarm(context, schedules.get(i), alarm_id);
                    if (appPreferences.isLoginned()) {
                        new AppDatabaseHelper(context).addReminderForStop(appPreferences.getUser().getUser_id(), schedules.get(i).getStop_id(), schedules.get(i).getSchedule_id());
                        schedules.get(i).setAlarmSet(true);
                    } else {
                        new AppDatabaseHelper(context).addReminderForStop(User.DEFAULT_USER_ID, schedules.get(i).getStop_id(), schedules.get(i).getSchedule_id());
                        schedules.get(i).setAlarmSet(true);
                    }*/
                }

                notifyDataSetChanged();

            }
        });

        if (i == 0)
            holder.reach_above.setAlpha(0);
        else
            holder.reach_above.setAlpha(1);

        if (i == schedules.size() - 1)
            holder.reach_below.setVisibility(View.GONE);
        else
            holder.reach_below.setVisibility(View.VISIBLE);


        return view;
    }

    private void sendAlarmRequest(Schedule schedule, String alarmSet) {
        WebService webService = new WebService(context);
        webService.sendAlarmRequest(schedule, selectedVehicle, busMapFragment, alarmSet);
    }

}
