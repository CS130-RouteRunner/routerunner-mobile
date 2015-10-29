package com.cs130.routerunner.android.ApiServer;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;

/**
 * Created by graceychin on 10/28/15.
 */
public class ApiServer {

    public ApiServer() {}

    public JSONObject postRequest(String endpoint, String params) {
        JSONObject jsonResponse = new JSONObject();

        try {
            // Set up URL Connection
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Write to Connection Stream
            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(params);
            os.flush();
            os.close();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { //success

                // Handle Response
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                jsonResponse = new JSONObject(response.toString());
            }


        } catch (Exception e) {
            System.out.println(e.toString());
            return jsonResponse;
        }
        return jsonResponse;
    }

    public JSONObject getRequest(String endpoint) {
        JSONObject jsonResponse = new JSONObject();

        try {
            // Set up URL Connection
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            // Handle Response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                jsonResponse = new JSONObject(response.toString());
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return jsonResponse;

    }
}
