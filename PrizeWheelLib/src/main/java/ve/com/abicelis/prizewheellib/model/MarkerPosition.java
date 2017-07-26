package ve.com.abicelis.prizewheellib.model;

/**
 * Created by abicelis on 26/7/2017.
 */

public enum MarkerPosition {
    TOP(90),
    TOP_LEFT(135),
    TOP_RIGHT(45),
    LEFT(180),
    RIGHT(0),
    BOTTOM(270),
    BOTTOM_LEFT(225),
    BOTTOM_RIGHT(315)
    ;

    int degreeOffset;

    MarkerPosition(int degreeOffset) {
        degreeOffset = degreeOffset;
    }


    public int getDegreeOffset() {
        return degreeOffset;
    }
}
