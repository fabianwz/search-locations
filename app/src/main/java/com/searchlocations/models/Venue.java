package com.searchlocations.models;

import com.google.android.gms.location.Geofence;

import java.util.UUID;

public class Venue {

    private String id;
    private String name;
    private String description;
    private String url;
    private Contact contact;
    private Location location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Geofence geofence() {
        id = UUID.randomUUID().toString();
        return new Geofence.Builder()
                .setRequestId(id)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setCircularRegion(Double.parseDouble(this.getLocation().getLat()), Double.parseDouble(this.getLocation().getLng()), 0)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }
}

