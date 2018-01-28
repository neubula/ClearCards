package com.clearcardsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clearcardsapp.util.AppPref;
import com.clearcardsapp.util.OnSwipeTouchListener;

public class GuideActivity extends Activity {

    public Context context;
    public AppPref appPref;

    LinearLayout header, body, footer;
    TextView guideText, previous, next, title;
    ImageView cardBackground;
    private int position = 0;

    private static final String TAG = ">--GuideActivity-->";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        context = this;

        appPref = new AppPref(context);

        Typeface typeFaceRobotoL = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        Typeface typeFaceRobotoLI = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-LightItalic.ttf");
        Typeface typeFaceRobotoR = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");

        header = (LinearLayout) findViewById(R.id.guide_header);
        body = (LinearLayout) findViewById(R.id.guide_body);
        footer = (LinearLayout) findViewById(R.id.guide_footer);
        guideText = (TextView) findViewById(R.id.guide_text);
        previous = (TextView) findViewById(R.id.guide_previous);
        next = (TextView) findViewById(R.id.guide_next);
        title = (TextView) findViewById(R.id.guide_title);
        cardBackground = (ImageView) findViewById(R.id.guide_card_background);


        guideText.setTypeface(typeFaceRobotoL);
        previous.setTypeface(typeFaceRobotoR);
        next.setTypeface(typeFaceRobotoR);
        title.setTypeface(typeFaceRobotoR);


        header.getBackground().setAlpha(180);
        footer.getBackground().setAlpha(180);

        body.getBackground().setAlpha(180);

        setData();

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0)
                    position--;
                setData();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                setData();
            }
        });

        cardBackground.setOnTouchListener(new OnSwipeTouchListener(context) {
            public void onSwipeTop() {
//                Toast.makeText(context, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                if (position > 0)
                    position--;
                setData();
            }

            public void onSwipeLeft() {
                position++;
                setData();
            }

            public void onSwipeBottom() {
//                Toast.makeText(context, "bottom", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void setData() {
        if (position == 0) {
            cardBackground.setBackgroundResource(R.drawable.swipe_left);
            guideText.setText("Swipe left for next card");
            previous.setVisibility(ImageView.GONE);
        } else if (position == 1) {
            cardBackground.setBackgroundResource(R.drawable.swipe_right);
            guideText.setText("Swipe right for previous card");
            previous.setVisibility(ImageView.VISIBLE);
        } else if (position == 2) {
            cardBackground.setBackgroundResource(R.drawable.click);
            guideText.setText("Tap anywhere to get more options");
            previous.setVisibility(ImageView.VISIBLE);
        } else {
            appPref.setBoolean("IS_GUIDED", true);
            Intent intent = new Intent(context, ScreenSlideActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
