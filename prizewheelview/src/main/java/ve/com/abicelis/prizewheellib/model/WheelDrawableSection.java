package ve.com.abicelis.prizewheellib.model;

import android.support.annotation.DrawableRes;

/**
 * Created by abicelis on 26/7/2017.
 */

public class WheelDrawableSection extends WheelSection {

    private @DrawableRes int drawable;


    public WheelDrawableSection(@DrawableRes int drawable) {
        this.drawable = drawable;
    }



    public SectionType getType() {
        return SectionType.DRAWABLE;
    }


    public @DrawableRes int getDrawableRes() {
        return drawable;
    }


    @Override
    public String toString() {
        return
                "SectionType= " +   getType() +
                        ", DrawableRes= " + drawable;
    }
}
