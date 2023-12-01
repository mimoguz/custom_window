package io.github.mimoguz.customwindow;

import javafx.stage.Stage;

/**
 * When {@link WindowHandle#tryFind(Stage) WindowHandle.tryFind} method fails,
 * it throws this exception.
 */
@SuppressWarnings("unused")
public final class HwndLookupException extends Exception {
    private final Error error;

    /**
     * Get the main cause of the exception.
     *
     * @return The error type
     */
    public Error getError() {
        return error;
    }

    public HwndLookupException(final Error error) {
        this.error = error;
    }

    public enum Error {
        /**
         * Current platform is not supported
         */
        NOT_SUPPORTED,

        /**
         * Couldn't find the window
         */
        NOT_FOUND,

        /**
         * titleProperty of the window is bound, can't perform the search operation.
         */
        BOUND
    }
}
