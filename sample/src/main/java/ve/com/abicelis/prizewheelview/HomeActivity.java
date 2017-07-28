package ve.com.abicelis.prizewheelview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.prizewheellib.PrizeWheelView;
import ve.com.abicelis.prizewheellib.model.MarkerPosition;
import ve.com.abicelis.prizewheellib.model.WheelBitmapSection;
import ve.com.abicelis.prizewheellib.model.WheelColorSection;
import ve.com.abicelis.prizewheellib.model.WheelDrawableSection;
import ve.com.abicelis.prizewheellib.model.WheelSection;

/**
 * Created by abicelis on 25/7/2017.
 */

public class HomeActivity extends AppCompatActivity {

    PrizeWheelView wheelView;
    ImageView homeImage;
    Button stop;
    Button flingCC;
    Button flingCW;
    final List<WheelSection> wheelSections = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeImage = (ImageView) findViewById(R.id.home_image);
        stop = (Button) findViewById(R.id.stop_wheel);
        flingCC = (Button) findViewById(R.id.fling_wheel_cc);
        flingCW = (Button) findViewById(R.id.fling_wheel_cw);


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wheelView.stopWheel();
            }
        });
        flingCW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wheelView.flingWheel(20000, true);
            }
        });
        flingCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wheelView.flingWheel(20000, false);
            }
        });


        Bitmap someBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.abstract_1);

        //Init WheelSection list
        wheelSections.add(new WheelBitmapSection(someBitmap));
        wheelSections.add(new WheelDrawableSection(R.drawable.abstract_2));
        wheelSections.add(new WheelDrawableSection(R.drawable.abstract_3));
        wheelSections.add(new WheelDrawableSection(R.drawable.abstract_4));
        wheelSections.add(new WheelDrawableSection(R.drawable.abstract_5));
        wheelSections.add(new WheelDrawableSection(R.drawable.abstract_6));
        wheelSections.add(new WheelDrawableSection(R.drawable.abstract_7));
        wheelSections.add(new WheelDrawableSection(R.drawable.abstract_8));
        wheelSections.add(new WheelColorSection(R.color.green));
        wheelSections.add(new WheelColorSection(R.color.orange));
        wheelSections.add(new WheelColorSection(R.color.blue));


        //Init wheelView and set parameters
        wheelView = (PrizeWheelView) findViewById(R.id.home_prize_wheel_view);
        wheelView.setWheelSections(wheelSections);
        wheelView.setMarkerPosition(MarkerPosition.TOP_RIGHT);

        wheelView.setWheelBorderLineColor(R.color.border);
        wheelView.setWheelBorderLineThickness(5);

        wheelView.setWheelSeparatorLineColor(R.color.separator);
        wheelView.setWheelSeparatorLineThickness(5);

        //Set onSettled listener
        wheelView.setWheelSettledListener(new WheelEventsListener());

        //Finally, generate wheel background
        wheelView.generateWheel();

    }



    private class WheelEventsListener implements ve.com.abicelis.prizewheellib.WheelEventsListener {

        @Override
        public void onWheelStopped() {
            Toast.makeText(HomeActivity.this, "Wheel has just been stopped", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onWheelFlung() {
            Toast.makeText(HomeActivity.this, "Wheel has just been flung", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onWheelSettled(final int sectionIndex, double angle) {
            Toast.makeText(HomeActivity.this, "Wheel settled at " + angle + "°. Section #" + sectionIndex, Toast.LENGTH_SHORT).show();


            Animation fadeOut = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.fade_out);
            homeImage.startAnimation(fadeOut);

            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {

                    WheelSection section = wheelSections.get(sectionIndex);
                    switch (section.getType()){
                        case BITMAP:
                            homeImage.setImageBitmap( ((WheelBitmapSection)section).getBitmap() );
                            break;
                        case DRAWABLE:
                            homeImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), ((WheelDrawableSection)section).getDrawableRes()));
                            break;
                        case COLOR:
                            homeImage.setImageDrawable(null);
                            homeImage.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), ((WheelColorSection)section).getColor()));
                            break;
                    }

                    Animation fadeIn = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.fade_in);
                    homeImage.startAnimation(fadeIn);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

        }
    }

}
