package com.vitartha.hisabkitab.Activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.RelativeLayout;

import com.vitartha.hisabkitab.R;

public class NewLoginscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_loginscreen);

        RelativeLayout rel = findViewById(R.id.blurlayout1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rel.getBackground().setAlpha( 200); // dim
        }

        //  final ImageView imageView = findViewById(R.id.backimg);
        /*final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
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
        }*/
    }
}
