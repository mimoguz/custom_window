package io.github.mimoguz.customwindow;

import com.sun.jna.Platform;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * A wrapper and relevant methods for a Win32 HWND.
 * Use the {@link #tryFind(Stage) tryFind} static method to acquire.
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class WindowHandle {
    private final WinDef.HWND hwnd;

    private WindowHandle(final WinDef.HWND hwnd) {
        this.hwnd = hwnd;
    }

    /**
     * Try to acquire Win32 window handle.
     * 
     * @param stage The top level window to search
     * @return Handle to the top level window
     * @throws HwndLookupException When the platform is not supported, titleProperty
     *                             is bound, or the window was not found.
     * @see HwndLookupException.Error
     */
    public static WindowHandle tryFind(final Stage stage) throws HwndLookupException {
        if (Platform.getOSType() != Platform.WINDOWS) {
            throw new HwndLookupException(HwndLookupException.Error.NOT_SUPPORTED);
        }

        if (stage.titleProperty().isBound()) {
            throw new HwndLookupException(HwndLookupException.Error.BOUND);
        }

        final var searchString = "stage_" + java.util.UUID.randomUUID();
        final var title = stage.getTitle();
        stage.setTitle(searchString);
        final var hwnd = User32.INSTANCE.FindWindow(null, searchString);
        stage.setTitle(title);
        if (hwnd != null) {
            return new WindowHandle(hwnd);
        }

        throw new HwndLookupException(HwndLookupException.Error.NOT_FOUND);
    }

    /**
     * Sets the border color.
     *
     * @param color Border color
     * @return True if it was successful, false if it wasn't.
     */
    public boolean setBorderColor(final Color color) {
        return dwmSetIntValue(DwmAttribute.DWMWA_BORDER_COLOR, rgb(color));
    }

    /**
     * Sets the border color.
     *
     * @param color Border color
     * @return This
     */
    public WindowHandle withBorderColor(final Color color) {
        setBorderColor(color);
        return this;
    }

    /**
     * Sets the title bar background color.
     *
     * @param color Caption color
     * @return True if it was successful, false if it wasn't.
     */
    public boolean setCaptionColor(final Color color) {
        return dwmSetIntValue(DwmAttribute.DWMWA_CAPTION_COLOR, rgb(color));
    }

    /**
     * Sets the title bar background color.
     *
     * @param color Caption color
     * @return This.
     */
    public WindowHandle withCaptionColor(final Color color) {
        setCaptionColor(color);
        return this;
    }

    /**
     * Sets the title text color.
     *
     * @param color Caption color
     * @return True if it was successful, false if it wasn't.
     */
    public boolean setTextColor(final Color color) {
        return dwmSetIntValue(DwmAttribute.DWMWA_TEXT_COLOR, rgb(color));
    }

    /**
     * Sets the title text color.
     *
     * @param color Caption color
     * @return This.
     */
    public WindowHandle withTextColor(final Color color) {
        setTextColor(color);
        return this;
    }

    /**
     * A wrapper for DwmSetWindowAttribute.
     *
     * @param attribute dwAttribute
     * @param value     pvAttribute
     * @return True if it was successful, false if it wasn't.
     */
    public boolean dwmSetBooleanValue(final DwmAttribute attribute, final boolean value) {
        return isOk(
                DwmSupport.INSTANCE.DwmSetWindowAttribute(
                        hwnd,
                        attribute.value,
                        new WinDef.BOOLByReference(new WinDef.BOOL(value)),
                        WinDef.BOOL.SIZE));
    }

    /**
     * A wrapper for DwmSetWindowAttribute.
     *
     * @param attribute dwAttribute
     * @param value     pvAttribute
     * @return True if it was successful, false if it wasn't.
     */
    public boolean dwmSetIntValue(final DwmAttribute attribute, final int value) {
        return isOk(
                DwmSupport.INSTANCE.DwmSetWindowAttribute(
                        hwnd,
                        attribute.value,
                        new WinDef.DWORDByReference(new WinDef.DWORD(value)),
                        WinDef.DWORD.SIZE));
    }

    private static int floatingTo8Bit(final double n) {
        return (int) Math.min(255.0, Math.max(n * 255.0, 0.0));
    }

    private static boolean isOk(final WinNT.HRESULT result) {
        return WinNT.HRESULT.compare(result, W32Errors.S_OK) == 0;
    }

    private static int rgb(final Color color) {
        return (floatingTo8Bit(color.getBlue()) << 16)
                | (floatingTo8Bit(color.getGreen()) << 8)
                | floatingTo8Bit(color.getRed());
    }
}
