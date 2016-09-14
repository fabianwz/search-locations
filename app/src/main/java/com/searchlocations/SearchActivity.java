package com.searchlocations;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;

import com.searchlocations.fragments.SearchFragment;
import com.searchlocations.fragments.VenueFragment;
import com.searchlocations.models.Venue;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends FragmentActivity implements SearchFragment.OnItemSelectedListener, VenueFragment.OnFragmentInteractionListener {

    @BindView(R.id.fragment_container) public FrameLayout mFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        if(mFragmentContainer != null) {
            if(savedInstanceState != null) {
                return;
            }

            SearchFragment searchFragment = new SearchFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, searchFragment).commit();
        }
    }

    @Override
    public void onVenueItemSelected(Venue venue, double lat, double lon) {
        VenueFragment venueFragment = VenueFragment.newInstance(venue, lat, lon);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, venueFragment).addToBackStack(null).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
