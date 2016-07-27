package com.searchlocations;

import android.app.Activity;
import android.content.Context;
import android.widget.SearchView;
import android.os.Bundle;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Search extends Activity {

    @BindView(R.id.searchView) SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.searchView)
    public void search() {
        CharSequence searchWord = searchView.getQuery();

        Context context = getApplicationContext();
        CharSequence text = "Searching for " + searchWord;
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, text, duration).show();
    }
}
