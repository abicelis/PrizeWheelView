package ve.com.abicelis.prizewheellib.exceptions;

import ve.com.abicelis.prizewheellib.Constants;

/**
 * Created by abicelis on 26/7/2017.
 */

public class InvalidWheelSectionDataException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Invalid data in wheel section";

    public InvalidWheelSectionDataException() {
        super(DEFAULT_MESSAGE);
    }
    public InvalidWheelSectionDataException(String message) {
        super(message);
    }
    public InvalidWheelSectionDataException(String message, Throwable cause) {
        super(message, cause);
    }

}
