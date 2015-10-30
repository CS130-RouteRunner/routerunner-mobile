package com.cs130.routerunner.android;

//import android.app.Activity;
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.content.IntentSender;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.View;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.Scopes;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.Scope;
//import com.google.android.gms.plus.Plus;

public class SignInActivity {}

//public class SignInActivity extends Activity implements
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener,
//        View.OnClickListener {
//
//    private static final String TAG = "MainActivity";
//
//    /* RequestCode for resolutions involving sign-in */
//    private static final int RC_SIGN_IN = 1;
//
//    /* RequestCode for resolutions to get GET_ACCOUNTS permission on M */
//    private static final int RC_PERM_GET_ACCOUNTS = 2;
//
//    /* Keys for persisting instance variables in savedInstanceState */
//    private static final String KEY_IS_RESOLVING = "is_resolving";
//    private static final String KEY_SHOULD_RESOLVE = "should_resolve";
//
//    /* Client for accessing Google APIs */
//    private GoogleApiClient mGoogleApiClient;
//
//    /* View to display current status (signed-in, signed-out, disconnected, etc) */
//    private TextView mStatus;
//
//    // [START resolution_variables]
//    /* Is there a ConnectionResult resolution in progress? */
//    private boolean mIsResolving = false;
//
//    /* Should we automatically resolve ConnectionResults when possible? */
//    private boolean mShouldResolve = false;
//    // [END resolution_variables]
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        findViewById(R.id.sign_in_button).setOnClickListener(this);
//
//        // Build GoogleApiClient with access to basic profile
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Plus.API)
//                .addScope(new Scope(Scopes.PROFILE))
//                .addScope(new Scope(Scopes.EMAIL))
//                .build();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mGoogleApiClient.disconnect();
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        // onConnected indicates that an account was selected on the device, that the selected
//        // account has granted any requested permissions to our app and that we were able to
//        // establish a service connection to Google Play services.
//        Log.d(TAG, "onConnected:" + bundle);
//        mShouldResolve = false;
//
//        // Show the signed-in UI
////        showSignedInUI();
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
//
//        if (requestCode == RC_SIGN_IN) {
//            // If the error resolution was not successful we should not resolve further.
//            if (resultCode != RESULT_OK) {
//                mShouldResolve = false;
//            }
//
//            mIsResolving = false;
//            mGoogleApiClient.connect();
//        }
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.sign_in_button) {
//            onSignInClicked();
//        }
////        if (view.getId() == R.id.sign_out_button) {
////            onSignOutClicked()
////        }
//    }
//
//    private void onSignInClicked() {
//        // User clicked the sign-in button, so begin the sign-in process and automatically
//        // attempt to resolve any errors that occur.
//        mShouldResolve = true;
//        mGoogleApiClient.connect();
//
//        // Show a message to the user that we are signing in.
////        mStatus.setText(R.string.signing_in);
//    }
//
//    private void onSignOutClicked() {
//        // Clear the default account so that GoogleApiClient will not automatically
//        // connect in the future.
//        if (mGoogleApiClient.isConnected()) {
//            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//            mGoogleApiClient.disconnect();
//        }
//
////        showSignedOutUI();
//    }
//
//    public void signUpClicked(View v){
//        try {
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.google.com/signup"));
//            startActivity(browserIntent);
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(this, "No application can handle this request."
//                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        // Could not connect to Google Play Services.  The user needs to select an account,
//        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
//        // ConnectionResult to see possible error codes.
//        Log.d(TAG, "onConnectionFailed:" + connectionResult);
//
//        if (!mIsResolving && mShouldResolve) {
//            if (connectionResult.hasResolution()) {
//                try {
//                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
//                    mIsResolving = true;
//                } catch (IntentSender.SendIntentException e) {
//                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
//                    mIsResolving = false;
//                    mGoogleApiClient.connect();
//                }
//            } else {
//                // Could not resolve the connection result, show the user an
//                // error dialog.
////                showErrorDialog(connectionResult);
//            }
//        } else {
//            // Show the signed-out UI
////            showSignedOutUI();
//        }
//    }
//}