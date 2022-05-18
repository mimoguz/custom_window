package custom_window;

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

@SuppressWarnings("UnusedReturnValue")
public class StageOps {
    public static class WindowHandle {
        private WinDef.HWND value;
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
     * @param stage JavaFX Stage
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

    public static boolean setBorderColor(WindowHandle handle, Color color) {
        return dwmSetIntValue(handle, DwmAttribute.DWMWA_BORDER_COLOR, RGB(color));
    }

    public static boolean setCaptionColor(WindowHandle handle, Color color) {
        return dwmSetIntValue(handle, DwmAttribute.DWMWA_CAPTION_COLOR, RGB(color));
    }

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
}
