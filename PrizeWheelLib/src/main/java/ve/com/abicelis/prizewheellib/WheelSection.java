package ve.com.abicelis.prizewheellib;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/**
 * Created by abicelis on 26/7/2017.
 */

public class WheelSection {


    private SectionType sectionType;
    private Bitmap bitmap;
    private @DrawableRes int drawable;
    private Color color;


    public WheelSection(@NonNull Bitmap bitmap) {
        sectionType = SectionType.BITMAP;
        this.bitmap = bitmap;
    }
    public WheelSection(@DrawableRes int drawable) {
        sectionType = SectionType.DRAWABLE;
        this.drawable = drawable;
    }
    public WheelSection(Color color) {
        sectionType = SectionType.COLOR;
        this.color = color;
    }




    public SectionType getType() {
        return sectionType;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public @DrawableRes int getDrawable() {
        return drawable;
    }

    public Color getColor() {
        return color;
    }


}
