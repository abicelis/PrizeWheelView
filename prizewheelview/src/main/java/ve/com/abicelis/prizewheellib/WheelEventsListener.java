package ve.com.abicelis.prizewheellib;

/**
 * Created by abicelis on 26/7/2017.
 */

public interface WheelEventsListener {
    void onWheelFlung();
    void onWheelSettled(int sectionIndex, double angle);
}
