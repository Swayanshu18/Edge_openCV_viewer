// Final and complete MainActivity.java with rotation and toggle feature
package com.example.opencvintegrate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button; // <<<--- ADDED IMPORT
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;

    private GLSurfaceView glSurfaceView;
    private MyGLRenderer myRenderer;
    private Mat matRgba;
    private int frameCount = 0;

    // <<<--- NEW CODE (1/4): State variable for the toggle ---
    private boolean isEdgeDetectionEnabled = true;

    static {
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV loaded successfully");
            System.loadLibrary("native-lib");
        } else {
            Log.e(TAG, "OpenCV initialization failed");
        }
    }

    // <<<--- NEW CODE (2/4): Updated native function signature ---
    public native void processFrame(long matAddrRgba, int rotation, boolean applyEdgeDetection);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        glSurfaceView = findViewById(R.id.glSurfaceView);
        matRgba = new Mat();

        // <<<--- NEW CODE (3/4): Button click listener ---
        Button toggleButton = findViewById(R.id.toggle_button);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Flip the state
                isEdgeDetectionEnabled = !isEdgeDetectionEnabled;

                // Update button text
                if (isEdgeDetectionEnabled) {
                    toggleButton.setText("Show Normal Feed");
                } else {
                    toggleButton.setText("Show Edge Detection");
                }
            }
        });

        glSurfaceView.setEGLContextClientVersion(2);
        myRenderer = new MyGLRenderer();
        glSurfaceView.setRenderer(myRenderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        get_permissions();
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), this::processImageProxy);
                cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "CameraX binding failed", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void processImageProxy(ImageProxy imageProxy) {
        ImageConverter.yuvToRgba(imageProxy, matRgba);

        int rotation = imageProxy.getImageInfo().getRotationDegrees();

        // <<<--- NEW CODE (4/4): Pass the boolean state to C++ ---
        processFrame(matRgba.getNativeObjAddr(), rotation, isEdgeDetectionEnabled);

        Bitmap processedBitmap = Bitmap.createBitmap(matRgba.cols(), matRgba.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(matRgba, processedBitmap);

        myRenderer.updateFrame(processedBitmap);
        glSurfaceView.requestRender();

        // Save the first processed frame to a file
        if (frameCount < 1) {
            try {
                File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File file = new File(dir, "processed_frame.png");
                FileOutputStream out = new FileOutputStream(file);
                processedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                Log.d(TAG, "Saved frame to: " + file.getAbsolutePath());
                runOnUiThread(() -> Toast.makeText(this, "Frame Saved!", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                e.printStackTrace();
            }
            frameCount++;
        }

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

    void get_permissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to use this app", Toast.LENGTH_LONG).show();
            }
        }
    }
}