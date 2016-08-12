package com.searchlocations.model;

import java.util.List;

public class VenueResponse {

    public VenueList response;

    public class VenueList {
        public List<Venue> venues;
    }
}
