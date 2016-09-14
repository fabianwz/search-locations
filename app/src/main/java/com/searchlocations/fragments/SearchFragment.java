package com.searchlocations.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.searchlocations.R;
import com.searchlocations.adapters.VenueAdapter;
import com.searchlocations.models.FoursquareAPI;
import com.searchlocations.models.Venue;
import com.searchlocations.models.VenueResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    @BindView(R.id.editText)
    public EditText mTextBox;

    @BindView(R.id.listView)
    public ListView mListView;

    private OnItemSelectedListener listener;
    private GoogleApiClient mApiClient;
    private String mText;
    private double mUserLat;
    private double mUserLon;

    protected static final String TAG = "SearchActivity";
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    private static final int LOCATION_REFRESH_TIME = 10000;
    public static final String BASE_URL = "https://api.foursquare.com/";
    private final String CLIENT_ID = "DTBSOM4GRROOOLKZ3KAYH0GK4VVH1M4HVFVPQSMBXBGGUKHJ";
    private final String CLIENT_SECRET = "H3LLXD3FOHSTA5A1RBUKTWIAQSPAAYMLBN5HBVVBOJZFUUB3";

    private FusedLocationProviderApi mProviderApi;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    FoursquareAPI apiService;

    public interface OnItemSelectedListener  {
        void onVenueItemSelected(Venue venue, double lat, double lon);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        apiService = retrofit.create(FoursquareAPI.class);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Venue venue = (Venue) mListView.getItemAtPosition(position);
                listener.onVenueItemSelected(venue, mUserLat, mUserLon);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                + " must implement SearchFragment.OnItemSelectedListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mApiClient != null) {
            mApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mApiClient.isConnected()) {
            mApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(getActivity(), "Connected to API", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
        mApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), "Unable to connect to API", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(getActivity(), "Need your location!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            mUserLat = location.getLatitude();
            mUserLon = location.getLongitude();
            if(mText != null){
                Call<VenueResponse> call = apiService.getVenues(mUserLat + "," + mUserLon, mText, CLIENT_ID, CLIENT_SECRET, "20160810");
                call.enqueue(new Callback<VenueResponse>() {
                    @Override
                    public void onResponse(Call<VenueResponse> call, final Response<VenueResponse> response) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<Venue> venues = response.body().response.venues;
                                int venuesTotal = (venues != null) ? venues.size() : 0;
                                Toast.makeText(getActivity(), "Found " + venuesTotal + " venues", Toast.LENGTH_SHORT).show();

                                VenueAdapter adapter = new VenueAdapter(getActivity(), venues);
                                mListView.setAdapter(adapter);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<VenueResponse> call, Throwable t) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                mText = null;
            }

        } else {
            Toast.makeText(getActivity(), "Unable to get location", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.searchButton)
    public void search() {
        mText = mTextBox.getText().toString().trim();
        System.out.print("HELLO");
        String text;
        if (mText.isEmpty()) {
            text = "Enter a value";
        } else {
            text = "Searching for " + mText;
            getLocation();
        }
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(getActivity(), text, duration).show();
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(LOCATION_REFRESH_TIME);
            locationRequest.setFastestInterval(LOCATION_REFRESH_TIME);

            mProviderApi = LocationServices.FusedLocationApi;

            mProviderApi.removeLocationUpdates(mApiClient, this);
            mProviderApi.requestLocationUpdates(mApiClient, locationRequest, this);

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    PERMISSION_ACCESS_FINE_LOCATION);
        }
    }
}
