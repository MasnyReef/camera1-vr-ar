package com.example.mycamera1;

import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.OpenCVLoader;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements Camera.PreviewCallback, SurfaceHolder.Callback {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder0;
    private Camera camera;

    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                // Obsługa sukcesu w ustawieniu ostrości
            } else {
                // Obsługa niepowodzenia w ustawieniu ostrości
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        surfaceView = findViewById(R.id.surfaceView);

        surfaceHolder0 = surfaceView.getHolder();

        surfaceHolder0.addCallback(this);

        // Otwieranie kamery
        try {
            camera = Camera.open();
            camera.setPreviewDisplay(surfaceHolder0);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if(OpenCVLoader.initDebug()) Log.d("LOADED","success");
//        else Log.d("LOADED","err");
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try {
            camera.setPreviewDisplay(surfaceHolder0);
            camera.startPreview();
            camera.autoFocus(autoFocusCallback); // wywołanie metody autoFocus z przekazaniem obiektu AutoFocusCallback

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
}