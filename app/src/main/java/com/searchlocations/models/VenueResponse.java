package com.searchlocations.models;

import java.util.List;

public class VenueResponse {

    public VenueList response;

    public class VenueList {
        public List<Venue> venues;
    }
}
