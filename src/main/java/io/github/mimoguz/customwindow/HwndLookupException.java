package io.github.mimoguz.customwindow;

@SuppressWarnings("unused")
public final class HwndLookupException extends Exception {
    private final Error error;

    /**
     * @return The error type
     */
    public Error getError() {
        return error;
    }

    public HwndLookupException(final Error error) {
        this.error = error;
    }

    public enum Error {
        /** Current platform is not supported */
        NOT_SUPPORTED,

        /** Couldn't find the window */
        NOT_FOUND,

        /** titleProperty of the window is bound, can't perform the search operation. */
        BOUND
    }
}
