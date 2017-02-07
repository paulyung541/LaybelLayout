package com.paulyung.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.paulyung.laybellayout.LaybelLayout;

public class DefaultTextViewActivity extends AppCompatActivity {
    LaybelLayout laybelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_text_view);

        laybelLayout = (LaybelLayout) findViewById(R.id.laybelLayout);
        laybelLayout.setAdapter(new LaybelLayout.Adapter(Content.content));
    }
}
