package com.searchlocations;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends Activity {

    @BindView(R.id.editText) EditText textBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.searchButton)
    public void search() {
        String searchWord = textBox.getText().toString().trim();
        String text = (searchWord.isEmpty()) ? "Enter a value" : "Searching for " + searchWord;
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(this, text, duration).show();
    }
}
