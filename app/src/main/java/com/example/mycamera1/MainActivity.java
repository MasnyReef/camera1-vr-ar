package com.example.mycamera1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.Collections;
import java.util.List;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

public class MainActivity extends CameraActivity {

    CameraBridgeViewBase cameraBridgeViewBase;
    SurfaceView surfaceView1, surfaceView2;
    TextView textView1 , textView2;
    Mat mRGBA ,gray;
    SeekBar zoomSeekBar;
    private double currentZoomLevel = 1.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ustawienie fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //nie wygaszaj ekranu
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getPermission();

        surfaceView1 = findViewById(R.id.my_camera_view2);
        surfaceView2 = findViewById(R.id.my_camera_view3);

        textView1 = findViewById(R.id.my_text_view1);
        textView2 = findViewById(R.id.my_text_view2);

        cameraBridgeViewBase = findViewById(R.id.my_camera_view1);

        zoomSeekBar = findViewById(R.id.zoomSeekBar);

        zoomSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentZoomLevel = 1.0 - (double) progress / 200.0;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        cameraBridgeViewBase.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {
                mRGBA = new Mat();
                gray = new Mat();

            }

            @Override
            public void onCameraViewStopped() {
                mRGBA.release();
                gray.release();
            }

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                mRGBA = inputFrame.rgba();
                gray = inputFrame.gray();

                // Zoom
//                double centerX = mRGBA.width() / 2.0;
//                double centerY = mRGBA.height() / 2.0;
//                double scaledWidth = mRGBA.width() / currentZoomLevel;
//                double scaledHeight = mRGBA.height() / currentZoomLevel;
//                double zoomX = centerX - scaledWidth / 2.0;
//                double zoomY = centerY - scaledHeight / 2.0;
//
//                mRGBA = new Mat(mRGBA, new org.opencv.core.Rect((int) zoomX, (int) zoomY, (int) scaledWidth, (int) scaledHeight));
//                gray = new Mat(gray, new org.opencv.core.Rect((int) zoomX, (int) zoomY, (int) scaledWidth, (int) scaledHeight));

                double zoom = currentZoomLevel;
                Size orig = mRGBA.size();
                int offx = (int) (0.5 * (1.0-zoom) * orig.width);
                int offy = (int) (0.5 * (1.0-zoom) * orig.height);

                // crop the part, you want to zoom into:
                mRGBA = mRGBA.submat(offy, (int) (orig.height-offy), offx, (int) (orig.width-offx));
                // resize to original:
                Imgproc.resize(mRGBA, mRGBA, orig);


                float scaleX = (float) surfaceView1.getWidth() / mRGBA.width();
                float scaleY = (float) surfaceView1.getHeight() / mRGBA.height();

                Matrix matrix = new Matrix();
                matrix.setScale(scaleX, scaleY);


                // Konwersja do skali szarości
                Imgproc.cvtColor(mRGBA, gray, Imgproc.COLOR_RGBA2GRAY);

                // Skopiowanie klatki z JavaCameraView do Mat
                Mat rgbaCopy = mRGBA.clone();

                Bitmap bitmap = Bitmap.createBitmap(rgbaCopy.cols(), rgbaCopy.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(rgbaCopy, bitmap);

                processQRCode(bitmap);

                // Wyświetlenie klatki z JavaCameraView na SurfaceView
                Canvas canvas1 = surfaceView1.getHolder().lockCanvas();
                Canvas canvas2 = surfaceView2.getHolder().lockCanvas();

                if (canvas1 != null && canvas2 != null) {

                    canvas1.setMatrix(matrix);
                    canvas1.drawBitmap(bitmap, 0, 0, null);
                    surfaceView1.getHolder().unlockCanvasAndPost(canvas1);

                    canvas2.setMatrix(matrix);
                    canvas2.drawBitmap(bitmap, 0, 0, null);
                    surfaceView2.getHolder().unlockCanvasAndPost(canvas2);
                }

                return mRGBA;
            }
        });

        if(OpenCVLoader.initDebug()){
            cameraBridgeViewBase.enableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraBridgeViewBase.enableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraBridgeViewBase.disableView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraBridgeViewBase.disableView();
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(cameraBridgeViewBase);
    }

    void getPermission(){
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED){
            getPermission();
        }
    }


    private String lastQRCode = ""; // Zmienna przechowująca ostatnio odczytany kod QR
    private static final int TEXT_CLEAR_DELAY = 1000; // Opóźnienie w milisekundach

    private Handler handler = new Handler();
    private Runnable clearTextRunnable = new Runnable() {
        @Override
        public void run() {
            textView1.setText("");
            textView2.setText("");
        }
    };

    private void processQRCode(Bitmap bitmap) {
        int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

        MultiFormatReader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(binaryBitmap);
            String qrCode = result.getText();
            // Tutaj możesz obsłużyć odczytany kod QR
            Log.d("QRScannerActivity", "Zeskanowany kod QR: " + qrCode);
            textView1.setText(qrCode);
            textView2.setText(qrCode);

            handler.removeCallbacks(clearTextRunnable); // Usuń istniejące zaplanowane wyczyszczenie
            handler.postDelayed(clearTextRunnable, TEXT_CLEAR_DELAY);
        } catch (Exception e) {
            // Obsłuż błąd dekodowania kodu QR
            e.printStackTrace();
            //textView.setText("");
        }
    }
}


