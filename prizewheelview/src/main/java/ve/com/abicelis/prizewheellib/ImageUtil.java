package ve.com.abicelis.prizewheellib;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;

import java.util.Arrays;

/**
 * Created by abicelis on 28/7/2017.
 */

public class ImageUtil {


    /**
     * Evaluates a Bitmap, returns a Rect with its transparency cropped
     * Not my code, extracted from:
     * https://github.com/AlvaroMenezes/CropTrimTransparentImage
     */
    public static Rect cropTransparentPixelsFromImage(Bitmap bitmap){

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int[] empty = new int[width];
        int[] buffer = new int[width];
        Arrays.fill(empty,0);
        int top = 0;
        int left = 0;
        int bottom = height;
        int right = width;

        for (int y = 0; y < height; y++) {
            bitmap.getPixels(buffer, 0, width, 0, y, width, 1);
            if (!Arrays.equals(empty, buffer)) {
                top = y;
                break;
            }
        }

        for (int y = height - 1; y > top; y--) {
            bitmap.getPixels(buffer, 0, width, 0, y, width, 1);
            if (!Arrays.equals(empty, buffer)) {
                bottom = y;
                break;
            }
        }

        int bufferSize = bottom -top +1;
        empty = new int[bufferSize];
        buffer = new int[bufferSize];
        Arrays.fill(empty,0);

        for (int x = 0; x < width; x++) {
            bitmap.getPixels(buffer, 0, 1, x, top + 1, 1, bufferSize);
            if (!Arrays.equals(empty, buffer)) {
                left = x;
                break;
            }
        }

        for (int x = width - 1; x > left; x--) {
            bitmap.getPixels(buffer, 0, 1, x, top + 1, 1, bufferSize);
            if (!Arrays.equals(empty, buffer)) {
                right = x;
                break;
            }
        }

        return new Rect(left, top, right, bottom);
        //Bitmap cropedBitmap = Bitmap.createBitmap(bitmap, left, top, right-left, bottom-top);
        //return cropedBitmap;
    }


    /**
     * Returns a center cropped bitmap from a source bitmap given a destination width and height
     * @param bitmap The source bitmap
     * @param destWidth The destination's width
     * @param destHeight The destination's height
     * @return A center cropped Bitmap
     */
    public static Bitmap getCenterCropBitmap(Bitmap bitmap, float destWidth, float destHeight) {
        float destAspectRatio = destWidth/destHeight;
        float srcAspectRatio = ((float)bitmap.getWidth())/bitmap.getHeight();

        if(destAspectRatio == srcAspectRatio) {             //No need to crop
            return bitmap;
        } else if (destAspectRatio > srcAspectRatio) {
            float calulatedHeight = destWidth/destAspectRatio;
            return ThumbnailUtils.extractThumbnail(bitmap, (int)destWidth, (int)calulatedHeight);
        } else {
            float calculatedWidth = destHeight*destAspectRatio;
            return ThumbnailUtils.extractThumbnail(bitmap, (int)calculatedWidth, (int)destHeight);
        }
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
