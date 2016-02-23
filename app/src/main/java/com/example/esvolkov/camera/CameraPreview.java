package com.example.esvolkov.camera;

import android.content.Context;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by esvolkov on 18.02.2016.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    @NonNull private Camera camera;
    private SurfaceDestroyCallback destroyCallback;

    public CameraPreview(Context context, @NonNull Camera camera, SurfaceDestroyCallback callback) {
        super(context);
        this.camera = camera;
        this.destroyCallback = callback;

        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
            // TODO
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Ther is no changes on surface changed
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        destroyCallback.onSurfaceDestroy();
    }

}
