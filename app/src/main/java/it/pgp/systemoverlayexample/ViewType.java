package it.pgp.systemoverlayexample;

import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

public enum ViewType {

    CONTAINER,
    ANCHOR;

    WindowManager.LayoutParams params;

    static final Map<ViewType, WindowManager.LayoutParams> m;

    static {
        m = new HashMap<>();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                200,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.START | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        m.put(CONTAINER,params);

        WindowManager.LayoutParams paramsA = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        paramsA.gravity = Gravity.START | Gravity.TOP;
        paramsA.x = 0;
        paramsA.y = 0;
        paramsA.width = 0;
        paramsA.height = 0;

        m.put(ANCHOR,paramsA);
    }

    public WindowManager.LayoutParams getParams() {
        return m.get(this);
    }
}
