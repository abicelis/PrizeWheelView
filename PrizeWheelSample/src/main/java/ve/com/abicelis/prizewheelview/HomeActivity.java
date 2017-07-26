package ve.com.abicelis.prizewheelview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.prizewheellib.PrizeWheelView;
import ve.com.abicelis.prizewheellib.WheelSection;

/**
 * Created by abicelis on 25/7/2017.
 */

public class HomeActivity extends AppCompatActivity {

    PrizeWheelView wheelView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        List<WheelSection> wheelSections = new ArrayList<>();
        wheelSections.add(new WheelSection(R.drawable.burger));
        wheelSections.add(new WheelSection(R.drawable.chicken));
        wheelSections.add(new WheelSection(R.drawable.hummus));
        wheelSections.add(new WheelSection(R.drawable.pasta));
        wheelSections.add(new WheelSection(R.drawable.sausages));

        wheelView = (PrizeWheelView) findViewById(R.id.home_prize_wheel_view);
        wheelView.setWheelSections(wheelSections);
        wheelView.setWheelBorderLineColor(R.color.colorAccent);
        wheelView.setWheelSeparatorLineColor(R.color.colorPrimary);
        wheelView.generateWheel();

    }

}
