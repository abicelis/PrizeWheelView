package ve.com.abicelis.prizewheellib.model;

import android.support.annotation.ColorRes;

/**
 * Created by abicelis on 26/7/2017.
 */

public class WheelColorSection extends WheelSection {
    private @ColorRes int color;


    public WheelColorSection(@ColorRes int color) {
        this.color = color;
    }



    public SectionType getType() {
        return SectionType.COLOR;
    }

    public @ColorRes int getColor() {
        return color;
    }


    @Override
    public String toString() {
        return
                "SectionType= " +  getType() +
                ", Color= " +      color;
    }
}
