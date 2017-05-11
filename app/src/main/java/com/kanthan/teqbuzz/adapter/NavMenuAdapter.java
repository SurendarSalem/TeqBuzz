package com.kanthan.teqbuzz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kanthan.teqbuzz.R;
import com.kanthan.teqbuzz.models.NavItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by user on 8/18/2015.
 */
public class NavMenuAdapter extends BaseAdapter {


    Context context;
    LayoutInflater inflater;
    ArrayList<NavItem> navItems;
    private int selectedPosition;

    public NavMenuAdapter(Context applicationContext, ArrayList<NavItem> navItems) {
        this.context = applicationContext;
        this.navItems = navItems;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return navItems.size();
    }

    @Override
    public NavItem getItem(int i) {
        return navItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setSelecedPosition(int i) {

        selectedPosition = i;
    }

    class Holder {
        TextView txt_title;
        ImageView img_icon, nav_arrow;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Holder holder;


        if (view == null) {
            holder = new Holder();
            view = inflater.inflate(R.layout.nav_list_item, viewGroup, false);
            holder.txt_title = (TextView) view.findViewById(R.id.nav_title);
            holder.img_icon = (ImageView) view.findViewById(R.id.nav_icon);
            holder.nav_arrow = (ImageView) view.findViewById(R.id.right_arrow);
            view.setTag(holder);

        } else {
            holder = (Holder) view.getTag();

        }
        holder.txt_title.setText(navItems.get(i).getTitle());
        Picasso.with(context).load(navItems.get(i).getIcon()).fit().centerCrop().into(holder.img_icon);

        if (selectedPosition == i) {
            view.setBackgroundColor(Color.parseColor("#D2D3D6"));
            holder.nav_arrow.setVisibility(View.VISIBLE);
            holder.txt_title.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.img_icon.setColorFilter(context.getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {

            view.setBackgroundColor(Color.WHITE);
            holder.nav_arrow.setVisibility(View.GONE);
            holder.txt_title.setTextColor(context.getResources().getColor(R.color.black));
            holder.img_icon.setColorFilter(context.getResources().getColor(R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        return view;
    }
}
