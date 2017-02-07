package com.paulyung.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.paulyung.laybellayout.LaybelLayout;

public class MainActivity extends AppCompatActivity {
    LaybelLayout laybelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click1(View view) {
        startActivity(new Intent(this, DefaultTextViewActivity.class));
    }

    public void click2(View view) {
        startActivity(new Intent(this, CustomBackgroundTextViewActivity.class));
    }

    public void click3(View view) {
        startActivity(new Intent(this, ImageActivity.class));
    }
}
