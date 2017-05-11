package com.kanthan.teqbuzz.onebusaway;

import java.util.ArrayList;

/**
 * Created by suren on 7/19/2016.
 */
public interface OBAWebServiceCallBack {

    void onOBANearByRoutesLoaded(String stopResponse, OBAStopEntity obaStopEntity);

    void onOBAStopsForRoutesLoaded(String res, ArrayList<OBAStopListEntity> obaStopListEntity);
}
