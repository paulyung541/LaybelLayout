package com.paulyung.demo;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.paulyung.laybellayout.LaybelLayout;

import org.jokar.permissiondispatcher.annotation.NeedsPermission;
import org.jokar.permissiondispatcher.annotation.RuntimePermissions;

/**
 * 使用 ImageView 就必须重写 Adapter 的 getView()
 */
@RuntimePermissions
public class ImageActivity extends AppCompatActivity {
    LaybelLayout laybelLayout;

    @Override
    @NeedsPermission(Manifest.permission.INTERNET)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        laybelLayout = (LaybelLayout) findViewById(R.id.laybelLayout);
        laybelLayout.setAdapter(new LaybelLayout.Adapter(Content.images) {
            @Override
            public View getView() {
                ImageView imageView = new ImageView(ImageActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(getWidth() / 3, getWidth() / 3);
                imageView.setLayoutParams(params);
                return imageView;
            }

            @Override
            public void onDataSet(View v, String data) {
                Glide.with(ImageActivity.this).load(data).into((ImageView) v);
            }
        });
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getWidth() {
        WindowManager wm1 = this.getWindowManager();
        return wm1.getDefaultDisplay().getWidth();
    }
}
