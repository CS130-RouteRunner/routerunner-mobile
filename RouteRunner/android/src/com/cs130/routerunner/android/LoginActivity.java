package com.cs130.routerunner.android;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cs130.routerunner.Settings;
import com.cs130.routerunner.android.ApiServer.ApiServerPostTask;

import org.json.JSONObject;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goToMain(View view) {
        EditText username = (EditText) findViewById(R.id.editText2);

        if (isEmpty(username)) {
            Toast.makeText(this, "You did not enter a username", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent main = new Intent(this, MainActivity.class);
        main.putExtra("username", username.getText().toString());
        // TODO: Kailin Make a POST request to CREATE_USER_URL
        try {
            createUser(username.getText().toString());
            startActivity(main);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private void createUser(String username) throws Exception {
        ApiServerPostTask apiServerPostTask = new ApiServerPostTask();
        String endpoint = Settings.CREATE_USER_URL;
        JSONObject params = new JSONObject();
        String userId = username;
        params.put("uid", userId);
        JSONObject response = apiServerPostTask.execute(endpoint, params.toString()).get();
        System.out.println(response.toString());
    }

}
