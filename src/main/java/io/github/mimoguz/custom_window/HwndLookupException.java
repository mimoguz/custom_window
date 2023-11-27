package io.github.mimoguz.custom_window;

public final class HwndLookupException extends Exception {
    private final HwndLookupError error;

    /**
     * @return The error type
     */
    public HwndLookupError getError() {
        return error;
    }

    public HwndLookupException(HwndLookupError error) {
        this.error = error;
    }
}
