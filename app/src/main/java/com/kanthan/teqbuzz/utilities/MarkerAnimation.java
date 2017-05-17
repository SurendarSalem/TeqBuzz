/* Copyright 2013 Google Inc.
   Licensed under Apache 2.0: http://www.apache.org/licenses/LICENSE-2.0.html */

package com.kanthan.teqbuzz.utilities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.Property;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.kanthan.teqbuzz.fragments.BusMapFragment;
import com.kanthan.teqbuzz.models.Vehicle;

public class MarkerAnimation {
    public static void animateMarkerToGB(final Marker marker, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator) {
        final LatLng startPosition = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 3000;

        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition));

                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void animateMarkerToHC(final Marker marker, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator) {
        final LatLng startPosition = marker.getPosition();

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = animation.getAnimatedFraction();
                LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, finalPosition);
                marker.setPosition(newPosition);
            }
        });
        valueAnimator.setFloatValues(0, 1); // Ignored.
        valueAnimator.setDuration(500);
        valueAnimator.start();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void animateMarkerToICS(final BusMapFragment busMapFragment, final String latitude, final String longitude, final int teqBuzzVehiclePosition, final Vehicle receivedVehicle, final Marker marker, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator, final Circle user_circle, final boolean isGpsMarker) {
        TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {
            @Override
            public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
                return latLngInterpolator.interpolate(fraction, startValue, endValue);
            }
        };
        Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");
        ObjectAnimator animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition);
        animator.setDuration(Constants.MAP_SPEED);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                Log.d("MarkerAnimation", "vehicle :" + receivedVehicle.getVehicle_id() + " animation started");
                receivedVehicle.setMoving(true);
                busMapFragment.onTeqbuzzVehicleAnimationProgress(teqBuzzVehiclePosition, receivedVehicle, true);
                busMapFragment.onTeqbuzzVehicleAnimatedCompleted(teqBuzzVehiclePosition, receivedVehicle, latitude, longitude, finalPosition);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Log.d("MarkerAnimation", "vehicle :" + receivedVehicle.getVehicle_id() + " animation end");
                receivedVehicle.setMoving(false);
                busMapFragment.onTeqbuzzVehicleAnimationProgress(teqBuzzVehiclePosition, receivedVehicle, false);
                //busMapFragment.onTeqbuzzVehicleAnimatedCompleted(teqBuzzVehiclePosition, teqBuzzVehicle, finalPosition);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                Log.d("MarkerAnimation", "vehicle :" + receivedVehicle.getVehicle_id() + " animation cancelled");
                receivedVehicle.setMoving(false);
                busMapFragment.onTeqbuzzVehicleAnimationProgress(teqBuzzVehiclePosition, receivedVehicle, false);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
            }
        });
        animator.start();

    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void animateTeqbuzzMarker(final BusMapFragment busMapFragment, Vehicle teqbuzzVehicle, final Marker marker, final LatLngInterpolator latLngInterpolator) {
        Log.d("animateTeqbuzzMarker", "Animating marker to " + teqbuzzVehicle.getLatitude() + " and " + teqbuzzVehicle.getLongitude());
        LatLng finalPosition = new LatLng(Double.parseDouble(teqbuzzVehicle.getLatitude()), Double.parseDouble(teqbuzzVehicle.getLongitude()));
        Double latitude = finalPosition.latitude;// + 00.3;
        Double longitude = finalPosition.longitude;// + 00.3;
        finalPosition = new LatLng(latitude, longitude);
        TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {
            @Override
            public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
                return latLngInterpolator.interpolate(fraction, startValue, endValue);
            }
        };
        Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");
        ObjectAnimator animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition);
        Log.d("Vehicle Animation", "Id is " + teqbuzzVehicle.getVehicle_id() + " " + "old position is latitude : "+marker.getPosition().latitude + " " + "is longitude : "+marker.getPosition().longitude);
        Log.d("Vehicle Animation", "Id is " + teqbuzzVehicle.getVehicle_id() + " " + "new position is latitude : "+finalPosition.latitude + " " + "is longitude : "+finalPosition.longitude);
        animator.setDuration(Constants.MAP_SPEED);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                /*Log.d("MarkerAnimation", "vehicle :" + receivedVehicle.getVehicle_id() + " animation started");
                receivedVehicle.setMoving(true);
                busMapFragment.onTeqbuzzVehicleAnimationProgress(teqBuzzVehiclePosition, receivedVehicle, true);
                busMapFragment.onTeqbuzzVehicleAnimatedCompleted(teqBuzzVehiclePosition, receivedVehicle, latitude, longitude, finalPosition);*/
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                /*Log.d("MarkerAnimation", "vehicle :" + receivedVehicle.getVehicle_id() + " animation end");
                receivedVehicle.setMoving(false);
                busMapFragment.onTeqbuzzVehicleAnimationProgress(teqBuzzVehiclePosition, receivedVehicle, false);
                //busMapFragment.onTeqbuzzVehicleAnimatedCompleted(teqBuzzVehiclePosition, teqBuzzVehicle, finalPosition);*/
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                /*Log.d("MarkerAnimation", "vehicle :" + receivedVehicle.getVehicle_id() + " animation cancelled");
                receivedVehicle.setMoving(false);
                busMapFragment.onTeqbuzzVehicleAnimationProgress(teqBuzzVehiclePosition, receivedVehicle, false);*/
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
            }
        });
        animator.start();

    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void animateSmoothMarkerToICS(final BusMapFragment busMapFragment,
                                                final String latitude, final String longitude,
                                                final int teqBuzzVehiclePosition,
                                                final Vehicle teqBuzzVehicle,
                                                final Marker myMarker, final LatLng finalPosition,
                                                final LatLngInterpolator latLngInterpolator,
                                                final Circle user_circle,
                                                final boolean isGpsMarker) {


        final LatLng startPosition = myMarker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 3000;
        final boolean hideMarker = false;

        teqBuzzVehicle.setMoving(true);
        busMapFragment.onTeqbuzzVehicleAnimationProgress(teqBuzzVehiclePosition, teqBuzzVehicle, true);
        busMapFragment.onTeqbuzzVehicleAnimatedCompleted(teqBuzzVehiclePosition, teqBuzzVehicle, latitude, longitude, finalPosition);


        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                LatLng currentPosition = new LatLng(
                        startPosition.latitude * (1 - t) + finalPosition.latitude * t,
                        startPosition.longitude * (1 - t) + finalPosition.longitude * t);

                myMarker.setPosition(currentPosition);

                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 1);

                } else {

                    teqBuzzVehicle.setMoving(false);
                    busMapFragment.onTeqbuzzVehicleAnimationProgress(teqBuzzVehiclePosition, teqBuzzVehicle, false);

                }
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void animateMarkerToICS(final Marker marker, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator, final Circle user_circle, final boolean isGpsMarker) {
        TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {
            @Override
            public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
                return latLngInterpolator.interpolate(fraction, startValue, endValue);
            }
        };
        Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");
        ObjectAnimator animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition);
        animator.setDuration(Constants.MAP_SPEED);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();

    }
}