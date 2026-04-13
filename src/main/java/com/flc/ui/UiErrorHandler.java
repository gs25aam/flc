package com.flc.ui;

import com.flc.exception.BookingStateException;
import com.flc.exception.CapacityExceededException;
import com.flc.exception.DuplicateBookingException;
import com.flc.exception.EntityNotFoundException;
import com.flc.exception.InvalidRatingException;
import com.flc.exception.TimeSlotConflictException;

public final class UiErrorHandler {
    private UiErrorHandler() {
    }

    public static String messageFor(RuntimeException exception) {
        if (isUserFacing(exception)) {
            return "Action failed: " + exception.getMessage();
        }

        exception.printStackTrace(System.err);
        return "Unexpected application error: "
                + exception.getClass().getSimpleName()
                + ". See console for details.";
    }

    private static boolean isUserFacing(RuntimeException exception) {
        return exception instanceof BookingStateException
                || exception instanceof CapacityExceededException
                || exception instanceof DuplicateBookingException
                || exception instanceof EntityNotFoundException
                || exception instanceof InvalidRatingException
                || exception instanceof TimeSlotConflictException
                || exception instanceof IllegalArgumentException;
    }
}
