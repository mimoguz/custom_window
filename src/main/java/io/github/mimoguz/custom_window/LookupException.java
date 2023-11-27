package io.github.mimoguz.custom_window;

public final class LookupException extends Exception {
    private final LookupError error;

    /**
     * @return The error type
     */
    public LookupError getError() {
        return error;
    }

    public LookupException(LookupError error) {
        this.error = error;
    }
}
