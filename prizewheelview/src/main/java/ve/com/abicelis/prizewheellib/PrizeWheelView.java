package ve.com.abicelis.prizewheellib;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import ve.com.abicelis.prizewheellib.exceptions.InvalidWheelSectionDataException;
import ve.com.abicelis.prizewheellib.exceptions.InvalidWheelSectionsException;
import ve.com.abicelis.prizewheellib.model.MarkerPosition;
import ve.com.abicelis.prizewheellib.model.SectionType;
import ve.com.abicelis.prizewheellib.model.WheelBitmapSection;
import ve.com.abicelis.prizewheellib.model.WheelColorSection;
import ve.com.abicelis.prizewheellib.model.WheelDrawableSection;
import ve.com.abicelis.prizewheellib.model.WheelSection;

/**
 * Created by abicelis on 25/7/2017.
 */

public class PrizeWheelView extends RelativeLayout {


    //Internal data
    ImageView mWheel;
    ImageView mMarker;
    RelativeLayout mMarkerContainer;
    private int wheelHeight, wheelWidth;
    private static Matrix matrix;
    private GestureDetector gestureDetector;
    private WheelTouchListener touchListener;
    private boolean[] quadrantTouched = new boolean[] { false, false, false, false, false };
    private boolean allowRotating = true;
    private FlingRunnable flingRunnable;

    //Configurable options
    private List<WheelSection> mWheelSections;
    private MarkerPosition mMarkerPosition = MarkerPosition.TOP;
    private boolean mCanGenerateWheel;
    private @ColorRes int mWheelBorderLineColor = -1;
    private int mWheelBorderLineThickness = 10;
    private @ColorRes int mWheelSeparatorLineColor = -1;
    private int mWheelSeparatorLineThickness = 10;
    private WheelEventsListener mListener;
    private float initialFlingDampening = Constants.INITIAL_FLING_VELOCITY_DAMPENING;
    private float flingVelocityDampening = Constants.FLING_VELOCITY_DAMPENING;






    /* Constructors and init */
    public PrizeWheelView(Context context) {
        super(context);
        init(context, null);
    }

    public PrizeWheelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PrizeWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.prize_wheel_view, this);

        mWheel = (ImageView) findViewById(R.id.prize_wheel_view_wheel);
        mMarker = (ImageView) findViewById(R.id.prize_wheel_view_marker);
        mMarkerContainer = (RelativeLayout) findViewById(R.id.prize_wheel_view_marker_container);

        mWheel.setScaleType(ImageView.ScaleType.MATRIX);


        matrix = new Matrix();
        gestureDetector = new GestureDetector(context, new WheelGestureListener());
        touchListener = new WheelTouchListener();
        mWheel.setOnTouchListener(touchListener);



        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // method called more than once, but the values only need to be initialized one time
                if (wheelHeight == 0 || wheelWidth == 0) {

                    //Grab the shortest of the container dimensions
                    int minDimen = wheelHeight = Math.min(getHeight(), getWidth());

                    //Apply the margin for the marker. Use those dimensions for the wheel
                    wheelHeight = wheelWidth = minDimen - (int)(DimensionUtil.convertDpToPixel(Constants.WHEEL_MARGIN_FOR_MARKER_DP)*2);

                    //Resize the wheel's imageview
                    mWheel.getLayoutParams().height = wheelHeight;
                    mWheel.getLayoutParams().width = wheelWidth;
                    mWheel.requestLayout();

                    //Resize the marker's container
                    mMarkerContainer.getLayoutParams().height = minDimen;
                    mMarkerContainer.getLayoutParams().width = minDimen;
                    mMarkerContainer.requestLayout();
                    mMarkerContainer.setRotation(mMarkerPosition.getDegreeOffset());

                    if(mCanGenerateWheel)
                        generateWheelImage();
                }
            }
        });

    }






    /* Public setters */

    public void setWheelSections(List<WheelSection> wheelSections) {
        if(wheelSections == null || wheelSections.size() < Constants.MINIMUM_WHEEL_SECTIONS || wheelSections.size() > Constants.MAXIMUM_WHEEL_SECTIONS)
            throw new InvalidWheelSectionsException();

        mWheelSections = wheelSections;
    }

    public void setMarkerPosition(@NonNull MarkerPosition markerPosition) {
        mMarkerPosition = markerPosition;
        mMarkerContainer.setRotation(mMarkerPosition.getDegreeOffset());
    }

    /**
     * Set the initial fling dampening.
     * Recommend a number between 1.0 (no dampening) and 5.0 (lots of dampening). Default 3.0
     */
    public void setInitialFlingDampening(float dampening) {
        initialFlingDampening = dampening;
    }

    /**
     * Set the velocity dampening when wheel is flung.
     * Recommend a number between 1 (no dampening) and 1.1 (lots of dampening). Default 1.06
     */
    public void setFlingVelocityDampening(float dampening) {
        initialFlingDampening = dampening;
    }

    public void setWheelBorderLineColor(@ColorRes int color) {
        mWheelBorderLineColor = color;
    }

    public void setWheelBorderLineThickness(int thickness) {
        if(thickness >= 0)
            mWheelBorderLineThickness = thickness;
    }

    public void setWheelSeparatorLineColor(@ColorRes int color) {
        mWheelSeparatorLineColor = color;
    }

    public void setWheelSeparatorLineThickness(int thickness) {
        if(thickness >= 0)
            mWheelSeparatorLineThickness = thickness;
    }

    public void setWheelEventsListener(WheelEventsListener listener) {
        mListener = listener;
    }

    public void generateWheel() {
        if(wheelHeight == 0)        //If view doesn't have width/height yet
            mCanGenerateWheel = true;
        else
            generateWheelImage();
    }

    public void stopWheel(){
        allowRotating = false;
    }

    /**
     * Fling the wheel
     * @param velocity the speed of the fling
     * @param clockwise
     */
    public void flingWheel(int velocity, boolean clockwise) {
        doFlingWheel((clockwise ? -velocity : velocity));
    }




    /* Internal methods */

    /**
     * Rotate the wheel.
     *
     * @param degrees The degrees, the wheel should get rotated.
     */
    private void rotateWheel(float degrees) {
        matrix.postRotate(degrees, wheelWidth / 2, wheelHeight / 2);
        mWheel.setImageMatrix(matrix);
    }

    /**
     * Reset touch quadrants to false.
     *
     */
    private void resetQuadrants() {
        for (int i = 0; i < quadrantTouched.length; i++) {
            quadrantTouched[i] = false;
        }
    }

    /**
     * @return The selected quadrant.
     */
    private static int getQuadrant(double x, double y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
            return y >= 0 ? 2 : 3;
        }
    }

    /**
     * @return The current rotation of the wheel.
     */
    private static double getCurrentRotation() {
        float[] v = new float[9];
        matrix.getValues(v);
        double angle = Math.round((Math.atan2(v[Matrix.MSKEW_X], v[Matrix.MSCALE_X]) * (180 / Math.PI)) );

        if(angle > 0)
            return angle;
        else
            return 360 + angle;
    }

    /**
     * @return The current section.
     */
    private int getCurrentSelectedSectionIndex() {
        double sectionAngle = (360.0f / mWheelSections.size());
        double currentRotation = getCurrentRotation() + mMarkerPosition.getDegreeOffset();

        if(currentRotation > 360)
            currentRotation = currentRotation - 360;

        int selection = (int)Math.floor(currentRotation/sectionAngle);

        if(selection >= mWheelSections.size())      //Rounding errors occur. Limit to items-1
            selection = mWheelSections.size()-1;

        return selection;
    }

    /**
     * Generates the wheel bitmap.
     *
     */
    private void generateWheelImage() {

        if(mWheelSections == null)
            throw new InvalidWheelSectionsException("You must use setWheelSections() to set the sections of the wheel.");

        float startAngle = 0f;
        float sweepAngle = (360.0f / mWheelSections.size());
        RectF box = new RectF(2, 2, wheelWidth-2 , wheelHeight-2);

        //Init whitePaint for masking
        Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStrokeWidth(1f);
        whitePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //Init maskPaint for erasing unmasked (transparent) sections
        Paint maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        //Init mask and result canvases
        Bitmap mask = Bitmap.createBitmap(wheelWidth, wheelHeight, Bitmap.Config.ARGB_8888);
        Bitmap result = Bitmap.createBitmap(wheelWidth, wheelHeight, Bitmap.Config.ARGB_8888);
        Canvas resultCanvas = new Canvas(result);
        Canvas maskCanvas = new Canvas(mask);


        for(WheelSection section : mWheelSections) {

            //If drawing a color, process is much simpler, handle it here
            if(section.getType().equals(SectionType.COLOR)) {
                int colorRes = ((WheelColorSection)section).getColor();

                Paint colorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                colorPaint.setColor(ContextCompat.getColor(getContext(), colorRes));
                colorPaint.setStrokeWidth(1f);
                colorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                resultCanvas.drawArc(box, startAngle, sweepAngle, true, colorPaint);

                startAngle += sweepAngle;
                continue;
            }


            //Clear maskCanvas, draw new arc mask
            maskCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            maskCanvas.drawArc(box, startAngle, sweepAngle, true, whitePaint);
            Rect drawnMaskRect = ImageUtil.getCroppedRect(mask);

            //Grab the bitmap for this section
            Bitmap sectionBitmap;
            switch (section.getType()) {
                case BITMAP:
                    sectionBitmap = ((WheelBitmapSection)section).getBitmap();
                    if(sectionBitmap == null)
                        throw new InvalidWheelSectionDataException("Invalid bitmap. WheelSection data = " + section.toString());
                    break;
                case DRAWABLE:
                    int drawableRes = ((WheelDrawableSection)section).getDrawableRes();
                    sectionBitmap = BitmapFactory.decodeResource(getContext().getResources(), drawableRes);
                    if(sectionBitmap == null) {
                        try {
                            //Try to get the name
                            String resourceEntryName = getResources().getResourceEntryName(drawableRes);
                            throw new InvalidWheelSectionDataException("Problem generating bitmap from drawable. Resource name='" + resourceEntryName + "', Resource ID="+ drawableRes);
                        } catch (Resources.NotFoundException e) {
                            throw new InvalidWheelSectionDataException("Problem generating bitmap from drawable. Could not find resource. Resource ID="+ drawableRes);
                        }
                    }
                    break;
                default:
                    throw new InvalidWheelSectionDataException("Unexpected SectionType error. Please report this error. Section data=" + section.toString());
            }



            //Temp bitmap is necessary because sectionBitmap is immutable and therefore cannot be drawn to
            Bitmap temp = Bitmap.createBitmap(wheelWidth, wheelHeight, Bitmap.Config.ARGB_8888);
            Canvas tempCanvas = new Canvas(temp);

            //Draw section image onto temp, then draw mask on it
            tempCanvas.drawBitmap(sectionBitmap, null, drawnMaskRect, null);
            tempCanvas.drawBitmap(mask, 0, 0, maskPaint);

            //Draw masked image to resultCanvas
            resultCanvas.drawBitmap(temp, 0, 0, new Paint());

            //Increment startAngle
            startAngle += sweepAngle;
        }

        //If a wheel separator line color was set
        if(mWheelSeparatorLineColor != -1) {
            Paint color = new Paint(Paint.ANTI_ALIAS_FLAG);
            color.setColor(ContextCompat.getColor(getContext(), mWheelSeparatorLineColor));
            color.setStyle(Paint.Style.STROKE);
            color.setStrokeWidth(mWheelSeparatorLineThickness);


            int r = Math.min(wheelWidth, wheelHeight)/2;
            int centerX = wheelWidth/2;
            int centerY = wheelHeight/2;

            for(int i = 0; i < mWheelSections.size() ; i++) {


                double t = 2 * Math.PI * i / mWheelSections.size();
                int x = (int) Math.round(centerX + r * Math.cos(t));
                int y = (int) Math.round(centerY + r * Math.sin(t));

                resultCanvas.drawLine(centerX, centerY, x, y, color);

            }
        }


        //If a wheelBorder line color was set
        if(mWheelBorderLineColor != -1) {
            Paint color = new Paint(Paint.ANTI_ALIAS_FLAG);
            color.setColor(ContextCompat.getColor(getContext(), mWheelBorderLineColor));
            color.setStyle(Paint.Style.STROKE);
            color.setStrokeWidth(mWheelBorderLineThickness);

            resultCanvas.drawCircle(wheelWidth/2, wheelHeight/2, (Math.min(wheelWidth, wheelHeight)-mWheelBorderLineThickness)/2, color);
        }



        mWheel.setImageBitmap(result);
    }


    private void doFlingWheel(float velocity) {

        //Stop previous fling
        removeCallbacks(flingRunnable);

        //Notify fling
        if(mListener != null)
            mListener.onWheelFlung();

        //Launch new fling
        allowRotating = true;
        flingRunnable = new FlingRunnable( velocity / initialFlingDampening );
        post(flingRunnable);
    }





    private class WheelTouchListener implements View.OnTouchListener {

        private double startAngle;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    resetQuadrants();
                    startAngle = getAngle(event.getX(), event.getY());
                    allowRotating = false;
                    break;

                case MotionEvent.ACTION_MOVE:
                    double currentAngle = getAngle(event.getX(), event.getY());
                    rotateWheel((float) (startAngle - currentAngle));
                    startAngle = currentAngle;
                    break;

                case MotionEvent.ACTION_UP:
                    allowRotating = true;
                    break;
            }

            // set the touched quadrant to true
            quadrantTouched[getQuadrant(event.getX() - (wheelWidth / 2), wheelHeight - event.getY() - (wheelHeight / 2))] = true;

            //Notify gesture detector
            gestureDetector.onTouchEvent(event);

            return true;
        }

        /**
         * @return The angle of the unit circle with the image view's center
         */
        private double getAngle(double xTouch, double yTouch) {
            double x = xTouch - (wheelWidth / 2d);
            double y = wheelHeight - yTouch - (wheelHeight / 2d);

            return (Math.atan2(y,x) * 180) / Math.PI;
        }

    }
    private class WheelGestureListener implements GestureDetector.OnGestureListener {



        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // get the quadrant of the start and the end of the fling
            int q1 = getQuadrant(e1.getX() - (wheelWidth / 2), wheelHeight - e1.getY() - (wheelHeight / 2));
            int q2 = getQuadrant(e2.getX() - (wheelWidth / 2), wheelHeight - e2.getY() - (wheelHeight / 2));

            // the inversed rotations
            if ((q1 == 2 && q2 == 2 && Math.abs(velocityX) < Math.abs(velocityY))
                    || (q1 == 3 && q2 == 3)
                    || (q1 == 1 && q2 == 3)
                    || (q1 == 4 && q2 == 4 && Math.abs(velocityX) > Math.abs(velocityY))
                    || ((q1 == 2 && q2 == 3) || (q1 == 3 && q2 == 2))
                    || ((q1 == 3 && q2 == 4) || (q1 == 4 && q2 == 3))
                    || (q1 == 2 && q2 == 4 && quadrantTouched[3])
                    || (q1 == 4 && q2 == 2 && quadrantTouched[3])) {

                doFlingWheel((-1 * (velocityX + velocityY)));
                //post(new FlingRunnable( (-1 * (velocityX + velocityY)) / initialFlingDampening ));
                //if(mListener != null)
                    //mListener.onWheelFlung();
            } else {
                // the normal rotation
                doFlingWheel((velocityX + velocityY));

                //post(new FlingRunnable( (velocityX + velocityY) / initialFlingDampening ));
                //if(mListener != null)
                    //mListener.onWheelFlung();
            }

            return true;
        }


    }


    /**
     * A {@link Runnable} for animating the the dialer's fling.
     */
    private class FlingRunnable implements Runnable {

        private float velocity;

        public FlingRunnable(float velocity) {
            this.velocity = velocity;
        }

        @Override
        public void run() {

            if(!allowRotating) {        //Fling has been stopped, so stop now.
                if(mListener != null) {
                    mListener.onWheelStopped();
                }
            } else if (Math.abs(velocity) > 5) {
                rotateWheel(velocity / 75);
                velocity /= flingVelocityDampening;

                // post this instance again
                post(this);
            } else {
                if(mListener != null) {
                    mListener.onWheelSettled(getCurrentSelectedSectionIndex(), getCurrentRotation());
                }
            }
        }
    }



}
