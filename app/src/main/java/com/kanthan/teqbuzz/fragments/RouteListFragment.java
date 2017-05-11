package com.kanthan.teqbuzz.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kanthan.teqbuzz.R;

/**
 * Created by user on 5/19/2016.
 */
public class RouteListFragment extends Fragment {

    private View rootView;
    private DrawerLayout Drawer;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_routelist, container, false);

        Drawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);        // Drawer object Assigned to the view
       /* mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }


        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
*/
        return rootView;
    }
}
