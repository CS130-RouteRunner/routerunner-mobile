package com.cs130.routerunner.android.ApiServer;

import android.os.AsyncTask;

import com.cs130.routerunner.android.ApiServer.ApiServer;

import org.json.JSONObject;

/**
 * Created by graceychin on 10/28/15.
 */
public class ApiServerPostTask extends AsyncTask<String, Void, JSONObject> {

    /*
     * input: params must be 2 strings
     *        params[0] = endpoint
     *        params[1] = parameters in JSON formatted string
     */
    @Override
    protected JSONObject doInBackground(String... params) {
        ApiServer apiServer = new ApiServer();
        JSONObject response = apiServer.postRequest(params[0], params[1]);
        return response;
    }

}
