package com.sec.daliview;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class DaliView extends SurfaceView implements SurfaceHolder.Callback {

    public DaliView(Context context) {
        this(context, null);
    }

    public DaliView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DaliView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);

        System.loadLibrary("dali-core");
        System.loadLibrary("dali-adaptor");
        System.loadLibrary("dali-toolkit");

        System.loadLibrary("daliview");
        nativeOnConfigure(context.getAssets(), context.getFilesDir().getAbsolutePath());

        System.loadLibrary("dalidemo");
        nativeHandle = nativeOnCreate();
    }

    public DaliView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getHolder().addCallback(this);

        System.loadLibrary("dali-core");
        System.loadLibrary("dali-adaptor");
        System.loadLibrary("dali-toolkit");

        System.loadLibrary("daliview");
        nativeOnConfigure(context.getAssets(), context.getFilesDir().getAbsolutePath());

        System.loadLibrary("dalidemo");
        nativeHandle = nativeOnCreate();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        nativeSetSurface(nativeHandle, holder.getSurface());
        nativeOnResume(nativeHandle);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        nativeOnPause(nativeHandle);
        nativeSetSurface(nativeHandle, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        nativeOnTouchEvent(nativeHandle, event.getDeviceId(), event.getAction(), event.getX(),  event.getY(), event.getEventTime());
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        nativeOnKeyEvent(nativeHandle, event.getDeviceId(), event.getAction(), event.getKeyCode(), event.getEventTime());
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            nativeOnFinalize(nativeHandle);
        } finally {
            super.finalize();
        }
    }

    static class NativeCallback extends Handler {
        long nativeCallback;
        long nativeCallbackData;
        long start;
        long delay;
        boolean paused;
        int id;

        NativeCallback(int id, long nativeCallback, long nativeCallbackData, long delay) {
            this.id = id;
            this.nativeCallback = nativeCallback;
            this.nativeCallbackData = nativeCallbackData;
            this.delay = delay;
            start = System.currentTimeMillis();
            paused = true;
        }

        void pause() {
            if (!paused) {
                delay = start + delay - System.currentTimeMillis();
                if (delay < 0)
                    delay = 0;

                removeCallbacksAndMessages(null);
                paused = true;
            }
        }

        void resume() {
            if (paused) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (nativeOnCallback(nativeCallback, nativeCallbackData))
                            postDelayed(this, delay);
                        else
                            mIdleCallbacks.remove(new Integer(id));
                    }
                }, delay);
                paused = false;
            }
        }

        void clear() {
            removeCallbacksAndMessages(null);
            paused = true;
        }
    }

    private static ArrayMap<Integer, NativeCallback> mIdleCallbacks = new ArrayMap<Integer, NativeCallback>();
    private static int mIdleId = 0;
    public static int addIdle(long nativeCallback, long nativeCallbackData, long delay) {
        ++mIdleId;
        if (mIdleId == 0)
            ++mIdleId;

        NativeCallback callback = new NativeCallback(mIdleId, nativeCallback, nativeCallbackData, delay);
        mIdleCallbacks.put(new Integer(mIdleId), callback);
        callback.resume();

        return mIdleId;
    }

    public static void removeIdle(int idleId) {
        Integer key = new Integer(idleId);
        NativeCallback callback = mIdleCallbacks.get(key);
        if (callback != null) {
            callback.clear();
            mIdleCallbacks.remove(key);
        }
    }

    public static void pauseIdle(int idleId) {
        Integer key = new Integer(idleId);
        NativeCallback callback = mIdleCallbacks.get(key);
        if (callback != null)
            callback.pause();
    }

    public static void resumeIdle(int idleId) {
        Integer key = new Integer(idleId);
        NativeCallback callback = mIdleCallbacks.get(key);
        if (callback != null)
            callback.resume();
    }

    public long nativeHandle = 0;
    public native void nativeOnConfigure(AssetManager assetManager, String dataPath);
    public native long nativeOnCreate();
    public native void nativeOnResume(long handle);
    public native void nativeOnPause(long handle);
    public native void nativeSetSurface(long handle, Surface surface);
    public native void nativeOnTouchEvent(long handle, int deviceId, int action, float x, float y, long timestamp);
    public native void nativeOnKeyEvent(long handle, int deviceId, int action, int keyCode, long timestamp);
    public native void nativeOnFinalize(long handle);

    public static native boolean nativeOnCallback(long nativeCallback, long nativeCallbackData);

//    static {
//        System.loadLibrary("daliview");
//    }
}

