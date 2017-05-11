package com.kanthan.teqbuzz.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kanthan.teqbuzz.MainActivity;
import com.kanthan.teqbuzz.R;
import com.rey.material.widget.TabPageIndicator;

/**
 * Created by user on 5/3/2016.
 */
public class AuthenticationFragment extends Fragment {

    View rootView;
    TabPageIndicator vehicleTabPageIndicator;
    ViewPager fragment_viewpager;
    String[] titles;
    private Activity mActivity;
    private Fragment loginFragment, signupFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_authentication, container, false);
        titles = mActivity.getResources().getStringArray(R.array.vehicle_detail_titles);
        vehicleTabPageIndicator = (TabPageIndicator) rootView.findViewById(R.id.fragment_page_indicator);
        fragment_viewpager = (ViewPager) rootView.findViewById(R.id.fragment_viewpager);
        fragment_viewpager.setOffscreenPageLimit(2);
        fragment_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                                       @Override
                                                       public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                                       }

                                                       @Override
                                                       public void onPageSelected(int position) {

                                                           if (position == 0) {
                                                               loginFragment.setHasOptionsMenu(true);
                                                               signupFragment.setHasOptionsMenu(false);
                                                           } else if (position == 1) {
                                                               loginFragment.setHasOptionsMenu(false);
                                                               signupFragment.setHasOptionsMenu(true);
                                                           }

                                                       }

                                                       @Override
                                                       public void onPageScrollStateChanged(int state) {

                                                       }
                                                   }

        );

        fragment_viewpager.setAdapter(new AuthenticationAdapter(getChildFragmentManager(), mActivity

        ));
        vehicleTabPageIndicator.setViewPager(fragment_viewpager);

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_dummy, menu);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onPause() {
        super.onPause();
        loginFragment.setHasOptionsMenu(false);
        signupFragment.setHasOptionsMenu(false);

    }


    class AuthenticationAdapter extends FragmentStatePagerAdapter {
        private final String[] titles;

        public AuthenticationAdapter(FragmentManager fm, Activity mActivity) {

            super(fm);
            titles = mActivity.getResources().getStringArray(R.array.authentication_titles);

        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            if (position == 1) {
                signupFragment = new RegisterFragment();
                signupFragment.setHasOptionsMenu(true);
                ((RegisterFragment) signupFragment).setAuthenticationFragment(AuthenticationFragment.this);
                fragment = signupFragment;

            } else if (position == 0) {
                loginFragment = new LoginFragment();
                loginFragment.setHasOptionsMenu(true);
                ((MainActivity)mActivity).setLoginFragment((LoginFragment) loginFragment);
                fragment = loginFragment;
            }

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public void moveToLoginFragment() {
        fragment_viewpager.setCurrentItem(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

    }
}