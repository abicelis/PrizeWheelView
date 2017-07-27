package ve.com.abicelis.prizewheellib.model;

/**
 * Created by abicelis on 26/7/2017.
 */

public enum MarkerPosition {
    BOTTOM_RIGHT(45),
    BOTTOM(90),
    BOTTOM_LEFT(135),
    LEFT(180),
    TOP_LEFT(225),
    TOP(270),
    TOP_RIGHT(315),
    RIGHT(0),
    ;

    int degreeOffset;

    MarkerPosition(int degreeOffset) {
        this.degreeOffset = degreeOffset;
    }


    public int getDegreeOffset() {
        return degreeOffset;
    }
}
