package io.github.mimoguz.custom_window;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.Platform;

import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * A small collection of utility methods to customize a window.
 * Targets Windows 11+, won't show any effect on unsupported OSes.
 */
@SuppressWarnings("UnusedReturnValue")
public class StageOps {
    /**
     * A wrapper for HWND type.
     */
    public static class WindowHandle {
        private WinDef.HWND value;

        private WindowHandle() {
        }
    }

    private interface DwmSupport extends Library {
        DwmSupport INSTANCE = Native.load("dwmapi", DwmSupport.class);

        WinNT.HRESULT DwmSetWindowAttribute(
                WinDef.HWND hwnd,
                int dwAttribute,
                PointerType pvAttribute,
                int cbAttribute
        );
    }

    /**
     * A wrapper for DwmSetWindowAttribute.
     *
     * @param handle    WindowHandle for the window. Can be obtained by using findWindowHandle method. Can be null.
     * @param attribute dwAttribute
     * @param value     pvAttribute
     * @return True if was successful, false if it wasn't.
     */
    public static boolean dwmSetBooleanValue(WindowHandle handle, DwmAttribute attribute, boolean value) {
        if (handle == null) {
            return false;
        }
        return isOk(
                DwmSupport.INSTANCE.DwmSetWindowAttribute(
                        handle.value,
                        attribute.value,
                        new WinDef.BOOLByReference(new WinDef.BOOL(value)),
                        WinDef.BOOL.SIZE
                )
        );
    }

    /**
     * A wrapper for DwmSetWindowAttribute.
     *
     * @param handle    WindowHandle for the window. Can be obtained by using findWindowHandle method. Can be null.
     * @param attribute dwAttribute
     * @param value     pvAttribute
     * @return True if was successful, false if it wasn't.
     */
    public static boolean dwmSetIntValue(WindowHandle handle, DwmAttribute attribute, int value) {
        if (handle == null) {
            return false;
        }
        return isOk(
                DwmSupport.INSTANCE.DwmSetWindowAttribute(
                        handle.value,
                        attribute.value,
                        new WinDef.DWORDByReference(new WinDef.DWORD(value)),
                        WinDef.DWORD.SIZE
                )
        );
    }

    /**
     * Try find the window handle.
     *
     * @param stage JavaFX Stage to search.
     * @return WindowHandle if it can find, null otherwise.
     */
    public static WindowHandle findWindowHandle(Stage stage) {
        if (Platform.getOSType() != Platform.WINDOWS) {
            return null;
        }
        final var searchString = "stage_" + java.util.UUID.randomUUID();
        final var title = stage.getTitle();
        stage.setTitle(searchString);
        final var hwnd = User32.INSTANCE.FindWindow(null, searchString);
        stage.setTitle(title);
        if (hwnd != null) {
            final var wrapper = new WindowHandle();
            wrapper.value = hwnd;
            return wrapper;
        }
        return null;
    }

    /**
     * Sets the border color of a window.
     *
     * @param handle WindowHandle for the window. Can be obtained by using findWindowHandle method. Can be null.
     * @param color  Border color
     * @return True if was successful, false if it wasn't.
     */
    public static boolean setBorderColor(WindowHandle handle, Color color) {
        return dwmSetIntValue(handle, DwmAttribute.DWMWA_BORDER_COLOR, RGB(color));
    }

    /**
     * Sets the title bar background color of a window.
     *
     * @param handle WindowHandle for the window. Can be obtained by using findWindowHandle method. Can be null.
     * @param color  Caption color
     * @return True if was successful, false if it wasn't.
     */
    public static boolean setCaptionColor(WindowHandle handle, Color color) {
        return dwmSetIntValue(handle, DwmAttribute.DWMWA_CAPTION_COLOR, RGB(color));
    }

    /**
     * Sets the title text color of a window.
     *
     * @param handle WindowHandle for the window. Can be obtained by using findWindowHandle method. Can be null.
     * @param color  Caption color
     * @return True if was successful, false if it wasn't.
     */
    public static boolean setTextColor(WindowHandle handle, Color color) {
        return dwmSetIntValue(handle, DwmAttribute.DWMWA_TEXT_COLOR, RGB(color));
    }

    private static int floatingTo8Bit(double n) {
        return (int) Math.min(255.0, Math.max(n * 255.0, 0.0));
    }

    private static boolean isOk(WinNT.HRESULT result) {
        return WinNT.HRESULT.compare(result, W32Errors.S_OK) == 0;
    }

    private static int RGB(Color color) {
        return (floatingTo8Bit(color.getBlue()) << 16)
                | (floatingTo8Bit(color.getGreen()) << 8)
                | floatingTo8Bit(color.getRed());
    }

    private StageOps() {
    }
}
