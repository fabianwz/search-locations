package com.searchlocations.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoursquareAPI {

    @GET("/search")
    Call<List<Venue>> getVenues(@Query("ll") String latLong,
                                @Query("query") String searchTerm,
                                @Query("client_id") String clientId,
                                @Query("client_secret") String clientSecret);
}
