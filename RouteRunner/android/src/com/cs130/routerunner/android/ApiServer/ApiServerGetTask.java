package com.cs130.routerunner.android.ApiServer;

import android.os.AsyncTask;

import org.json.JSONObject;

/**
 * Created by graceychin on 10/29/15.
 */
public class ApiServerGetTask extends AsyncTask<String, Void, JSONObject> {

    /*
     * input: params must be 1 string
     *        params[0] = endpoint
     */
    @Override
    protected JSONObject doInBackground(String... params) {
        ApiServer apiServer = new ApiServer();
        JSONObject response = apiServer.getRequest(params[0]);
        return response;
    }
}
