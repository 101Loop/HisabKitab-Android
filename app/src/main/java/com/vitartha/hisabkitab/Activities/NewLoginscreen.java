package com.vitartha.hisabkitab.Activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.RenderScript;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.vitartha.hisabkitab.Class.RSBlurProcessor;
import com.vitartha.hisabkitab.R;

import static com.vitartha.hisabkitab.Class.BitmapUtils.getBitmapFromView;

public class NewLoginscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_loginscreen);

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content));

        if (viewGroup.getWidth() > 0) {
            Bitmap image = RSBlurProcessor.blur(viewGroup);
            viewGroup.setBackgroundDrawable(new BitmapDrawable(getResources(), image));
        } else {
            viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Bitmap image = RSBlurProcessor.blur(viewGroup);
                    viewGroup.setBackgroundDrawable(new BitmapDrawable(getResources(), image));
                }
            });
        }
    }
}
