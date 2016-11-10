package com.paulyung.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paulyung.laybellayout.LaybelLayout;

public class MainActivity extends AppCompatActivity {
    LaybelLayout laybelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        laybelLayout = (LaybelLayout) findViewById(R.id.laybel_layout);

        for (int i = 0; i < Content.content.length; i++) {
            TextView tv = new TextView(this);
            tv.setText(Content.content[i]);
            tv.setTextSize(dip2px(8));
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            lp.leftMargin = dip2px(5);
            lp.rightMargin = dip2px(5);
            tv.setBackground(getDrawable(R.drawable.background));
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
            laybelLayout.addView(tv, lp);
        }
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
