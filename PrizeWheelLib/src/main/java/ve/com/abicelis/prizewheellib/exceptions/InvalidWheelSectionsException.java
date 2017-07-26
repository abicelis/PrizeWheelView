package ve.com.abicelis.prizewheellib.exceptions;

import ve.com.abicelis.prizewheellib.Constants;

/**
 * Created by abicelis on 26/7/2017.
 */

public class InvalidWheelSectionsException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Invalid amount of Wheel Sections. Should be more or equal than " + Constants.MINIMUM_WHEEL_SECTIONS
            + " and less or equal than " + Constants.MAXIMUM_WHEEL_SECTIONS;

    public InvalidWheelSectionsException() {
        super(DEFAULT_MESSAGE);
    }
    public InvalidWheelSectionsException(String message) {
        super(message);
    }
    public InvalidWheelSectionsException(String message, Throwable cause) {
        super(message, cause);
    }

}
