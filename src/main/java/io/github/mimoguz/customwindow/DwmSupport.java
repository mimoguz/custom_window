package io.github.mimoguz.customwindow;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

interface DwmSupport extends Library {
    DwmSupport INSTANCE = Native.load("dwmapi", DwmSupport.class);

    WinNT.HRESULT DwmSetWindowAttribute(
            WinDef.HWND hwnd,
            int dwAttribute,
            PointerType pvAttribute,
            int cbAttribute);
}
