package com.paulyung.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.paulyung.laybellayout.LaybelLayout;

public class DefaultTextViewActivity extends AppCompatActivity {
    LaybelLayout laybelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_text_view);

        laybelLayout = (LaybelLayout) findViewById(R.id.laybelLayout);
        laybelLayout.setAdapter(new LaybelLayout.Adapter(Content.content));
        laybelLayout.setOnItemClickListener(new LaybelLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int p) {
                Toast.makeText(DefaultTextViewActivity.this, Content.content[p], Toast.LENGTH_SHORT).show();
            }
        });
    }
}
