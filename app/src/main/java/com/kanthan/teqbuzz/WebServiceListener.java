package com.kanthan.teqbuzz;

/**
 * Created by suren on 6/20/2016.
 */
public interface WebServiceListener {

    void onWebServiceSuccess(String response);

    void onWebServicefailed(String response);

}
