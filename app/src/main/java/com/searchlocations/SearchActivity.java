package com.searchlocations;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.searchlocations.model.*;

import java.util.List;

public class SearchActivity extends Activity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    protected static final String TAG = "SearchActivity";
    private static final int LOCATION_REFRESH_TIME = 10000;

    private FusedLocationProviderApi mProviderApi;

    private GoogleApiClient mApiClient;

    private String mText;

    public static final String BASE_URL = "https://api.foursquare.com/v2/venues/";
    private final String CLIENT_ID = "DTBSOM4GRROOOLKZ3KAYH0GK4VVH1M4HVFVPQSMBXBGGUKHJ";
    private final String CLIENT_SECRET = "H3LLXD3FOHSTA5A1RBUKTWIAQSPAAYMLBN5HBVVBOJZFUUB3";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    FoursquareAPI apiService;

    @BindView(R.id.editText) EditText textBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        mApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        apiService = retrofit.create(FoursquareAPI.class);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mApiClient != null) {
            mApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mApiClient.isConnected()) {
            mApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected to API", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
        mApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Unable to connect to API", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.searchButton)
    public void search() {
        mText = textBox.getText().toString().trim();
        String text;
        if (mText.isEmpty()) {
           text = "Enter a value";
        } else {
            text = "Searching for " + mText;
            getLocation();
        }
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(this, text, duration).show();
    }


    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(LOCATION_REFRESH_TIME);
            locationRequest.setFastestInterval(LOCATION_REFRESH_TIME);

            mProviderApi = LocationServices.FusedLocationApi;

            mProviderApi.removeLocationUpdates(mApiClient, this);
            mProviderApi.requestLocationUpdates(mApiClient, locationRequest, this);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    PERMISSION_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            double lat = location.getLatitude(), lon = location.getLongitude();
            String message = "Lat: " + lat + ", Long: " + lon;
            String results;
            if(mText != null){
                //Busqueda pendiente
                Call<List<Venue>> call = apiService.getVenues(lat + "," + lon, mText, CLIENT_ID, CLIENT_SECRET);
                call.enqueue(new Callback<List<Venue>>() {
                    @Override
                    public void onResponse(Call<List<Venue>> call, final Response<List<Venue>> response) {
                        final int venuesTotal = (response.body() != null) ? response.body().size() : 0;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(SearchActivity.this, "Found " + venuesTotal + "venues", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 0);
                                toast.show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<List<Venue>> call, Throwable t) {

                    }
                });
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                
                mText = null;
            }

        } else {
            Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
        }
    }
}
