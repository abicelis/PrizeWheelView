package ve.com.abicelis.prizewheelview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.prizewheellib.PrizeWheelView;
import ve.com.abicelis.prizewheellib.WheelSettledListener;
import ve.com.abicelis.prizewheellib.model.WheelBitmapSection;
import ve.com.abicelis.prizewheellib.model.WheelColorSection;
import ve.com.abicelis.prizewheellib.model.WheelDrawableSection;
import ve.com.abicelis.prizewheellib.model.WheelSection;

/**
 * Created by abicelis on 25/7/2017.
 */

public class HomeActivity extends AppCompatActivity {

    PrizeWheelView wheelView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bitmap someBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.abstract_1);

        //Init WheelSection list
        List<WheelSection> wheelSections = new ArrayList<>();
        wheelSections.add(new WheelBitmapSection(someBitmap));
        wheelSections.add(new WheelDrawableSection(R.drawable.abstract_2));
        wheelSections.add(new WheelDrawableSection(R.drawable.abstract_3));
        wheelSections.add(new WheelDrawableSection(R.drawable.abstract_4));
        wheelSections.add(new WheelDrawableSection(R.drawable.abstract_5));
        wheelSections.add(new WheelDrawableSection(R.drawable.abstract_6));
        wheelSections.add(new WheelDrawableSection(R.drawable.abstract_7));
        wheelSections.add(new WheelDrawableSection(R.drawable.abstract_8));
        wheelSections.add(new WheelColorSection(R.color.green));
        wheelSections.add(new WheelColorSection(R.color.red));
        wheelSections.add(new WheelColorSection(R.color.blue));


        //Init wheelView and set parameters
        wheelView = (PrizeWheelView) findViewById(R.id.home_prize_wheel_view);
        wheelView.setWheelSections(wheelSections);

        wheelView.setWheelBorderLineColor(R.color.border);
        wheelView.setWheelBorderLineThickness(5);

        wheelView.setWheelSeparatorLineColor(R.color.separator);
        wheelView.setWheelSeparatorLineThickness(5);

        //Set onSettled listener
        wheelView.setWheelSettledListener(new WheelSettledListener() {
            @Override
            public void onWheelSettled(int sectionIndex, double angle) {
                Toast.makeText(HomeActivity.this, "Wheel settled! Angle=" + angle + " SectionIndex=" + sectionIndex, Toast.LENGTH_SHORT).show();
            }
        });

        //Finally, generate wheel background
        wheelView.generateWheel();

    }

}
