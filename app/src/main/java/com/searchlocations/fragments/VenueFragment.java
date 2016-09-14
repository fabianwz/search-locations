package com.searchlocations.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.searchlocations.R;
import com.searchlocations.models.Venue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VenueFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.venue_title)
    TextView mVenueTitle;

    @BindView(R.id.venue_description)
    TextView mVenueDescription;

    @BindView(R.id.venue_address)
    TextView mVenueAddress;

    @BindView(R.id.venue_phone)
    TextView mVenuePhone;

    @BindView(R.id.venue_url)
    TextView mVenueUrl;

    private Venue mVenue;
    private double mUserLat;
    private double mUserLon;

    public static VenueFragment newInstance(Venue venue, double lat, double lon) {
        VenueFragment venueFragment = new VenueFragment();
        venueFragment.setmVenue(venue);
        venueFragment.setmUserLat(lat);
        venueFragment.setmUserLon(lon);

        return venueFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.venue_fragment, container, false);
        ButterKnife.bind(this, view);

        mVenueTitle.setText(mVenue.getName());
        mVenueDescription.setText(mVenue.getDescription());
        mVenueAddress.setText(mVenue.getLocation().getAddress());
        mVenuePhone.setText(
                (mVenue.getContact().getPhone() != null) ? mVenue.getContact().getPhone() : "Not available");
        mVenueUrl.setText(mVenue.getUrl());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.venue_address)
    public void getMapInstructions() {
        String uri =
                "https://maps.google.com/maps?f=d&hl=en&saddr="
                        + mUserLat + "," + mUserLon + "&daddr=" + mVenue.getLocation().getLat() + "," + mVenue.getLocation().getLng();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    @OnClick(R.id.venue_url)
    public void shareUrl(){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        share.putExtra(Intent.EXTRA_TEXT, mVenue.getUrl());

        startActivity(Intent.createChooser(share, "Share link via"));
    }


    public Venue getmVenue() {
        return mVenue;
    }

    public void setmVenue(Venue mVenue) {
        this.mVenue = mVenue;
    }

    public double getmUserLat() {
        return mUserLat;
    }

    public void setmUserLat(double lat) {
        this.mUserLat = lat;
    }

    public double getmUserLon() {
        return mUserLon;
    }

    public void setmUserLon(double lon) {
        this.mUserLon = lon;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
