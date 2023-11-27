package io.github.mimoguz.custom_window;

public enum HwndLookupError {
    /** Current platform is not supported */
    NOT_SUPPORTED,

    /** Couldn't find the window */
    NOT_FOUND,

    /** titleProperty of the window is bound, can't perform the search operation. */
    BOUND
}
