package com.example.esvolkov.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SurfaceDestroyCallback {
    private static final String CAMERA_TAG = "CUSTOM_CAMERA";
    private static final int REQ_CAMERA = 1;
    @Nullable private Camera camera;
    @Nullable private CameraPreview cameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            toast("Camera is not available");
            return;
        }
        checkPermissions();
    }

    private void checkPermissions() {
        if (hasAccessToCamera()) {
            initializeCameraPreview();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                // TODO
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQ_CAMERA);
            }
        }
    }

    private boolean hasAccessToCamera() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeCameraPreview();
                } else {
                    // TODO
                }
            }
            break;
        }
    }

    private void initializeCameraPreview() {
        camera = getCameraInstance();
        if (camera == null) return;
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.camera_preview);
        frameLayout.removeAllViews();

        cameraPreview = new CameraPreview(this, camera, this);
        frameLayout.addView(cameraPreview);
    }

    @Nullable private Camera getCameraInstance() {
        Camera camera = null;
        try {
            Log.d(CAMERA_TAG, "Number of cameras: " + Camera.getNumberOfCameras());
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        } catch (Exception e) {
            toast(e.getMessage());
            e.printStackTrace();
            // TODO: handle exception
        }
        return camera;
    }

    private void toast(@NonNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onSurfaceDestroy() {
        releaseCamera();
    }

    @Override protected void onStart() {
        super.onStart();
        if (camera == null) checkPermissions();
    }

    @Override protected void onStop() {
        releaseCamera();
        super.onStop();
    }

    private void releaseCamera() {
        if (camera == null) return;

        if (cameraPreview != null) {
            cameraPreview.getHolder().removeCallback(cameraPreview);
        }
        camera.release();
        camera = null;
    }

}
