package com.example.esvolkov.camera;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements SurfaceDestoryCallback {
    private static final String CAMERA_TAG = "CUSTOM_CAMERA";
    private Camera camera;
    private CameraPreview cameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            throw new RuntimeException( "Camera is not available" );
        }

        if (camera != null ) {
            Log.d( CAMERA_TAG, "Not null: " );
            camera.release();
        }

        camera = getCameraInstance();
        cameraPreview = new CameraPreview( this, camera, this );

        FrameLayout frameLayour = (FrameLayout)findViewById(R.id.camera_preview);
        frameLayour.addView( cameraPreview );
    }

    private Camera getCameraInstance() {

        Camera camera = null;
        try {
            Log.d( CAMERA_TAG, "Number of cameras: " + Camera.getNumberOfCameras() );
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
            throw new RuntimeException( e );
        }
        return camera;

    }

    public void onSurfaceDestroy() {
        camera.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
            throw new RuntimeException( e );
        }
    }
}
