// Final and complete MainActivity.java with rotation fix
package com.example.opencvintegrate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;

    private GLSurfaceView glSurfaceView;
    private MyGLRenderer myRenderer;
    private Mat matRgba;

    // Load the native library that contains our C++ code
    static {
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV loaded successfully");
            System.loadLibrary("native-lib");
        } else {
            Log.e(TAG, "OpenCV initialization failed");
        }
    }

    // Define the native function signature from our native-lib.cpp with rotation parameter
    public native void processFrame(long matAddrRgba, int rotation);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        glSurfaceView = findViewById(R.id.glSurfaceView);
        matRgba = new Mat();

        // Configure the GLSurfaceView
        glSurfaceView.setEGLContextClientVersion(2); // Use OpenGL ES 2.0
        myRenderer = new MyGLRenderer();
        glSurfaceView.setRenderer(myRenderer);
        // Render only when there's a new frame to draw
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        // Check for camera permissions and start the camera
        get_permissions();
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // We don't need to display the preview, but it's required for the lifecycle
                Preview preview = new Preview.Builder().build();
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        // This strategy ensures we only process the latest available frame
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), this::processImageProxy);

                // Bind the camera lifecycle to the activity
                cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);

            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "CameraX binding failed", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void processImageProxy(ImageProxy imageProxy) {
        // 1. Convert the YUV ImageProxy directly to an RGBA Mat using our new converter
        ImageConverter.yuvToRgba(imageProxy, matRgba);

        // 2. Get the rotation and call the native C++ function to process the frame in-place
        int rotation = imageProxy.getImageInfo().getRotationDegrees();
        processFrame(matRgba.getNativeObjAddr(), rotation);

        // 3. Convert the processed Mat back to a Bitmap for rendering
        Bitmap processedBitmap = Bitmap.createBitmap(matRgba.cols(), matRgba.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(matRgba, processedBitmap);

        // 4. Pass the final Bitmap to our OpenGL renderer
        myRenderer.updateFrame(processedBitmap);

        // 5. Tell the GLSurfaceView that it needs to re-render
        glSurfaceView.requestRender();

        // 6. IMPORTANT: Close the ImageProxy
        imageProxy.close();
    }



    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    // Your original permission handling logic
    void get_permissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions are already granted, start the camera
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, start the camera
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to use this app", Toast.LENGTH_LONG).show();
                // Handle the case where the user denies the permission
            }
        }
    }
}