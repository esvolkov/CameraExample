package com.example.esvolkov.camera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by esvolkov on 18.02.2016.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera camera;
    private SurfaceDestoryCallback destroyCallback;

    public CameraPreview(Context context, Camera camera, SurfaceDestoryCallback callback) {
        super(context);
        this.camera = camera;
        this.destroyCallback = callback;

        mHolder = getHolder();
        mHolder.addCallback( this );
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException( e );
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
