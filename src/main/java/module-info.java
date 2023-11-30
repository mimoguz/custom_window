/** The library depends on JavaFX and JNA. */
module io.github.mimoguz.custom_window {
    requires transitive javafx.graphics;
    requires com.sun.jna;
    requires com.sun.jna.platform;

    exports io.github.mimoguz.customwindow;
}