package com.searchlocations.controllers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

public class GeofenceController {

    private final String TAG = GeofenceController.class.getName();

    private Context mContext;
    private GoogleApiClient mApiClient;
    private Gson mGson;
    private SharedPreferences mPrefs;
}
