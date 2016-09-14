package com.searchlocations.models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoursquareAPI {

    @GET("/v2/venues/search")
    Call<VenueResponse> getVenues(@Query("ll") String latLong,
                                @Query("query") String searchTerm,
                                @Query("client_id") String clientId,
                                @Query("client_secret") String clientSecret,
                                @Query("v") String apiVersion);
}
