package ve.com.abicelis.prizewheellib;

import android.graphics.Bitmap;
import android.graphics.Rect;

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
    public static Rect getCroppedRect(Bitmap bitmap){

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

}
