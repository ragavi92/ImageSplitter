package com.dageek.imagesplitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class NumberActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);
        getSupportActionBar().setTitle("");
    }

    public void onNumberClick(View v) {
        Intent i = new Intent(getApplicationContext(), ImageSplitActivity.class);
        i.putExtra("numberOfFrames", Integer.valueOf(((Button) v).getText().toString()));
        startActivity(i);
    }

}

