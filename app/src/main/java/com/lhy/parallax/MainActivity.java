package com.lhy.parallax;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        final ParallaxListView parallaxListView = (ParallaxListView) findViewById(R.id.pl_list_view);
        View headerView = View.inflate(getApplicationContext(), R.layout.layout_list_view_header, null);
        final ImageView ivHeader = (ImageView) headerView.findViewById(R.id.iv_list_view_header);
        parallaxListView.addHeaderView(headerView);//添加头布局,必须放在setAdapter之前
        ivHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {//绘制完成的监听
            @Override
            public void onGlobalLayout() {//界面绘制完成的时候再传入ivHeader,否则拿不到宽高
                parallaxListView.setParallaxImage(ivHeader);
                ivHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        parallaxListView.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Cheeses.NAMES));
    }
}
